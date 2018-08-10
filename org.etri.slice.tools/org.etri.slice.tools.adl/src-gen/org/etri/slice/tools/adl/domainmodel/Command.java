/**
 * generated by Xtext
 */
package org.etri.slice.tools.adl.domainmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.common.types.JvmType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Command</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.etri.slice.tools.adl.domainmodel.Command#getName <em>Name</em>}</li>
 *   <li>{@link org.etri.slice.tools.adl.domainmodel.Command#getContexts <em>Contexts</em>}</li>
 *   <li>{@link org.etri.slice.tools.adl.domainmodel.Command#getAction <em>Action</em>}</li>
 *   <li>{@link org.etri.slice.tools.adl.domainmodel.Command#getMethod <em>Method</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.etri.slice.tools.adl.domainmodel.DomainmodelPackage#getCommand()
 * @model
 * @generated
 */
public interface Command extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.etri.slice.tools.adl.domainmodel.DomainmodelPackage#getCommand_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.etri.slice.tools.adl.domainmodel.Command#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Contexts</b></em>' containment reference list.
   * The list contents are of type {@link org.etri.slice.tools.adl.domainmodel.CommandContext}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Contexts</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Contexts</em>' containment reference list.
   * @see org.etri.slice.tools.adl.domainmodel.DomainmodelPackage#getCommand_Contexts()
   * @model containment="true"
   * @generated
   */
  EList<CommandContext> getContexts();

  /**
   * Returns the value of the '<em><b>Action</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Action</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Action</em>' reference.
   * @see #setAction(JvmType)
   * @see org.etri.slice.tools.adl.domainmodel.DomainmodelPackage#getCommand_Action()
   * @model
   * @generated
   */
  JvmType getAction();

  /**
   * Sets the value of the '{@link org.etri.slice.tools.adl.domainmodel.Command#getAction <em>Action</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Action</em>' reference.
   * @see #getAction()
   * @generated
   */
  void setAction(JvmType value);

  /**
   * Returns the value of the '<em><b>Method</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Method</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Method</em>' attribute.
   * @see #setMethod(String)
   * @see org.etri.slice.tools.adl.domainmodel.DomainmodelPackage#getCommand_Method()
   * @model
   * @generated
   */
  String getMethod();

  /**
   * Sets the value of the '{@link org.etri.slice.tools.adl.domainmodel.Command#getMethod <em>Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Method</em>' attribute.
   * @see #getMethod()
   * @generated
   */
  void setMethod(String value);

} // Command
