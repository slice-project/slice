package org.etri.slice.tools.adl.validation;

public interface IssueCodes {

	public static final String PREFIX = "org.etri.slice.tools.adl.domainmodel.";
	
	public static final String INVALID_TYPE_NAME = PREFIX + "InvalidTypeName";
	
	public static final String INVALID_FEATURE_NAME = PREFIX + "InvalidFeatureName";

	public static final String MISSING_TYPE = PREFIX + "MissingType";

	public static final String DUPLICATE_ELEMENT = PREFIX + "DuplicateElement";
	
	public static final String DUPLICATE_FEATURE = PREFIX + "DuplicateFeature";
	
	public static final String DUPLICATE_PROPERTY = PREFIX + "DuplicateProperty";

	public static final String CONTEXT_HIERARCHY_CYCLE = PREFIX + "ContextHierarchyCycle";
	
	public static final String CONTROL_HIERARCHY_CYCLE = PREFIX + "ControlHierarchyCycle";
	
	public static final String EVENT_HIERARCHY_CYCLE = PREFIX + "EventHierarchyCycle";
	
	public static final String EXCEPTION_HIERARCHY_CYCLE = PREFIX + "ExceptionHierarchyCycle";
}
