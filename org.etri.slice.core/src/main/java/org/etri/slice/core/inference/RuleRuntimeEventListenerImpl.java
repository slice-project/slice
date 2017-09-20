/**
 *
 * Copyright (c) 2017-2017 SLICE project team (yhsuh@etri.re.kr)
 * http://slice.etri.re.kr
 *
 * This file is part of The SLICE components
 *
 * This Program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The SLICE components; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
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
