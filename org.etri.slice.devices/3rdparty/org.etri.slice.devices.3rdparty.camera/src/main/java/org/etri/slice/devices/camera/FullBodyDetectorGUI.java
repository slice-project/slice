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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.car.BodyPartLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullBodyDetectorGUI extends JDialog {
	private static final long serialVersionUID = -6851989135154088890L;
	private static Logger s_logger = LoggerFactory.getLogger(FullBodyDetectorGUI.class);

	private final JPanel contentPanel = new JPanel();
	private JTextField m_head;
	private JTextField m_torso;
	private JTextField m_arms;
	private JTextField m_legs;
	private JTextField m_height;

	private Publisher m_publisher;

	/**
	 * Create the dialog.
	 */
	public FullBodyDetectorGUI(Publisher publisher) {

		m_publisher = publisher;

		setTitle("Full-Body Detector");
		setBounds(100, 100, 196, 280);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblHead = new JLabel("head");
		lblHead.setBounds(44, 29, 57, 15);
		contentPanel.add(lblHead);

		JLabel lbltorso = new JLabel("torso");
		lbltorso.setBounds(44, 64, 57, 15);
		contentPanel.add(lbltorso);

		JLabel lblarm = new JLabel("arm");
		lblarm.setBounds(44, 95, 36, 15);
		contentPanel.add(lblarm);

		JLabel lblleg = new JLabel("leg");
		lblleg.setBounds(44, 128, 57, 15);
		contentPanel.add(lblleg);

		JLabel lblHeight = new JLabel("height");
		lblHeight.setBounds(44, 157, 57, 15);
		contentPanel.add(lblHeight);

		m_head = new JTextField();
		m_head.setBounds(98, 26, 44, 21);
		contentPanel.add(m_head);
		m_head.setColumns(10);

		m_torso = new JTextField();
		m_torso.setBounds(98, 61, 44, 21);
		contentPanel.add(m_torso);
		m_torso.setColumns(10);

		m_arms = new JTextField();
		m_arms.setBounds(98, 92, 44, 21);
		contentPanel.add(m_arms);
		m_arms.setColumns(10);

		m_legs = new JTextField();
		m_legs.setBounds(98, 125, 44, 21);
		contentPanel.add(m_legs);
		m_legs.setColumns(10);

		m_height = new JTextField();
		m_height.setColumns(10);
		m_height.setBounds(98, 156, 44, 21);
		contentPanel.add(m_height);

		JButton okButton = new JButton("OK");
		okButton.setBounds(25, 199, 57, 23);
		contentPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double head = Double.parseDouble((String) m_head.getText().trim());
				double torso = Double.parseDouble((String) m_torso.getText().trim());
				double arms = Double.parseDouble((String) m_arms.getText().trim());
				double legs = Double.parseDouble((String) m_legs.getText().trim());
				double height = Double.parseDouble((String) m_height.getText().trim());

				BodyPartLength bodyLength = new BodyPartLength(head, torso, arms, legs, height);
				m_publisher.sendData(bodyLength);
				s_logger.info("PUB: " + bodyLength);
				FullBodyDetectorGUI.this.dispose();
			}
		});
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(87, 199, 73, 23);
		contentPanel.add(cancelButton);
		cancelButton.setActionCommand("Cancel");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}
}
