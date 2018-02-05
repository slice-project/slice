package org.etri.slice.devices.camera;

import javax.swing.JDialog;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.service.FullBodyDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
//@Instantiate
public class FullBodyDetectorEmulator implements FullBodyDetector {
	
	private static Logger s_logger = LoggerFactory.getLogger(CVFullBodyDetector.class);	
	
	@Publishes(name="CVFullBodyDetector", topics=BodyPartLength.topic, dataKey=BodyPartLength.dataKey)
	private Publisher m_publisher;	

	@Override
	public void start() throws SliceException {
		s_logger.info("STARTED : " + this.getClass().getSimpleName());		
	}

	@Override
	public void stop() {
		s_logger.info("STOPPED : " + this.getClass().getSimpleName());		
	}

	@Override
	public void detect(double distance) throws SliceException {
	
		try {
			FullBodyDetectorGUI dialog = new FullBodyDetectorGUI(m_publisher);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
