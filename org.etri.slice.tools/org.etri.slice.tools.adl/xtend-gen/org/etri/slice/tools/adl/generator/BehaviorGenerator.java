package org.etri.slice.tools.adl.generator;

import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.AdaptorCompiler;
import org.etri.slice.tools.adl.generator.compiler.ControlWrapperCompiler;
import org.etri.slice.tools.adl.generator.compiler.EventWrapperCompiler;
import org.etri.slice.tools.adl.generator.compiler.SensorCompiler;
import org.etri.slice.tools.adl.generator.compiler.ServiceCompiler;
import org.etri.slice.tools.adl.generator.compiler.StreamCompiler;

@SuppressWarnings("all")
public class BehaviorGenerator {
  @Inject
  @Extension
  private ControlWrapperCompiler _controlWrapperCompiler;
  
  @Inject
  @Extension
  private AdaptorCompiler _adaptorCompiler;
  
  @Inject
  @Extension
  private EventWrapperCompiler _eventWrapperCompiler;
  
  @Inject
  @Extension
  private StreamCompiler _streamCompiler;
  
  @Inject
  @Extension
  private ServiceCompiler _serviceCompiler;
  
  @Inject
  @Extension
  private SensorCompiler _sensorCompiler;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  protected void _generateAdaptor(final Context it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getAgentFullyQualifiedName(agent).replace(".", "/");
    String _agentMavenSrcHome = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus = (_agentMavenSrcHome + package_);
    String _plus_1 = (_plus + "/adaptor/");
    String _name = it.getName();
    String _plus_2 = (_plus_1 + _name);
    final String adaptor = (_plus_2 + "Adaptor.java");
    fsa.generateFile(adaptor, this._adaptorCompiler.compileAdaptor(it, agent));
    String _agentMavenSrcHome_1 = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus_3 = (_agentMavenSrcHome_1 + package_);
    String _plus_4 = (_plus_3 + "/stream/");
    String _name_1 = it.getName();
    String _plus_5 = (_plus_4 + _name_1);
    final String stream = (_plus_5 + "Stream.java");
    fsa.generateFile(stream, this._streamCompiler.compileStream(it, agent));
  }
  
  protected void _generateAdaptor(final Event it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getAgentFullyQualifiedName(agent).replace(".", "/");
    String _agentMavenSrcHome = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus = (_agentMavenSrcHome + package_);
    String _plus_1 = (_plus + "/adaptor/");
    String _name = it.getName();
    String _plus_2 = (_plus_1 + _name);
    final String adaptor = (_plus_2 + "Adaptor.java");
    fsa.generateFile(adaptor, this._adaptorCompiler.compileAdaptor(it, agent));
    String _agentMavenSrcHome_1 = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus_3 = (_agentMavenSrcHome_1 + package_);
    String _plus_4 = (_plus_3 + "/stream/");
    String _name_1 = it.getName();
    String _plus_5 = (_plus_4 + _name_1);
    final String stream = (_plus_5 + "Stream.java");
    fsa.generateFile(stream, this._streamCompiler.compileStream(it, agent));
  }
  
  public void generateEventWrapper(final JvmTypeReference it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getAgentFullyQualifiedName(agent).replace(".", "/");
    String _agentMavenSrcHome = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus = (_agentMavenSrcHome + package_);
    String _plus_1 = (_plus + "/wrapper/");
    String _simpleName = it.getSimpleName();
    String _plus_2 = (_plus_1 + _simpleName);
    final String wrapper = (_plus_2 + "Channel.java");
    fsa.generateFile(wrapper, this._eventWrapperCompiler.compileEventWrapper(it.getType(), agent));
    String _agentMavenSrcHome_1 = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus_3 = (_agentMavenSrcHome_1 + package_);
    String _plus_4 = (_plus_3 + "/stream/");
    String _simpleName_1 = it.getSimpleName();
    String _plus_5 = (_plus_4 + _simpleName_1);
    final String stream = (_plus_5 + "Stream.java");
    fsa.generateFile(stream, this._streamCompiler.compileStream(it, agent));
  }
  
  public void generateControlWrapper(final JvmType it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getAgentFullyQualifiedName(agent).replace(".", "/");
    String _agentMavenSrcHome = this._outputPathUtils.getAgentMavenSrcHome(agent);
    String _plus = (_agentMavenSrcHome + package_);
    String _plus_1 = (_plus + "/wrapper/");
    String _simpleName = it.getSimpleName();
    String _plus_2 = (_plus_1 + _simpleName);
    final String wrapper = (_plus_2 + "Wrapper.java");
    fsa.generateFile(wrapper, this._controlWrapperCompiler.compileWrapper(it, agent));
  }
  
  public void generateSensor(final Context it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getDeviceFullyQualifiedName(agent).replace(".", "/");
    String _deviceMavenSrcHome = this._outputPathUtils.getDeviceMavenSrcHome(agent);
    String _plus = (_deviceMavenSrcHome + package_);
    String _plus_1 = (_plus + "/");
    String _name = it.getName();
    String _plus_2 = (_plus_1 + _name);
    final String file = (_plus_2 + "Sensor.java");
    fsa.generateFile(file, this._sensorCompiler.sensorCompile(it, agent));
  }
  
  public void generateService(final JvmType it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getDeviceFullyQualifiedName(agent).replace(".", "/");
    String _deviceMavenSrcHome = this._outputPathUtils.getDeviceMavenSrcHome(agent);
    String _plus = (_deviceMavenSrcHome + package_);
    String _plus_1 = (_plus + "/");
    String _simpleName = it.getSimpleName();
    String _plus_2 = (_plus_1 + _simpleName);
    final String file = (_plus_2 + "Service.java");
    fsa.generateFile(file, this._serviceCompiler.serviceCompile(it, agent));
  }
  
  public void generateAdaptor(final EObject it, final AgentDeclaration agent, final IFileSystemAccess fsa) {
    if (it instanceof Context) {
      _generateAdaptor((Context)it, agent, fsa);
      return;
    } else if (it instanceof Event) {
      _generateAdaptor((Event)it, agent, fsa);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(it, agent, fsa).toString());
    }
  }
}
