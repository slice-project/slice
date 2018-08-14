package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.generator.GeneratorUtils;

@SuppressWarnings("all")
public class ServiceCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  public CharSequence serviceCompile(final JvmType it, final AgentDeclaration agent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("\t");
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    final CharSequence body = this.compile(it, importManager);
    _builder.newLineIfNotEmpty();
    {
      EObject _eContainer = agent.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("\t");
        _builder.append("package org.etri.slice.devices.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(agent.eContainer());
        _builder.append(_fullyQualifiedName, "\t");
        _builder.append(".");
        String _lowerCase = agent.getName().toLowerCase();
        _builder.append(_lowerCase, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.apache.felix.ipojo.annotations.Component;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.apache.felix.ipojo.annotations.Instantiate;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.apache.felix.ipojo.annotations.Invalidate;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.apache.felix.ipojo.annotations.Provides;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.apache.felix.ipojo.annotations.Validate;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.etri.slice.commons.SliceException;");
    _builder.newLine();
    {
      List<String> _imports = importManager.getImports();
      for(final String i : _imports) {
        _builder.append("\t");
        _builder.append("import ");
        _builder.append(i, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("import org.slf4j.Logger;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import org.slf4j.LoggerFactory;\t\t");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Component(publicFactory=false, immediate=true)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Provides");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Instantiate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(body, "\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence compile(final JvmType it, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      importManager.addImportFor(it);
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public class ");
      String _simpleName = it.getSimpleName();
      _builder.append(_simpleName, "\t");
      _builder.append("Service implements ");
      String _simpleName_1 = it.getSimpleName();
      _builder.append(_simpleName_1, "\t");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("private static Logger s_logger = LoggerFactory.getLogger(");
      String _simpleName_2 = it.getSimpleName();
      _builder.append(_simpleName_2, "\t\t");
      _builder.append("Service.class);\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("private ");
      String _simpleName_3 = it.getSimpleName();
      _builder.append(_simpleName_3, "\t\t");
      _builder.append(" m_service;\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("@Validate");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("public void init() throws SliceException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("@Invalidate");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("public void fini() throws SliceException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.newLine();
      {
        int _size = ((JvmGenericType) it).getSuperTypes().size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          _builder.append("\t\t");
          CharSequence _compileSuperType = this.compileSuperType(((JvmGenericType) it), importManager);
          _builder.append(_compileSuperType, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        Iterable<JvmField> _declaredFields = ((JvmGenericType) it).getDeclaredFields();
        for(final JvmField f : _declaredFields) {
          _builder.append("\t\t");
          CharSequence _compileFeature = this.compileFeature(f, importManager);
          _builder.append(_compileFeature, "\t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.newLine();
        }
      }
      {
        Iterable<JvmOperation> _declaredOperations = ((JvmGenericType) it).getDeclaredOperations();
        for(final JvmOperation o : _declaredOperations) {
          _builder.append("\t\t");
          CharSequence _compileFeature_1 = this.compileFeature(o, importManager);
          _builder.append(_compileFeature_1, "\t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.append("\t\t");
          _builder.newLine();
        }
      }
      _builder.append("\t");
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
  
  private CharSequence compileFeature(final JvmFeature it, final ImportManager importManager) {
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
      _builder.append("return m_service.get");
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
      _builder.append("m_service.set");
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
        _builder.append("m_service.");
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
            _builder.append(" ");
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
