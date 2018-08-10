package org.etri.slice.tools.adl.utils

import com.google.inject.Inject
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.IResourceDescription
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.etri.slice.tools.adl.domainmodel.DomainmodelPackage

/**
 * Index Utility Class
 */
class DomainModeIndex {

	/**
	 * get the index
	 */
	@Inject ResourceDescriptionsProvider rdp
	
	/**
	 * provide information about the containers that are visible from a given container(jar
	 */
	@Inject IContainer.Manager cm

	/**
	 * get the resource description contains given eobject 
	 */
	def getResourceDescription(EObject o) {
		val index = rdp.getResourceDescriptions(o.eResource)
		index.getResourceDescription(o.eResource.URI)
	}

	/**
	 * get all the exported eobject from the resource contains given eobject.
	 */
	def getExportedEObjectDescriptions(EObject o) {
		o.resourceDescription.exportedObjects
	}

	/**
	 * get all the exported controls in the current adl file 
	 */
	def getExportedControlEObjectDescriptions(EObject o) {
		o.resourceDescription.getExportedObjectsByType(DomainmodelPackage.eINSTANCE.control)
	}

	/**
	 * get all the visible eobject description in given type from given eobject.
	 */
	def getVisibleEObjectDescriptions(EObject o, EClass type) {
		o.visibleContainers.map [ container |
			container.getExportedObjectsByType(type)
		].flatten
	}

	/**
	 * get all the visible controls from given eobject.
	 */
	def getVisibleControlsDescriptions(EObject o) {
		o.getVisibleEObjectDescriptions(DomainmodelPackage.eINSTANCE.control)
	}

	/**
	 * get all the visible containers from given eobject, visibility across resources is delegated to IContainer
	 */
	def getVisibleContainers(EObject o) {
		val index = rdp.getResourceDescriptions(o.eResource)
		val rd = index.getResourceDescription(o.eResource.URI)

		val localContainer = cm.getContainer(rd, index);

		// get all the visible containers
		// rd: eobject's resource description
		// index: global index
		cm.getVisibleContainers(rd, index)
	}

	/**
	 * get all the external control descriptions defined in another adl files 
	 */
	def getVisibleExternalControlDescriptions(EObject o) {
		val allVisibleControls = o.visibleControlsDescriptions
		val allExportedControls = o.exportedControlEObjectDescriptions

		val difference = allVisibleControls.toSet
		difference.removeAll(allExportedControls.toSet)

		difference.toMap[qualifiedName]
	}

	/**
	 * get all control descriptions from global index
	 */
	def getAllControlDescriptions(EObject o) {
		val allVisibleControls = o.visibleControlsDescriptions
		val allExportedControls = o.exportedControlEObjectDescriptions

		allVisibleControls.forEach[System.out.println("allVisibleControls = " + it)]
		allExportedControls.forEach[System.out.println("allExportedControls = " + it)]

		val difference = allVisibleControls.toSet
		difference.removeAll(allExportedControls.toSet)

		difference.addAll(allExportedControls)

		val all = difference.map[EcoreUtil2.resolve(it.EObjectOrProxy, o)]

		all
	}

	def getObject(IResourceDescription rd, URI eObjectURI)
	{
		for (IEObjectDescription eObjectDescription : rd.getExportedObjects()) {
			if (eObjectDescription.getEObjectURI().equals(eObjectURI)) {
				return eObjectDescription
			}
		}
		
		null
	}
}
