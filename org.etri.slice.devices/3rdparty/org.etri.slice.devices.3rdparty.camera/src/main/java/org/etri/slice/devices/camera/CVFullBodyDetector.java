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
package org.etri.slice.devices.camera;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.service.FullBodyDetector;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory = false, immediate = true)
@Provides
@Instantiate
public class CVFullBodyDetector implements FullBodyDetector {

	private static Logger s_logger = LoggerFactory.getLogger(CVFullBodyDetector.class);
	private OpenCVDetect m_detector = OpenCVDetect.getInstance();
	private final ExecutorService m_executor = Executors.newSingleThreadExecutor();

	@Publishes(name = "CVFullBodyDetector", topics = BodyPartLength.topic, dataKey = BodyPartLength.dataKey)
	private Publisher m_publisher;

	@Validate
	@Override
	public void start() throws SliceException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if (!m_detector.openCamera(3280, 2464)) {
			throw new SliceException("failed to open a camera");
		}
		s_logger.info("STARTED: " + this.getClass().getSimpleName());
	}

	@Invalidate
	@Override
	public void stop() {
		m_detector.closeCamera();
		s_logger.info("STOPPED: " + this.getClass().getSimpleName());
	}

	@Override
	public void detect(double distance) throws SliceException {

		Runnable aRunnable = new Runnable() {
			public void run() {
				long before = System.currentTimeMillis();
				double height = m_detector.analysisImage(distance);
				long after = System.currentTimeMillis();
				s_logger.info("Elapsed time: " + (float) (after - before) / 1000f + "secs.");

				BodyPartLength bodyLength = BodyPartLength.builder().height(height).build();
				m_publisher.sendData(bodyLength);

				s_logger.info("PUB: " + bodyLength);

			}
		};

		m_executor.execute(aRunnable);
	}
}
