/**
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.etri.slice.tools.adl;

import java.util.Set;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.generator.OutputConfigurationProvider;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * @author yhsuh - Initial contribution and API
 */
@SuppressWarnings("all")
public class DomainModelOutputConfigurationProvider extends OutputConfigurationProvider {
  public final static String DOMAINMODEL_GEN = "./org.etri.slice.generated";
  
  @Override
  public Set<OutputConfiguration> getOutputConfigurations() {
    Set<OutputConfiguration> _outputConfigurations = super.getOutputConfigurations();
    final Procedure1<Set<OutputConfiguration>> _function = (Set<OutputConfiguration> it) -> {
      OutputConfiguration _head = IterableExtensions.<OutputConfiguration>head(it);
      _head.setOutputDirectory(DomainModelOutputConfigurationProvider.DOMAINMODEL_GEN);
    };
    return ObjectExtensions.<Set<OutputConfiguration>>operator_doubleArrow(_outputConfigurations, _function);
  }
}
