package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.Operation;
import org.etri.slice.tools.adl.domainmodel.Property;
import org.etri.slice.tools.adl.generator.GeneratorUtils;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;

@SuppressWarnings("all")
public class ControlGenerator implements IGeneratorForMultiInput {
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
    final Function1<Resource, Iterable<Control>> _function = (Resource it) -> {
      return Iterables.<Control>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), Control.class);
    };
    Iterable<Control> _flatten = Iterables.<Control>concat(ListExtensions.<Resource, Iterable<Control>>map(resources, _function));
    for (final Control e : _flatten) {
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
  
  public CharSequence compile(final Control it) {
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
        _builder.append(".service;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("import javax.management.MXBean;");
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
    _builder.append("@MXBean");
    _builder.newLine();
    _builder.append(body);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence body(final Control it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public interface ");
    String _name = it.getName();
    _builder.append(_name);
    _builder.append(" ");
    {
      int _size = it.getSuperTypes().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("extends ");
        {
          EList<JvmParameterizedTypeReference> _superTypes = it.getSuperTypes();
          boolean _hasElements = false;
          for(final JvmParameterizedTypeReference superType : _superTypes) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "");
            }
            String _shortName = this._generatorUtils.shortName(superType, importManager);
            _builder.append(_shortName);
          }
        }
        _builder.append(" ");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    final String id = StringExtensions.toFirstLower(it.getName());
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("static final String id = \"");
    _builder.append(id, "\t");
    _builder.append("\";");
    _builder.newLineIfNotEmpty();
    {
      EList<Feature> _features = it.getFeatures();
      for(final Feature f : _features) {
        _builder.append("\t");
        CharSequence _preprocess = this.preprocess(f, id, importManager);
        _builder.append(_preprocess, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    {
      EList<Feature> _features_1 = it.getFeatures();
      for(final Feature f_1 : _features_1) {
        _builder.append("\t");
        CharSequence _compile = this.compile(f_1, importManager);
        _builder.append(_compile, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence preprocess(final Feature it, final String idStr, final ImportManager importManager) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof Property) {
      _matched=true;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("static final String set");
      String _firstUpper = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper);
      _builder.append(" = \"");
      _builder.append(idStr);
      _builder.append(".set");
      String _firstUpper_1 = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper_1);
      _builder.append("\";");
      _builder.newLineIfNotEmpty();
      _switchResult = _builder;
    }
    return _switchResult;
  }
  
  public CharSequence compile(final Feature it, final ImportManager importManager) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof Property) {
      _matched=true;
      StringConcatenation _builder = new StringConcatenation();
      String _shortName = this._generatorUtils.shortName(((Property)it).getType(), importManager);
      _builder.append(_shortName);
      _builder.append(" get");
      String _firstUpper = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper);
      _builder.append("();");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t        ");
      _builder.newLine();
      _builder.append("void set");
      String _firstUpper_1 = StringExtensions.toFirstUpper(((Property)it).getName());
      _builder.append(_firstUpper_1);
      _builder.append("(");
      String _shortName_1 = this._generatorUtils.shortName(((Property)it).getType(), importManager);
      _builder.append(_shortName_1);
      _builder.append(" ");
      String _name = ((Property)it).getName();
      _builder.append(_name);
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _switchResult = _builder;
    }
    if (!_matched) {
      if (it instanceof Operation) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        String _shortName = this._generatorUtils.shortName(((Operation)it).getType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _name = ((Operation)it).getName();
        _builder.append(_name);
        CharSequence _parameters = this.parameters(((Operation)it), importManager);
        _builder.append(_parameters);
        CharSequence _exceptions = this.exceptions(((Operation)it), importManager);
        _builder.append(_exceptions);
        _builder.newLineIfNotEmpty();
        _switchResult = _builder;
      }
    }
    return _switchResult;
  }
  
  public CharSequence parameters(final Operation it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      EList<JvmFormalParameter> _params = it.getParams();
      boolean _hasElements = false;
      for(final JvmFormalParameter p : _params) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        String _shortName = this._generatorUtils.shortName(p.getParameterType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _name = p.getName();
        _builder.append(_name);
      }
    }
    _builder.append(")");
    return _builder;
  }
  
  public CharSequence exceptions(final Operation it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    {
      int _size = it.getExceptions().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append(" throws ");
        {
          EList<JvmTypeReference> _exceptions = it.getExceptions();
          boolean _hasElements = false;
          for(final JvmTypeReference e : _exceptions) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "");
            }
            String _shortName = this._generatorUtils.shortName(e, importManager);
            _builder.append(_shortName);
          }
        }
        _builder.append(";");
      } else {
        _builder.append(";");
      }
    }
    return _builder;
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
