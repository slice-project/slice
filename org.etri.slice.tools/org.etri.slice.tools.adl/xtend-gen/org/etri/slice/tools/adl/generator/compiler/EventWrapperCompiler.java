package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.List;
import java.util.UUID;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil;

@SuppressWarnings("all")
public class EventWrapperCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private DomainDependencyUtil _domainDependencyUtil;
  
  public CharSequence compileEventWrapper(final JvmType it, final AgentDeclaration agent) {
    CharSequence _xblockexpression = null;
    {
      final String domain = this._domainDependencyUtil.getDomain(this._iQualifiedNameProvider.getFullyQualifiedName(it).toString(), "org.etri.slice.commons", "event");
      StringConcatenation _builder = new StringConcatenation();
      final ImportManager importManager = new ImportManager(true);
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      final CharSequence body = this.body(it, agent, importManager);
      _builder.newLineIfNotEmpty();
      {
        if ((domain != null)) {
          _builder.append("package org.etri.slice.agents.");
          _builder.append(domain);
          _builder.append(".");
          String _lowerCase = agent.getName().toLowerCase();
          _builder.append(_lowerCase);
          _builder.append(".wrapper;");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      _builder.append("import org.apache.felix.ipojo.annotations.Component;");
      _builder.newLine();
      _builder.append("import org.apache.felix.ipojo.annotations.Instantiate;");
      _builder.newLine();
      _builder.append("import org.apache.felix.ipojo.annotations.Invalidate;");
      _builder.newLine();
      _builder.append("import org.apache.felix.ipojo.annotations.Property;");
      _builder.newLine();
      _builder.append("import org.apache.felix.ipojo.annotations.Requires;");
      _builder.newLine();
      _builder.append("import org.apache.felix.ipojo.annotations.Validate;");
      _builder.newLine();
      _builder.append("import org.etri.slice.api.agent.Agent;");
      _builder.newLine();
      _builder.append("import org.etri.slice.api.inference.WorkingMemory;");
      _builder.newLine();
      _builder.append("import org.etri.slice.api.perception.EventStream;");
      _builder.newLine();
      _builder.append("import org.etri.slice.core.perception.MqttEventPublisher;");
      _builder.newLine();
      _builder.append("import org.etri.slice.agents.");
      QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(agent.eContainer());
      _builder.append(_fullyQualifiedName);
      _builder.append(".");
      String _lowerCase_1 = agent.getName().toLowerCase();
      _builder.append(_lowerCase_1);
      _builder.append(".stream.");
      String _simpleName = it.getSimpleName();
      _builder.append(_simpleName);
      _builder.append("Stream;");
      _builder.newLineIfNotEmpty();
      {
        List<String> _imports = importManager.getImports();
        for(final String i : _imports) {
          _builder.append("import ");
          _builder.append(i);
          _builder.append(";");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      _builder.append(body);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence body(final JvmType it, final AgentDeclaration agent, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Component");
    _builder.newLine();
    _builder.append("@Instantiate");
    _builder.newLine();
    _builder.append("public class ");
    String _simpleName = it.getSimpleName();
    _builder.append(_simpleName);
    _builder.append("Channel extends MqttEventPublisher<");
    CharSequence _serialize = importManager.serialize(((JvmGenericType) it));
    _builder.append(_serialize);
    _builder.append("> {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private static final long serialVersionUID = ");
    long _mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
    _builder.append(_mostSignificantBits, "\t");
    _builder.append("L;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Property(name=\"topic\", value=");
    String _simpleName_1 = it.getSimpleName();
    _builder.append(_simpleName_1, "\t");
    _builder.append(".TOPIC)");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private String m_topic;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Property(name=\"url\", value=\"tcp://");
    String _ip = agent.getAgency().getIp();
    _builder.append(_ip, "\t");
    _builder.append(":");
    int _port = agent.getAgency().getPort();
    _builder.append(_port, "\t");
    _builder.append("\")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private String m_url;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private WorkingMemory m_wm;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private Agent m_agent;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires(from=");
    String _simpleName_2 = it.getSimpleName();
    _builder.append(_simpleName_2, "\t");
    _builder.append("Stream.SERVICE_NAME)");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private EventStream<");
    String _simpleName_3 = it.getSimpleName();
    _builder.append(_simpleName_3, "\t");
    _builder.append("> m_streaming;\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected  String getTopicName() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return m_topic;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected String getMqttURL() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return m_url;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected WorkingMemory getWorkingMemory() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return m_wm;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected Agent getAgent() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return m_agent;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected EventStream<");
    String _simpleName_4 = it.getSimpleName();
    _builder.append(_simpleName_4, "\t");
    _builder.append("> getEventStream() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return m_streaming;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Validate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void start() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("super.start();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Invalidate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void stop() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("super.stop();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
