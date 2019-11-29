package br.com.brokenbits.martelada.engine;

import java.io.Serializable;
import java.util.Locale;

/**
 * This class implements an immutable adapter to Java Locale that defines the
 * concept of a default value and also implements the Comparable interface.
 * 
 * <h3>Ordering</h3>
 * 
 * The ResourceLocale ordering respects the same ordering applied for
 * strings returned by ResourceLocale.toString().
 *  
 * @author Fabio Jun Takada Chino
 * @since 2019.11.28
 */
public final class ResourceLocale implements Comparable<ResourceLocale>, Serializable {
	
	private static final long serialVersionUID = 1L;

	public static ResourceLocale DEFAULT = new ResourceLocale(null);
	
	private final Locale locale;
	
	public ResourceLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof ResourceLocale) {
			return this.toString().equals(obj.toString());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public int compareTo(ResourceLocale obj) {
		return this.toString().compareTo(obj.toString());
	}
	
	@Override
	public String toString() {
		if (this.locale == null) {
			return "";
		} else {
			return this.locale.toString();
		}
	}

	public Locale getLocale() {
		return locale;
	}
}
