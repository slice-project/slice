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
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.GuardedBy;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.ProductionMemory;
import org.etri.slice.api.learning.ActionLogger;
import org.etri.slice.api.learning.ActionRuleLearner;
import org.etri.slice.api.learning.ActionRuleLearnerException;
import org.etri.slice.api.rule.Rule;
import org.etri.slice.api.rule.RuleBody;
import org.etri.slice.api.rule.RuleExistsException;
import org.etri.slice.api.rule.RuleModule;
import org.etri.slice.api.rule.RuleSet;
import org.etri.slice.api.rule.RuleSetNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.REPTree;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;


@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ActionRuleLearnerImpl implements ActionRuleLearner {

	private static Logger s_logger = LoggerFactory.getLogger(ActionRuleLearnerImpl.class);		
	
	@Property(name="actionrulelearner.minnumofrules", value="1")
	public long MIN_NUM_OF_RULES;
	@Property(name="actionrulelearner.loggingintervalbetweenlearnings", value="10")
	private int LOGGING_INTERVAL_BETWEEN_LEARNINGS;
	
	@Requires
	private ProductionMemory m_pm;
	@Requires
	private ActionLogger m_actionLogger;
	private RuleModule m_ruleModule;
	
	@GuardedBy("m_lock") private Classifier m_classifier;
	@GuardedBy("m_lock") private Filter m_filter;
	private Lock m_lock = new ReentrantLock();
		
	@Validate
	public void init() throws Exception {
		m_ruleModule = m_pm.getRuleModule();
		
//		String classifierName = "weka.classifiers.trees.REPTree";
		String[] classifierOptions = weka.core.Utils.splitOptions("-M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0");
//		classifier = AbstractClassifier.forName(classifierName, classifierOptions);
		REPTree repTree = new REPTree();
		repTree.setOptions(classifierOptions);
		m_classifier = repTree;
		
//		String filterName = "weka.filters.unsupervised.instance.Randomize";
//		filterOptions = weka.core.Utils.splitOptions("-S 42");
		String[] filterOptions = new String[0];
//		filter = (Filter)Class.forName(filterName).newInstance();
//		if (filter instanceof OptionHandler) {
//			((OptionHandler)filter).setOptions(filterOptions);
//		}
		
		Randomize rand = new Randomize();
		rand.setOptions(filterOptions);
		m_filter = rand;
	}
	
	@Invalidate
	public void finalize() {
	}	
	
	@Override
	public boolean learnActionRules() throws ActionRuleLearnerException {
		try {
			Collection<String> logIds = m_actionLogger.getActionLogIdsAll();
			if ( logIds.isEmpty() ) return false;
			
			ExecutorService executor = Executors.newFixedThreadPool(logIds.size()); 
			List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
			
			for ( String logId : logIds ) {
				futures.add(executor.submit(new ActionLogLearner(logId)));
			}
			
			int learnedRulesCount = 0;
			for ( Future<Integer> future : futures ) {
				learnedRulesCount += future.get();
			}
			if ( learnedRulesCount >= MIN_NUM_OF_RULES ) {
				m_ruleModule.setVersion(m_pm.getNewVersion());
				m_pm.update(m_ruleModule);
				
				return true;
			}
		}
		catch (Exception ex) {
			s_logger.warn("unexpected situation in learning action rules!!!", ex);
		}
		
		return false;
	}
	
	class ActionLogLearner implements Callable<Integer> {		
		
		private final String m_logId;
		private RuleSet m_ruleSet;
		
		public ActionLogLearner(String logId) {
			m_logId = logId;
			try {
				m_ruleSet = m_ruleModule.getRuleSet(m_logId);
			} 
			catch ( RuleSetNotFoundException neverHappens ) { }
		}
		
		@Override
		public Integer call() {
			List<String> constructedRules = null;
			try {
				Instances trainingData = loadData();
				Attribute classAttribute = trainingData.classAttribute();
				String classAttributeName = classAttribute.name();
				String classAttributeType = Attribute.typeToString(classAttribute);
				m_lock.lock();
				String classificationRuleTree = null;
				try {	
					Instances filteredData = applyFilter(m_filter, trainingData);
					m_classifier.buildClassifier(filteredData); // train classifier on complete file for tree
					classificationRuleTree = m_classifier.toString();
				}
				finally {
					m_lock.unlock();
				}
				constructedRules = contructActionRules(classAttributeName, classAttributeType, classificationRuleTree);
				s_logger.info("Number of Action Rules learned : {}/{}", constructedRules.size(), MIN_NUM_OF_RULES);
			}
			catch ( Exception e ) {
				s_logger.error("ERR: " + e.getMessage());
			}
			
			return constructedRules.size();
		}
		
		private Instances loadData() throws Exception {			
			File logFile = m_actionLogger.getActionLogFile(m_logId);			
			Instances trainingData = new Instances(new BufferedReader(new FileReader(logFile)));
			trainingData.setClassIndex(trainingData.numAttributes() - 1);

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
		
		private List<String> contructActionRules(String classAttributeName, String classAttributeType, String classificationRuleTree) {
			List<String> addedRules = new ArrayList<String>();
			Rule.RuleBuilder ruleBuilder = Rule.builder();
			List<String> treePathList = makeTreePathList(classificationRuleTree);
			for (int i = 0; i < treePathList.size(); i++) {
				ruleBuilder.setId(classAttributeName + " #" + (i + 1));				
				String treePath = treePathList.get(i);				
				RuleBody ruleBody = makeRuleBody(classAttributeName, classAttributeType, treePath);
				ruleBuilder.setBody(ruleBody);
				try {
					Rule addedRule = ruleBuilder.build();
					m_ruleSet.addRule(addedRule);
					addedRules.add(addedRule.toString());
				} 
				catch ( RuleExistsException e ) {
					// To do
				}
			}
			return addedRules;
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
//					logger.info("Tree path = {}", pathStringBuffer.toString());
				}
			}

			return treePathList;
		}

		private RuleBody makeRuleBody(String classAttributeName, String classAttributeType, String treePath) {
			String[] sides = treePath.split(":");

			String conclusion = sides[1].trim();
			if (classAttributeType.equals("INTEGER") == true || classAttributeType.equals("integer") == true)
				conclusion = conclusion.replaceAll("\\.\\d+", "");

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

			RuleBody.RuleBodyBuilder ruleBodyBuilder = RuleBody.builder();
			ruleBodyBuilder.addAttribute("salience 10");
			int i = 0;
			for (String type : conditionMap.keySet()) {
				StringBuffer conditionStringBuffer = new StringBuffer();
				
				String[] elements = type.split("\\.");
				conditionStringBuffer.append("$" + i + ":" + elements[0] + "(");
				List<String> constraintList = conditionMap.get(type);
				for (int j = 0; j < constraintList.size(); j++) {
					String constraint = constraintList.get(j);

					conditionStringBuffer.append(elements[1] + " " + constraint);
					if (j < constraintList.size() - 1) {
						conditionStringBuffer.append(", ");
					}
				}
				conditionStringBuffer.append(")");
				i++;
				ruleBodyBuilder.addCondition(conditionStringBuffer.toString());
			}

			ruleBodyBuilder.addAction(classAttributeName + "(" + conclusion + ");");
			return ruleBodyBuilder.build();
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
	}
}
