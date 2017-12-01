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
package org.etri.slice.core.learning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.ProductionMemory;
import org.etri.slice.api.learning.ActionRuleLearner;
import org.etri.slice.api.learning.ActionRuleLearnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.filters.Filter;


@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ActionRuleLearnerImpl implements ActionRuleLearner {

	private static Logger logger = LoggerFactory.getLogger(ActionRuleLearnerImpl.class);		
	
	@Property(name="learner.minNumOfRules", value="1000")
	public long MIN_NUM_OF_RULES;
	
	@Requires
	private ProductionMemory m_pm;
	
	private String classifierName;
	private String[] classifierOptions;

	private String filterName;
	private String[] filterOptions;	
	
	@Validate
	public void initialize() throws Exception {
		classifierName = "weka.classifiers.trees.REPTree";
		classifierOptions = weka.core.Utils.splitOptions("-M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0");

		filterName = "weka.filters.unsupervised.instance.Randomize";
		filterOptions = weka.core.Utils.splitOptions("-S 42");		
	}
	
	@Invalidate
	public void finalize() {
	}	
	
	@Override
	public void learnActionRules(String dataFilePath) throws ActionRuleLearnerException {
		try {
			Classifier classifier = createClassifier(classifierName, classifierOptions);
			Filter filter = createFilter(filterName, filterOptions);
			
			// 1
			List<String> lines = Files.readAllLines(new File(dataFilePath).toPath(), Charset.forName("UTF-8"));
			List<String> metaDataList = new ArrayList<>();
			String classAttributeName = "";
			String classAttributeType = "";
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i).trim();
				if (line.startsWith("%") == true) {
					String metaData = line.replaceFirst("%", "");
					metaDataList.add(metaData);
				}
				else if (line.startsWith("@ATTRIBUTE") == true) {
					String[] metaDataElements = line.split("\\s+");
					classAttributeName = metaDataElements[1];
					classAttributeType = metaDataElements[2];
				}
			}

			
			// 2
			Instances trainingData = loadData(dataFilePath);
			Instances filteredData = applyFilter(filter, trainingData);
			classifier.buildClassifier(filteredData); // train classifier on complete file for tree
			
			String classificationRuleTree = classifier.toString();
			logger.info("Classification Rule Tree :\n{}", classificationRuleTree);
			
			
			// 3
//			String evaluationResultSummary = evaluate(classifier, filteredData);
//			logger.info("Evaluation Result Summary :\n{}", evaluationResultSrummary);

			List<String> actionRuleList = contructActionRules(classAttributeName, classAttributeType, classificationRuleTree);
			if (actionRuleList.size() < MIN_NUM_OF_RULES) {
				logger.info("--terminates learning action rules : the number of rules is too small!! (minimum is {})", MIN_NUM_OF_RULES);
				return;
			}
			logger.info("Action Rules :\n{}", actionRuleList);

			String ruleFileContent = createDroolsRuleFileContent(metaDataList, actionRuleList);
//			m_pm.update(ruleFileContent);
			
			
			// 4
//			File originalKieModuleJarFile = repository.resolveArtifact(droolsKB.rulePackageReleaseId).getFile();
//			String ruleFilePath = droolsKB.ruleDirectoryInPackage + dataFilePath.replaceAll("\\.arff", "") + ".drl";
//			String ruleFileContent = createDroolsRuleFileContent(metaDataList, actionRuleList);
//			File newKieModuleJarFile = createKieModuleJarFile(originalKieModuleJarFile, ruleFilePath, ruleFileContent);
//
//			String pomFilePath = "META-INF/maven/" + droolsKB.rulePackageReleaseId.getGroupId() + "/" + droolsKB.rulePackageReleaseId.getArtifactId() + "/pom.xml";
//			File pomFile = getPomFileFromInputStream(droolsKB.container.getClassLoader().getResourceAsStream(pomFilePath));
//			repository.installArtifact(droolsKB.rulePackageReleaseId, newKieModuleJarFile, pomFile);
			logger.info("--installed the new action rules");
		}
		catch (Exception ex) {
			logger.warn("unexpected situation in learning action rules!!!", ex);
		}

	}
	
	private Classifier createClassifier(String classifierName, String[] options) throws Exception {
		Classifier classifier = null;

		if (classifierName != null && classifierName.isEmpty() == false) {
			classifier = AbstractClassifier.forName(classifierName, options);
		}

		return classifier;
	}

	private Filter createFilter(String name, String[] options) throws Exception {
		Filter filter = null;

		if (name != null && name.isEmpty() == false) {
			filter = (Filter)Class.forName(name).newInstance();
			if (filter instanceof OptionHandler) {
				((OptionHandler)filter).setOptions(options);
			}
		}

		return filter;
	}
	
	private Instances loadData(String dataFileName) throws Exception {
		Instances trainingData = new Instances(new BufferedReader(new FileReader(dataFileName)));

		trainingData.setClassIndex(trainingData.numAttributes() - 1);
//		trainingData.setClassIndex(trainingData.numAttributes() - 2);

		return trainingData;
	}
	
	private Instances applyFilter(Filter filter, Instances trainingData) throws Exception {
		if (filter == null)
			return trainingData;
		
		// run filter
		filter.setInputFormat(trainingData);
		Instances filtered = Filter.useFilter(trainingData, filter);

		return filtered;
	}
	
	
	private String evaluate(Classifier classifier, Instances evaluationData) throws Exception {
		// 10fold CV with seed=1
		Evaluation evaluation = new Evaluation(evaluationData);
		evaluation.crossValidateModel(classifier, evaluationData, 10, evaluationData.getRandomNumberGenerator(1));

		StringBuffer resultStringBuffer = new StringBuffer();
		resultStringBuffer.append("Evaluation results\n");
		resultStringBuffer.append("============\n");
		resultStringBuffer.append(evaluation.toSummaryString());

		return resultStringBuffer.toString();
	}
	
	
	private List<String> contructActionRules(String classAttributeName, String classAttributeType, String classificationRuleTree) {
		List<String> actionRuleList = new ArrayList<>();

		List<String> treePathList = makeTreePathList(classificationRuleTree);
		for (int i = 0; i < treePathList.size(); i++) {
			String treePath = treePathList.get(i);

			String actionRuleBody = makeRuleBody(classAttributeName, classAttributeType, treePath);

			String actionRule = "rule \"" + classAttributeName + " #" + (i + 1) + "\"\n";
			actionRule = actionRule + actionRuleBody + "\n";
			actionRule = actionRule + "end";
			actionRuleList.add(actionRule);
		}

		return actionRuleList;
	}
	
	private List<String> makeTreePathList(String classificationRuleTree) {
		List<String> treePathList = new ArrayList<>();

		List<String> branchList = new ArrayList<>();
		String[] lines = classificationRuleTree.split("\n");
		for (int i = 4; i < lines.length - 2; i++) {
			String line = lines[i].trim();

			int indentCount = 0;
			int lastIndex = line.lastIndexOf("|");
			if (lastIndex >= 0) {
				indentCount = lastIndex / 4 + 1;
			}

			line = line.replaceAll("\\||\\(.*\\)|\\[.*\\]", "").trim();

			if (line.contains(" : ") == false) {
				branchList.add(indentCount, line);
			}
			else {
				StringBuffer pathStringBuffer = new StringBuffer();
				for (int j = 0; j < indentCount; j++) {
					pathStringBuffer.append(branchList.get(j)).append(",");
				}
				pathStringBuffer.append(line);

				treePathList.add(pathStringBuffer.toString());
				logger.info("Tree path = {}", pathStringBuffer.toString());
			}
		}

		return treePathList;
	}

	private String makeRuleBody(String classAttributeName, String classAttributeType, String treePath) {
		String[] sides = treePath.split(":");

		String conclution = sides[1].trim();
		if (classAttributeType.equals("INTEGER") == true || classAttributeType.equals("integer") == true)
			conclution = conclution.replaceAll("\\.\\d+", "");

		Map<String, List<String>> conditionMap = new HashMap<>();
		String[] conditions = sides[0].trim().split(",");
		for (String condition : conditions) {
			condition = condition.trim();
			int splitPoint = condition.indexOf(" ");
			String type = condition.substring(0, splitPoint).trim();
			String constraint = condition.substring(splitPoint + 1).trim();

			if (conditionMap.containsKey(type) == true) {
				conditionMap.get(type).add(constraint);
			}
			else {
				List<String> constraintList = new ArrayList<>();
				constraintList.add(constraint);

				conditionMap.put(type, constraintList);
			}
		}

		StringBuffer ruleBodyStringBuffer = new StringBuffer();
		ruleBodyStringBuffer.append("\twhen\n");
		int i = 0;
		for (String type : conditionMap.keySet()) {
			String[] elements = type.split("\\.");

			ruleBodyStringBuffer.append("\t\t$" + i + ":" + elements[0] + "(");
			List<String> constraintList = conditionMap.get(type);
			for (int j = 0; j < constraintList.size(); j++) {
				String constraint = constraintList.get(j);

				ruleBodyStringBuffer.append(elements[1] + " " + constraint);
				if (j < constraintList.size() - 1) {
					ruleBodyStringBuffer.append(", ");
				}
			}
			ruleBodyStringBuffer.append(")\n");

			i++;
		}

		ruleBodyStringBuffer.append("\tthen\n");
		ruleBodyStringBuffer.append("\t\t" + classAttributeName + "(");
		ruleBodyStringBuffer.append(conclution + ");");

		return ruleBodyStringBuffer.toString();
	}

	
	private File createKieModuleJarFile(File srcJarFile, String filePath, String fileContent) throws IOException {
		File tmpJarFile = File.createTempFile("tempJar", ".tmp");
		JarFile jarFile = new JarFile(srcJarFile);
		
		boolean jarUpdated = false;
		try {
			JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tmpJarFile));

			try {
				JarEntry newJarEntry = new JarEntry(filePath);
				tempJarOutputStream.putNextEntry(newJarEntry);
				tempJarOutputStream.write(fileContent.getBytes());

				//Copy original jar file to the temporary one.
				Enumeration jarEntries = jarFile.entries();
				while(jarEntries.hasMoreElements()) {
					JarEntry entry = (JarEntry)(jarEntries.nextElement());
					if (entry.getName().equals(newJarEntry.getName()) == false) {
						InputStream entryInputStream = jarFile.getInputStream(entry);
						tempJarOutputStream.putNextEntry(entry);
						byte[] buffer = new byte[1024];
						int bytesRead = 0;
						while ((bytesRead = entryInputStream.read(buffer)) != -1)
							tempJarOutputStream.write(buffer, 0, bytesRead);
					}
				}

				jarUpdated = true;
			}
			catch(Exception ex) {
				ex.printStackTrace();
				tempJarOutputStream.putNextEntry(new JarEntry("stub"));
			}
			finally {
				tempJarOutputStream.close();
			}

		}
		finally {
			jarFile.close();
			if (!jarUpdated) {
				tmpJarFile.delete();
			}
		}

		if (jarUpdated) {
			tmpJarFile.renameTo(srcJarFile);
			
			return tmpJarFile;
		}
		else {
			return null;
		}
	}

	private File getPomFileFromInputStream(InputStream is) throws IOException {
		File pomFile = File.createTempFile("pom", ".xml");
		IOUtils.copy(is, new FileOutputStream(pomFile));

		return pomFile;
	}
	
	private String createDroolsRuleFileContent(List<String> metaDataList, List<String> actionRuleList) {
		StringBuilder ruleFileContentBuilder = new StringBuilder();

		for (int i = 0; i < metaDataList.size(); i++) {
			ruleFileContentBuilder.append(metaDataList.get(i)).append("\n");
		}
		ruleFileContentBuilder.append("\n\n");

		for (int i = 0; i < actionRuleList.size(); i++) {
			ruleFileContentBuilder.append(actionRuleList.get(i)).append("\n\n");
		}

		return ruleFileContentBuilder.toString();
	}	

}
