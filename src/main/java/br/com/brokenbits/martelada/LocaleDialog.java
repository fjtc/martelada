package br.com.brokenbits.martelada;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import br.com.brokenbits.martelada.engine.ResourceFileUtils;

public class LocaleDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JValidatedTextField languageTextField;
	
	private JValidatedTextField countryTextField;
	
	private JValidatedTextField scriptTextField;
	
	private JValidatedTextField variantTextField;
	
	private Locale locale;
	
	public LocaleDialog() {
		buildUI();
	}

	private void buildUI() {
		
		this.setTitle("New locale");		
		this.setSize(350, 200);
		this.getContentPane().setLayout(new BorderLayout());		
		
		JPanel editPanel = new JPanel();
		this.add(editPanel, BorderLayout.CENTER);
		SpringLayout layout = new SpringLayout();
		editPanel.setLayout(layout);
		
		JLabel languageLabel = new JLabel("Language code");  
		editPanel.add(languageLabel);
		
		languageTextField = new JValidatedTextField(ResourceFileUtils.LANGUAGE_PATTERN, false);
		languageTextField.setColumns(3);
		languageTextField.setToolTipText("2 or 3 letter ISO 639 language code in lower case.");
		editPanel.add(languageTextField);

		JLabel countryLabel = new JLabel("Country code");  
		editPanel.add(countryLabel);

		countryTextField = new JValidatedTextField(ResourceFileUtils.COUNTRY_PATTERN, true);
		countryTextField.setToolTipText("Two-letter ISO 3166 (all upper case) or 3 digit UN M.49 country code.");
		countryTextField.setColumns(3);
		editPanel.add(countryTextField);

		JLabel scriptLabel = new JLabel("Script code");
		editPanel.add(scriptLabel);
		
		scriptTextField = new JValidatedTextField(ResourceFileUtils.SCRIPT_PATTERN, true);
		scriptTextField.setColumns(4);
		scriptTextField.setToolTipText("ISO 15924 4 letter script name. The first as character is upper case while the rest must be lower case.");
		editPanel.add(scriptTextField);

		JLabel variantLabel = new JLabel("variant");  
		editPanel.add(variantLabel);

		variantTextField = new JValidatedTextField(ResourceFileUtils.VARIANT_WITH_DASH_PATTERN, true);
		variantTextField.setToolTipText("ISO 15924 4 letter script name. The first as character is upper case while the rest must be lower case.");
		editPanel.add(variantTextField);
		
		// Row 1
		layout.putConstraint(SpringLayout.NORTH, languageLabel, 5, SpringLayout.NORTH, editPanel);
		layout.putConstraint(SpringLayout.WEST, languageLabel, 5, SpringLayout.WEST, editPanel);
		
		layout.putConstraint(SpringLayout.NORTH, languageTextField, 0, SpringLayout.NORTH, languageLabel);
		layout.putConstraint(SpringLayout.WEST, languageTextField, 5, SpringLayout.EAST, languageLabel);

		layout.putConstraint(SpringLayout.NORTH, countryLabel, 0, SpringLayout.NORTH, languageTextField);
		layout.putConstraint(SpringLayout.WEST, countryLabel, 15, SpringLayout.EAST, languageTextField);
		
		layout.putConstraint(SpringLayout.NORTH, countryTextField, 0, SpringLayout.NORTH, languageTextField);
		layout.putConstraint(SpringLayout.WEST, countryTextField, 5, SpringLayout.EAST, countryLabel);

		// Row 2
		layout.putConstraint(SpringLayout.NORTH, scriptLabel, 5, SpringLayout.SOUTH, languageLabel);
		layout.putConstraint(SpringLayout.WEST, scriptLabel, 0, SpringLayout.WEST, languageLabel);

		layout.putConstraint(SpringLayout.NORTH, scriptTextField, 0, SpringLayout.NORTH, scriptLabel);
		layout.putConstraint(SpringLayout.WEST, scriptTextField, 5, SpringLayout.EAST, scriptLabel);
		
		// Row 3
		layout.putConstraint(SpringLayout.NORTH, variantLabel, 5, SpringLayout.SOUTH, scriptLabel);
		layout.putConstraint(SpringLayout.WEST, variantLabel, 0, SpringLayout.WEST, scriptLabel);
		
		layout.putConstraint(SpringLayout.NORTH, variantTextField, 0, SpringLayout.NORTH, variantLabel);
		layout.putConstraint(SpringLayout.WEST, variantTextField, 5, SpringLayout.EAST, variantLabel);
		layout.putConstraint(SpringLayout.EAST, variantTextField, -5, SpringLayout.EAST, editPanel);
		
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

	private boolean checkValues() {
		
		if (!this.languageTextField.isTextValid()) {
			JOptionPane.showMessageDialog(this, "Invalid language code.", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (!this.countryTextField.isTextValid()) {
			JOptionPane.showMessageDialog(this, "Invalid country code.", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!this.scriptTextField.isTextValid()) {
			JOptionPane.showMessageDialog(this, "Invalid script code.", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (!this.variantTextField.isTextValid()) {
			JOptionPane.showMessageDialog(this, "Invalid variant.", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private void doOK() {
		
		if (this.checkValues()) {
			Locale.Builder builder = new Locale.Builder();

			builder.setLanguage(this.languageTextField.getText());
			if (!this.countryTextField.getText().isEmpty()) {
				builder.setRegion(this.countryTextField.getText());
			}
			if (!this.scriptTextField.getText().isEmpty()) {
				builder.setScript(this.scriptTextField.getText());
			}
			if (!this.variantTextField.getText().isEmpty()) {
				builder.setVariant(this.variantTextField.getText());
			}
			this.locale = builder.build();
			this.setVisible(false);
		}
	}
	
	private void doCancel() {
		this.locale = null;
		this.setVisible(false);
	}
	
	public Locale getLocale() {
		return this.locale;
	}
}
