package org.etri.slice.tools.adl.validation.domain_dependency

import java.util.ArrayList
import java.util.List
import java.util.Stack
import org.eclipse.emf.ecore.EObject
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration

class Domain {
	public String domain	
	public EObject eObject
	public List<Domain> dependencies = new ArrayList<Domain>()
	
	new(String domain)
	{
		this.domain = domain
	}
	
	new(DomainDeclaration domain, String domainFQN)
	{
		this.eObject = domain
		this.domain = domainFQN
	}
	
	def addRef(Domain domain)
	{
		if(!dependencies.contains(domain))
			dependencies.add(domain)
	}
		
	def hasCycle()
	{
		val visited = new Stack<String>();
		return hasCycle(visited)
	}
	
	def boolean hasCycle(Domain current, Stack<String> visited)
	{
		// exit condition
		if(visited.isVisited(current))
			return true
		
		visited.push(current.domain)
		
		// recursion		
		for(domain : current.dependencies)
		{			
			if(domain.hasCycle(visited))
				return true
		}
		
		visited.pop()
		
		return false;
	}
	
	def isVisited(Stack<String> visited, Domain current)
	{
		val exists = visited.filter[domain|
			current.domain.equals(domain)
		]
		
		exists.size > 0
	}
	
	def hasDependencies()
	{
		dependencies.size != 0
	}
}