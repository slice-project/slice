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

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class OpenCVDetect {

	private static OpenCVDetect s_instance;
	private Net m_net;
	private VideoCapture m_camera;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private OpenCVDetect() {
		String proto = "MobileNetSSD_deploy.prototxt";
		String model = "MobileNetSSD_deploy.caffemodel";
		m_net = Dnn.readNetFromCaffe(proto, model);
	}

	public static OpenCVDetect getInstance() {
		if (null == s_instance)
			s_instance = new OpenCVDetect();
		return s_instance;
	}

	public boolean openCamera(int width, int height) {
		m_camera = new VideoCapture(0);
		m_camera.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, width);
		m_camera.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, height);

		try {
			Thread.sleep(1000);
		} 
		catch ( Exception ignored ) { }
		
		if ( !m_camera.isOpened() ) {
			return false;
		} 
		return true;
	}
	
	public void closeCamera() {
		m_camera.release();
	}

	public double analysisImage(double distance) {
		final int IN_WIDTH = 300;
		final int IN_HEIGHT = 300;
		final double IN_SCALE_FACTOR = 0.007843;
		final double MEAN_VAL = 127.5;
		final double THRESHOLD = 0.2;
		final int PERSON_CLASSID = 15;

		Mat image = new Mat();
		m_camera.read(image);

		int cols = image.cols();
		int rows = image.rows();

		Mat blob = Dnn.blobFromImage(image, IN_SCALE_FACTOR, new Size(IN_WIDTH, IN_HEIGHT),
				new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false, false);

		m_net.setInput(blob);

		Mat detections = m_net.forward();

		detections = detections.reshape(1, (int) detections.total() / 7);

		double maxHeightPixel = 0.0;

		for (int i = 0; i < detections.rows(); ++i) {
			double confidence = detections.get(i, 2)[0];
			if (confidence <= THRESHOLD)
				continue;
			int classId = (int) detections.get(i, 1)[0];
			if (classId != PERSON_CLASSID)
				continue;

			double xLeftBottom = (double) (detections.get(i, 3)[0] * cols);
			double yLeftBottom = (double) (detections.get(i, 4)[0] * rows);
			double xRightTop = (double) (detections.get(i, 5)[0] * cols);
			double yRightTop = (double) (detections.get(i, 6)[0] * rows);

			double heightPixel = yRightTop - yLeftBottom;
			if (heightPixel > maxHeightPixel)
				maxHeightPixel = heightPixel;
		}
		if (maxHeightPixel <= 0.0)
			return 0.0;

		double result = 0.0000404 * (distance * 100) * maxHeightPixel;
		result = (result + (distance * 100) * 0.0078) * 0.826;
		result = result - 14.0;

		return result;
	}
}
