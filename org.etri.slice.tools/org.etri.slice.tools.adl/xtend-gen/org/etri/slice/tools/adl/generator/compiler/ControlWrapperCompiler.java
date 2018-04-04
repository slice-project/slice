package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.Operation;
import org.etri.slice.tools.adl.domainmodel.Property;
import org.etri.slice.tools.adl.generator.GeneratorUtils;

@SuppressWarnings("all")
public class ControlWrapperCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  public CharSequence compileWrapper(final Control it, final AgentDeclaration agent) {
    StringConcatenation _builder = new StringConcatenation();
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    final CharSequence body = this.body(it, importManager);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      EObject _eContainer = it.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("package org.etri.slice.agents.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
        _builder.append(_fullyQualifiedName);
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
    _builder.append("import org.apache.felix.ipojo.annotations.Requires;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.inference.WorkingMemory;");
    _builder.newLine();
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
    _builder.append("@Component");
    _builder.newLine();
    _builder.append("@Instantiate");
    _builder.newLine();
    _builder.append(body);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private CharSequence body(final Control it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class ");
    String _name = it.getName();
    _builder.append(_name);
    _builder.append("Wrapper implements ");
    CharSequence _shortName = this._generatorUtils.shortName(it, importManager);
    _builder.append(_shortName);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    String _name_1 = it.getName();
    _builder.append(_name_1, "\t");
    _builder.append(" m_proxy;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private WorkingMemory m_wm;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    String _name_2 = it.getName();
    _builder.append(_name_2, "\t");
    _builder.append("Wrapper() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("m_wm.addServiceWrapper(");
    String _name_3 = it.getName();
    _builder.append(_name_3, "\t\t");
    _builder.append(".id, this);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _compileSuperType = this.compileSuperType(it, importManager);
    _builder.append(_compileSuperType, "\t");
    _builder.newLineIfNotEmpty();
    {
      EList<Feature> _features = it.getFeatures();
      for(final Feature f : _features) {
        _builder.append("\t");
        CharSequence _compile = this.compile(f, importManager);
        _builder.append(_compile, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence compileSuperType(final Control it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Control _superType = it.getSuperType();
      boolean _tripleNotEquals = (_superType != null);
      if (_tripleNotEquals) {
        {
          EList<Feature> _features = it.getSuperType().getFeatures();
          for(final Feature f : _features) {
            CharSequence _compile = this.compile(f, importManager);
            _builder.append(_compile);
            _builder.newLineIfNotEmpty();
            _builder.newLine();
          }
        }
        Object _compileSuperType = this.compileSuperType(it.getSuperType(), importManager);
        _builder.append(_compileSuperType);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  private CharSequence compile(final Feature it, final ImportManager importManager) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof Property) {
      _matched=true;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("public ");
      String _shortName = this._generatorUtils.shortName(((Property)it).getType(), importManager);
      _builder.append(_shortName);
      _builder.append(" get");
      String _firstUpper = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper);
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("return m_proxy.get");
      String _firstUpper_1 = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper_1, "\t");
      _builder.append("();");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("@Override\t\t        ");
      _builder.newLine();
      _builder.append("public void set");
      String _firstUpper_2 = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper_2);
      _builder.append("(");
      String _shortName_1 = this._generatorUtils.shortName(((Property)it).getType(), importManager);
      _builder.append(_shortName_1);
      _builder.append(" ");
      String _name = ((Property)it).getName();
      _builder.append(_name);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("m_proxy.set");
      String _firstUpper_3 = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper_3, "\t");
      _builder.append("(");
      String _name_1 = ((Property)it).getName();
      _builder.append(_name_1, "\t");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _switchResult = _builder;
    }
    if (!_matched) {
      if (it instanceof Operation) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("public ");
        String _shortName = this._generatorUtils.shortName(((Operation)it).getType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _name = ((Operation)it).getName();
        _builder.append(_name);
        CharSequence _parameters = this.parameters(((Operation)it), importManager);
        _builder.append(_parameters);
        CharSequence _exceptions = this.exceptions(((Operation)it), importManager);
        _builder.append(_exceptions);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          boolean _equals = ((Operation)it).getType().getType().getSimpleName().equals("void");
          boolean _not = (!_equals);
          if (_not) {
            _builder.append("return ");
          }
        }
        _builder.append("m_proxy.");
        String _name_1 = ((Operation)it).getName();
        _builder.append(_name_1, "\t");
        _builder.append("(");
        {
          EList<JvmFormalParameter> _params = ((Operation)it).getParams();
          boolean _hasElements = false;
          for(final JvmFormalParameter p : _params) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append(" ");
            String _name_2 = p.getName();
            _builder.append(_name_2, "\t");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _switchResult = _builder;
      }
    }
    return _switchResult;
  }
  
  private CharSequence parameters(final Operation it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      EList<JvmFormalParameter> _params = it.getParams();
      boolean _hasElements = false;
      for(final JvmFormalParameter p : _params) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        String _shortName = this._generatorUtils.shortName(p.getParameterType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _name = p.getName();
        _builder.append(_name);
      }
    }
    _builder.append(")");
    return _builder;
  }
  
  private CharSequence exceptions(final Operation it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      int _size = it.getExceptions().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append(" throws ");
        {
          EList<JvmTypeReference> _exceptions = it.getExceptions();
          boolean _hasElements = false;
          for(final JvmTypeReference e : _exceptions) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "");
            }
            String _shortName = this._generatorUtils.shortName(e, importManager);
            _builder.append(_shortName);
          }
        }
      } else {
      }
    }
    return _builder;
  }
}
