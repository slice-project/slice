/**
 * generated by Xtext
 */
package org.etri.slice.tools.adl.validation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.etri.slice.tools.adl.domainmodel.Agency;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.domainmodel.DomainmodelPackage;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.domainmodel.Feature;
import org.etri.slice.tools.adl.domainmodel.Property;
import org.etri.slice.tools.adl.domainmodel.Topic;
import org.etri.slice.tools.adl.jvmmodel.CommonInterfaces;
import org.etri.slice.tools.adl.utils.DomainModeIndex;
import org.etri.slice.tools.adl.validation.AbstractDomainmodelValidator;
import org.etri.slice.tools.adl.validation.IssueCodes;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager;

/**
 * This class contains custom validation rules.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
@SuppressWarnings("all")
public class DomainmodelValidator extends AbstractDomainmodelValidator {
  private final String TOPIC_LEVEL_REG_EXPR = "[a-zA-Z\\$][a-zA-Z0-9_]*";
  
  private final String TOPIC_NAME_REG_EXPR = (((("^" + this.TOPIC_LEVEL_REG_EXPR) + "(/") + this.TOPIC_LEVEL_REG_EXPR) + ")*$");
  
  private Pattern topicNamePattern;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private IJvmModelAssociations _iJvmModelAssociations;
  
  @Inject
  @Extension
  private DomainModeIndex _domainModeIndex;
  
  @Inject
  @Extension
  private IJvmTypeProvider.Factory _factory;
  
  @Inject
  private DomainManager domainManager;
  
  @Check
  public void checkTypeNameStartsWithCapital(final Context context) {
    boolean _isUpperCase = Character.isUpperCase(context.getName().charAt(0));
    boolean _not = (!_isUpperCase);
    if (_not) {
      this.warning("Name should start with a capital", DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
        ValidationMessageAcceptor.INSIGNIFICANT_INDEX, IssueCodes.INVALID_TYPE_NAME, context.getName());
    }
  }
  
  @Check
  public void checkFeatureNameStartsWithLowercase(final Feature feature) {
    boolean _isLowerCase = Character.isLowerCase(feature.getName().charAt(0));
    boolean _not = (!_isLowerCase);
    if (_not) {
      this.warning("Name should start with a lowercase", DomainmodelPackage.Literals.FEATURE__NAME, 
        ValidationMessageAcceptor.INSIGNIFICANT_INDEX, IssueCodes.INVALID_FEATURE_NAME, feature.getName());
    }
  }
  
  @Check
  public void checkPackage(final DomainDeclaration domains) {
    boolean _isEmpty = Strings.isEmpty(domains.getName());
    if (_isEmpty) {
      this.error("Name cannot be empty", DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME);
    }
    boolean _equals = domains.getName().equals("java");
    if (_equals) {
      this.error("Invalid package name", DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME);
    }
  }
  
  @Check
  public void checkExternalDuplicatedControls(final Control control) {
    final Map<QualifiedName, IEObjectDescription> externalControls = this._domainModeIndex.getVisibleExternalControlDescriptions(control);
    final QualifiedName controlName = this._iQualifiedNameProvider.getFullyQualifiedName(control);
    boolean _containsKey = externalControls.containsKey(controlName);
    if (_containsKey) {
      this.error(
        "Duplicated control name in external adl file", control, 
        DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
        IssueCodes.DUPLICATE_ELEMENT);
    }
  }
  
  /**
   * check local duplicated names
   */
  @Check
  public void checkDuplicatedNames(final DomainDeclaration domain) {
    final HashSet<String> contextNames = CollectionLiterals.<String>newHashSet();
    Iterable<Context> _filter = Iterables.<Context>filter(domain.getElements(), Context.class);
    for (final Context context : _filter) {
      boolean _add = contextNames.add(context.getName());
      boolean _not = (!_add);
      if (_not) {
        this.error(
          "Duplicated context name", context, 
          DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
          IssueCodes.DUPLICATE_ELEMENT);
      }
    }
    Iterable<Control> _filter_1 = Iterables.<Control>filter(domain.getElements(), Control.class);
    for (final Control control : _filter_1) {
      boolean _add_1 = contextNames.add(control.getName());
      boolean _not_1 = (!_add_1);
      if (_not_1) {
        this.error(
          "Duplicated control name", control, 
          DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
          IssueCodes.DUPLICATE_ELEMENT);
      }
    }
    final HashSet<String> agentNames = CollectionLiterals.<String>newHashSet();
    Iterable<AgentDeclaration> _filter_2 = Iterables.<AgentDeclaration>filter(domain.getElements(), AgentDeclaration.class);
    for (final AgentDeclaration agent : _filter_2) {
      boolean _add_2 = agentNames.add(agent.getName());
      boolean _not_2 = (!_add_2);
      if (_not_2) {
        this.error(
          "Duplicated agent name", agent, 
          DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
          IssueCodes.DUPLICATE_ELEMENT);
      }
    }
    final HashSet<String> eventNames = CollectionLiterals.<String>newHashSet();
    Iterable<Event> _filter_3 = Iterables.<Event>filter(domain.getElements(), Event.class);
    for (final Event event : _filter_3) {
      boolean _add_3 = eventNames.add(event.getName());
      boolean _not_3 = (!_add_3);
      if (_not_3) {
        this.error(
          "Duplicated event name", event, 
          DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
          IssueCodes.DUPLICATE_ELEMENT);
      }
    }
    final HashSet<String> exceptionNames = CollectionLiterals.<String>newHashSet();
    Iterable<org.etri.slice.tools.adl.domainmodel.Exception> _filter_4 = Iterables.<org.etri.slice.tools.adl.domainmodel.Exception>filter(domain.getElements(), org.etri.slice.tools.adl.domainmodel.Exception.class);
    for (final org.etri.slice.tools.adl.domainmodel.Exception exception : _filter_4) {
      boolean _add_4 = exceptionNames.add(exception.getName());
      boolean _not_4 = (!_add_4);
      if (_not_4) {
        this.error(
          "Duplicated exception name", exception, 
          DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, 
          IssueCodes.DUPLICATE_ELEMENT);
      }
    }
  }
  
  private void checkNoDuplicateElements(final Iterable<? extends Feature> elements, final String desc, final EStructuralFeature feature, final String issueCode) {
    final HashMultimap<String, Feature> multiMap = HashMultimap.<String, Feature>create();
    for (final Feature e : elements) {
      multiMap.put(e.getName(), e);
    }
    Set<Map.Entry<String, Collection<Feature>>> _entrySet = multiMap.asMap().entrySet();
    for (final Map.Entry<String, Collection<Feature>> entry : _entrySet) {
      {
        final Collection<Feature> duplicates = entry.getValue();
        int _size = duplicates.size();
        boolean _greaterThan = (_size > 1);
        if (_greaterThan) {
          for (final Feature d : duplicates) {
            String _name = d.getName();
            String _plus = ((("Duplicate " + desc) + " \'") + _name);
            String _plus_1 = (_plus + "\'");
            this.error(_plus_1, d, feature, issueCode);
          }
        }
      }
    }
  }
  
  @Check
  public void checkFeatureDuplicatedInControl(final Control control) {
    this.checkNoDuplicateElements(Iterables.<Feature>filter(control.getFeatures(), Feature.class), "feature", 
      DomainmodelPackage.Literals.FEATURE__NAME, IssueCodes.DUPLICATE_FEATURE);
  }
  
  @Check
  public void checkPropertyDuplicatedInContext(final Context context) {
    this.checkNoDuplicateElements(Iterables.<Property>filter(context.getProperties(), Property.class), "property", 
      DomainmodelPackage.Literals.FEATURE__NAME, IssueCodes.DUPLICATE_PROPERTY);
  }
  
  @Check
  public void checkPropertyDuplicatedInEvent(final Event event) {
    this.checkNoDuplicateElements(Iterables.<Property>filter(event.getProperties(), Property.class), "property", 
      DomainmodelPackage.Literals.FEATURE__NAME, IssueCodes.DUPLICATE_PROPERTY);
  }
  
  @Check
  public void checkAgencyIPFormat(final Agency agentcy) {
  }
  
  @Check
  public void checkTopicName(final Topic topic) {
    String name = topic.getName();
    int _length = name.length();
    boolean _equals = (_length == 0);
    if (_equals) {
      this.error("Invalid topic name, topic name must consist of at least one character to be valid", topic, 
        DomainmodelPackage.Literals.TOPIC__NAME);
    } else {
      if ((null == this.topicNamePattern)) {
        this.topicNamePattern = Pattern.compile(this.TOPIC_NAME_REG_EXPR);
      }
      boolean _matches = this.topicNamePattern.matcher(name).matches();
      boolean _not = (!_matches);
      if (_not) {
        this.error("Invalid topic name, topic name format is invalid.", topic, 
          DomainmodelPackage.Literals.TOPIC__NAME);
      }
    }
  }
  
  private boolean hasCycleInHierarchy(final JvmGenericType t, final Set<JvmGenericType> processed) {
    if (((t.getIdentifier().equalsIgnoreCase(CommonInterfaces.CONTEXT_BASE) || t.getIdentifier().equalsIgnoreCase(CommonInterfaces.EVENT_BASE)) || t.getIdentifier().equalsIgnoreCase(CommonInterfaces.EXCEPTION_INTERFACE))) {
      return false;
    }
    boolean _contains = processed.contains(t);
    if (_contains) {
      return true;
    }
    processed.add(t);
    final Function1<JvmTypeReference, JvmType> _function = (JvmTypeReference it) -> {
      return it.getType();
    };
    final Function1<JvmGenericType, Boolean> _function_1 = (JvmGenericType it) -> {
      return Boolean.valueOf(this.hasCycleInHierarchy(it, processed));
    };
    return IterableExtensions.<JvmGenericType>exists(Iterables.<JvmGenericType>filter(ListExtensions.<JvmTypeReference, JvmType>map(t.getSuperTypes(), _function), JvmGenericType.class), _function_1);
  }
  
  @Check
  public void checkNoCycleInContextHierarchy(final Context context) {
    final JvmGenericType inferredJavaType = IterableExtensions.<JvmGenericType>head(Iterables.<JvmGenericType>filter(this._iJvmModelAssociations.getJvmElements(context), JvmGenericType.class));
    boolean _hasCycleInHierarchy = this.hasCycleInHierarchy(inferredJavaType, CollectionLiterals.<JvmGenericType>newHashSet());
    if (_hasCycleInHierarchy) {
      String _name = context.getName();
      String _plus = ("cycle in hierarchy of context \'" + _name);
      String _plus_1 = (_plus + "\'");
      this.error(_plus_1, context, 
        DomainmodelPackage.Literals.CONTEXT__SUPER_TYPE, IssueCodes.CONTEXT_HIERARCHY_CYCLE);
    }
  }
  
  @Check
  public void checkNoCycleInControlHierarchy(final Control cotrol) {
    final Consumer<JvmGenericType> _function = (JvmGenericType inferredJavaType) -> {
      boolean _hasCycleInHierarchy = this.hasCycleInHierarchy(inferredJavaType, CollectionLiterals.<JvmGenericType>newHashSet());
      if (_hasCycleInHierarchy) {
        String _name = cotrol.getName();
        String _plus = ("cycle in hierarchy of control \'" + _name);
        String _plus_1 = (_plus + "\'");
        this.error(_plus_1, cotrol, 
          DomainmodelPackage.Literals.CONTROL__SUPER_TYPES, IssueCodes.CONTROL_HIERARCHY_CYCLE);
      }
    };
    Iterables.<JvmGenericType>filter(this._iJvmModelAssociations.getJvmElements(cotrol), JvmGenericType.class).forEach(_function);
  }
  
  @Check
  public void checkNoCycleInEventHierarchy(final Event event) {
    final JvmGenericType inferredJavaType = IterableExtensions.<JvmGenericType>head(Iterables.<JvmGenericType>filter(this._iJvmModelAssociations.getJvmElements(event), JvmGenericType.class));
    boolean _hasCycleInHierarchy = this.hasCycleInHierarchy(inferredJavaType, CollectionLiterals.<JvmGenericType>newHashSet());
    if (_hasCycleInHierarchy) {
      String _name = event.getName();
      String _plus = ("cycle in hierarchy of event \'" + _name);
      String _plus_1 = (_plus + "\'");
      this.error(_plus_1, event, 
        DomainmodelPackage.Literals.EVENT__SUPER_TYPE, IssueCodes.EVENT_HIERARCHY_CYCLE);
    }
  }
  
  @Check
  public void checkNoCycleInExceptionHierarchy(final org.etri.slice.tools.adl.domainmodel.Exception exception) {
    final JvmGenericType inferredJavaType = IterableExtensions.<JvmGenericType>head(Iterables.<JvmGenericType>filter(this._iJvmModelAssociations.getJvmElements(exception), JvmGenericType.class));
    boolean _hasCycleInHierarchy = this.hasCycleInHierarchy(inferredJavaType, CollectionLiterals.<JvmGenericType>newHashSet());
    if (_hasCycleInHierarchy) {
      String _name = exception.getName();
      String _plus = ("cycle in hierarchy of exception \'" + _name);
      String _plus_1 = (_plus + "\'");
      this.error(_plus_1, exception, 
        DomainmodelPackage.Literals.EXCEPTION__SUPER_TYPE, IssueCodes.EXCEPTION_HIERARCHY_CYCLE);
    }
  }
  
  @Check
  public void checkContextSuperType(final Context context) {
    JvmParameterizedTypeReference _superType = context.getSuperType();
    boolean _tripleNotEquals = (_superType != null);
    if (_tripleNotEquals) {
      final IJvmTypeProvider typeProvider = this._factory.createTypeProvider(context.eResource().getResourceSet());
      final JvmType contextBase = typeProvider.findTypeByName(CommonInterfaces.CONTEXT_BASE);
      LightweightTypeReference _superType_1 = this.toLightweightTypeReference(context.getSuperType()).getSuperType(contextBase);
      boolean _tripleEquals = (null == _superType_1);
      if (_tripleEquals) {
        String _name = context.getName();
        String _plus = ("context \'" + _name);
        String _plus_1 = (_plus + "\' must inherits another context.");
        this.error(_plus_1, context, 
          DomainmodelPackage.Literals.CONTEXT__SUPER_TYPE, IssueCodes.CONTEXT_MUST_EXTENDS_CONTEXT);
      }
    }
  }
  
  @Check
  public void checkEventSuperType(final Event event) {
    JvmParameterizedTypeReference _superType = event.getSuperType();
    boolean _tripleNotEquals = (_superType != null);
    if (_tripleNotEquals) {
      final IJvmTypeProvider typeProvider = this._factory.createTypeProvider(event.eResource().getResourceSet());
      final JvmType eventBase = typeProvider.findTypeByName(CommonInterfaces.EVENT_BASE);
      LightweightTypeReference _superType_1 = this.toLightweightTypeReference(event.getSuperType()).getSuperType(eventBase);
      boolean _tripleEquals = (null == _superType_1);
      if (_tripleEquals) {
        String _name = event.getName();
        String _plus = ("event \'" + _name);
        String _plus_1 = (_plus + "\' must inherits another event.");
        this.error(_plus_1, event, 
          DomainmodelPackage.Literals.EVENT__SUPER_TYPE, IssueCodes.EVENT_MUST_EXTENDS_EVENT);
      }
    }
  }
  
  @Check
  public void checkExceptionSuperType(final org.etri.slice.tools.adl.domainmodel.Exception exception) {
    JvmParameterizedTypeReference _superType = exception.getSuperType();
    boolean _tripleNotEquals = (_superType != null);
    if (_tripleNotEquals) {
      final IJvmTypeProvider typeProvider = this._factory.createTypeProvider(exception.eResource().getResourceSet());
      final JvmType exceptionBase = typeProvider.findTypeByName(CommonInterfaces.EXCEPTION_INTERFACE);
      LightweightTypeReference _superType_1 = this.toLightweightTypeReference(exception.getSuperType()).getSuperType(exceptionBase);
      boolean _tripleEquals = (null == _superType_1);
      if (_tripleEquals) {
        String _name = exception.getName();
        String _plus = ("exception \'" + _name);
        String _plus_1 = (_plus + "\' must inherits another exception.");
        this.error(_plus_1, exception, 
          DomainmodelPackage.Literals.EXCEPTION__SUPER_TYPE, IssueCodes.EXCEPTION_MUST_EXTENDS_EXCEPTION);
      }
    }
  }
  
  @Check(CheckType.EXPENSIVE)
  public void checkDomainCrossReference(final DomainDeclaration domain) {
    boolean _hasCycle = this.domainManager.getDomain(this._iQualifiedNameProvider.getFullyQualifiedName(domain).toString()).hasCycle();
    if (_hasCycle) {
      this.error((("control \'" + this.domainManager.getDomain(this._iQualifiedNameProvider.getFullyQualifiedName(domain).toString()).domain) + "\' has cycle in dependency of domain."), domain, 
        DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME, IssueCodes.DOMAIN_DEPENDENCY_CYCLE);
    }
  }
}
