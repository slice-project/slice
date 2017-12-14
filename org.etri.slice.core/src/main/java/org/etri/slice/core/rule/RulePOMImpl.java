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
package org.etri.slice.core.rule;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.aether.artifact.Artifact;
import org.etri.slice.api.rule.RulePOM;
import org.kie.api.builder.ReleaseId;
import org.kie.scanner.MavenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulePOMImpl implements RulePOM {
	private static Logger s_logger = LoggerFactory.getLogger(RulePOMImpl.class);		
	
	private MavenXpp3Reader m_pomReader = new MavenXpp3Reader();
	private MavenXpp3Writer m_pomWriter = new MavenXpp3Writer();
	private Model m_pom;
	private String m_pomStr;
	
	public RulePOMImpl() { }

	@Override
	public String getGroupId() {
		return m_pom.getGroupId();
	}
	
	@Override
	public void setGroupId(String groupId) {
		m_pom.setGroupId(groupId);
	}

	@Override
	public String getArtifactId() {
		return m_pom.getArtifactId();
	}
	
	@Override
	public void setArtifactId(String artfId) {
		m_pom.setArtifactId(artfId);
	}
	
	@Override
	public String getVersion() {
		return m_pom.getVersion();
	}	
	
	@Override
	public void setVersion(String version) {
		m_pom.setVersion(version);
	}
	
	@Override
	public String getName() {
		return m_pom.getName();
	}
	
	@Override
	public void setName(String name) {
		m_pom.setName(name);
	}
	
	public void importPOM(File pomFile) throws Exception {
		FileReader reader = new FileReader(pomFile);
		m_pom = m_pomReader.read(reader);	
		reader.close();
		
		byte[] encoded = Files.readAllBytes(Paths.get(pomFile.getAbsolutePath()));
		m_pomStr = new String(encoded, Charset.forName("UTF-8"));		
	}
	
	public void importPOM(MavenRepository repo, ReleaseId id) throws Exception {
		String coords = id.getGroupId() + ":" + id.getArtifactId() + ":" + "pom" + ":" + id.getVersion();
		Artifact artfPOM = repo.resolveArtifact(coords);
		FileReader reader = new FileReader(artfPOM.getFile());
		m_pom = m_pomReader.read(reader);	
		reader.close();
		
		byte[] encoded = Files.readAllBytes(Paths.get(artfPOM.getFile().getAbsolutePath()));
		m_pomStr = new String(encoded, Charset.forName("UTF-8"));		
	}
	
	public void exportPOM(File pomFile) throws IOException {
		FileWriter writer = new FileWriter(pomFile);
		m_pomWriter.write(writer, m_pom);
		writer.close();
	
		byte[] encoded = Files.readAllBytes(Paths.get(pomFile.getAbsolutePath()));
		m_pomStr = new String(encoded, Charset.forName("UTF-8"));
	}
	
	public String getPOMString() {
		return m_pomStr;
	}
	
}
