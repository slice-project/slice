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

/**
 * @author yhsuh - Initial contribution and API
 */
class OutputPathUtils {
	
	static val sliceCommons = "org.etri.slice.commons."
	static val sliceDevice = "org.etri.slice.devices."
	static val sliceRule = "org.etri.slice.rules."
	@Inject extension IQualifiedNameProvider	
	
	dispatch def getSliceFullyQualifiedName(Context it) {
		sliceCommons + eContainer.fullyQualifiedName + ".context"
	}
	
	dispatch def getSliceFullyQualifiedName(Event it) {
		sliceCommons + eContainer.fullyQualifiedName + ".event"
	}
	
	dispatch def getSliceFullyQualifiedName(Control it) {
		sliceCommons + eContainer.fullyQualifiedName + ".service"
	}		
	
	dispatch def getCommonsMavenSrcHome(Context it) {
		sliceCommons + eContainer.fullyQualifiedName + "/src/main/java/"
	}
	
	dispatch def getCommonsMavenSrcHome(Event it) {
		sliceCommons + eContainer.fullyQualifiedName + "/src/main/java/"
	}
	
	dispatch def getCommonsMavenSrcHome(Control it) {
		sliceCommons + eContainer.fullyQualifiedName + "/src/main/java/"
	}	
	
	def getDeviceFullyQualifiedName(AgentDeclaration it) {
		sliceDevice + eContainer.fullyQualifiedName + "." + name.toLowerCase
	}
	
	def getDeviceMavenHome(AgentDeclaration it) {
		deviceFullyQualifiedName + "/"
	}
	
	def getDeviceMavenSrcHome(AgentDeclaration it) {
		deviceFullyQualifiedName + "/src/main/java/"
	}

	def getDeviceMavenResHome(AgentDeclaration it) {
		deviceFullyQualifiedName + "/src/main/resources/"
	}
	
	def getRuleFullyQualifiedName(AgentDeclaration it) {
		sliceRule + eContainer.fullyQualifiedName + "." + name.toLowerCase
	}
		
	def getRuleMavenHome(AgentDeclaration it) {
		ruleFullyQualifiedName + "/"
	}
	
	def getRuleMavenResHome(AgentDeclaration it) {
		it.ruleFullyQualifiedName + "/src/main/resources/"
	}			
}