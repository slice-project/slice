package org.etri.slice.api.inference;

import java.util.Collection;

import org.kie.api.runtime.ObjectFilter;

public interface WorkingMemory {
	
	void insert(Object fact);
	void delete(Object fact);
	void update(Object before, Object after);
	
	long getFactCount();
	Collection<? extends Object> getObjects();
	Collection<? extends Object> getObjects(ObjectFilter filter);
}
