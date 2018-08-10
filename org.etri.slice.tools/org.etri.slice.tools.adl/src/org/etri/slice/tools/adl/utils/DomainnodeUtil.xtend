package org.etri.slice.tools.adl.utils

import com.google.inject.Inject
import java.util.LinkedHashSet
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Operation
import org.etri.slice.tools.adl.domainmodel.Property

class DomainnodeUtil {
	
	@Inject extension IJvmModelAssociations
	
	def classHierarchyFields(Context context)
	{
		val inferredJavaType = context.jvmElements.filter(JvmGenericType).head	
		val superTypes = inferredJavaType.superTypes
		
		superTypes.map[
			{
				val genericType = type as JvmGenericType
				genericType.members.map[declaringType.declaredFields].flatten()
			}
		].flatten;
	}
	
	def controlSuperTypes(Control control)
	{
		val superTypes = new LinkedHashSet<Control>()
		
		var current = control.superTypes
				
		while(current !== null && current instanceof Control && !superTypes.contains(current))
		{
			superTypes.add(current as Control)		
			
			if(current instanceof Control)
				current = current.superTypes
			else
				current = null
		}
		
		superTypes.forEach[System.out.println]
		
		superTypes
	}
	
	def classHierarchyFields(Control control)
	{
		val superTypes = control.controlSuperTypes
		
		superTypes.map[ type | 
			{
				type.features.filter[it instanceof Property]
			}
		].flatten;
	}
	
	def classHierarchyMethods(Control control)
	{
		val superTypes = control.controlSuperTypes
		
		superTypes.map[ type | 
			{
				type.features.filter[it instanceof Operation]
			}
		].flatten;
	}
}
