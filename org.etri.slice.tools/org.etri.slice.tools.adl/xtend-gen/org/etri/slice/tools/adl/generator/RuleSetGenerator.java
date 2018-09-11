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
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;
import org.etri.slice.tools.adl.generator.compiler.RuleSetCompiler;

@SuppressWarnings("all")
public class RuleSetGenerator implements IGeneratorForMultiInput {
  @Inject
  @Extension
  private RuleSetCompiler _ruleSetCompiler;
  
  @Inject
  @Extension
  private POMCompiler _pOMCompiler;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
      return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
    };
    Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
    for (final AgentDeclaration e : _flatten) {
      {
        this.generateMavenProject(e, fsa);
        this.generateRule(e, fsa);
        this.generateMetaINF(e, fsa);
      }
    }
  }
  
  public void generateRule(final AgentDeclaration it, final IFileSystemAccess fsa) {
    final String package_ = this._outputPathUtils.getRuleFullyQualifiedName(it).replace(".", "/");
    String _ruleMavenResHome = this._outputPathUtils.getRuleMavenResHome(it);
    String _plus = (_ruleMavenResHome + package_);
    String _plus_1 = (_plus + "/");
    String _lowerCase = it.getName().toLowerCase();
    String _plus_2 = (_plus_1 + _lowerCase);
    final String file = (_plus_2 + "-rules.drl");
    fsa.generateFile(file, this._ruleSetCompiler.compileRuleSet(it, fsa));
  }
  
  public void generateMavenProject(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _ruleMavenHome = this._outputPathUtils.getRuleMavenHome(it);
    String _plus = (_ruleMavenHome + "pom.xml");
    fsa.generateFile(_plus, this._pOMCompiler.compileRulePOM(it));
  }
  
  public void generateMetaINF(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _ruleMavenResHome = this._outputPathUtils.getRuleMavenResHome(it);
    final String file = (_ruleMavenResHome + "/META-INF/kmodule.xml");
    fsa.generateFile(file, this.compileKmodule(it));
  }
  
  public CharSequence compileKmodule(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<kmodule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xmlns=\"http://jboss.org/kie/6.0.0/kmodule\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<kbase name=\"org.etri.slice.rules.");
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    _builder.append(_fullyQualifiedName, "\t");
    _builder.append(".");
    String _lowerCase = it.getName().toLowerCase();
    _builder.append(_lowerCase, "\t");
    _builder.append("\" default=\"true\" eventProcessingMode=\"stream\" packages=\"org.etri.slice.rules.");
    QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    _builder.append(_fullyQualifiedName_1, "\t");
    _builder.append(".");
    String _lowerCase_1 = it.getName().toLowerCase();
    _builder.append(_lowerCase_1, "\t");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("<ksession name=\"org.etri.slice\" type=\"stateful\" default=\"true\"/>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</kbase>");
    _builder.newLine();
    _builder.append("</kmodule>\t");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
