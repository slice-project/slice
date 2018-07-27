package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
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
import org.etri.slice.tools.adl.domainmodel.CommandSet;
import org.etri.slice.tools.adl.generator.AgentProjectGenerator;
import org.etri.slice.tools.adl.generator.CommanderGenerator;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.RuleSetGenerator;

@SuppressWarnings("all")
public class AgentGenerator implements IGeneratorForMultiInput {
  @Inject
  private AgentProjectGenerator agentProjectGenerator;
  
  @Inject
  private RuleSetGenerator ruleSetGenerator;
  
  @Inject
  @Extension
  private CommanderGenerator _commanderGenerator;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    fsa.generateFile((OutputPathUtils.sliceAgents + "/pom.xml"), this.compileAgentsPOM(resources));
    fsa.generateFile((OutputPathUtils.sliceRules + "/pom.xml"), this.compileRulesPOM(resources));
    final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
      return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
    };
    Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
    for (final AgentDeclaration e : _flatten) {
      {
        this.agentProjectGenerator.doGenerate(resources, fsa);
        this.ruleSetGenerator.doGenerate(resources, fsa);
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
  
  public CharSequence compileAgentsPOM(final List<Resource> resources) {
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
      final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
        return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
      };
      Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
      for(final AgentDeclaration e : _flatten) {
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
  
  public CharSequence compileRulesPOM(final List<Resource> resources) {
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
      final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
        return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
      };
      Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
      for(final AgentDeclaration e : _flatten) {
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
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<distributionManagement>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<repository>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<id>slice-mvn-hosted</id>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<url>http://129.254.88.119:8081/nexus/content/repositories/releases/</url>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</repository>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</distributionManagement>\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<build>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<plugins>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<plugin>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("<groupId>org.sonatype.plugins</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("<artifactId>nexus-staging-maven-plugin</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("<version>1.5.1</version>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("<executions>");
    _builder.newLine();
    _builder.append("\t\t\t\t      ");
    _builder.append("<execution>");
    _builder.newLine();
    _builder.append("\t\t\t\t         ");
    _builder.append("<id>default-deploy</id>");
    _builder.newLine();
    _builder.append("\t\t\t\t         ");
    _builder.append("<phase>deploy</phase>");
    _builder.newLine();
    _builder.append("\t\t\t\t         ");
    _builder.append("<goals>");
    _builder.newLine();
    _builder.append("\t\t\t\t            ");
    _builder.append("<goal>deploy</goal>");
    _builder.newLine();
    _builder.append("\t\t\t\t         ");
    _builder.append("</goals>");
    _builder.newLine();
    _builder.append("\t\t\t\t      ");
    _builder.append("</execution>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("</executions>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("<configuration>");
    _builder.newLine();
    _builder.append("\t\t\t\t      ");
    _builder.append("<serverId>nexus</serverId>");
    _builder.newLine();
    _builder.append("\t\t\t\t      ");
    _builder.append("<nexusUrl>http://129.254.88.119:8081/nexus/</nexusUrl>");
    _builder.newLine();
    _builder.append("\t\t\t\t      ");
    _builder.append("<skipStaging>true</skipStaging>");
    _builder.newLine();
    _builder.append("\t\t\t\t   ");
    _builder.append("</configuration>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("</plugin>\t\t\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</plugins>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</build>");
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
