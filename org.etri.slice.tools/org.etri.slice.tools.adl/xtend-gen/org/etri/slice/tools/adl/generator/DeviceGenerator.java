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
import org.etri.slice.tools.adl.generator.DeviceProjectGenerator;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;

@SuppressWarnings("all")
public class DeviceGenerator implements IGeneratorForMultiInput {
  @Inject
  private DeviceProjectGenerator deviceProjectGenerator;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    fsa.generateFile((OutputPathUtils.sliceDevices + "/pom.xml"), this.compileDevicesPOM(resources));
    final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
      return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
    };
    Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
    for (final AgentDeclaration e : _flatten) {
      this.deviceProjectGenerator.doGenerate(resources, fsa);
    }
  }
  
  public CharSequence compileDevicesPOM(final List<Resource> resources) {
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
    _builder.append("<groupId>org.etri.slice.devices</groupId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<artifactId>org.etri.slice.devices</artifactId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>The 3rd party implementation for the SLICE devices</name>");
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
        _builder.append("<module>org.etri.slice.devices.");
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
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
