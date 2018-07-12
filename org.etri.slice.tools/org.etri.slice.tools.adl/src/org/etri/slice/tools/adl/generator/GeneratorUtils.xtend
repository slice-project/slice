/*******************************************************************************
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import java.util.StringTokenizer
import java.util.UUID
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.eclipse.xtext.xbase.compiler.StringBuilderBasedAppendable
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.domainmodel.Operation
import org.etri.slice.tools.adl.domainmodel.Property

/**
 * @author yhsuh - Initial contribution and API
 */
class GeneratorUtils {
	
	@Inject extension TypeReferenceSerializer
	@Inject extension JvmTypesBuilder	
	@Inject extension IQualifiedNameProvider
	
	def adaptToSlice(QualifiedName qname, String infix) {
		val segments = newArrayList('org', 'etri', 'slice', 'commons')
		val tokens = new StringTokenizer(qname.toString, ".")
		while ( tokens.hasMoreElements ) {
			segments.add(tokens.nextToken)
		}
		if ( infix.length > 0 ) segments.add(segments.size-1, infix)
		
		return QualifiedName.create(segments)
	}	
	
  
	def shortName(JvmTypeReference ref, ImportManager importManager) {
		val result = new StringBuilderBasedAppendable(importManager)
		ref.serialize(ref.eContainer, result);
		result.toString
	}
	
	def shortName(Context it, ImportManager importManager) {
		importManager.serialize(toClass(fullyQualifiedName.adaptToSlice("context")))
	}
	
	def shortName(Control it, ImportManager importManager) {
		importManager.serialize(toClass(fullyQualifiedName.adaptToSlice("service")))
	}
	
	def shortName(Event it, ImportManager importManager) {
		importManager.serialize(toClass(fullyQualifiedName.adaptToSlice("event")))
	}		
	
	def generateSerialVersionUID() {
		return UUID.randomUUID.mostSignificantBits
	}
	
	def toJvmGenericType(EObject obj, QualifiedName qname, String infix) {
		return toClass(obj, adaptToSlice(qname, infix))
	}
	
   	def parameters(Operation it, ImportManager importManager) 
   		'''(«FOR p : params SEPARATOR ', '»«p.parameterType.shortName(importManager)» «p.name»«ENDFOR»)'''
   	
   	def parameters(JvmOperation it, ImportManager importManager)
   		'''(«FOR p : parameters SEPARATOR ', '»«p.parameterType.shortName(importManager)» «p.name»«ENDFOR»)'''
    	
   	def exceptions(Operation it, ImportManager importManager) 
 		'''«IF exceptions.size > 0» throws «FOR e : exceptions SEPARATOR ', '»«e.shortName(importManager)»«ENDFOR»«ELSE»«ENDIF»'''
 		
   	def exceptions(JvmOperation it, ImportManager importManager) 
 		'''«IF exceptions.size > 0» throws «FOR e : exceptions SEPARATOR ', '»«e.shortName(importManager)»«ENDFOR»«ELSE»«ENDIF»''' 		
 	
 	def synchronized JvmGenericType toInterface(Control control) {
		control.toInterface( control.fullyQualifiedName.adaptToSlice("service").toString ) [
			documentation = control.documentation
			for (superType : control.superTypes ) {
				superTypes += superType.cloneWithProxies
			}
			
			// now let's go over the features
			for ( f : control.features ) {
				switch f {
			
					// for properties we create a field, a getter and a setter
					Property : {
						members += f.toGetter(f.name, f.type)
						members += f.toSetter(f.name, f.type)
					}
			
					// operations are mapped to methods
					Operation : {
						members += f.toMethod(f.name, f.type ?: inferredType) [
							documentation = f.documentation
							for (p : f.params) {
								parameters += p.toParameter(p.name, p.parameterType)
							}
							
							for (e : f.exceptions) {
								exceptions += e.cloneWithProxies;
							}	
						]
					}
				}
			}
		]		
 	}	
}
