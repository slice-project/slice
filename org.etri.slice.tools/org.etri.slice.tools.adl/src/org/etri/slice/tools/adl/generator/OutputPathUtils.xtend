/*******************************************************************************
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.domainmodel.Exception

/**
 * @author yhsuh - Initial contribution and API
 */
class OutputPathUtils {
	
	public static val sliceAgents = "org.etri.slice.agents"
	public static val sliceCommons = "org.etri.slice.commons"
	public static val sliceDevices = "org.etri.slice.devices"
	public static val sliceDistribution = "org.etri.slice.distribution"
	public static val sliceModels = "org.etri.slice.models"
	public static val sliceRules = "org.etri.slice.rules"
	
	@Inject extension IQualifiedNameProvider	
	
	dispatch def getSliceFullyQualifiedName(Context it) {
		sliceCommons + "." + eContainer.fullyQualifiedName + ".context"
	}
	
	dispatch def getSliceFullyQualifiedName(Event it) {
		sliceCommons + "." + eContainer.fullyQualifiedName + ".event"
	}
	
	dispatch def getSliceFullyQualifiedName( Exception it) {
		sliceCommons + "." + eContainer.fullyQualifiedName
	}	
	
	dispatch def getSliceFullyQualifiedName(Control it) {
		sliceCommons + "." + eContainer.fullyQualifiedName + ".service"
	}	
	
	def getCommonsMavenHome(AgentDeclaration it) {
		sliceModels + "/" + sliceCommons + "." + eContainer.fullyQualifiedName + "/"
	}	
	
	dispatch def getCommonsMavenSrcHome(Context it) {
		sliceModels + "/" + sliceCommons + "." + eContainer.fullyQualifiedName + "/src/main/java/"
	}
	
	dispatch def getCommonsMavenSrcHome(Event it) {
		sliceModels + "/" + sliceCommons + "." + eContainer.fullyQualifiedName + "/src/main/java/"
	}
	
	dispatch def getCommonsMavenSrcHome(Exception it) {
		sliceModels + "/" + sliceCommons + "." + eContainer.fullyQualifiedName + "/src/main/java/"
	}	
	
	dispatch def getCommonsMavenSrcHome(Control it) {
		sliceModels + "/" + sliceCommons + "." + eContainer.fullyQualifiedName + "/src/main/java/"
	}	
	
	def getAgentFullyQualifiedName(AgentDeclaration it) {
		sliceAgents + "." + eContainer.fullyQualifiedName + "." + name.toLowerCase
	}
	
	def getAgentMavenHome(AgentDeclaration it) {
		sliceAgents + "/" + agentFullyQualifiedName + "/"
	}
	
	def getAgentMavenSrcHome(AgentDeclaration it) {
		sliceAgents + "/" + agentFullyQualifiedName + "/src/main/java/"
	}

	def getAgentMavenResHome(AgentDeclaration it) {
		sliceAgents + "/" + agentFullyQualifiedName + "/src/main/resources/"
	}
	
	def getDeviceFullyQualifiedName(AgentDeclaration it) {
		sliceDevices + "." + eContainer.fullyQualifiedName + "." + name.toLowerCase
	}
	
	def getDeviceMavenHome(AgentDeclaration it) {
		sliceDevices + "/" + deviceFullyQualifiedName + "/"
	}
	
	def getDeviceMavenSrcHome(AgentDeclaration it) {
		sliceDevices + "/" + deviceFullyQualifiedName + "/src/main/java/"
	}

	def getDeviceMavenResHome(AgentDeclaration it) {
		sliceDevices + "/" + deviceFullyQualifiedName + "/src/main/resources/"
	}	
	
	def getRuleFullyQualifiedName(AgentDeclaration it) {
		sliceRules + "." + eContainer.fullyQualifiedName + "." + name.toLowerCase
	}
		
	def getRuleMavenHome(AgentDeclaration it) {
		sliceRules + "/" + ruleFullyQualifiedName + "/"
	}
	
	def getRuleMavenResHome(AgentDeclaration it) {
		sliceRules + "/" + ruleFullyQualifiedName + "/src/main/resources/"
	}			
}