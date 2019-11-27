package marteladabr.com.brokenbits.martelada;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import marteladabr.com.brokenbits.martelada.engine.PropertiesFileEngine;

public class PropertyListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final PropertiesFileEngine engine;
	
	private JList<String> propertyList;
	
	public PropertyListPanel(PropertiesFileEngine engine) {
		this.engine = engine;
		this.buildUI();
	}
	
	private void buildUI() {

		this.setLayout(new BorderLayout());
		
		JPanel commandPanel = new JPanel();
		this.add(commandPanel, BorderLayout.NORTH);
		
		JButton addButton = new JButton("+");
		commandPanel.add(addButton);
		
		propertyList = new JList<String>(new PropertiesFileListModel(this.engine));
		JScrollPane scrollPane = new JScrollPane(propertyList); 
		this.add(scrollPane);
		propertyList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				engine.setSelected(event.getFirstIndex());
			}
		});
	}
	
	protected void doAdd() {
		
	}
}
