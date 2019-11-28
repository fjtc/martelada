package br.com.brokenbits.martelada.engine;

import java.io.Serializable;
import java.util.Locale;

/**
 * This class implements an immutable adapter to Java Locale that defines the
 * concept of a default value and also implements the Comparable interface.
 *  
 * @author Fabio Jun Takada Chino
 */
public class ResourceLocale implements Comparable<ResourceLocale>, Serializable {
	
	private static final long serialVersionUID = 1L;

	public static ResourceLocale DEFAULT = new ResourceLocale(null);
	
	private final Locale locale;
	
	public ResourceLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof ResourceLocale) {
			return this.equals((ResourceLocale)obj);
		} else {
			return false;
		}
	}
	
	public boolean equals(ResourceLocale obj) {
		
		if (this.locale == null) {
			return (obj.locale == null);
		} else {
			if (obj.locale == null) {
				return false;
			} else {
				return this.locale.toString().equals(obj.locale.toString());
			}
		}
	}
	
	@Override
	public int hashCode() {
		if (this.locale == null) {
			return "".hashCode();
		} else {
			return this.locale.toString().hashCode();
		}
	}
	
	@Override
	public int compareTo(ResourceLocale obj) {
		if (this.equals(obj)) {
			return 0;
		} else {
			return this.locale.toString().compareTo(obj.locale.toString());	
		}
	}
	
	@Override
	public String toString() {
		if (this.locale == null) {
			return "DEFAULT";
		} else {
			return this.locale.toString();
		}
	}

	public Locale getLocale() {
		return locale;
	}
}
