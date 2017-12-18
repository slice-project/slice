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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.codehaus.plexus.util.FileUtils;
import org.eclipse.aether.artifact.Artifact;
import org.etri.slice.api.rule.RuleModule;
import org.etri.slice.api.rule.RulePOM;
import org.etri.slice.api.rule.RuleSet;
import org.etri.slice.api.rule.RuleSetExistsException;
import org.etri.slice.api.rule.RuleSetNotFoundException;
import org.kie.api.builder.ReleaseId;
import org.kie.scanner.MavenRepository;

public class RuleModuleImpl implements RuleModule {
	
	private static final int BUFFER_SIZE = 2156;
	private byte[] mBuffer = new byte[BUFFER_SIZE];
	private int mByteCount = 0;
	private String mDestJarName = "";
	private RulePOMImpl m_pom = new RulePOMImpl();
	private Map<String, RuleSet> m_ruleSets = new HashMap<String, RuleSet>();
	
	private ReleaseId m_releaseId;
	private final File m_root;
	private final File m_tempJar;
	private File m_pomFile;

	public RuleModuleImpl(String root, ReleaseId releaseId) throws Exception {
		m_root = new File(root);
		m_tempJar = new File(m_root, "temp.jar");
		m_releaseId = releaseId;
		
		if ( m_root.exists() ) {
			FileUtils.deleteDirectory(m_root);
		}		
		m_root.mkdirs();
	}
	
	public void setUp() throws Exception {
		loadRuleModule();
	}
	
	public void tearDown() {
	}	
	
	@Override
	public synchronized String getVersion() {
		return m_pom.getVersion();
	}		

	@Override
	public synchronized void setVersion(String version) {
		m_pom.setVersion(version);
	}	

	@Override
	public synchronized RuleSet getRuleSet(String id) throws RuleSetNotFoundException {
		if ( !m_ruleSets.containsKey(id) ) {
			throw new RuleSetNotFoundException("RuleSet[id = " + id + "]" );
		}
		
		return m_ruleSets.get(id);
	}	
	
	@Override
	public synchronized Collection<RuleSet> getRuleSets() {		
		return m_ruleSets.values();
	}

	@Override
	public synchronized void addRuleSet(RuleSet ruleSet) throws RuleSetExistsException {
		String id = ruleSet.getId();		
		if ( m_ruleSets.containsKey(id) ) {
			throw new RuleSetExistsException("RuleSet[id = " + id + "]" );
		}
		
		m_ruleSets.put(id, ruleSet);
	}

	@Override
	public RuleSet createNewRuleSet(String id) {		
		return new RuleSetImpl(m_root, id);
	}

	@Override
	public synchronized RulePOM getRulePOM() {
		return m_pom;
	}
	
	@Override
	public synchronized File getRuleJarFile() throws IOException {
		m_pom.exportPOM(m_pomFile);
		saveRuleModule();
		
		return m_tempJar;
	}

	@Override
	public synchronized File getRulePOMFile() throws IOException{
		m_pom.exportPOM(m_pomFile);
		
		return m_pomFile;
	}	
	
	private void loadRuleModule() throws Exception {
		Artifact artfJar = MavenRepository.getMavenRepository().resolveArtifact(m_releaseId);
		JarFile jarWrapper = new JarFile(artfJar.getFile());
		Enumeration<JarEntry> entries = jarWrapper.entries();
		while ( entries.hasMoreElements() ) {
			JarEntry entry = entries.nextElement();
			if (entry.isDirectory()) {
				File dir = new File(m_root, entry.getName());
				dir.mkdir();
				if (entry.getTime() != -1) {
					dir.setLastModified(entry.getTime());
				}
				continue;
			}
			byte[] data = new byte[BUFFER_SIZE];
			File destFile = new File(m_root, entry.getName());
			FileOutputStream fos = new FileOutputStream(destFile);
			BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);
			int bytesRead = 0;
			try {
				InputStream is = jarWrapper.getInputStream(entry);
				while ((bytesRead = is.read(data)) != -1) {
					dest.write(data, 0, bytesRead);
				}
				dest.flush();
			} 
			finally {
				dest.close();
			}
			if (entry.getTime() != -1) {
				destFile.setLastModified(entry.getTime());
			}
			
			if( entry.getName().endsWith(".drl") ) {
				loadRuleSet(destFile);
			}
			
			if ( entry.getName().endsWith("pom.xml") ) {
				m_pomFile = destFile;
				m_pom.importPOM(m_pomFile);
			}
		}
		jarWrapper.close();		
	}
	
	private void saveRuleModule() throws IOException {		
		Collection<RuleSet> ruleSets = m_ruleSets.values();
		for ( RuleSet ruleSet : ruleSets ) {
			ruleSet.saveToFile();
		}
		
		mDestJarName = m_tempJar.getCanonicalPath();
		FileOutputStream fout = new FileOutputStream(m_tempJar);
		JarOutputStream jout = new JarOutputStream(fout);
		try {
			jarDir(m_root, jout, null);
		} 
		catch (IOException ioe) {
			throw ioe;
		} 
		finally {
			jout.close();
			fout.close();
		}		
	}
	
	private static final char SEP = '/';
	private void jarDir(File dirOrFile2jar, JarOutputStream jos, String path) throws IOException {
		if (dirOrFile2jar.isDirectory()) {
			String[] dirList = dirOrFile2jar.list();
			String subPath = (path == null) ? "" : (path + dirOrFile2jar.getName() + SEP);
			if (path != null) {
				JarEntry je = new JarEntry(subPath);
				je.setTime(dirOrFile2jar.lastModified());
				jos.putNextEntry(je);
				jos.flush();
				jos.closeEntry();
			}
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(dirOrFile2jar, dirList[i]);
				jarDir(f, jos, subPath);
			}
		} 
		else if (dirOrFile2jar.exists()) {
			if (dirOrFile2jar.getCanonicalPath().equals(mDestJarName)) {
				return;
			}

			FileInputStream fis = new FileInputStream(dirOrFile2jar);
			try {
				JarEntry entry = new JarEntry(path + dirOrFile2jar.getName());
				entry.setTime(dirOrFile2jar.lastModified());
				jos.putNextEntry(entry);
				while ((mByteCount = fis.read(mBuffer)) != -1) {
					jos.write(mBuffer, 0, mByteCount);
				}
				jos.flush();
				jos.closeEntry();
			} 
			catch (IOException ioe) {
				throw ioe;
			} 
			finally {
				fis.close();
			}
		}
	}	
	
	private void loadRuleSet(File ruleFile) throws Exception {
		RuleSetStreamHandler handler = new RuleSetStreamHandler(m_root, ruleFile);
		BufferedReader in = new BufferedReader(new FileReader(new File(ruleFile.getAbsolutePath())));
		in.lines().map(s -> s.trim()).filter(s -> s.length() > 0).forEach(handler);
		in.close();
		
		RuleSetImpl ruleSet = handler.getRuleSet();
		m_ruleSets.put(ruleSet.getId(), ruleSet);		
	}
}
