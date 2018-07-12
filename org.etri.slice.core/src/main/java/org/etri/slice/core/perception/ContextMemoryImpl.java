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
package org.etri.slice.core.perception;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.ContextMemory;
import org.etri.slice.api.perception.ContextNotFoundException;
import org.etri.slice.commons.SliceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ContextMemoryImpl implements ContextMemory {
	private static Logger s_logger = LoggerFactory.getLogger(ContextMemoryImpl.class);	
	
	private ConcurrentMap<String,Object> m_contexts = new ConcurrentHashMap<String,Object>(); 
	
	@Override
	public void addContext(Object context) {
		try {
			insertContext(context);
		} 
		catch ( Exception e ) {
			s_logger.error("ERR: " + e.getMessage());
		}
	}

	@Override
	public Object getContext(String contextId) throws ContextNotFoundException {
		if ( !m_contexts.containsKey(contextId) ) {
			throw new ContextNotFoundException("context[id=" + contextId + "]");
		}
		
		return m_contexts.get(contextId);
	}

	@Override
	public void removeContext(Object context) {
		try {
			deleteContext(context);
		} 
		catch ( Exception e ) {
			s_logger.error("ERR: " + e.getMessage());
		}
	}
	
	private void insertContext(Object context) throws Exception {
		Class<?> cls = context.getClass();		
		Field fieldlist[] = context.getClass().getDeclaredFields();
		for ( Field field : fieldlist ) {
			if ( Modifier.isStatic(field.getModifiers()) ) {
				continue;
			}
			StringBuffer sbuff = new StringBuffer();
			sbuff.append(cls.getSimpleName());
			sbuff.append(".");
			sbuff.append(field.getName());
			field.setAccessible(true);
			Object value = field.get(context);
			String key = sbuff.toString();
			m_contexts.put(key, value);
			s_logger.info("CONTEXT INSERTED : [" + key + "," + value +"]");
		}		
	}
	
	private void deleteContext(Object context) throws Exception {
		Class<?> cls = context.getClass();		
		Field fieldlist[] = context.getClass().getDeclaredFields();
		for ( Field field : fieldlist ) {
			if ( Modifier.isStatic(field.getModifiers()) ) {
				continue;
			}
			StringBuffer sbuff = new StringBuffer();
			sbuff.append(cls.getSimpleName());
			sbuff.append(".");
			sbuff.append(field.getName());
			field.setAccessible(true);
			Object value = field.get(context);
			String key = sbuff.toString();
			m_contexts.remove(key, value);
			s_logger.info("CONTEXT REMOVED : [" + key + "," + value +"]");

			if ( field.getType().isAnnotationPresent(SliceContext.class) ) {
				deleteContext(value);
			}			
		}		
	}
}
