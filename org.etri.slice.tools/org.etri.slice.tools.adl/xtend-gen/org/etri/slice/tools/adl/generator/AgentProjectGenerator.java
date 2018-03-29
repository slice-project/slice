package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.AgentCompiler;
import org.etri.slice.tools.adl.generator.compiler.LogbackCompiler;
import org.etri.slice.tools.adl.generator.compiler.MetaDataCompiler;
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;

@SuppressWarnings("all")
public class AgentProjectGenerator implements IGenerator {
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
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    Iterable<AgentDeclaration> _filter = Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), AgentDeclaration.class);
    for (final AgentDeclaration e : _filter) {
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
}
