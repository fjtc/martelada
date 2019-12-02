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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class CopyPatternEditorPanel extends JPanel implements PreferencesEditor {

	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(CopyPatternEditorPanel.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private JTextField patternTextField;
	
	private JTextField previewTextField;
	
	public CopyPatternEditorPanel() {
		buildUI();
	}
	
	private void buildUI() {

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		JLabel patternEditLabel = new JLabel(RESOURCES.getString("pattern.label"));
		this.add(patternEditLabel);
		
		patternTextField = new JTextField();
		this.add(patternTextField);
		
		JButton updateButton = new JButton("\u02714");
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doUpdatePreview();
			}
		});
		this.add(updateButton);
		 
		JTextArea descriptionTextArea = new JTextArea(
				String.format(RESOURCES.getString("pattern.description"), 
						CopyPattern.KEY_STRING));
		descriptionTextArea.setEditable(false);
		descriptionTextArea.setBackground(this.getBackground());
		descriptionTextArea.setLineWrap(true);
		this.add(descriptionTextArea);
		
		JLabel patternPreviewLabel = new JLabel(RESOURCES.getString("pattern.preview.label"));
		this.add(patternPreviewLabel);
		
		previewTextField = new JTextField();
		previewTextField.setEditable(false);
		this.add(previewTextField);
		
		layout.putConstraint(SpringLayout.NORTH, patternEditLabel, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, patternEditLabel, 5, SpringLayout.WEST, this);
	
		layout.putConstraint(SpringLayout.NORTH, patternTextField, 5, SpringLayout.SOUTH, patternEditLabel);
		layout.putConstraint(SpringLayout.WEST, patternTextField, 0, SpringLayout.WEST, patternEditLabel);
		layout.putConstraint(SpringLayout.EAST, patternTextField, 5, SpringLayout.WEST, updateButton);

		layout.putConstraint(SpringLayout.NORTH, updateButton, 0, SpringLayout.NORTH, patternTextField);
		layout.putConstraint(SpringLayout.SOUTH, updateButton, 0, SpringLayout.SOUTH, patternTextField);
		layout.putConstraint(SpringLayout.WEST, updateButton, -40, SpringLayout.EAST, this); 
		layout.putConstraint(SpringLayout.EAST, updateButton, -5, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.NORTH, descriptionTextArea, 10, SpringLayout.SOUTH, patternTextField);
		layout.putConstraint(SpringLayout.WEST, descriptionTextArea, 0, SpringLayout.WEST, patternTextField);
		layout.putConstraint(SpringLayout.EAST, descriptionTextArea, -5, SpringLayout.EAST, this);
		

		layout.putConstraint(SpringLayout.NORTH, patternPreviewLabel, 10, SpringLayout.SOUTH, descriptionTextArea);
		layout.putConstraint(SpringLayout.WEST, patternPreviewLabel, 0, SpringLayout.WEST, descriptionTextArea);
		layout.putConstraint(SpringLayout.EAST, patternPreviewLabel, 0, SpringLayout.EAST, descriptionTextArea);
		
		layout.putConstraint(SpringLayout.NORTH, previewTextField, 5, SpringLayout.SOUTH, patternPreviewLabel);
		layout.putConstraint(SpringLayout.WEST, previewTextField, 0, SpringLayout.WEST, patternPreviewLabel);
		layout.putConstraint(SpringLayout.EAST, previewTextField, 0, SpringLayout.EAST, patternPreviewLabel);
	}

	@Override
	public String getTitle() {
		return RESOURCES.getString("panel.title");
	}
	
	@Override
	public boolean apply() {
		
		CopyPattern pattern = this.createPattern();
		if (pattern != null) {
			 AppPreferences.getPreferences().setCopyPattern(pattern);
		}
		return pattern != null;
	}
	
	private CopyPattern createPattern() {

		if (this.patternTextField.getText().isEmpty()) {
			return null;
		} else {
			try {
				return new CopyPattern(this.patternTextField.getText());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, RESOURCES.getString("message.invalidPattern"),
						this.getTitle(), JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
	}

	@Override
	public void reload() {

		CopyPattern pattern = AppPreferences.getPreferences().getCopyPattern();
		if (pattern != null) {
			this.patternTextField.setText(pattern.getPattern());
		} else {
			this.patternTextField.setText(CopyPattern.KEY_STRING);
		}
		this.doUpdatePreview();
	}
	
	protected void doUpdatePreview() {
		CopyPattern pattern = this.createPattern();
		if (pattern != null) {
			this.previewTextField.setText(pattern.format("keyValue"));
		} else {
			this.previewTextField.setText("");
		}			
		this.revalidate();
	}
}
