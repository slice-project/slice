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
package org.etri.slice.commons.sensor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.annotation.GuardedBy;
import org.etri.slice.commons.ContextListener;
import org.etri.slice.commons.Sensor;
import org.etri.slice.commons.SliceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPulledSensor<E> implements Sensor, Runnable, ContextListener<E> {
	
	private static Logger s_logger = LoggerFactory.getLogger(AbstractPulledSensor.class);	

	private Lock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private Condition m_stopCondition = m_lock.newCondition();
	@GuardedBy("m_lock") private volatile boolean m_stopRequested = false;	
	private final ExecutorService m_executor = Executors.newSingleThreadExecutor();		
	private final BlockingQueue<E> m_queue = new LinkedBlockingQueue<E>();
	
	protected abstract void publish(E e);
	
	@Override
	public void contextReceived(E context) {
		try {
			m_queue.put(context);
		} 
		catch ( InterruptedException e ) {	
			s_logger.debug("Interrupted: PUT - " + context);
		}
	}
	
	@Override
	public void start() throws SliceException {
		m_executor.execute(this);
	}

	@Override
	public void stop() throws SliceException {
		m_lock.lock();
		m_stopRequested = true;
		m_lock.unlock();
	}

	@Override
	public void run() {
		m_lock.lock();
		while ( !m_stopRequested ) {
			E element = null;
			try {
				element = m_queue.poll(100, TimeUnit.MILLISECONDS);
			} 
			catch (InterruptedException e) {
				s_logger.debug("Interrupted: POLLING");
			}
			if ( element != null ) {
				publish(element);
			}
		}
		m_lock.unlock();
	}

}
