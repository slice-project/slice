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
package org.etri.slice.devices.pressuresensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.car.event.Pressure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PressureSensorGUI {
	
	private static Logger s_logger = LoggerFactory.getLogger(PressureSensorGUI.class);	

	private Publisher m_publisher;
	public JFrame m_frame;	
	private JTextField m_pressure;
	
	public PressureSensorGUI(Publisher publisher) {
		m_publisher = publisher;
		initialize();
	}
	
	private void initialize() {
		m_frame = new JFrame();
		m_frame.setTitle("PressureSensor");
		m_frame.setBounds(100, 100, 225, 148);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.getContentPane().setLayout(null);
		
		JLabel lblDistance = new JLabel("pressure");
		lblDistance.setBounds(48, 23, 57, 15);
		m_frame.getContentPane().add(lblDistance);
		
		m_pressure = new JTextField();
		m_pressure.setBounds(112, 20, 44, 21);
		m_frame.getContentPane().add(m_pressure);
		m_pressure.setColumns(10);
		
		JButton btnDetect = new JButton("Detect");
		btnDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double pressure = Double.parseDouble((String)m_pressure.getText().trim());
				Pressure detected = Pressure.builder().value(pressure).build();
				m_publisher.sendData(detected);
				
				s_logger.info("PUB: " + detected);				
			}
		});
		btnDetect.setBounds(46, 65, 110, 23);
		m_frame.getContentPane().add(btnDetect);
	}
}
