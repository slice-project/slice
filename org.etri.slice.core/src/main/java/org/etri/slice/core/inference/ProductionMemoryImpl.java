/**
 *
 * Copyright (c) 2017-2017 SLICE project team (yhsuh@etri.re.kr)
 * http://slice.etri.re.kr
 *
 * This file is part of The SLICE components
 *
 * This Program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The SLICE components; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.etri.slice.core.inference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.aether.artifact.Artifact;
import org.etri.slice.api.inference.ProductionMemory;
import org.etri.slice.api.inference.ProductionMemoryException;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.scanner.MavenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ProductionMemoryImpl implements ProductionMemory {

	private static Logger s_logger = LoggerFactory.getLogger(ProductionMemoryImpl.class);	
	
	@Requires
	private DroolsRuleEngine m_drools;	
	
	private KieContainer m_container;
	private MavenRepository m_repository;
	private SliceRulePOM m_rulePOM = new SliceRulePOM();
	private SliceRuleJar m_ruleJar = new SliceRuleJar();
	
	@Validate
	public void start() throws Exception {
		m_container = m_drools.getKieContainer();
		m_repository = m_drools.getMavenRepository();
/*		
		final ReleaseId releaseId = m_drools.getReleaseId();
		System.out.println(releaseId);
		m_rulePOM.importPOM(m_repository, releaseId);
		m_rulePOM.setName("xxxx");
		m_rulePOM.setVersion("0.0.7");
		
		File pomFile = File.createTempFile("pom",".xml");
		m_rulePOM.exportPOM(pomFile);
		pomFile.delete();
		
		m_ruleJar.importJar(m_repository, releaseId);
        StringBuilder ruleBuilder = new StringBuilder();
        ruleBuilder.append("rule 'Silver Customers - Discount'\n");
        ruleBuilder.append("when\n");
        ruleBuilder.append("    $o: Order( $customer: customer, discount == null)\n");
        ruleBuilder.append("    $c: Customer( category == Customer.Category.SILVER, this == $customer )\n");
        ruleBuilder.append("then\n");
        ruleBuilder.append("    System.out.println(\"Executing Silver Customer ").append(30).append("% Discount Rule!\");\n");
        ruleBuilder.append("    $o.setDiscount(new Discount(").append(30).append("));\n");
        ruleBuilder.append("    update($o);\n");
        ruleBuilder.append("end\n");		
		m_ruleJar.setRuleBody(ruleBuilder.toString());
		
		File jarFile = File.createTempFile("rule",".jar");
		m_ruleJar.exportJar(jarFile);
		jarFile.delete();
*/
		s_logger.info("SLICE ProductionMemory is started");
	}
	
	@Invalidate
	public void stop() {
		m_container.dispose();
	}
	
	@Override
	public synchronized String getCurrentVersion() {
		ReleaseId releaseId = m_drools.getReleaseId();
		return releaseId.getVersion();
	}

	@Override
	public synchronized void install(String version, byte[] jarContent, byte[] pomContent) {
		ReleaseId releaseId = m_drools.newReleaseId(version);
		m_drools.getMavenRepository().installArtifact(releaseId, jarContent, pomContent);
		m_container.updateToVersion(releaseId);
	}

	@Override
	public synchronized void install(String version, File jar, File pomFile) {
		ReleaseId releaseId = m_drools.newReleaseId(version);
		m_drools.getMavenRepository().installArtifact(releaseId, jar, pomFile);
		m_container.updateToVersion(releaseId);
	}

	@Override
	public synchronized void update(String... rules) throws ProductionMemoryException {
		try {
			final ReleaseId releaseId = m_drools.getReleaseId();
			String version = releaseId.getVersion();
			m_rulePOM.importPOM(m_repository, releaseId);
			m_rulePOM.setVersion(version);
			File pomFile = File.createTempFile("pom",".xml");
			m_rulePOM.exportPOM(pomFile);			
			
			ReleaseId newReleaseId = m_drools.newReleaseId(version);
		}
		catch ( Throwable e ) {		
			s_logger.error("ERR", e);
			throw new ProductionMemoryException(e);
		}
	}
	
	@Override
	public synchronized void update(String version, String... rules) throws ProductionMemoryException {
		try {
			final ReleaseId releaseId = m_drools.getReleaseId();
			m_rulePOM.importPOM(m_repository, releaseId);
			m_rulePOM.setVersion(version);
			File pomFile = File.createTempFile("pom",".xml");
			m_rulePOM.exportPOM(pomFile);			
			
			ReleaseId newReleaseId = m_drools.newReleaseId(version);
		}
		catch ( Throwable e ) {		
			s_logger.error("ERR", e);
			throw new ProductionMemoryException(e);
		}
	}

	static class SliceRulePOM {
		private static Logger s_logger = LoggerFactory.getLogger(SliceRulePOM.class);		
		
		private MavenXpp3Reader m_pomReader = new MavenXpp3Reader();
		private MavenXpp3Writer m_pomWriter = new MavenXpp3Writer();
		private Model m_pom;
		
		public SliceRulePOM() { }

		public void setGroupId(String groupId) {
			m_pom.setGroupId(groupId);
		}
		
		public void setArtifactId(String artfId) {
			m_pom.setArtifactId(artfId);
		}
		
		public void setVersion(String version) {
			m_pom.setVersion(version);
		}
		
		public void setName(String name) {
			m_pom.setName(name);
		}
		
		public void importPOM(MavenRepository repo, ReleaseId id) throws Exception {
			String coords = id.getGroupId() + ":" + id.getArtifactId() + ":" + "pom" + ":" + id.getVersion();
			Artifact artfPOM = repo.resolveArtifact(coords);
			m_pom = m_pomReader.read(new FileReader(artfPOM.getFile()));		
		}
		
		public void exportPOM(File pomFile) throws Exception {
			FileWriter writer = new FileWriter(pomFile);
			m_pomWriter.write(new FileWriter(pomFile), m_pom);
			writer.close();

		}
	}	
	
	static class SliceRuleJar {
		private static Logger s_logger = LoggerFactory.getLogger(SliceRulePOM.class);
		
		private JarFile m_jarFile;
		private String m_ruleHeader;
		private String m_ruleBody;
		private JarEntry m_ruleEntry;
		private File m_ruleFile;
		private byte[] m_jarBytes;
		private byte[] m_ruleBytes;
		private List<JarEntry> m_rules;
		
		public SliceRuleJar() {	}
		
		public void setRuleBody(String ruleBody) {
			m_ruleBody = ruleBody;
		}
		
		public void importJar(MavenRepository repo, ReleaseId id) throws Exception {
			Artifact artfJar = repo.resolveArtifact(id);
			m_jarFile = new JarFile(artfJar.getFile());
			FileInputStream istream = new FileInputStream(artfJar.getFile());
			m_jarBytes = new byte[istream.available()];
			istream.read(m_jarBytes);
			
			Enumeration<JarEntry> entries = m_jarFile.entries();
			while ( m_jarFile.entries().hasMoreElements() ) {
				m_ruleEntry = entries.nextElement();
				if ( m_ruleEntry.isDirectory() ) continue;
				if( m_ruleEntry.getName().endsWith(".drl") ) break;
			}
			
			byte[] rules = new byte[(int)m_ruleEntry.getSize()];
			m_jarFile.getInputStream(m_ruleEntry).read(rules);
			String ruleString = new String(rules, Charset.forName("UTF-8"));
			m_ruleHeader = ruleString.substring(0, ruleString.indexOf("rule "));
		}
		
		public void exportJar(File jarFile) throws Exception {			
			FileOutputStream ostream = new FileOutputStream(jarFile);
			ostream.write(m_jarBytes);			
			JarOutputStream jarStream = new JarOutputStream(ostream);
			jarStream.putNextEntry(m_ruleEntry);
			jarStream.write(m_ruleBody.getBytes());
			jarStream.close();
		}
	}	
}
