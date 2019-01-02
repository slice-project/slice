/** 
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.etri.slice.tools.adl

import com.google.inject.Binder
import com.google.inject.name.Names
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.OutputConfigurationProvider
import org.eclipse.xtext.resource.persistence.IResourceStorageFacade
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.eclipse.xtext.xbase.resource.BatchLinkableResourceStorageFacade
import org.etri.slice.tools.adl.generator.ADLGenerator
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput
import org.etri.slice.tools.adl.scoping.DomainModelXImportSectionNamespaceScopeProvider
import org.etri.slice.tools.adl.utils.DomainnodeUtil
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class DomainmodelRuntimeModule extends AbstractDomainmodelRuntimeModule {
	def Class<? extends IResourceStorageFacade> bindResourceStorageFacade() {
		return BatchLinkableResourceStorageFacade
	}

	def Class<? extends OutputConfigurationProvider> bindOutputConfigurationProvider() {
		return DomainModelOutputConfigurationProvider
	}

	override Class<? extends IGenerator> bindIGenerator() {
		return ADLGenerator;
	}

	def Class<? extends IGeneratorForMultiInput> bindIGeneratorForMultiInput() {
		return ADLGenerator;
	}

	def Class<? extends DomainnodeUtil> bindDomainmodeUtil() {
		return DomainnodeUtil
	}

	def Class<? extends DomainDependencyUtil> bindDomainDependencyUtil() {
		return DomainDependencyUtil
	}

	def Class<? extends DomainManager> bindDomainManager() {
		return DomainManager
	}

	override configureIScopeProviderDelegate(Binder binder) {
		binder.bind(IScopeProvider).annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).to(
			DomainModelXImportSectionNamespaceScopeProvider)
	}
}
