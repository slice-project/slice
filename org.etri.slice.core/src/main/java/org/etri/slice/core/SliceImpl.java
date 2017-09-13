package org.etri.slice.core;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.Slice;
import org.etri.slice.api.inference.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class SliceImpl implements Slice {

	 private static Logger logger = LoggerFactory.getLogger("org.etri.slice");
	
	@Override
	public RuleEngine getRuleEngine() {
		return null;
	}

	@Validate
	public void start() {
		logger.debug("started.");
	}
	
	@Invalidate
	public void stop() {
		
	}		
}
