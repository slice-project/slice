package org.etri.slice.tools.adl.validation;

public interface IssueCodes {

	public static final String PREFIX = "org.etri.slice.tools.adl.domainmodel.";
	
	public static final String INVALID_TYPE_NAME = PREFIX + "InvalidTypeName";
	
	public static final String INVALID_FEATURE_NAME = PREFIX + "InvalidFeatureName";

	public static final String MISSING_TYPE = PREFIX + "MissingType";

	public static final String DUPLICATE_ELEMENT = PREFIX + "DuplicateElement";

	public static final String DUPLICATE_EXTERNAL_ELEMENT = PREFIX + "DuplicateExternalElement";
	
	public static final String DUPLICATE_FEATURE = PREFIX + "DuplicateFeature";
	
	public static final String DUPLICATE_PROPERTY = PREFIX + "DuplicateProperty";

	public static final String CONTEXT_HIERARCHY_CYCLE = PREFIX + "ContextHierarchyCycle";
	
	public static final String CONTROL_HIERARCHY_CYCLE = PREFIX + "ControlHierarchyCycle";
	
	public static final String EVENT_HIERARCHY_CYCLE = PREFIX + "EventHierarchyCycle";
	
	public static final String EXCEPTION_HIERARCHY_CYCLE = PREFIX + "ExceptionHierarchyCycle";
	
	public static final String CONTEXT_MUST_EXTENDS_CONTEXT = PREFIX + "ContextMustExtendsContext";
	
	public static final String EVENT_MUST_EXTENDS_EVENT = PREFIX + "EventMustExtendsEvent";

	public static final String EXCEPTION_MUST_EXTENDS_EXCEPTION = PREFIX + "ExceptionMustExtendsException";
	
	public static final String DOMAIN_DEPENDENCY_CYCLE = PREFIX + "DomainDependencyCycle";
	
	public static final String INVALID_COMMAND_CONTEXT_PROPERTY = PREFIX + "InvalidCommandContextProperty";
	
	public static final String INVALID_COMMAND_METHOD = PREFIX + "InvalidCommandMethod";
	
	public static final String INVALID_CALL_METHOD = PREFIX + "InvalidCallMethod";
	
}
