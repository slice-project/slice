/**
 * generated by Xtext
 */
package org.etri.slice.tools.adl.domainmodel.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.etri.slice.tools.adl.domainmodel.AbstractElement;
import org.etri.slice.tools.adl.domainmodel.Action;
import org.etri.slice.tools.adl.domainmodel.Agency;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Behavior;
import org.etri.slice.tools.adl.domainmodel.BehaviorSet;
import org.etri.slice.tools.adl.domainmodel.Call;
import org.etri.slice.tools.adl.domainmodel.Command;
import org.etri.slice.tools.adl.domainmodel.CommandContext;
import org.etri.slice.tools.adl.domainmodel.CommandSet;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.DataType;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.domainmodel.DomainModel;
import org.etri.slice.tools.adl.domainmodel.DomainmodelPackage;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.NoOp;
import org.etri.slice.tools.adl.domainmodel.Operation;
import org.etri.slice.tools.adl.domainmodel.Property;
import org.etri.slice.tools.adl.domainmodel.Publish;
import org.etri.slice.tools.adl.domainmodel.RuleSet;
import org.etri.slice.tools.adl.domainmodel.Situation;
import org.etri.slice.tools.adl.domainmodel.Topic;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.etri.slice.tools.adl.domainmodel.DomainmodelPackage
 * @generated
 */
public class DomainmodelSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static DomainmodelPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DomainmodelSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = DomainmodelPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case DomainmodelPackage.DOMAIN_MODEL:
      {
        DomainModel domainModel = (DomainModel)theEObject;
        T result = caseDomainModel(domainModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.ABSTRACT_ELEMENT:
      {
        AbstractElement abstractElement = (AbstractElement)theEObject;
        T result = caseAbstractElement(abstractElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.DOMAIN_DECLARATION:
      {
        DomainDeclaration domainDeclaration = (DomainDeclaration)theEObject;
        T result = caseDomainDeclaration(domainDeclaration);
        if (result == null) result = caseAbstractElement(domainDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.CONTEXT:
      {
        Context context = (Context)theEObject;
        T result = caseContext(context);
        if (result == null) result = caseAbstractElement(context);
        if (result == null) result = caseDataType(context);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.EVENT:
      {
        Event event = (Event)theEObject;
        T result = caseEvent(event);
        if (result == null) result = caseAbstractElement(event);
        if (result == null) result = caseDataType(event);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.EXCEPTION:
      {
        org.etri.slice.tools.adl.domainmodel.Exception exception = (org.etri.slice.tools.adl.domainmodel.Exception)theEObject;
        T result = caseException(exception);
        if (result == null) result = caseAbstractElement(exception);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.CONTROL:
      {
        Control control = (Control)theEObject;
        T result = caseControl(control);
        if (result == null) result = caseAbstractElement(control);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.AGENT_DECLARATION:
      {
        AgentDeclaration agentDeclaration = (AgentDeclaration)theEObject;
        T result = caseAgentDeclaration(agentDeclaration);
        if (result == null) result = caseAbstractElement(agentDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.RULE_SET:
      {
        RuleSet ruleSet = (RuleSet)theEObject;
        T result = caseRuleSet(ruleSet);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.BEHAVIOR_SET:
      {
        BehaviorSet behaviorSet = (BehaviorSet)theEObject;
        T result = caseBehaviorSet(behaviorSet);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.BEHAVIOR:
      {
        Behavior behavior = (Behavior)theEObject;
        T result = caseBehavior(behavior);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.COMMAND_SET:
      {
        CommandSet commandSet = (CommandSet)theEObject;
        T result = caseCommandSet(commandSet);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.COMMAND:
      {
        Command command = (Command)theEObject;
        T result = caseCommand(command);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.COMMAND_CONTEXT:
      {
        CommandContext commandContext = (CommandContext)theEObject;
        T result = caseCommandContext(commandContext);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.DATA_TYPE:
      {
        DataType dataType = (DataType)theEObject;
        T result = caseDataType(dataType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.SITUATION:
      {
        Situation situation = (Situation)theEObject;
        T result = caseSituation(situation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.ACTION:
      {
        Action action = (Action)theEObject;
        T result = caseAction(action);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.PUBLISH:
      {
        Publish publish = (Publish)theEObject;
        T result = casePublish(publish);
        if (result == null) result = caseAction(publish);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.NO_OP:
      {
        NoOp noOp = (NoOp)theEObject;
        T result = caseNoOp(noOp);
        if (result == null) result = caseAction(noOp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.CALL:
      {
        Call call = (Call)theEObject;
        T result = caseCall(call);
        if (result == null) result = caseAction(call);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.FEATURE:
      {
        Feature feature = (Feature)theEObject;
        T result = caseFeature(feature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.PROPERTY:
      {
        Property property = (Property)theEObject;
        T result = caseProperty(property);
        if (result == null) result = caseFeature(property);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.OPERATION:
      {
        Operation operation = (Operation)theEObject;
        T result = caseOperation(operation);
        if (result == null) result = caseFeature(operation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.TOPIC:
      {
        Topic topic = (Topic)theEObject;
        T result = caseTopic(topic);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DomainmodelPackage.AGENCY:
      {
        Agency agency = (Agency)theEObject;
        T result = caseAgency(agency);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Domain Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Domain Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDomainModel(DomainModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Abstract Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Abstract Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAbstractElement(AbstractElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Domain Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Domain Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDomainDeclaration(DomainDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Context</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Context</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseContext(Context object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Event</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Event</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEvent(Event object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Exception</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Exception</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseException(org.etri.slice.tools.adl.domainmodel.Exception object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Control</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Control</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseControl(Control object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Agent Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Agent Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAgentDeclaration(AgentDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Rule Set</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Rule Set</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRuleSet(RuleSet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Behavior Set</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Behavior Set</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBehaviorSet(BehaviorSet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Behavior</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Behavior</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBehavior(Behavior object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Command Set</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Command Set</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCommandSet(CommandSet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Command</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Command</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCommand(Command object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Command Context</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Command Context</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCommandContext(CommandContext object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Data Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDataType(DataType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Situation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Situation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSituation(Situation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Action</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Action</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAction(Action object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Publish</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Publish</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePublish(Publish object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>No Op</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>No Op</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNoOp(NoOp object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Call</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Call</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCall(Call object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFeature(Feature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Property</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProperty(Property object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperation(Operation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Topic</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Topic</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTopic(Topic object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Agency</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Agency</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAgency(Agency object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //DomainmodelSwitch
