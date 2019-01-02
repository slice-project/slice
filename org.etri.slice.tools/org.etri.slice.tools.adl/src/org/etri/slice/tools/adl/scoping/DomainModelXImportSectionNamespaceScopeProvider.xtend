package org.etri.slice.tools.adl.scoping

import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.concurrent.ConcurrentHashMap
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.scoping.impl.ImportNormalizer
import org.eclipse.xtext.xbase.scoping.XImportSectionNamespaceScopeProvider
 
class DomainModelXImportSectionNamespaceScopeProvider extends XImportSectionNamespaceScopeProvider 
{
 	static List<QualifiedName> s_imports = new ArrayList<QualifiedName>();
 	
	static def addImplicitImport(QualifiedName qname) 
	{
		if(!s_imports.contains(qname.append("context"))) 
			s_imports.add(qname.append("context"))
			
		if(!s_imports.contains(qname.append("event")))
			s_imports.add(qname.append("event"))
			
		if(!s_imports.contains(qname.append("service"))) 
			s_imports.add(qname.append("service"))
	}
 
	override List<ImportNormalizer> getImplicitImports(boolean ignoreCase) 
	{
		// java.* and org.eclipse.xtext.xbase.lib.*
		val predefinedImplicitImport = super.getImplicitImports(ignoreCase);
		
		var imports = new ArrayList<ImportNormalizer>()

		// Call doCreateImportNormalizer to create an ImportNormalizer.
		for (key : s_imports)
			imports.add(doCreateImportNormalizer(key, true, ignoreCase))

		imports.addAll(predefinedImplicitImport)

		return imports
	}
}
