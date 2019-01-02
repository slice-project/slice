
package org.etri.slice.agents.room.fancontroller.wrapper;

import org.apache.edgent.execution.services.ControlService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.api.learning.Action;
import org.etri.slice.api.learning.Action.ActionBuilder;
import org.etri.slice.api.learning.ActionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;		

import org.etri.slice.commons.room.context.Temperature;
import org.etri.slice.commons.room.service.ElectronicFan;

@Component
@Instantiate
public class ElectronicFanCommander implements ElectronicFan {
	private static Logger s_logger = LoggerFactory.getLogger(ElectronicFanCommander.class);
	
	@Requires
	private ElectronicFan m_proxy;
	
	@Requires
	private ActionLogger m_logger;
	
	@Requires
	private Agent m_agent;
	
	public ElectronicFanCommander() {
		ControlService control = m_agent.getService(ControlService.class);
		control.registerControl(this.getClass().getSimpleName(), ElectronicFan.id, null, ElectronicFan.class, this);
	}
	
	@Override
	public int getLevel() {
		return m_proxy.getLevel();
	}
	
	@Override		        
	public void setLevel(int level) {
		m_proxy.setLevel(level);
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("fan_level");
		builder.addContext(Temperature.Field.value);
		builder.setAction(ElectronicFan.setLevel, level);
		logAction(builder.build());		
	}
	private void logAction(Action action) {
		try {
			m_logger.log(action);
		} 
		catch ( Exception e ) {
			s_logger.error("ERR : " + e.getMessage());
		}			
	}			
}
