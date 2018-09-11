package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.generator.GeneratorUtils;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;

@SuppressWarnings("all")
public class ExceptionGenerator implements IGeneratorForMultiInput {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private GeneratorUtils _generatorUtils;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    final Function1<Resource, Iterable<org.etri.slice.tools.adl.domainmodel.Exception>> _function = (Resource it) -> {
      return Iterables.<org.etri.slice.tools.adl.domainmodel.Exception>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), org.etri.slice.tools.adl.domainmodel.Exception.class);
    };
    Iterable<org.etri.slice.tools.adl.domainmodel.Exception> _flatten = Iterables.<org.etri.slice.tools.adl.domainmodel.Exception>concat(ListExtensions.<Resource, Iterable<org.etri.slice.tools.adl.domainmodel.Exception>>map(resources, _function));
    for (final org.etri.slice.tools.adl.domainmodel.Exception e : _flatten) {
      {
        final String package_ = this._outputPathUtils.getSliceFullyQualifiedName(e).replace(".", "/");
        String _commonsMavenSrcHome = this._outputPathUtils.getCommonsMavenSrcHome(e);
        String _plus = (_commonsMavenSrcHome + package_);
        String _plus_1 = (_plus + "/");
        String _name = e.getName();
        String _plus_2 = (_plus_1 + _name);
        final String file = (_plus_2 + ".java");
        fsa.generateFile(file, this.compile(e));
      }
    }
  }
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  public CharSequence compile(final org.etri.slice.tools.adl.domainmodel.Exception it) {
    StringConcatenation _builder = new StringConcatenation();
    final ImportManager importManager = new ImportManager(true);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    final CharSequence body = this.body(it, importManager);
    _builder.newLineIfNotEmpty();
    {
      EObject _eContainer = it.eContainer();
      boolean _tripleNotEquals = (_eContainer != null);
      if (_tripleNotEquals) {
        _builder.append("package org.etri.slice.commons.");
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
        _builder.append(_fullyQualifiedName);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      JvmParameterizedTypeReference _superType = it.getSuperType();
      boolean _tripleEquals = (_superType == null);
      if (_tripleEquals) {
        _builder.append("import org.etri.slice.commons.SliceException;");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      List<String> _imports = importManager.getImports();
      for(final String i : _imports) {
        _builder.append("import ");
        _builder.append(i);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append(body);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence body(final org.etri.slice.tools.adl.domainmodel.Exception it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class ");
    String _name = it.getName();
    _builder.append(_name);
    _builder.append(" extends ");
    {
      JvmParameterizedTypeReference _superType = it.getSuperType();
      boolean _tripleNotEquals = (_superType != null);
      if (_tripleNotEquals) {
        String _shortName = this._generatorUtils.shortName(it.getSuperType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
      } else {
        _builder.append("SliceException ");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private static final long serialVersionUID = ");
    long _generateSerialVersionUID = this._generatorUtils.generateSerialVersionUID();
    _builder.append(_generateSerialVersionUID, "\t");
    _builder.append("L;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    String _name_1 = it.getName();
    _builder.append(_name_1, "\t");
    _builder.append("(String msg) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("super(msg);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    String _name_2 = it.getName();
    _builder.append(_name_2, "\t");
    _builder.append("(Throwable e) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("super(e);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
