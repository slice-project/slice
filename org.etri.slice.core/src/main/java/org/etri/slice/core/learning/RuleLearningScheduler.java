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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.GuardedBy;

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

@Component(publicFactory=false, immediate=true, managedservice="org.etri.slice.core")
@Instantiate
public class RuleLearningScheduler implements Runnable {

	private static Logger s_logger = LoggerFactory.getLogger(RuleLearningScheduler.class);
	private static final String LogInterval = "core.scanning.logs.interval";
	private static final String LogCount = "core.minimum.logs.count";
	
	
	@Property(name=LogInterval, value="3")
	public long m_interval;
	
	@Property(name=LogCount, value="20")
	public long m_minimumLogCount;
	
	@Requires
	private ActionLogger m_logger;
	@Requires 
	private ActionRuleLearner m_learner;
	private int m_loggingCount;
	private Lock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private Condition m_stopCondition = m_lock.newCondition();
	@GuardedBy("m_lock") private volatile boolean m_stopRequested = false;
	
	@Property(name=LogInterval)
	public void setLogScanInterval(long interval) {
		m_lock.lock();
		m_interval = interval;
		s_logger.info("SET: Property[core.scanning.logs.interval=" + interval + "]" );
		m_lock.unlock();
	}
	
	@Property(name=LogCount)
	public void setMinimunLoggingCount(long count) {
		m_lock.lock();
		m_minimumLogCount = count;
		s_logger.info("SET: Property[core.minimum.logs.count=" + count + "]" );
		m_lock.unlock();
	}	
	
	@Validate
	public void init() throws Exception {
		m_loggingCount = m_logger.getLogCount();
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
					int loggingCount = m_logger.getLogCount();
					int newLoggingCount = loggingCount - m_loggingCount;
					s_logger.info("SCANNED: new logging count = " + newLoggingCount);	
					
					if ( newLoggingCount >= m_minimumLogCount ) {
						if ( m_learner.learnActionRules() ) {
							m_loggingCount = loggingCount;
						}
					}
					m_stopCondition.await(m_interval, TimeUnit.MINUTES);
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
}
