package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.AgentCompiler;
import org.etri.slice.tools.adl.generator.compiler.LogbackCompiler;
import org.etri.slice.tools.adl.generator.compiler.MetaDataCompiler;
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;

@SuppressWarnings("all")
public class AgentProjectGenerator implements IGeneratorForMultiInput {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private AgentCompiler _agentCompiler;
  
  @Inject
  @Extension
  private MetaDataCompiler _metaDataCompiler;
  
  @Inject
  @Extension
  private POMCompiler _pOMCompiler;
  
  @Inject
  @Extension
  private LogbackCompiler _logbackCompiler;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
      return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
    };
    Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
    for (final AgentDeclaration e : _flatten) {
      {
        this.generateMavenProject(e, fsa);
        this.generateAgent(e, fsa);
        this.generateMetaData(e, fsa);
        this.generateLogback(e, fsa);
      }
    }
  }
  
  public void generateAgent(final AgentDeclaration it, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getAgentFullyQualifiedName(it).replace(".", "/");
    String _agentMavenSrcHome = this._outputPathUtils.getAgentMavenSrcHome(it);
    String _plus = (_agentMavenSrcHome + package_);
    String _plus_1 = (_plus + "/");
    String _name = it.getName();
    String _plus_2 = (_plus_1 + _name);
    final String file = (_plus_2 + ".java");
    fsa.generateFile(file, this._agentCompiler.agentCompile(it));
  }
  
  public void generateMavenProject(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _agentMavenHome = this._outputPathUtils.getAgentMavenHome(it);
    String _plus = (_agentMavenHome + "bundle.properties");
    fsa.generateFile(_plus, this.compileBundleProperties(it));
    String _agentMavenHome_1 = this._outputPathUtils.getAgentMavenHome(it);
    String _plus_1 = (_agentMavenHome_1 + "pom.xml");
    fsa.generateFile(_plus_1, this._pOMCompiler.compileAgentPOM(it));
  }
  
  public void generateMetaData(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _agentMavenResHome = this._outputPathUtils.getAgentMavenResHome(it);
    String _plus = (_agentMavenResHome + "metadata.xml");
    fsa.generateFile(_plus, this._metaDataCompiler.compileMetaData(it));
  }
  
  public void generateLogback(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _agentMavenResHome = this._outputPathUtils.getAgentMavenResHome(it);
    String _plus = (_agentMavenResHome + "logback.xml");
    fsa.generateFile(_plus, this._logbackCompiler.compileLogback(it));
  }
  
  public CharSequence compileBundleProperties(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# Configure the created bundle");
    _builder.newLine();
    _builder.append("private.packages=org.etri.slice.agents.");
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    _builder.append(_fullyQualifiedName);
    _builder.append(".");
    String _lowerCase = it.getName().toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(".*");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
