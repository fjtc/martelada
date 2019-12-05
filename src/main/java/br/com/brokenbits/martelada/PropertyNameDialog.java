/**
 * martelada - a very simple Java resource file editor 
 * Copyright (C) 2019 Fabio Jun Takada Chino
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package br.com.brokenbits.martelada;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class PropertyNameDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private Set<String> keys;
	
	private JTextField keyTextField;
	
	private String value;
	
	public PropertyNameDialog() {
		this.buildUI();
	}
	
	private void buildUI() {

		this.setSize(300, 150);
		this.getContentPane().setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel();		
		SpringLayout springLayout = new SpringLayout();
		mainPanel.setLayout(springLayout);
		
		JLabel keyLabel = new JLabel("New property name");
		mainPanel.add(keyLabel);
		
		keyTextField = new JTextField();
		mainPanel.add(keyTextField);
		keyTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doOK();
			}
		});
	
		
		springLayout.putConstraint(SpringLayout.NORTH, keyLabel, 5, SpringLayout.NORTH, mainPanel);
		springLayout.putConstraint(SpringLayout.WEST, keyLabel, 5, SpringLayout.WEST, mainPanel);
		
		springLayout.putConstraint(SpringLayout.NORTH, keyTextField, 5, SpringLayout.SOUTH, keyLabel);
		springLayout.putConstraint(SpringLayout.WEST, keyTextField, 5, SpringLayout.WEST, keyLabel);
		springLayout.putConstraint(SpringLayout.EAST, keyTextField, -5, SpringLayout.EAST, mainPanel);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOK();
			}
		});
		buttonPanel.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		buttonPanel.add(cancelButton);
		
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);		
	}
	
	public String showDialog(String title, String currentValue, Set<String> keys) {
		
		this.setTitle(title);
		this.setLocationRelativeTo(this.getParent());
		this.keys = keys;
		value = null;
		if (currentValue != null) {
			this.keyTextField.setText(currentValue);
		} else {
			this.keyTextField.setText("");
		}
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		return value;
	}
	
	private boolean checkValues() {

		String key = this.keyTextField.getText();
		if (key.isEmpty()) {
			JOptionPane.showMessageDialog(this, "The property name cannot be empty.", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (keys.contains(key)) {
			JOptionPane.showMessageDialog(this, "The property already exists.", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void doOK() {
		if (checkValues()) {
			this.value = this.keyTextField.getText();
			this.setVisible(false);
		}		
	}
	
	private void doCancel() {
		this.setVisible(false);
	}
}
