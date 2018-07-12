/**
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.etri.slice.tools.adl.generator;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable;
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.Operation;
import org.etri.slice.tools.adl.domainmodel.Property;

/**
 * @author yhsuh - Initial contribution and API
 */
@SuppressWarnings("all")
public class GeneratorUtils {
  @Inject
  @Extension
  private TypeReferenceSerializer _typeReferenceSerializer;
  
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  public QualifiedName adaptToSlice(final QualifiedName qname, final String infix) {
    final ArrayList<String> segments = CollectionLiterals.<String>newArrayList("org", "etri", "slice", "commons");
    String _string = qname.toString();
    final StringTokenizer tokens = new StringTokenizer(_string, ".");
    while (tokens.hasMoreElements()) {
      segments.add(tokens.nextToken());
    }
    int _length = infix.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      int _size = segments.size();
      int _minus = (_size - 1);
      segments.add(_minus, infix);
    }
    return QualifiedName.create(segments);
  }
  
  public String shortName(final JvmTypeReference ref, final ImportManager importManager) {
    String _xblockexpression = null;
    {
      final StringBuilderBasedAppendable result = new StringBuilderBasedAppendable(importManager);
      this._typeReferenceSerializer.serialize(ref, ref.eContainer(), result);
      _xblockexpression = result.toString();
    }
    return _xblockexpression;
  }
  
  public CharSequence shortName(final Context it, final ImportManager importManager) {
    return importManager.serialize(this._jvmTypesBuilder.toClass(it, this.adaptToSlice(this._iQualifiedNameProvider.getFullyQualifiedName(it), "context")));
  }
  
  public CharSequence shortName(final Control it, final ImportManager importManager) {
    return importManager.serialize(this._jvmTypesBuilder.toClass(it, this.adaptToSlice(this._iQualifiedNameProvider.getFullyQualifiedName(it), "service")));
  }
  
  public CharSequence shortName(final Event it, final ImportManager importManager) {
    return importManager.serialize(this._jvmTypesBuilder.toClass(it, this.adaptToSlice(this._iQualifiedNameProvider.getFullyQualifiedName(it), "event")));
  }
  
  public long generateSerialVersionUID() {
    return UUID.randomUUID().getMostSignificantBits();
  }
  
  public JvmGenericType toJvmGenericType(final EObject obj, final QualifiedName qname, final String infix) {
    return this._jvmTypesBuilder.toClass(obj, this.adaptToSlice(qname, infix));
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
        String _shortName = this.shortName(p.getParameterType(), importManager);
        _builder.append(_shortName);
        _builder.append(" ");
        String _name = p.getName();
        _builder.append(_name);
      }
    }
    _builder.append(")");
    return _builder;
  }
  
  public CharSequence parameters(final JvmOperation it, final ImportManager importManager) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      EList<JvmFormalParameter> _parameters = it.getParameters();
      boolean _hasElements = false;
      for(final JvmFormalParameter p : _parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        String _shortName = this.shortName(p.getParameterType(), importManager);
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
            String _shortName = this.shortName(e, importManager);
            _builder.append(_shortName);
          }
        }
      } else {
      }
    }
    return _builder;
  }
  
  public CharSequence exceptions(final JvmOperation it, final ImportManager importManager) {
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
            String _shortName = this.shortName(e, importManager);
            _builder.append(_shortName);
          }
        }
      } else {
      }
    }
    return _builder;
  }
  
  public synchronized JvmGenericType toInterface(final Control control) {
    final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
      this._jvmTypesBuilder.setDocumentation(it, this._jvmTypesBuilder.getDocumentation(control));
      EList<JvmParameterizedTypeReference> _superTypes = control.getSuperTypes();
      for (final JvmParameterizedTypeReference superType : _superTypes) {
        EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
        JvmTypeReference _cloneWithProxies = this._jvmTypesBuilder.cloneWithProxies(superType);
        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _cloneWithProxies);
      }
      EList<Feature> _features = control.getFeatures();
      for (final Feature f : _features) {
        boolean _matched = false;
        if (f instanceof Property) {
          _matched=true;
          EList<JvmMember> _members = it.getMembers();
          JvmOperation _getter = this._jvmTypesBuilder.toGetter(f, ((Property)f).getName(), ((Property)f).getType());
          this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _getter);
          EList<JvmMember> _members_1 = it.getMembers();
          JvmOperation _setter = this._jvmTypesBuilder.toSetter(f, ((Property)f).getName(), ((Property)f).getType());
          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
        }
        if (!_matched) {
          if (f instanceof Operation) {
            _matched=true;
            EList<JvmMember> _members = it.getMembers();
            String _name = ((Operation)f).getName();
            JvmTypeReference _elvis = null;
            JvmTypeReference _type = ((Operation)f).getType();
            if (_type != null) {
              _elvis = _type;
            } else {
              JvmTypeReference _inferredType = this._jvmTypesBuilder.inferredType();
              _elvis = _inferredType;
            }
            final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
              this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(f));
              EList<JvmFormalParameter> _params = ((Operation)f).getParams();
              for (final JvmFormalParameter p : _params) {
                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, p.getName(), p.getParameterType());
                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              }
              EList<JvmTypeReference> _exceptions = ((Operation)f).getExceptions();
              for (final JvmTypeReference e : _exceptions) {
                EList<JvmTypeReference> _exceptions_1 = it_1.getExceptions();
                JvmTypeReference _cloneWithProxies_1 = this._jvmTypesBuilder.cloneWithProxies(e);
                this._jvmTypesBuilder.<JvmTypeReference>operator_add(_exceptions_1, _cloneWithProxies_1);
              }
            };
            JvmOperation _method = this._jvmTypesBuilder.toMethod(f, _name, _elvis, _function_1);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
          }
        }
      }
    };
    return this._jvmTypesBuilder.toInterface(control, this.adaptToSlice(this._iQualifiedNameProvider.getFullyQualifiedName(control), "service").toString(), _function);
  }
}
