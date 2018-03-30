package org.etri.slice.tools.adl.ui.tests

import org.etri.slice.tools.adl.ui.internal.AdlActivator
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.ui.testing.AbstractOutlineTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Lorenzo Bettini - Initial contribution and API
 */
@RunWith(XtextRunner)
@InjectWith(DomainmodelUiInjectorProvider)
class OutlineTest extends AbstractOutlineTest {

	override protected getEditorId() {
		AdlActivator.ORG_ETRI_SLICE_TOOLS_ADL_DOMAINMODEL
	}

	@Test def void testOutline() {
		'''
			entity Foo {
				name : String
				op doStuff(String x) : String {
					return x + ' ' + this.name
				}
			}
		'''.assertAllLabels(
			'''
				Foo
				  name : String
				  doStuff(String) : String
			'''
		)
	}

	@Test def void testOutlineWithPackage() {
		'''
			package mypackage {
				entity Foo {
					name : String
					op doStuff(String x) : String {
						return x + ' ' + this.name
					}
				}
			}
		'''.assertAllLabels(
			'''
				mypackage
				  Foo
				    name : String
				    doStuff(String) : String
			'''
		)
	}
}
