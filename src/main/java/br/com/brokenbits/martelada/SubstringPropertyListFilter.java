package br.com.brokenbits.martelada;

public class SubstringPropertyListFilter implements PropertyListFilter {

	private final String substring;
	
	public SubstringPropertyListFilter(String substring) {
		this.substring = substring;
	}
	
	@Override
	public boolean accept(String key) {
		
		if ((substring == null) || (substring.isBlank())) {
			return true;
		} else {
			return key.contains(this.substring);
		}
	}

	public String getSubstring() {
		return substring;
	}
}
