package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImplCustom;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.generator.GeneratorUtils;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil;

@SuppressWarnings("all")
public class ControlWrapperCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  @Inject
  @Extension
  private DomainDependencyUtil _domainDependencyUtil;
  
  public CharSequence compileWrapper(final JvmType it, final AgentDeclaration agent) {
    CharSequence _xblockexpression = null;
    {
      final String domain = this._domainDependencyUtil.getDomain(this._iQualifiedNameProvider.getFullyQualifiedName(it).toString(), "org.etri.slice.commons", "service");
      StringConcatenation _builder = new StringConcatenation();
      final ImportManager importManager = new ImportManager(true);
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      final CharSequence body = this.body(it, importManager);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
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
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  private CharSequence body(final JvmType it, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      importManager.addImportFor(it);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public class ");
      String _simpleName = it.getSimpleName();
      _builder.append(_simpleName);
      _builder.append("Wrapper implements ");
      String _simpleName_1 = it.getSimpleName();
      _builder.append(_simpleName_1);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("@Requires");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("private ");
      String _simpleName_2 = it.getSimpleName();
      _builder.append(_simpleName_2, "\t");
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
      String _simpleName_3 = it.getSimpleName();
      _builder.append(_simpleName_3, "\t");
      _builder.append("Wrapper() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("m_wm.addServiceWrapper(");
      String _simpleName_4 = it.getSimpleName();
      _builder.append(_simpleName_4, "\t\t");
      _builder.append(".id, this);");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        int _size = ((JvmGenericType) it).getSuperTypes().size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append("\t");
          CharSequence _compileSuperType = this.compileSuperType(((JvmGenericType) it), importManager);
          _builder.append(_compileSuperType, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        Iterable<JvmField> _declaredFields = ((JvmGenericType) it).getDeclaredFields();
        for(final JvmField f : _declaredFields) {
          _builder.append("\t");
          CharSequence _compile = this.compile(f, importManager);
          _builder.append(_compile, "\t");
          _builder.append("\t\t\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        Iterable<JvmOperation> _declaredOperations = ((JvmGenericType) it).getDeclaredOperations();
        for(final JvmOperation o : _declaredOperations) {
          _builder.append("\t");
          CharSequence _compile_1 = this.compile(o, importManager);
          _builder.append(_compile_1, "\t");
          _builder.append("\t\t\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  private CharSequence compileSuperType(final JvmGenericType it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<JvmTypeReference> _superTypes = it.getSuperTypes();
      for(final JvmTypeReference superType : _superTypes) {
        JvmType _type = superType.getType();
        final JvmGenericTypeImplCustom jvmType = ((JvmGenericTypeImplCustom) _type);
        _builder.newLineIfNotEmpty();
        {
          boolean _isInterface = jvmType.isInterface();
          if (_isInterface) {
            CharSequence _compile = this.compile(jvmType, importManager);
            _builder.append(_compile);
            _builder.append("\t\t\t\t");
            _builder.newLineIfNotEmpty();
            Object _compileSuperType = this.compileSuperType(jvmType, importManager);
            _builder.append(_compileSuperType);
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  private CharSequence compile(final JvmGenericType it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Iterable<JvmOperation> _declaredOperations = it.getDeclaredOperations();
      for(final JvmOperation method : _declaredOperations) {
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("public ");
        String _shortName = this._generatorUtils.shortName(method.getReturnType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _simpleName = method.getSimpleName();
        _builder.append(_simpleName);
        CharSequence _parameters = this._generatorUtils.parameters(method, importManager);
        _builder.append(_parameters);
        CharSequence _exceptions = this._generatorUtils.exceptions(method, importManager);
        _builder.append(_exceptions);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          boolean _equals = method.getReturnType().getType().getSimpleName().equals("void");
          boolean _not = (!_equals);
          if (_not) {
            _builder.append("return ");
          }
        }
        _builder.append("m_proxy.");
        String _simpleName_1 = method.getSimpleName();
        _builder.append(_simpleName_1, "\t");
        _builder.append("(");
        {
          EList<JvmFormalParameter> _parameters_1 = method.getParameters();
          boolean _hasElements = false;
          for(final JvmFormalParameter p : _parameters_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            String _name = p.getName();
            _builder.append(_name, "\t");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private CharSequence compile(final JvmFeature it, final ImportManager importManager) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof JvmField) {
      _matched=true;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("public ");
      String _shortName = this._generatorUtils.shortName(((JvmField)it).getType(), importManager);
      _builder.append(_shortName);
      _builder.append(" get");
      String _firstUpper = StringExtensions.toFirstUpper(((JvmField)it).getSimpleName());
      _builder.append(_firstUpper);
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("return m_proxy.get");
      String _firstUpper_1 = StringExtensions.toFirstUpper(((JvmField)it).getSimpleName());
      _builder.append(_firstUpper_1, "\t");
      _builder.append("();");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("@Override\t\t        ");
      _builder.newLine();
      _builder.append("public void set");
      String _firstUpper_2 = StringExtensions.toFirstUpper(((JvmField)it).getSimpleName());
      _builder.append(_firstUpper_2);
      _builder.append("(");
      String _shortName_1 = this._generatorUtils.shortName(((JvmField)it).getType(), importManager);
      _builder.append(_shortName_1);
      _builder.append(" ");
      String _simpleName = ((JvmField)it).getSimpleName();
      _builder.append(_simpleName);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("m_proxy.set");
      String _firstUpper_3 = StringExtensions.toFirstUpper(((JvmField)it).getSimpleName());
      _builder.append(_firstUpper_3, "\t");
      _builder.append("(");
      String _simpleName_1 = ((JvmField)it).getSimpleName();
      _builder.append(_simpleName_1, "\t");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _switchResult = _builder;
    }
    if (!_matched) {
      if (it instanceof JvmOperation) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("public ");
        String _shortName = this._generatorUtils.shortName(((JvmOperation)it).getReturnType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _simpleName = ((JvmOperation)it).getSimpleName();
        _builder.append(_simpleName);
        CharSequence _parameters = this._generatorUtils.parameters(((JvmOperation)it), importManager);
        _builder.append(_parameters);
        CharSequence _exceptions = this._generatorUtils.exceptions(((JvmOperation)it), importManager);
        _builder.append(_exceptions);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          boolean _equals = ((JvmOperation)it).getReturnType().getType().getSimpleName().equals("void");
          boolean _not = (!_equals);
          if (_not) {
            _builder.append("return ");
          }
        }
        _builder.append("m_proxy.");
        String _simpleName_1 = ((JvmOperation)it).getSimpleName();
        _builder.append(_simpleName_1, "\t");
        _builder.append("(");
        {
          EList<JvmFormalParameter> _parameters_1 = ((JvmOperation)it).getParameters();
          boolean _hasElements = false;
          for(final JvmFormalParameter p : _parameters_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            String _name = p.getName();
            _builder.append(_name, "\t");
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
}
