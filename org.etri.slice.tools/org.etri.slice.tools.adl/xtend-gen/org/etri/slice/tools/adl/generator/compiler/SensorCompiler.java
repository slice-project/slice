package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.generator.GeneratorUtils;

@SuppressWarnings("all")
public class SensorCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  public CharSequence sensorCompile(final JvmTypeReference it, final AgentDeclaration agent) {
    StringConcatenation _builder = new StringConcatenation();
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    final CharSequence body = this.compile(it, importManager);
    _builder.newLineIfNotEmpty();
    {
      EObject _eContainer = it.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("package org.etri.slice.devices.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(agent.eContainer());
        _builder.append(_fullyQualifiedName);
        _builder.append(".");
        String _lowerCase = agent.getName().toLowerCase();
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
    _builder.append("import org.apache.felix.ipojo.annotations.Invalidate;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Provides;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Validate;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.handlers.event.Publishes;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.handlers.event.publisher.Publisher;");
    _builder.newLine();
    _builder.append("import org.etri.slice.commons.Sensor;");
    _builder.newLine();
    _builder.append("import org.etri.slice.commons.SliceException;");
    _builder.newLine();
    {
      List<String> _imports = importManager.getImports();
      for(final String i : _imports) {
        _builder.append("import ");
        _builder.append(i);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("import org.slf4j.Logger;");
    _builder.newLine();
    _builder.append("import org.slf4j.LoggerFactory;\t\t");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Component(publicFactory=false, immediate=true)");
    _builder.newLine();
    _builder.append("@Provides");
    _builder.newLine();
    _builder.append("@Instantiate");
    _builder.newLine();
    _builder.append(body);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence compile(final JvmTypeReference it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class ");
    String _shortName = this._generatorUtils.shortName(it, importManager);
    _builder.append(_shortName);
    _builder.append("Sensor implements Sensor {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private static Logger s_logger = LoggerFactory.getLogger(");
    String _simpleName = it.getSimpleName();
    _builder.append(_simpleName, "\t");
    _builder.append("Sensor.class);\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Publishes(name=\"");
    String _simpleName_1 = it.getSimpleName();
    _builder.append(_simpleName_1, "\t");
    _builder.append("Sensor\", topics=");
    String _simpleName_2 = it.getSimpleName();
    _builder.append(_simpleName_2, "\t");
    _builder.append(".topic, dataKey=");
    String _simpleName_3 = it.getSimpleName();
    _builder.append(_simpleName_3, "\t");
    _builder.append(".dataKey)");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private Publisher m_publisher;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Validate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void start() throws SliceException {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Invalidate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void stop() throws SliceException {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
