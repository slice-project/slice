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
package org.etri.slice.devices.carseat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;


public class SeatPostureSensorGUI {
	
	private SeatControl m_controller;
	public JFrame m_frame;	
	private JTextField m_height;
	private JTextField m_position;
	private JTextField m_tilt;
	private JLabel m_currentHeight;
	private JLabel m_currentPosition;
	private JLabel m_currentTilt;
	
	public SeatPostureSensorGUI(SeatControl control) {
		m_controller = control;
		initialize();
	}
	
	public void setCurrentSeatPosture(SeatPosture posture) {
		m_currentHeight.setText(Double.toString(m_controller.getPosture().getHeight()));
		m_currentPosition.setText(Double.toString(m_controller.getPosture().getPosition()));
		m_currentTilt.setText(Double.toString(m_controller.getPosture().getTilt()));
		m_frame.invalidate();
	}
	
	private void initialize() {
		m_frame = new JFrame();
		m_frame.setTitle("SeatPostureController");
		m_frame.setBounds(100, 100, 260, 226);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.getContentPane().setLayout(null);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setBounds(57, 29, 44, 15);
		m_frame.getContentPane().add(lblHeight);
		
		m_height = new JTextField();
		m_height.setBounds(153, 26, 34, 21);
		m_frame.getContentPane().add(m_height);
		m_height.setColumns(10);
		
		JLabel lblPosition = new JLabel("position");
		lblPosition.setBounds(57, 65, 54, 15);
		m_frame.getContentPane().add(lblPosition);
		
		m_position = new JTextField();
		m_position.setBounds(153, 62, 34, 21);
		m_frame.getContentPane().add(m_position);
		m_position.setColumns(10);
		
		JLabel lblTilt = new JLabel("tilt");
		lblTilt.setBounds(57, 100, 19, 15);
		m_frame.getContentPane().add(lblTilt);
		
		m_tilt = new JTextField();
		m_tilt.setBounds(153, 94, 34, 21);
		m_frame.getContentPane().add(m_tilt);
		m_tilt.setColumns(10);		
		
		JButton btnDetect = new JButton("Apply");
		btnDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double height = Double.parseDouble((String)m_height.getText().trim());
				double position = Double.parseDouble((String)m_position.getText().trim());
				double tilt = Double.parseDouble((String)m_tilt.getText().trim());
				
				m_controller.setHeight(height);
				m_controller.setPosition(position);
				m_controller.setTilt(tilt);
			}
		});
		btnDetect.setBounds(73, 137, 114, 23);
		m_frame.getContentPane().add(btnDetect);
		
		m_currentHeight = new JLabel(Double.toString(m_controller.getPosture().getHeight()));
		m_currentHeight.setForeground(Color.RED);
		m_currentHeight.setBounds(113, 29, 30, 15);
		m_frame.getContentPane().add(m_currentHeight);
		
		m_currentPosition = new JLabel(Double.toString(m_controller.getPosture().getPosition()));
		m_currentPosition.setForeground(Color.RED);
		m_currentPosition.setBounds(113, 65, 31, 15);
		m_frame.getContentPane().add(m_currentPosition);
		
		m_currentTilt = new JLabel(Double.toString(m_controller.getPosture().getTilt()));
		m_currentTilt.setForeground(Color.RED);
		m_currentTilt.setBounds(113, 100, 30, 15);
		m_frame.getContentPane().add(m_currentTilt);
	}
}
