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

/**
 * @author yhsuh - Initial contribution and API
 */
class GeneratorUtils {
	
	@Inject extension TypeReferenceSerializer
	@Inject extension JvmTypesBuilder	
	@Inject extension IQualifiedNameProvider
	
	def  adaptToSlice(QualifiedName qname, String infix) {
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
}
