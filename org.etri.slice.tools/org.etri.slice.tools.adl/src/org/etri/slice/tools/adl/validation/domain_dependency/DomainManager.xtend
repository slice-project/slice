package org.etri.slice.tools.adl.validation.domain_dependency

import com.google.inject.Inject
import com.google.inject.Singleton
import java.util.ArrayList
import java.util.HashMap
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration

@Singleton
class DomainManager {
	@Inject extension IQualifiedNameProvider
	
	public HashMap<String, Domain> domains = new HashMap<String, Domain>()
		
	def init() {
		domains.clear
	}
	
	def existCycle() {
		for(domain : domains.values)
		{
			if(domain.hasCycle)
				return true
		}
		
		false
	}
	
	def addDomain(DomainDeclaration domain)
	{
		val domainFQN = domain.fullyQualifiedName.toString()
		
		if(!domains.containsKey(domain))
			domains.put(domainFQN, new Domain(domain, domainFQN))
	}
	
	def addRef(DomainDeclaration domain, String source, String target)
	{		
		if(!domains.containsKey(source))
			domains.put(source, new Domain(domain, source))
			
		if(!domains.containsKey(target))
			domains.put(target, new Domain(target))
			
		domains.get(source).addRef(domains.get(target))
	}
		
	def getBuildOrderedDomainList()
	{		
		val orderedList = new ArrayList<Domain>()		
				
		domains.values.forEach[ domain |
			if(-1 == orderedList.indexOf(domain))
				orderedList.add(domain)
			
			val index = orderedList.indexOf(domain)
						
			if(domain.hasDependencies)
			{
				domain.dependencies.forEach[ dep, index2 | 
					val depIndex = orderedList.indexOf(dep)
									
					if(-1 == depIndex)
						orderedList.add(index, dep)
					else if(index < depIndex)
					{
						// replace
						orderedList.remove(index)
						orderedList.remove(dep)
						orderedList.add(index, dep)
						orderedList.add(depIndex, domain)	
					}							
				]
			}					
		]
		
		orderedList
	}
	
	def getDomain(String domain)
	{
		return domains.get(domain)
	}	
}