package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;

@SuppressWarnings("all")
public class DomainGenerator implements IGeneratorForMultiInput {
  @Inject
  @Extension
  private POMCompiler _pOMCompiler;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    final Function1<Resource, Iterable<DomainDeclaration>> _function = (Resource it) -> {
      return Iterables.<DomainDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), DomainDeclaration.class);
    };
    Iterable<DomainDeclaration> _flatten = Iterables.<DomainDeclaration>concat(ListExtensions.<Resource, Iterable<DomainDeclaration>>map(resources, _function));
    for (final DomainDeclaration e : _flatten) {
      this.generateMavenProject(e, fsa);
    }
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  public void generateMavenProject(final DomainDeclaration it, final IFileSystemAccess fsa) {
    String _commonsMavenHome = this._outputPathUtils.getCommonsMavenHome(it);
    String _plus = (_commonsMavenHome + "bundle.properties");
    fsa.generateFile(_plus, this.compileBundleProperties(it));
    String _commonsMavenHome_1 = this._outputPathUtils.getCommonsMavenHome(it);
    String _plus_1 = (_commonsMavenHome_1 + "pom.xml");
    fsa.generateFile(_plus_1, this._pOMCompiler.compileDomainPOM(it));
  }
  
  public CharSequence compileBundleProperties(final DomainDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# Configure the created bundle");
    _builder.newLine();
    _builder.append("export.packages=!org.eclipse.jetty.*,javax.xml.stream,*");
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
