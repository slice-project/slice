package org.etri.slice.tools.adl.utils;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.domainmodel.DomainmodelPackage;

/**
 * Index Utility Class
 */
@SuppressWarnings("all")
public class DomainModeIndex {
  /**
   * get the index
   */
  @Inject
  private ResourceDescriptionsProvider rdp;
  
  /**
   * provide information about the containers that are visible from a given container(jar
   */
  @Inject
  private IContainer.Manager cm;
  
  /**
   * get the resource description contains given eobject
   */
  public IResourceDescription getResourceDescription(final EObject o) {
    IResourceDescription _xblockexpression = null;
    {
      final IResourceDescriptions index = this.rdp.getResourceDescriptions(o.eResource());
      _xblockexpression = index.getResourceDescription(o.eResource().getURI());
    }
    return _xblockexpression;
  }
  
  /**
   * get all the exported eobject from the resource contains given eobject.
   */
  public Iterable<IEObjectDescription> getExportedEObjectDescriptions(final EObject o) {
    return this.getResourceDescription(o).getExportedObjects();
  }
  
  /**
   * get all the exported controls in the current adl file
   */
  public Iterable<IEObjectDescription> getExportedControlEObjectDescriptions(final EObject o) {
    return this.getResourceDescription(o).getExportedObjectsByType(DomainmodelPackage.eINSTANCE.getControl());
  }
  
  /**
   * get all the visible eobject description in given type from given eobject.
   */
  public Iterable<IEObjectDescription> getVisibleEObjectDescriptions(final EObject o, final EClass type) {
    final Function1<IContainer, Iterable<IEObjectDescription>> _function = (IContainer container) -> {
      return container.getExportedObjectsByType(type);
    };
    return Iterables.<IEObjectDescription>concat(ListExtensions.<IContainer, Iterable<IEObjectDescription>>map(this.getVisibleContainers(o), _function));
  }
  
  /**
   * get all the visible controls from given eobject.
   */
  public Iterable<IEObjectDescription> getVisibleControlsDescriptions(final EObject o) {
    return this.getVisibleEObjectDescriptions(o, DomainmodelPackage.eINSTANCE.getControl());
  }
  
  /**
   * get all the visible containers from given eobject, visibility across resources is delegated to IContainer
   */
  public List<IContainer> getVisibleContainers(final EObject o) {
    List<IContainer> _xblockexpression = null;
    {
      final IResourceDescriptions index = this.rdp.getResourceDescriptions(o.eResource());
      final IResourceDescription rd = index.getResourceDescription(o.eResource().getURI());
      final IContainer localContainer = this.cm.getContainer(rd, index);
      _xblockexpression = this.cm.getVisibleContainers(rd, index);
    }
    return _xblockexpression;
  }
  
  /**
   * get all the external control descriptions defined in another adl files
   */
  public Map<QualifiedName, IEObjectDescription> getVisibleExternalControlDescriptions(final EObject o) {
    Map<QualifiedName, IEObjectDescription> _xblockexpression = null;
    {
      final Iterable<IEObjectDescription> allVisibleControls = this.getVisibleControlsDescriptions(o);
      final Iterable<IEObjectDescription> allExportedControls = this.getExportedControlEObjectDescriptions(o);
      final Set<IEObjectDescription> difference = IterableExtensions.<IEObjectDescription>toSet(allVisibleControls);
      difference.removeAll(IterableExtensions.<IEObjectDescription>toSet(allExportedControls));
      final Function1<IEObjectDescription, QualifiedName> _function = (IEObjectDescription it) -> {
        return it.getQualifiedName();
      };
      _xblockexpression = IterableExtensions.<QualifiedName, IEObjectDescription>toMap(difference, _function);
    }
    return _xblockexpression;
  }
  
  /**
   * get all control descriptions from global index
   */
  public Iterable<EObject> getAllControlDescriptions(final EObject o) {
    Iterable<EObject> _xblockexpression = null;
    {
      final Iterable<IEObjectDescription> allVisibleControls = this.getVisibleControlsDescriptions(o);
      final Iterable<IEObjectDescription> allExportedControls = this.getExportedControlEObjectDescriptions(o);
      final Consumer<IEObjectDescription> _function = (IEObjectDescription it) -> {
        System.out.println(("allVisibleControls = " + it));
      };
      allVisibleControls.forEach(_function);
      final Consumer<IEObjectDescription> _function_1 = (IEObjectDescription it) -> {
        System.out.println(("allExportedControls = " + it));
      };
      allExportedControls.forEach(_function_1);
      final Set<IEObjectDescription> difference = IterableExtensions.<IEObjectDescription>toSet(allVisibleControls);
      difference.removeAll(IterableExtensions.<IEObjectDescription>toSet(allExportedControls));
      Iterables.<IEObjectDescription>addAll(difference, allExportedControls);
      final Function1<IEObjectDescription, EObject> _function_2 = (IEObjectDescription it) -> {
        return EcoreUtil2.resolve(it.getEObjectOrProxy(), o);
      };
      final Iterable<EObject> all = IterableExtensions.<IEObjectDescription, EObject>map(difference, _function_2);
      _xblockexpression = all;
    }
    return _xblockexpression;
  }
  
  public IEObjectDescription getObject(final IResourceDescription rd, final URI eObjectURI) {
    Object _xblockexpression = null;
    {
      Iterable<IEObjectDescription> _exportedObjects = rd.getExportedObjects();
      for (final IEObjectDescription eObjectDescription : _exportedObjects) {
        boolean _equals = eObjectDescription.getEObjectURI().equals(eObjectURI);
        if (_equals) {
          return eObjectDescription;
        }
      }
      _xblockexpression = null;
    }
    return ((IEObjectDescription)_xblockexpression);
  }
}
