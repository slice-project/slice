package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.etri.slice.tools.adl.domainmodel.Command;
import org.etri.slice.tools.adl.domainmodel.CommandContext;
import org.etri.slice.tools.adl.domainmodel.CommandSet;
import org.etri.slice.tools.adl.generator.GeneratorUtils;

@SuppressWarnings("all")
public class CommandWrapperCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  private Map<String, Command> m_commands = new HashMap<String, Command>();
  
  public CharSequence compileCommandWrapper(final AgentDeclaration it, final CommandSet commandSet) {
    CharSequence _xblockexpression = null;
    {
      this.m_commands.clear();
      EList<Command> _commands = commandSet.getCommands();
      for (final Command c : _commands) {
        this.m_commands.put(this.toKey(c.getAction(), c.getMethod()), c);
      }
      _xblockexpression = this.compileWrapper(it, commandSet);
    }
    return _xblockexpression;
  }
  
  public CharSequence compileWrapper(final AgentDeclaration agent, final CommandSet it) {
    StringConcatenation _builder = new StringConcatenation();
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    final CharSequence body = this.body(it, importManager);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
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
        _builder.append(".wrapper;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("import org.apache.edgent.execution.services.ControlService;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Component;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Instantiate;");
    _builder.newLine();
    _builder.append("import org.apache.felix.ipojo.annotations.Requires;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.agent.Agent;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.learning.Action;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.learning.Action.ActionBuilder;");
    _builder.newLine();
    _builder.append("import org.etri.slice.api.learning.ActionLogger;");
    _builder.newLine();
    _builder.append("import org.slf4j.Logger;");
    _builder.newLine();
    _builder.append("import org.slf4j.LoggerFactory;\t\t");
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
  
  private CharSequence body(final CommandSet it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class ");
    String _simpleName = it.getControl().getSimpleName();
    _builder.append(_simpleName);
    _builder.append("Commander implements ");
    String _shortName = this._generatorUtils.shortName(it.getControl(), importManager);
    _builder.append(_shortName);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private static Logger s_logger = LoggerFactory.getLogger(");
    String _simpleName_1 = it.getControl().getSimpleName();
    _builder.append(_simpleName_1, "\t");
    _builder.append("Commander.class);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    String _simpleName_2 = it.getControl().getSimpleName();
    _builder.append(_simpleName_2, "\t");
    _builder.append(" m_proxy;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ActionLogger m_logger;");
    _builder.newLine();
    _builder.append("\t");
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
    _builder.append("public ");
    String _simpleName_3 = it.getControl().getSimpleName();
    _builder.append(_simpleName_3, "\t");
    _builder.append("Commander() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("ControlService control = m_agent.getService(ControlService.class);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("control.registerControl(this.getClass().getSimpleName(), ");
    String _simpleName_4 = it.getControl().getSimpleName();
    _builder.append(_simpleName_4, "\t\t");
    _builder.append(".id, null, ");
    String _simpleName_5 = it.getControl().getSimpleName();
    _builder.append(_simpleName_5, "\t\t");
    _builder.append(".class, this);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      JvmType _type = it.getControl().getType();
      int _size = ((JvmGenericType) _type).getSuperTypes().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("\t");
        JvmType _type_1 = it.getControl().getType();
        CharSequence _compileSuperType = this.compileSuperType(((JvmGenericType) _type_1), importManager);
        _builder.append(_compileSuperType, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      JvmType _type_2 = it.getControl().getType();
      Iterable<JvmField> _declaredFields = ((JvmGenericType) _type_2).getDeclaredFields();
      for(final JvmField f : _declaredFields) {
        _builder.append("\t");
        CharSequence _compileFeature = this.compileFeature(f, it.getControl(), importManager);
        _builder.append(_compileFeature, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      JvmType _type_3 = it.getControl().getType();
      Iterable<JvmOperation> _declaredOperations = ((JvmGenericType) _type_3).getDeclaredOperations();
      for(final JvmOperation o : _declaredOperations) {
        _builder.append("\t");
        CharSequence _compileFeature_1 = this.compileFeature(o, it.getControl(), importManager);
        _builder.append(_compileFeature_1, "\t");
        _builder.append("\t\t\t\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("private void logAction(Action action) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("m_logger.log(action);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("} ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("catch ( Exception e ) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("s_logger.error(\"ERR : \" + e.getMessage());");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
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
  
  private CharSequence compileFeature(final JvmFeature it, final JvmTypeReference control, final ImportManager importManager) {
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
      String _firstUpper_2 = StringExtensions.toFirstUpper(((JvmField)it).getSimpleName());
      final String methodName = ("set" + _firstUpper_2);
      _builder.newLineIfNotEmpty();
      final String actionKey = this.toKey(control.getType(), methodName);
      _builder.newLineIfNotEmpty();
      _builder.append("@Override\t\t        ");
      _builder.newLine();
      _builder.append("public void ");
      _builder.append(methodName);
      _builder.append("(");
      String _shortName_1 = this._generatorUtils.shortName(((JvmField)it).getType(), importManager);
      _builder.append(_shortName_1);
      _builder.append(" ");
      String _simpleName = ((JvmField)it).getSimpleName();
      _builder.append(_simpleName);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("m_proxy.");
      _builder.append(methodName, "\t");
      _builder.append("(");
      String _simpleName_1 = ((JvmField)it).getSimpleName();
      _builder.append(_simpleName_1, "\t");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _actionLog = this.actionLog(((JvmField)it), actionKey, importManager);
      _builder.append(_actionLog, "\t");
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
  
  private CharSequence actionLog(final JvmField it, final CharSequence actionKey, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _containsKey = this.m_commands.containsKey(actionKey);
      if (_containsKey) {
        final Command command = this.m_commands.get(actionKey);
        _builder.newLineIfNotEmpty();
        _builder.append("ActionBuilder builder = Action.builder();");
        _builder.newLine();
        _builder.append("builder.setRelation(\"");
        String _name = command.getName();
        _builder.append(_name);
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
        {
          EList<CommandContext> _contexts = command.getContexts();
          for(final CommandContext c : _contexts) {
            _builder.append("builder.addContext(");
            JvmType _context = c.getContext();
            CharSequence _serialize = importManager.serialize(((JvmGenericType) _context));
            _builder.append(_serialize);
            _builder.append(".Field.");
            String _property = c.getProperty();
            _builder.append(_property);
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("builder.setAction(");
        _builder.append(actionKey);
        _builder.append(", ");
        String _simpleName = it.getSimpleName();
        _builder.append(_simpleName);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("logAction(builder.build());\t\t");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private String toKey(final JvmType it, final String method) {
    String _simpleName = it.getSimpleName();
    String _plus = (_simpleName + ".");
    return (_plus + method);
  }
}
