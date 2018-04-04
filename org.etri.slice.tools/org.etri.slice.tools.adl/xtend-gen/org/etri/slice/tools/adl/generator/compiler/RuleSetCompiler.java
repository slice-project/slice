package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.etri.slice.tools.adl.domainmodel.AbstractElement;
import org.etri.slice.tools.adl.domainmodel.Action;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Behavior;
import org.etri.slice.tools.adl.domainmodel.Call;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.DataType;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.domainmodel.Publish;
import org.etri.slice.tools.adl.domainmodel.Situation;
import org.etri.slice.tools.adl.generator.BehaviorGenerator;
import org.etri.slice.tools.adl.generator.GeneratorUtils;

@SuppressWarnings("all")
public class RuleSetCompiler {
  private IFileSystemAccess m_fsa;
  
  @Extension
  private Set<Control> m_globals = new HashSet<Control>();
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private BehaviorGenerator _behaviorGenerator;
  
  public CharSequence compileRuleSet(final AgentDeclaration it, final IFileSystemAccess fsa) {
    CharSequence _xblockexpression = null;
    {
      this.m_fsa = fsa;
      this.m_globals.clear();
      StringConcatenation _builder = new StringConcatenation();
      final ImportManager importManager = new ImportManager(true);
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      final CharSequence body = this.ruleBody(it, importManager);
      _builder.newLineIfNotEmpty();
      {
        EObject _eContainer = it.eContainer();
        boolean _tripleNotEquals = (_eContainer != null);
        if (_tripleNotEquals) {
          _builder.append("package org.etri.slice.rules.");
          QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
          _builder.append(_fullyQualifiedName);
          _builder.append(";");
          _builder.newLineIfNotEmpty();
        }
      }
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
      {
        List<Control> _list = IterableExtensions.<Control>toList(this.m_globals);
        for(final Control c : _list) {
          _builder.append("global ");
          String _name = c.getName();
          _builder.append(_name);
          _builder.append(" ");
          String _firstLower = StringExtensions.toFirstLower(c.getName());
          _builder.append(_firstLower);
          _builder.append(";");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      _builder.append(body);
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence ruleBody(final AgentDeclaration it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Behavior> _behaviors = it.getBehaviorSet().getBehaviors();
      for(final Behavior b : _behaviors) {
        {
          boolean _equals = b.getAction().getAction().equals("no-op");
          boolean _not = (!_equals);
          if (_not) {
            CharSequence _compileBody = this.compileBody(b, it, importManager);
            _builder.append(_compileBody);
            _builder.newLineIfNotEmpty();
          } else {
            {
              EList<DataType> _types = b.getSituation().getTypes();
              for(final DataType context : _types) {
                this._behaviorGenerator.generateAdaptor(context, it, this.m_fsa);
              }
            }
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public CharSequence compileBody(final Behavior it, final AgentDeclaration agent, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("rule \"");
    String _name = it.getName();
    _builder.append(_name);
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("when");
    _builder.newLine();
    _builder.append("\t\t");
    CharSequence _compileSituation = this.compileSituation(it.getSituation(), agent, importManager);
    _builder.append(_compileSituation, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("then");
    _builder.newLine();
    _builder.append("\t\t");
    CharSequence _compileAction = this.compileAction(it.getAction(), agent, importManager);
    _builder.append(_compileAction, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("end");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence compileSituation(final Situation it, final AgentDeclaration agent, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    int index = 1;
    _builder.newLineIfNotEmpty();
    {
      int _size = it.getTypes().size();
      boolean _equals = (_size == 1);
      if (_equals) {
        _builder.append("$e: ");
        CharSequence _compileDataType = this.compileDataType(it.getTypes().get(0), agent, importManager);
        _builder.append(_compileDataType);
        _builder.newLineIfNotEmpty();
      } else {
        {
          EList<DataType> _types = it.getTypes();
          for(final DataType context : _types) {
            _builder.append("$e");
            int _plusPlus = index++;
            _builder.append(_plusPlus);
            _builder.append(": ");
            CharSequence _compileDataType_1 = this.compileDataType(context, agent, importManager);
            _builder.append(_compileDataType_1);
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence compileDataType(final DataType it, final AgentDeclaration agent, final ImportManager importManager) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof Context) {
      _matched=true;
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _compileContextAdaptor = this.compileContextAdaptor(((AbstractElement)it), agent, importManager);
      _builder.append(_compileContextAdaptor);
      _builder.append("(/* */)");
      _switchResult = _builder;
    }
    if (!_matched) {
      if (it instanceof Event) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        CharSequence _compileEventAdaptor = this.compileEventAdaptor(((AbstractElement)it), agent, importManager);
        _builder.append(_compileEventAdaptor);
        _builder.append("(/* */)");
        _switchResult = _builder;
      }
    }
    return _switchResult;
  }
  
  public CharSequence compileContextAdaptor(final AbstractElement it, final AgentDeclaration agent, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      final JvmGenericType type = this._generatorUtils.toJvmGenericType(it, this._iQualifiedNameProvider.getFullyQualifiedName(it), "context");
      this._behaviorGenerator.generateAdaptor(((Context) it), agent, this.m_fsa);
      _xblockexpression = importManager.serialize(type);
    }
    return _xblockexpression;
  }
  
  public CharSequence compileEventAdaptor(final AbstractElement it, final AgentDeclaration agent, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      final JvmGenericType type = this._generatorUtils.toJvmGenericType(it, this._iQualifiedNameProvider.getFullyQualifiedName(it), "event");
      this._behaviorGenerator.generateAdaptor(((Event) it), agent, this.m_fsa);
      _xblockexpression = importManager.serialize(type);
    }
    return _xblockexpression;
  }
  
  public CharSequence compileAction(final Action it, final AgentDeclaration agent, final ImportManager importManager) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof Publish) {
      _matched=true;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("channels[\"");
      String _name = ((Publish)it).getEvent().getTopic().getName();
      _builder.append(_name);
      _builder.append("\"].send(new ");
      CharSequence _compileEventWrapper = this.compileEventWrapper(((Publish)it).getEvent(), agent, importManager);
      _builder.append(_compileEventWrapper);
      _builder.append("(/* */));");
      _switchResult = _builder;
    }
    if (!_matched) {
      if (it instanceof Call) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        String _firstLower = StringExtensions.toFirstLower(this.compileControlWrapper(((Call)it).getControl(), agent, importManager).toString());
        _builder.append(_firstLower);
        _builder.append(".");
        String _method = ((Call)it).getMethod();
        _builder.append(_method);
        _builder.append("(/* */);");
        _switchResult = _builder;
      }
    }
    return _switchResult;
  }
  
  public CharSequence compileEventWrapper(final Event it, final AgentDeclaration agent, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      final JvmGenericType type = this._generatorUtils.toJvmGenericType(it, this._iQualifiedNameProvider.getFullyQualifiedName(it), "event");
      this._behaviorGenerator.generateEventWrapper(it, agent, this.m_fsa);
      _xblockexpression = importManager.serialize(type);
    }
    return _xblockexpression;
  }
  
  public CharSequence compileControlWrapper(final Control it, final AgentDeclaration agent, final ImportManager importManager) {
    CharSequence _xblockexpression = null;
    {
      this.m_globals.add(it);
      final JvmGenericType type = this._generatorUtils.toJvmGenericType(it, this._iQualifiedNameProvider.getFullyQualifiedName(it), "service");
      this._behaviorGenerator.generateControlWrapper(it, agent, this.m_fsa);
      _xblockexpression = importManager.serialize(type);
    }
    return _xblockexpression;
  }
}
