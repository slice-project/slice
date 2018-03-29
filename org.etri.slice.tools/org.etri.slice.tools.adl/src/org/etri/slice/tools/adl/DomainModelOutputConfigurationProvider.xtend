/*******************************************************************************
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.etri.slice.tools.adl

import org.eclipse.xtext.generator.OutputConfigurationProvider

/**
 * @author yhsuh - Initial contribution and API
 */
class DomainModelOutputConfigurationProvider extends OutputConfigurationProvider {
	public static val DOMAINMODEL_GEN = "./org.etri.slice.generated"
	
	override getOutputConfigurations() {
		super.getOutputConfigurations() => [
			head.outputDirectory = DOMAINMODEL_GEN
		]
	}
}