package org.etri.slice.tools.adl.utils;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.LinkedHashSet;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.Operation;
import org.etri.slice.tools.adl.domainmodel.Property;

@SuppressWarnings("all")
public class DomainnodeUtil {
  @Inject
  @Extension
  private IJvmModelAssociations _iJvmModelAssociations;
  
  public Iterable<JvmField> classHierarchyFields(final Context context) {
    Iterable<JvmField> _xblockexpression = null;
    {
      final JvmGenericType inferredJavaType = IterableExtensions.<JvmGenericType>head(Iterables.<JvmGenericType>filter(this._iJvmModelAssociations.getJvmElements(context), JvmGenericType.class));
      final EList<JvmTypeReference> superTypes = inferredJavaType.getSuperTypes();
      final Function1<JvmTypeReference, Iterable<JvmField>> _function = (JvmTypeReference it) -> {
        Iterable<JvmField> _xblockexpression_1 = null;
        {
          JvmType _type = it.getType();
          final JvmGenericType genericType = ((JvmGenericType) _type);
          final Function1<JvmMember, Iterable<JvmField>> _function_1 = (JvmMember it_1) -> {
            return it_1.getDeclaringType().getDeclaredFields();
          };
          _xblockexpression_1 = Iterables.<JvmField>concat(ListExtensions.<JvmMember, Iterable<JvmField>>map(genericType.getMembers(), _function_1));
        }
        return _xblockexpression_1;
      };
      _xblockexpression = Iterables.<JvmField>concat(ListExtensions.<JvmTypeReference, Iterable<JvmField>>map(superTypes, _function));
    }
    return _xblockexpression;
  }
  
  public LinkedHashSet<Control> controlSuperTypes(final Control control) {
    LinkedHashSet<Control> _xblockexpression = null;
    {
      final LinkedHashSet<Control> superTypes = new LinkedHashSet<Control>();
      EList<JvmParameterizedTypeReference> current = control.getSuperTypes();
      while ((((current != null) && (current instanceof Control)) && (!superTypes.contains(current)))) {
        {
          superTypes.add(((Control) current));
          if ((current instanceof Control)) {
            current = ((Control)current).getSuperTypes();
          } else {
            current = null;
          }
        }
      }
      final Consumer<Control> _function = (Control it) -> {
        System.out.println();
      };
      superTypes.forEach(_function);
      _xblockexpression = superTypes;
    }
    return _xblockexpression;
  }
  
  public Iterable<Feature> classHierarchyFields(final Control control) {
    Iterable<Feature> _xblockexpression = null;
    {
      final LinkedHashSet<Control> superTypes = this.controlSuperTypes(control);
      final Function1<Control, Iterable<Feature>> _function = (Control type) -> {
        final Function1<Feature, Boolean> _function_1 = (Feature it) -> {
          return Boolean.valueOf((it instanceof Property));
        };
        return IterableExtensions.<Feature>filter(type.getFeatures(), _function_1);
      };
      _xblockexpression = Iterables.<Feature>concat(IterableExtensions.<Control, Iterable<Feature>>map(superTypes, _function));
    }
    return _xblockexpression;
  }
  
  public Iterable<Feature> classHierarchyMethods(final Control control) {
    Iterable<Feature> _xblockexpression = null;
    {
      final LinkedHashSet<Control> superTypes = this.controlSuperTypes(control);
      final Function1<Control, Iterable<Feature>> _function = (Control type) -> {
        final Function1<Feature, Boolean> _function_1 = (Feature it) -> {
          return Boolean.valueOf((it instanceof Operation));
        };
        return IterableExtensions.<Feature>filter(type.getFeatures(), _function_1);
      };
      _xblockexpression = Iterables.<Feature>concat(IterableExtensions.<Control, Iterable<Feature>>map(superTypes, _function));
    }
    return _xblockexpression;
  }
}
