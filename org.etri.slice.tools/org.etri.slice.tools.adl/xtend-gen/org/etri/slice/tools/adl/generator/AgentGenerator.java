package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
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
import org.etri.slice.tools.adl.domainmodel.CommandSet;
import org.etri.slice.tools.adl.generator.AgentProjectGenerator;
import org.etri.slice.tools.adl.generator.CommanderGenerator;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.RuleSetGenerator;
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;

@SuppressWarnings("all")
public class AgentGenerator implements IGenerator {
  @Inject
  private AgentProjectGenerator agentProjectGenerator;
  
  @Inject
  private RuleSetGenerator ruleSetGenerator;
  
  @Inject
  @Extension
  private CommanderGenerator _commanderGenerator;
  
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
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    fsa.generateFile((OutputPathUtils.sliceAgents + "/pom.xml"), this.compileAgentsPOM(resource));
    fsa.generateFile((OutputPathUtils.sliceRules + "/pom.xml"), this.compileRulesPOM(resource));
    Iterable<AgentDeclaration> _filter = Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), AgentDeclaration.class);
    for (final AgentDeclaration e : _filter) {
      {
        this.generateMavenProject(e, fsa);
        this.agentProjectGenerator.doGenerate(resource, fsa);
        this.ruleSetGenerator.doGenerate(resource, fsa);
        EList<CommandSet> _commandSets = e.getCommandSets();
        boolean _tripleNotEquals = (_commandSets != null);
        if (_tripleNotEquals) {
          EList<CommandSet> _commandSets_1 = e.getCommandSets();
          for (final CommandSet c : _commandSets_1) {
            this._commanderGenerator.generateCommander(e, c, fsa);
          }
        }
      }
    }
  }
  
  public CharSequence compileAgentsPOM(final Resource resource) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<modelVersion>4.0.0</modelVersion>");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<parent>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<groupId>org.etri.slice</groupId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<artifactId>org.etri.slice</artifactId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<version>0.9.1</version>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<relativePath>../pom.xml</relativePath>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</parent>");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<artifactId>org.etri.slice.agents</artifactId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>The SLICE agents</name>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<packaging>pom</packaging>");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<modules>");
    _builder.newLine();
    {
      Iterable<AgentDeclaration> _filter = Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), AgentDeclaration.class);
      for(final AgentDeclaration e : _filter) {
        _builder.append("\t\t");
        _builder.append("<module>org.etri.slice.agents.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(e.eContainer());
        _builder.append(_fullyQualifiedName, "\t\t");
        _builder.append(".");
        String _lowerCase = e.getName().toLowerCase();
        _builder.append(_lowerCase, "\t\t");
        _builder.append("</module>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("</modules>");
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence compileRulesPOM(final Resource resource) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<modelVersion>4.0.0</modelVersion>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<groupId>org.etri.slice</groupId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<artifactId>org.etri.slice.rules</artifactId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<version>0.9.1</version>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<packaging>pom</packaging>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>The SLICE rules</name>");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<modules>");
    _builder.newLine();
    {
      Iterable<AgentDeclaration> _filter = Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), AgentDeclaration.class);
      for(final AgentDeclaration e : _filter) {
        _builder.append("\t\t");
        _builder.append("<module>org.etri.slice.rules.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(e.eContainer());
        _builder.append(_fullyQualifiedName, "\t\t");
        _builder.append(".");
        String _lowerCase = e.getName().toLowerCase();
        _builder.append(_lowerCase, "\t\t");
        _builder.append("</module>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("</modules>");
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    return _builder;
  }
  
  public void generateMavenProject(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _commonsMavenHome = this._outputPathUtils.getCommonsMavenHome(it);
    String _plus = (_commonsMavenHome + "bundle.properties");
    fsa.generateFile(_plus, this.compileBundleProperties(it));
    String _commonsMavenHome_1 = this._outputPathUtils.getCommonsMavenHome(it);
    String _plus_1 = (_commonsMavenHome_1 + "pom.xml");
    fsa.generateFile(_plus_1, this._pOMCompiler.compileModelPOM(it));
  }
  
  public CharSequence compileBundleProperties(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# Configure the created bundle");
    _builder.newLine();
    _builder.append("export.packages=*");
    _builder.newLine();
    _builder.append("embed.dependency=*,;scope=!provided|test;inline=true");
    _builder.newLine();
    _builder.append("embed.directory=lib");
    _builder.newLine();
    _builder.append("bundle.classpath=.,{maven-dependencies}");
    _builder.newLine();
    _builder.append("import.packages=*;resolution:=optional\t");
    _builder.newLine();
    return _builder;
  }
}
