package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.DataType;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.generator.GeneratorUtils;

@SuppressWarnings("all")
public class StreamCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  public CharSequence compileStream(final JvmTypeReference it, final AgentDeclaration agent) {
    StringConcatenation _builder = new StringConcatenation();
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    final CharSequence body = this.body(it, importManager);
    _builder.newLineIfNotEmpty();
    {
      EObject _eContainer = agent.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("package org.etri.slice.agents.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(agent.eContainer());
        _builder.append(_fullyQualifiedName);
        _builder.append(".");
        String _lowerCase = agent.getName().toLowerCase();
        _builder.append(_lowerCase);
        _builder.append(".stream;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("import org.apache.edgent.topology.TStream;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Component;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Instantiate;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Provides;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.perception.EventStream;\t");
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
    _builder.newLine();
    _builder.append(body);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence compileStream(final DataType it, final AgentDeclaration agent) {
    StringConcatenation _builder = new StringConcatenation();
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    final CharSequence body = this.body(it, importManager);
    _builder.newLineIfNotEmpty();
    {
      EObject _eContainer = agent.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("package org.etri.slice.agents.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(agent.eContainer());
        _builder.append(_fullyQualifiedName);
        _builder.append(".");
        String _lowerCase = agent.getName().toLowerCase();
        _builder.append(_lowerCase);
        _builder.append(".stream;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("import org.apache.edgent.topology.TStream;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Component;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Instantiate;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Provides;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.perception.EventStream;\t");
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
    _builder.newLine();
    _builder.append(body);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _body(final JvmTypeReference it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Component(publicFactory=false, immediate=true)");
    _builder.newLine();
    _builder.append("@Provides");
    _builder.newLine();
    _builder.append("@Instantiate(name=");
    CharSequence _serialize = importManager.serialize(it.getType());
    _builder.append(_serialize);
    _builder.append("Stream.SERVICE_NAME)");
    _builder.newLineIfNotEmpty();
    _builder.append("public class ");
    String _simpleName = it.getSimpleName();
    _builder.append(_simpleName);
    _builder.append("Stream implements EventStream<");
    String _simpleName_1 = it.getSimpleName();
    _builder.append(_simpleName_1);
    _builder.append("> {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static final String SERVICE_NAME = \"");
    String _simpleName_2 = it.getSimpleName();
    _builder.append(_simpleName_2, "\t");
    _builder.append("Stream\";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public TStream<");
    String _simpleName_3 = it.getSimpleName();
    _builder.append(_simpleName_3, "\t");
    _builder.append("> process(TStream<");
    String _simpleName_4 = it.getSimpleName();
    _builder.append(_simpleName_4, "\t");
    _builder.append("> stream) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return stream;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _body(final Event it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Component(publicFactory=false, immediate=true)");
    _builder.newLine();
    _builder.append("@Provides");
    _builder.newLine();
    _builder.append("@Instantiate(name=");
    CharSequence _serialize = importManager.serialize(this._generatorUtils.toJvmGenericType(it, this._iQualifiedNameProvider.getFullyQualifiedName(it), "event"));
    _builder.append(_serialize);
    _builder.append("Stream.SERVICE_NAME)");
    _builder.newLineIfNotEmpty();
    _builder.append("public class ");
    String _name = it.getName();
    _builder.append(_name);
    _builder.append("Stream implements EventStream<");
    String _name_1 = it.getName();
    _builder.append(_name_1);
    _builder.append("> {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static final String SERVICE_NAME = \"");
    String _name_2 = it.getName();
    _builder.append(_name_2, "\t");
    _builder.append("Stream\";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public TStream<");
    String _name_3 = it.getName();
    _builder.append(_name_3, "\t");
    _builder.append("> process(TStream<");
    String _name_4 = it.getName();
    _builder.append(_name_4, "\t");
    _builder.append("> stream) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return stream;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence body(final EObject it, final ImportManager importManager) {
    if (it instanceof Event) {
      return _body((Event)it, importManager);
    } else if (it instanceof JvmTypeReference) {
      return _body((JvmTypeReference)it, importManager);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(it, importManager).toString());
    }
  }
}
