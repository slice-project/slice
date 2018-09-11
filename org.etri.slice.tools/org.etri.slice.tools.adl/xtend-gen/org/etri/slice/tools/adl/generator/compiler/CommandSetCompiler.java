package org.etri.slice.tools.adl.generator.compiler;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Command;
import org.etri.slice.tools.adl.domainmodel.CommandContext;

@SuppressWarnings("all")
public class CommandSetCompiler {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  public CharSequence compileCommand(final AgentDeclaration agent, final Command it) {
    CharSequence _xblockexpression = null;
    {
      final ImportManager importManager = new ImportManager(true);
      EList<CommandContext> _contexts = it.getContexts();
      for (final CommandContext c : _contexts) {
        importManager.addImportFor(c.getContext());
      }
      importManager.addImportFor(it.getAction());
      StringConcatenation _builder = new StringConcatenation();
      {
        EObject _eContainer = agent.eContainer();
        boolean _tripleNotEquals = (_eContainer != null);
        if (_tripleNotEquals) {
          _builder.append("package org.etri.slice.rules.");
          QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(agent.eContainer());
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
      _builder.append("\t");
      _builder.newLine();
      _builder.append("global ");
      String _simpleName = it.getAction().getSimpleName();
      _builder.append(_simpleName);
      _builder.append(" ");
      String _firstLower = StringExtensions.toFirstLower(it.getAction().getSimpleName());
      _builder.append(_firstLower);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
}
