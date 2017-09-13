package org.etri.slice.core.inference;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleRuntimeEventListenerImpl implements RuleRuntimeEventListener {

	private static Logger logger = LoggerFactory.getLogger(RuleRuntimeEventListenerImpl.class);
	
    private int numberOfFacts;
    private int numberOfModifiedFacts;
    
    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        numberOfFacts++;
        logger.info("object inserted: " + event.getObject());
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        numberOfModifiedFacts++;
        logger.info("object updated: " + event.getObject());
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        numberOfFacts--;
        logger.info("object inserted: " + event.getOldObject());
    }

    public int getNumberOfFacts() {
        return numberOfFacts;
    }

    public int getNumberOfModifiedFacts() {
        return numberOfModifiedFacts;
    }
    
}
