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
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.generator.ContextGenerator;
import org.etri.slice.tools.adl.generator.ControlGenerator;
import org.etri.slice.tools.adl.generator.DomainGenerator;
import org.etri.slice.tools.adl.generator.EventGenerator;
import org.etri.slice.tools.adl.generator.ExceptionGenerator;
import org.etri.slice.tools.adl.generator.OutputPathUtils;

@SuppressWarnings("all")
public class DomainModelGenerator implements IGenerator {
  @Inject
  private ContextGenerator contextGenerator;
  
  @Inject
  private ControlGenerator controlGenerator;
  
  @Inject
  private DomainGenerator domainGenerator;
  
  @Inject
  private EventGenerator eventGenerator;
  
  @Inject
  private ExceptionGenerator exceptionGenerator;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    fsa.generateFile((OutputPathUtils.sliceModels + "/pom.xml"), this.compileModelsPOM(resource));
    this.domainGenerator.doGenerate(resource, fsa);
    this.contextGenerator.doGenerate(resource, fsa);
    this.controlGenerator.doGenerate(resource, fsa);
    this.eventGenerator.doGenerate(resource, fsa);
    this.exceptionGenerator.doGenerate(resource, fsa);
  }
  
  public CharSequence compileModelsPOM(final Resource resource) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<modelVersion>4.0.0</modelVersion>");
    _builder.newLine();
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
    _builder.append("<artifactId>org.etri.slice.models</artifactId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>The common data models for SLICE application domains</name>");
    _builder.newLine();
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
      Iterable<DomainDeclaration> _filter = Iterables.<DomainDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), DomainDeclaration.class);
      for(final DomainDeclaration e : _filter) {
        _builder.append("\t\t");
        _builder.append("<module>org.etri.slice.commons.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(e);
        _builder.append(_fullyQualifiedName, "\t\t");
        _builder.append("</module>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("</modules>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<dependencies>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>org.etri.slice</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>org.etri.slice.commons</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<version>0.9.1</version>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</dependencies>");
    _builder.newLine();
    _builder.append("</project>\t\t");
    _builder.newLine();
    return _builder;
  }
}
