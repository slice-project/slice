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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.compiler.ImportManager;
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable;
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.Event;

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
}
