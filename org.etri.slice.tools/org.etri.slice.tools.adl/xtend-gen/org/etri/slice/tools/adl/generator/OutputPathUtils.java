/**
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.etri.slice.tools.adl.generator;

import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AbstractElement;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.domainmodel.Event;

/**
 * @author yhsuh - Initial contribution and API
 */
@SuppressWarnings("all")
public class OutputPathUtils {
  public final static String sliceAgents = "org.etri.slice.agents";
  
  public final static String sliceCommons = "org.etri.slice.commons";
  
  public final static String sliceDevices = "org.etri.slice.devices";
  
  public final static String sliceDistribution = "org.etri.slice.distribution";
  
  public final static String sliceModels = "org.etri.slice.models";
  
  public final static String sliceRules = "org.etri.slice.rules";
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  protected String _getSliceFullyQualifiedName(final Context it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((OutputPathUtils.sliceCommons + ".") + _fullyQualifiedName);
    return (_plus + ".context");
  }
  
  protected String _getSliceFullyQualifiedName(final Event it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((OutputPathUtils.sliceCommons + ".") + _fullyQualifiedName);
    return (_plus + ".event");
  }
  
  protected String _getSliceFullyQualifiedName(final org.etri.slice.tools.adl.domainmodel.Exception it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    return ((OutputPathUtils.sliceCommons + ".") + _fullyQualifiedName);
  }
  
  protected String _getSliceFullyQualifiedName(final Control it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((OutputPathUtils.sliceCommons + ".") + _fullyQualifiedName);
    return (_plus + ".service");
  }
  
  public String getCommonsMavenHome(final DomainDeclaration it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it);
    String _plus = ((((OutputPathUtils.sliceModels + "/") + OutputPathUtils.sliceCommons) + ".") + _fullyQualifiedName);
    return (_plus + "/");
  }
  
  protected String _getCommonsMavenSrcHome(final Context it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((((OutputPathUtils.sliceModels + "/") + OutputPathUtils.sliceCommons) + ".") + _fullyQualifiedName);
    return (_plus + "/src/main/java/");
  }
  
  protected String _getCommonsMavenSrcHome(final Event it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((((OutputPathUtils.sliceModels + "/") + OutputPathUtils.sliceCommons) + ".") + _fullyQualifiedName);
    return (_plus + "/src/main/java/");
  }
  
  protected String _getCommonsMavenSrcHome(final org.etri.slice.tools.adl.domainmodel.Exception it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((((OutputPathUtils.sliceModels + "/") + OutputPathUtils.sliceCommons) + ".") + _fullyQualifiedName);
    return (_plus + "/src/main/java/");
  }
  
  protected String _getCommonsMavenSrcHome(final Control it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((((OutputPathUtils.sliceModels + "/") + OutputPathUtils.sliceCommons) + ".") + _fullyQualifiedName);
    return (_plus + "/src/main/java/");
  }
  
  public String getAgentFullyQualifiedName(final AgentDeclaration it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((OutputPathUtils.sliceAgents + ".") + _fullyQualifiedName);
    String _plus_1 = (_plus + ".");
    String _lowerCase = it.getName().toLowerCase();
    return (_plus_1 + _lowerCase);
  }
  
  public String getAgentMavenHome(final AgentDeclaration it) {
    String _agentFullyQualifiedName = this.getAgentFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceAgents + "/") + _agentFullyQualifiedName);
    return (_plus + "/");
  }
  
  public String getAgentMavenSrcHome(final AgentDeclaration it) {
    String _agentFullyQualifiedName = this.getAgentFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceAgents + "/") + _agentFullyQualifiedName);
    return (_plus + "/src/main/java/");
  }
  
  public String getAgentMavenResHome(final AgentDeclaration it) {
    String _agentFullyQualifiedName = this.getAgentFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceAgents + "/") + _agentFullyQualifiedName);
    return (_plus + "/src/main/resources/");
  }
  
  public String getDeviceFullyQualifiedName(final AgentDeclaration it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((OutputPathUtils.sliceDevices + ".") + _fullyQualifiedName);
    String _plus_1 = (_plus + ".");
    String _lowerCase = it.getName().toLowerCase();
    return (_plus_1 + _lowerCase);
  }
  
  public String getDeviceMavenHome(final AgentDeclaration it) {
    String _deviceFullyQualifiedName = this.getDeviceFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceDevices + "/") + _deviceFullyQualifiedName);
    return (_plus + "/");
  }
  
  public String getDeviceMavenSrcHome(final AgentDeclaration it) {
    String _deviceFullyQualifiedName = this.getDeviceFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceDevices + "/") + _deviceFullyQualifiedName);
    return (_plus + "/src/main/java/");
  }
  
  public String getDeviceMavenResHome(final AgentDeclaration it) {
    String _deviceFullyQualifiedName = this.getDeviceFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceDevices + "/") + _deviceFullyQualifiedName);
    return (_plus + "/src/main/resources/");
  }
  
  public String getRuleFullyQualifiedName(final AgentDeclaration it) {
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    String _plus = ((OutputPathUtils.sliceRules + ".") + _fullyQualifiedName);
    String _plus_1 = (_plus + ".");
    String _lowerCase = it.getName().toLowerCase();
    return (_plus_1 + _lowerCase);
  }
  
  public String getRuleMavenHome(final AgentDeclaration it) {
    String _ruleFullyQualifiedName = this.getRuleFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceRules + "/") + _ruleFullyQualifiedName);
    return (_plus + "/");
  }
  
  public String getRuleMavenResHome(final AgentDeclaration it) {
    String _ruleFullyQualifiedName = this.getRuleFullyQualifiedName(it);
    String _plus = ((OutputPathUtils.sliceRules + "/") + _ruleFullyQualifiedName);
    return (_plus + "/src/main/resources/");
  }
  
  public String getSliceFullyQualifiedName(final AbstractElement it) {
    if (it instanceof Context) {
      return _getSliceFullyQualifiedName((Context)it);
    } else if (it instanceof Control) {
      return _getSliceFullyQualifiedName((Control)it);
    } else if (it instanceof Event) {
      return _getSliceFullyQualifiedName((Event)it);
    } else if (it instanceof org.etri.slice.tools.adl.domainmodel.Exception) {
      return _getSliceFullyQualifiedName((org.etri.slice.tools.adl.domainmodel.Exception)it);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(it).toString());
    }
  }
  
  public String getCommonsMavenSrcHome(final AbstractElement it) {
    if (it instanceof Context) {
      return _getCommonsMavenSrcHome((Context)it);
    } else if (it instanceof Control) {
      return _getCommonsMavenSrcHome((Control)it);
    } else if (it instanceof Event) {
      return _getCommonsMavenSrcHome((Event)it);
    } else if (it instanceof org.etri.slice.tools.adl.domainmodel.Exception) {
      return _getCommonsMavenSrcHome((org.etri.slice.tools.adl.domainmodel.Exception)it);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(it).toString());
    }
  }
}
