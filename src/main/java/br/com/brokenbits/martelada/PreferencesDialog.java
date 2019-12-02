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
