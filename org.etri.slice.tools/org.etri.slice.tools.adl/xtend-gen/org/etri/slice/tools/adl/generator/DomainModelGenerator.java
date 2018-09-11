package org.etri.slice.tools.adl.generator;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.etri.slice.tools.adl.generator.ContextGenerator;
import org.etri.slice.tools.adl.generator.ControlGenerator;
import org.etri.slice.tools.adl.generator.DomainGenerator;
import org.etri.slice.tools.adl.generator.EventGenerator;
import org.etri.slice.tools.adl.generator.ExceptionGenerator;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.validation.domain_dependency.Domain;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager;

@SuppressWarnings("all")
public class DomainModelGenerator implements IGeneratorForMultiInput {
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
  private DomainManager domainManager;
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    fsa.generateFile((OutputPathUtils.sliceModels + "/pom.xml"), this.compileModelsPOM(this.domainManager));
    this.domainGenerator.doGenerate(resources, fsa);
    this.contextGenerator.doGenerate(resources, fsa);
    this.controlGenerator.doGenerate(resources, fsa);
    this.eventGenerator.doGenerate(resources, fsa);
    this.exceptionGenerator.doGenerate(resources, fsa);
  }
  
  public CharSequence compileModelsPOM(final DomainManager domainManager) {
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
      ArrayList<Domain> _buildOrderedDomainList = domainManager.getBuildOrderedDomainList();
      for(final Domain e : _buildOrderedDomainList) {
        _builder.append("\t\t");
        _builder.append("<module>org.etri.slice.commons.");
        _builder.append(e.domain, "\t\t");
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
