package org.etri.slice.tools.adl.generator;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Command;
import org.etri.slice.tools.adl.domainmodel.CommandSet;
import org.etri.slice.tools.adl.generator.BehaviorGenerator;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.CommandSetCompiler;
import org.etri.slice.tools.adl.generator.compiler.CommandWrapperCompiler;

@SuppressWarnings("all")
public class CommanderGenerator {
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Inject
  @Extension
  private CommandSetCompiler _commandSetCompiler;
  
  @Inject
  @Extension
  private CommandWrapperCompiler _commandWrapperCompiler;
  
  @Inject
  @Extension
  private BehaviorGenerator _behaviorGenerator;
  
  public void generateCommander(final AgentDeclaration it, final CommandSet commandSet, final IFileSystemAccess fsa) {
    this._behaviorGenerator.generateControlWrapper(commandSet.getControl().getType(), it, fsa);
    this._behaviorGenerator.generateService(commandSet.getControl().getType(), it, fsa);
    String package_ = this._outputPathUtils.getAgentFullyQualifiedName(it).replace(".", "/");
    String _agentMavenSrcHome = this._outputPathUtils.getAgentMavenSrcHome(it);
    String _plus = (_agentMavenSrcHome + package_);
    String _plus_1 = (_plus + "/wrapper/");
    String _simpleName = commandSet.getControl().getSimpleName();
    String _plus_2 = (_plus_1 + _simpleName);
    String file = (_plus_2 + "Commander.java");
    fsa.generateFile(file, this._commandWrapperCompiler.compileCommandWrapper(it, commandSet));
    package_ = this._outputPathUtils.getRuleFullyQualifiedName(it).replace(".", "/");
    EList<Command> _commands = commandSet.getCommands();
    for (final Command c : _commands) {
      {
        String _ruleMavenResHome = this._outputPathUtils.getRuleMavenResHome(it);
        String _plus_3 = (_ruleMavenResHome + package_);
        String _plus_4 = (_plus_3 + "/");
        String _name = c.getName();
        String _plus_5 = (_plus_4 + _name);
        String _plus_6 = (_plus_5 + ".drl");
        file = _plus_6;
        fsa.generateFile(file, this._commandSetCompiler.compileCommand(it, c));
      }
    }
  }
}
