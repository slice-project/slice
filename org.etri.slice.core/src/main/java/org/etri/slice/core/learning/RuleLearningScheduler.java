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
package org.etri.slice.core.learning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.learning.ActionLogger;
import org.etri.slice.api.learning.ActionRuleLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Instantiate
public class RuleLearningScheduler implements Runnable {

	private static Logger s_logger = LoggerFactory.getLogger(RuleLearningScheduler.class);	
	
	@Property(name="loggig.scan.interval", value="10")
	public long m_interval;
	
	@Property(name="minimum.logging.count", value="20")
	public long m_minimumLogCount;
	
	@Requires
	private ActionLogger m_logger;
	@Requires 
	private ActionRuleLearner m_learner;
	private int m_loggingCount;
	private Lock m_lock = new ReentrantLock();
	private Condition m_stopCondition = m_lock.newCondition();
	private volatile boolean m_stopRequested = false;
	
	@Validate
	public void init() throws Exception {
		m_loggingCount = scanLoggingCount();
		new Thread(this).start();
	}
	
	@Invalidate
	public void fini() {
		m_lock.lock();
		m_stopRequested = true;
		m_stopCondition.signal();
		m_lock.unlock();
	}
	
	@Override
	public void run() {
		s_logger.info("STARTED: RuleLearningScheduler.run()");		
		m_lock.lock();
		try {
			while ( !m_stopRequested ) {
				try {
					int loggingCount = scanLoggingCount();
					if ( (loggingCount - m_loggingCount) >= m_minimumLogCount ) {
						if ( m_learner.learnActionRules() ) {
							m_loggingCount = loggingCount;
						}
					}
					
					m_stopCondition.await(m_interval, TimeUnit.SECONDS);
				} 
				catch (Exception e) {
					s_logger.error("ERR: " + e.getMessage());
				}
			}
		}
		finally {
			m_lock.unlock();
		}
		s_logger.info("STOPPED: RuleLearningScheduler.run()");		
	}
	
	private int scanLoggingCount() throws Exception {
		Collection<String> logIds = m_logger.getActionLogIdsAll();
		int loggingCount = 0;
		for ( String logId : logIds ) {
			FileReader reader = new FileReader(m_logger.getActionLogFile(logId));
			BufferedReader buffered = new BufferedReader(reader);
			loggingCount += buffered.lines().count();
			buffered.close();
		}
		
		return loggingCount;
	}
}
