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
package org.etri.slice.devices.objectdetector.stream;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.TWindow;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;
import org.etri.slice.commons.car.ObjectInfo;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=ObjectInfoStream.SERVICE_NAME)
public class ObjectInfoStream implements EventStream<ObjectInfo> {	
	public static final String SERVICE_NAME = "ObjectInfoStream";
	
	@Override
	public TStream<ObjectInfo> process(TStream<ObjectInfo> stream) {
		TWindow<ObjectInfo,Integer> window = stream.last(10, TimeUnit.SECONDS, tuple -> 0);
		TStream<ObjectInfo> batched = window.batch((tuples, key) -> {
			Iterator<ObjectInfo> iter = tuples.iterator();
			double sum = 0;
			int count = 0;
			while ( iter.hasNext() ) {
				sum += iter.next().getDistance();
				count++;
			}
			double average = sum / count;	
			return ObjectInfo.builder().objectId("obj").distance(average).build();
		});
		
		return batched;
	}
}
