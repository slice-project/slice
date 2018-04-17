package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;

@SuppressWarnings("all")
public class DomainGenerator implements IGenerator {
  @Inject
  @Extension
  private POMCompiler _pOMCompiler;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    Iterable<DomainDeclaration> _filter = Iterables.<DomainDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), DomainDeclaration.class);
    for (final DomainDeclaration e : _filter) {
      this.generateMavenProject(e, fsa);
    }
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
    _builder.append("export.packages=!org.eclipse.jetty.*,*");
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
