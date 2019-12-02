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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;

	public PreferencesDialog() {
		
		buildUI();
	}
	
	private void buildUI() {

		this.setSize(300, 300);
		this.getContentPane().setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		CopyPatternEditorPanel patternEditorPanel = new CopyPatternEditorPanel();
		tabbedPane.add(patternEditorPanel.getTitle(), patternEditorPanel);
		
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
	
	@Override
	public void setVisible(boolean b) {
		
		if (b) {
			reloadAll();
		}
		super.setVisible(b);
	}
	
	public void reloadAll() {
		
		for (Component c: this.tabbedPane.getComponents()) {
			if (c instanceof PreferencesEditor) {
				((PreferencesEditor)c).reload();
			}
		}
	}
	
	private void doOK() {
		boolean hasError = false;
		for (Component c: this.tabbedPane.getComponents()) {
			if (c instanceof PreferencesEditor) {
				if (!((PreferencesEditor)c).apply()) {
					hasError = true;
					break;
				}
			}
		}
		if (!hasError) {
			this.setVisible(false);
		}
	}

	private void doCancel() {
		this.setVisible(false);
	}
}
