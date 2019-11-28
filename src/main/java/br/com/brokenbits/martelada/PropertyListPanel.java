package br.com.brokenbits.martelada;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.brokenbits.martelada.engine.PropertiesEditor;

public class PropertyListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final PropertiesEditor engine;
	
	private JList<String> propertyList;
	
	public PropertyListPanel(PropertiesEditor engine) {
		this.engine = engine;
		this.buildUI();
	}
	
	private void buildUI() {

		this.setLayout(new BorderLayout());
		
		JPanel commandPanel = new JPanel();
		this.add(commandPanel, BorderLayout.NORTH);
		
		JButton addButton = new JButton("+");
		commandPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAdd();
			}
		});
		
		JButton removeButton = new JButton("-");
		commandPanel.add(removeButton);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRemove();
			}
		});
		
		propertyList = new JList<String>(new PropertiesFileListModel(this.engine));
		JScrollPane scrollPane = new JScrollPane(propertyList); 
		this.add(scrollPane);
		propertyList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				engine.setSelected(propertyList.getSelectedIndex());
			}
		});
	}
	
	protected void doAdd() {
		
		String value = JOptionPane.showInputDialog(this, 
				"Name of the new property:", "dasd", 
				JOptionPane.INFORMATION_MESSAGE);
		if (value != null) {
			if (!this.engine.addProperty(value)) {
				JOptionPane.showMessageDialog(this, "", "", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected void doRemove() {

		this.engine.removeSelectedProperty();
	}
}
