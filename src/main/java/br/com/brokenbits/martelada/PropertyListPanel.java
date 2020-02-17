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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import br.com.brokenbits.martelada.engine.PropertiesEditor;

public class PropertyListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final PropertiesEditor propertyEditor;
	
	private JList<String> propertyList;
	
	private List<PropertySelectionListener> propertySelectionListenerList = new ArrayList<PropertySelectionListener>();
	
	private PropertiesListModel propertiesListModel;
	
	private JTextField filterTextField;
	
	public PropertyListPanel(PropertiesEditor propertyEditor) {
		this.propertyEditor = propertyEditor;
		this.buildUI();
	}
	
	private void buildUI() {

		this.setLayout(new BorderLayout());
		
		this.add(buildCommandPanel(), BorderLayout.NORTH);
		
		propertiesListModel = new PropertiesListModel(this.propertyEditor);
		propertyList = new JList<String>(propertiesListModel);
		JScrollPane scrollPane = new JScrollPane(propertyList); 
		this.add(scrollPane);
		propertyList.addListSelectionListener(e -> doSelect());
	}

	private JPanel buildCommandPanel() {
		
		JPanel commandPanel = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		commandPanel.setLayout(springLayout);
		
		JButton addButton = new JButton("+");
		commandPanel.add(addButton);
		addButton.addActionListener(e -> doAdd());
		
		JButton removeButton = new JButton("-");
		commandPanel.add(removeButton);
		removeButton.addActionListener(e -> doRemove());
		
		JLabel filterLabel = new JLabel("Search");
		commandPanel.add(filterLabel);
		
		filterTextField = new JTextField();
		filterTextField.addActionListener(e -> doFilter());
		commandPanel.add(filterTextField);
		
		springLayout.putConstraint(SpringLayout.NORTH, addButton, 5, SpringLayout.NORTH, commandPanel);
		springLayout.putConstraint(SpringLayout.EAST, addButton, 5, SpringLayout.WEST, removeButton);

		springLayout.putConstraint(SpringLayout.NORTH, removeButton, 0, SpringLayout.NORTH, addButton);
		springLayout.putConstraint(SpringLayout.EAST, removeButton, -5, SpringLayout.EAST, commandPanel);

		springLayout.putConstraint(SpringLayout.NORTH, filterLabel, 5, SpringLayout.SOUTH, addButton);
		springLayout.putConstraint(SpringLayout.WEST, filterLabel, 5, SpringLayout.WEST, commandPanel);

		springLayout.putConstraint(SpringLayout.NORTH, filterTextField, 0, SpringLayout.NORTH, filterLabel);
		springLayout.putConstraint(SpringLayout.WEST, filterTextField, 5, SpringLayout.EAST, filterLabel);
		springLayout.putConstraint(SpringLayout.EAST, filterTextField, -5, SpringLayout.EAST, commandPanel);
		
		springLayout.putConstraint(SpringLayout.SOUTH, commandPanel, 5, SpringLayout.SOUTH, filterTextField);
	
		return commandPanel;
	}
	
	protected void doAdd() {
		
		PropertyNameDialog d = new PropertyNameDialog();
		String newKey = d.showDialog("Add new key", this.getSelected(), this.propertyEditor.getKeys());
		if (newKey != null) {
			if (!this.propertyEditor.addProperty(newKey)) {
				JOptionPane.showMessageDialog(this, 
						"Unable to add the new property.", 
						"Add property.", JOptionPane.ERROR_MESSAGE);
			} else {
				setSelected(newKey);
			}
		}
	}
	
	public void setSelected(String key) {
		this.propertyList.setSelectedValue(key, true);
	}
	
	private void doSelect() {
		String selected = getSelected();
		for (PropertySelectionListener l: this.propertySelectionListenerList) {
			l.propertySelected(this, selected);
		}
	}
	
	public String getSelected() {
		return this.propertyList.getSelectedValue();
	}
	
	protected void doRemove() {

		String key = this.propertyList.getSelectedValue();
		if (key != null) {
			this.propertyEditor.removeProperty(key);
		}
	}
	
	public void addPropertySelectionListener(PropertySelectionListener l) {
		this.propertySelectionListenerList.add(l);
	}
	
	public void removePropertySelectionListener(PropertySelectionListener l) {
		this.propertySelectionListenerList.remove(l);
	}
	
	private void doFilter() {
		if (this.filterTextField.getText().isBlank()) {
			this.propertiesListModel.setFilter(null);
		} else {
			this.propertiesListModel.setFilter(new SubstringPropertyListFilter(this.filterTextField.getText()));
		}
	}
}
