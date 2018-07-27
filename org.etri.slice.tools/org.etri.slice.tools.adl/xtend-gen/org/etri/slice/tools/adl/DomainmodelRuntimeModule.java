/**
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.etri.slice.tools.adl;

import org.eclipse.xtext.generator.OutputConfigurationProvider;
import org.eclipse.xtext.resource.persistence.IResourceStorageFacade;
import org.eclipse.xtext.xbase.resource.BatchLinkableResourceStorageFacade;
import org.etri.slice.tools.adl.AbstractDomainmodelRuntimeModule;
import org.etri.slice.tools.adl.DomainModelOutputConfigurationProvider;
import org.etri.slice.tools.adl.generator.ADLGenerator;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.utils.DomainnodeUtil;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings("all")
public class DomainmodelRuntimeModule extends AbstractDomainmodelRuntimeModule {
  public Class<? extends IResourceStorageFacade> bindResourceStorageFacade() {
    return BatchLinkableResourceStorageFacade.class;
  }
  
  public Class<? extends OutputConfigurationProvider> bindOutputConfigurationProvider() {
    return DomainModelOutputConfigurationProvider.class;
  }
  
  public Class<? extends IGeneratorForMultiInput> bindIGenerator2() {
    return ADLGenerator.class;
  }
  
  public Class<? extends DomainnodeUtil> bindDomainmodeUtil() {
    return DomainnodeUtil.class;
  }
  
  public Class<? extends DomainDependencyUtil> bindDomainDependencyUtil() {
    return DomainDependencyUtil.class;
  }
  
  public Class<? extends DomainManager> bindDomainManager() {
    return DomainManager.class;
  }
}
