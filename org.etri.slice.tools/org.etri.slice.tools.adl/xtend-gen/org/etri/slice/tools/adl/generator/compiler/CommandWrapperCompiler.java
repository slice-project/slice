package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
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
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.Operation;
import org.etri.slice.tools.adl.domainmodel.Property;
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
      EObject _eContainer = it.eContainer();
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
    String _name = it.getControl().getName();
    _builder.append(_name);
    _builder.append("Commander implements ");
    CharSequence _shortName = this._generatorUtils.shortName(it.getControl(), importManager);
    _builder.append(_shortName);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private static Logger s_logger = LoggerFactory.getLogger(");
    String _name_1 = it.getControl().getName();
    _builder.append(_name_1, "\t");
    _builder.append("Commander.class);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Requires");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    String _name_2 = it.getControl().getName();
    _builder.append(_name_2, "\t");
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
    String _name_3 = it.getControl().getName();
    _builder.append(_name_3, "\t");
    _builder.append("Commander() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("ControlService control = m_agent.getService(ControlService.class);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("control.registerControl(this.getClass().getSimpleName(), ");
    String _name_4 = it.getControl().getName();
    _builder.append(_name_4, "\t\t");
    _builder.append(".id, null, ");
    String _name_5 = it.getControl().getName();
    _builder.append(_name_5, "\t\t");
    _builder.append(".class, this);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      int _size = it.getControl().getSuperTypes().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("\t");
        CharSequence _compileSuperType = this.compileSuperType(this._generatorUtils.toInterface(it.getControl()), importManager);
        _builder.append(_compileSuperType, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList<Feature> _features = it.getControl().getFeatures();
      for(final Feature f : _features) {
        _builder.append("\t");
        CharSequence _compileFeature = this.compileFeature(f, it.getControl(), importManager);
        _builder.append(_compileFeature, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
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
  
  private CharSequence compileFeature(final Feature it, final Control control, final ImportManager importManager) {
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
      String _firstUpper_2 = StringExtensions.toFirstUpper(((Property)it).getName());
      final String methodName = ("set" + _firstUpper_2);
      _builder.newLineIfNotEmpty();
      final String actionKey = this.toKey(control, methodName);
      _builder.newLineIfNotEmpty();
      _builder.append("@Override\t\t        ");
      _builder.newLine();
      _builder.append("public void ");
      _builder.append(methodName);
      _builder.append("(");
      String _shortName_1 = this._generatorUtils.shortName(((Property)it).getType(), importManager);
      _builder.append(_shortName_1);
      _builder.append(" ");
      String _name = ((Property)it).getName();
      _builder.append(_name);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("m_proxy.");
      _builder.append(methodName, "\t");
      _builder.append("(");
      String _name_1 = ((Property)it).getName();
      _builder.append(_name_1, "\t");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _actionLog = this.actionLog(((Property)it), actionKey, importManager);
      _builder.append(_actionLog, "\t");
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
        CharSequence _parameters = this._generatorUtils.parameters(((Operation)it), importManager);
        _builder.append(_parameters);
        CharSequence _exceptions = this._generatorUtils.exceptions(((Operation)it), importManager);
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
  
  private CharSequence actionLog(final Property it, final CharSequence actionKey, final ImportManager importManager) {
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
            CharSequence _serialize = importManager.serialize(this._generatorUtils.toJvmGenericType(c.getContext(), this._iQualifiedNameProvider.getFullyQualifiedName(c.getContext()), "context"));
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
        String _name_1 = it.getName();
        _builder.append(_name_1);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("logAction(builder.build());\t\t");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private String toKey(final Control it, final String method) {
    String _name = it.getName();
    String _plus = (_name + ".");
    return (_plus + method);
  }
}
