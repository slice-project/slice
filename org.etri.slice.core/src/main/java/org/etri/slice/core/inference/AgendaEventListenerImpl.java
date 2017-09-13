
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AgendaEventListenerImpl implements AgendaEventListener{

	private static Logger logger = LoggerFactory.getLogger(AgendaEventListenerImpl.class);
	
	private int numberOfMatches;
    private int numberOfFiredMatches;
    
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
