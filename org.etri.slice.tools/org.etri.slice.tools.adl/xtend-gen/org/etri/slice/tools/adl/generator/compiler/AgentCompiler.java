package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;

@SuppressWarnings("all")
public class AgentCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  public CharSequence agentCompile(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EObject _eContainer = it.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("package org.etri.slice.agents.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
        _builder.append(_fullyQualifiedName);
        _builder.append(".");
        String _lowerCase = it.getName().toLowerCase();
        _builder.append(_lowerCase);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Component;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Instantiate;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Property;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Provides;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.agent.Agent;");
    _builder.newLine();
    _builder.append("import org.etri.slice.core.agent.AbstractAgent;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Component(publicFactory=false, immediate=true)");
    _builder.newLine();
    _builder.append("@Provides");
    _builder.newLine();
    _builder.append("@Instantiate");
    _builder.newLine();
    CharSequence _agentBody = this.agentBody(it);
    _builder.append(_agentBody);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence agentBody(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class ");
    String _name = it.getName();
    _builder.append(_name);
    _builder.append(" extends AbstractAgent implements Agent {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Property(name=\"groupId\", value=\"");
    String _group_id = it.getRuleSet().getGroup_id();
    _builder.append(_group_id, "\t");
    _builder.append("\")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("public String groupId;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Property(name=\"artifactId\", value=\"org.etri.slice.rules.");
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    _builder.append(_fullyQualifiedName, "\t");
    _builder.append(".");
    String _artifact_id = it.getRuleSet().getArtifact_id();
    _builder.append(_artifact_id, "\t");
    _builder.append("\")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("public String artifactId;\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("//\t@Property(name=\"version\", value=\"0.0.1\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String version;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getGroupId() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return groupId;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getArtifactId() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return artifactId;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getVersion() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return version;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
