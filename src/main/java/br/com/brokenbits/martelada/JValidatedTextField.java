package br.com.brokenbits.martelada;

import java.util.regex.Pattern;

import javax.swing.JTextField;

public class JValidatedTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	
	private Pattern pattern;
	
	private boolean emptyAllowed;

	public JValidatedTextField(Pattern pattern, boolean emptyAllowed) {
		this.pattern = pattern;
		this.emptyAllowed = emptyAllowed;
	}
	
	public boolean isTextValid() {

		String text = this.getText();
		if (isEmptyAllowed() && text.isEmpty()) {
			return true;
		} else {
			return this.pattern.matcher(text).matches();
		}		
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isEmptyAllowed() {
		return emptyAllowed;
	}

	public void setEmptyAllowed(boolean emptyAllowed) {
		this.emptyAllowed = emptyAllowed;
	}
}
