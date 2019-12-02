package br.com.brokenbits.martelada;

public class CopyPattern {
	
	public static final String KEY_STRING = "${key}";

	private final String pattern;
	
	public CopyPattern(String pattern) throws IllegalArgumentException {
		this.pattern = pattern;
		if (!pattern.contains(KEY_STRING)) {
			throw new IllegalArgumentException(String.format("The pattern must contain the key %1$s.", KEY_STRING));
		}
	}
	
	protected String escapeKeyValue(String keyValue) {
		// TODO Perform the escaping here.
		return keyValue;
	}

	public String getPattern() {
		return pattern;
	}

	public String format(String keyValue) {
		return this.pattern.replace(KEY_STRING, escapeKeyValue(keyValue));
	}
}
