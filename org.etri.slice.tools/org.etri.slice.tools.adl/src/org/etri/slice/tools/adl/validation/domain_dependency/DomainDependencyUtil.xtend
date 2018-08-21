package org.etri.slice.tools.adl.validation.domain_dependency

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Call
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.domainmodel.Exception
import org.etri.slice.tools.adl.domainmodel.Publish

class DomainDependencyUtil {
	@Inject extension IQualifiedNameProvider
	@Inject DomainManager domainManager
	
	def checkDomainDependencies(List<Resource> resources) {
		domainManager.init
		
		for (domain : resources.map[allContents.toIterable.filter(typeof(DomainDeclaration))].flatten) {		
			domainManager.addDomain(domain);
		}
		
		for (domain : resources.map[allContents.toIterable.filter(typeof(DomainDeclaration))].flatten) {	
			traveseDomain(domain)
		}
		
		domainManager
	}


	def getDomain(String packageName, String prefix, String suffix) {		
		val tmp = packageName.replace(prefix, "")

		if (suffix !== null)
			tmp.substring(1, tmp.lastIndexOf(suffix) - 1)
		else
			tmp.substring(1)
	}

	def getDomain(String qName, String suffix) {		
		if (suffix !== null)
			qName.substring(1, qName.lastIndexOf(suffix) - 1)
		else
			qName.substring(1)
	}

	def void traveseDomain(DomainDeclaration domain) {
		val eContents = domain.eContents
		val domainFQN = domain.fullyQualifiedName.toString()
		
		for (context : eContents.filter(typeof(Context))) {
			if (null !== context.superType) {
				val qualifiedName = context.superType.qualifiedName
				
				if(qualifiedName.equals("void"))
				{
					return;
				}	
					
				val package = qualifiedName.substring(0, qualifiedName.lastIndexOf("."))
				val depDomain = getDomain(package, "org.etri.slice.commons", "context")
			
				if (!domainFQN.equals(depDomain))
					domainManager.addRef(domain, domainFQN, depDomain)
			}
		}

		for (control : eContents.filter(typeof(Control))) {
			control.superTypes.forEach [

				val qualifiedName = it.qualifiedName
				
				if(qualifiedName.equals("void"))
					return;
					
				val package = qualifiedName.substring(0, qualifiedName.lastIndexOf("."))

				val depDomain = getDomain(package, "org.etri.slice.commons", "service")

				if (!domainFQN.equals(depDomain))
					domainManager.addRef(domain, domainFQN, depDomain)
			]
		}

		for (event : eContents.filter(typeof(Event))) {
			if (null !== event.superType) {
				val qualifiedName = event.superType.qualifiedName
				val package = qualifiedName.substring(0, qualifiedName.lastIndexOf("."))

				val depDomain = getDomain(package, "org.etri.slice.commons", "event")
				if (!domainFQN.equals(depDomain))
					domainManager.addRef(domain, domainFQN, depDomain)
			}
		}

		for (exception : eContents.filter(typeof(Exception))) {
			if (null !== exception.superType) {
				val qualifiedName = exception.superType.qualifiedName
				val package = qualifiedName.substring(0, qualifiedName.lastIndexOf("."))

				val depDomain = getDomain(package, "org.etri.slice.commons", null)
				if (!domainFQN.equals(depDomain))
					domainManager.addRef(domain, domainFQN, depDomain)
			}
		}

		for (agent : eContents.filter(typeof(AgentDeclaration))) {
			// hasBehaviors
			agent.behaviorSet.behaviors.forEach [ behavior |
				// situation/type
				behavior.situation.types.forEach [ type |
					switch type {
						Context: {
							val name = type.qualifiedName
							
							if(name === "void")
								return;
							
							val depDomain = name.substring(0, name.lastIndexOf("."))
							if (!domainFQN.equals(depDomain))
								domainManager.addRef(domain, domainFQN, depDomain)
						}
						Event: {
							val name = type.qualifiedName
							
							if(name === "void")
								return;
							
							val depDomain = name.substring(0, name.lastIndexOf("."))
							if (!domainFQN.equals(depDomain))
								domainManager.addRef(domain, domainFQN, depDomain)
						}
					}
				]

				// action
				val action = behavior.action

				switch action {
					Publish: {
						System.out.println("action.event = " + action.event)
						System.out.println("action.event.fullyQualifiedName = " + action.event.type.qualifiedName)
						
						val name = action.event.type.qualifiedName
						
						if(name === "void")
							return;
							
						val depDomain = getDomain(name, "org.etri.slice.commons", "event")
//						
						if (!domainFQN.equals(depDomain))
							domainManager.addRef(domain, domainFQN, depDomain)
					}
					Call: {
						val name = action.control.qualifiedName
						
						if(name === "void")
							return;
						
						val depDomain = getDomain(name, "org.etri.slice.commons", "service")
						if (!domainFQN.equals(depDomain))
							domainManager.addRef(domain, domainFQN, depDomain)
					}
				}
			]

			// hasCommandsOf
			agent.commandSets.forEach [ commandSet |
				commandSet.commands.forEach [ command |
					val name = command.action.qualifiedName
					
					val depDomain = getDomain(name, "org.etri.slice.commons", "service")
					
					if (!domainFQN.equals(depDomain))
						domainManager.addRef(domain, domainFQN, depDomain)
				]
			]
		}

		// Subdomains
		for (subDomain : eContents.filter(typeof(DomainDeclaration))) {
			traveseDomain(subDomain)
		}
	}	
}