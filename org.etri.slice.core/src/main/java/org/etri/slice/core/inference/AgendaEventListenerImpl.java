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

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AgendaEventListenerImpl implements AgendaEventListener{

	private static Logger logger = LoggerFactory.getLogger(AgendaEventListenerImpl.class);
	
	private final KieSession m_session;
	private int numberOfMatches;
    private int numberOfFiredMatches;
    
    public AgendaEventListenerImpl(KieSession session) {
    	m_session = session;
    }
    
    @Override
    public void matchCreated(MatchCreatedEvent event) {
        numberOfMatches++;
        logger.info("rule matched[count=" + numberOfMatches + "] => " + event.getMatch().getRule());
    }

    @Override
    public void matchCancelled(MatchCancelledEvent event) {
        numberOfMatches--;
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        numberOfFiredMatches++;
        logger.info("rule fired: " + event.getMatch().getRule());
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent event) {
    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public int getNumberOfFiredMatches() {
        return numberOfFiredMatches;
    }
    
}
