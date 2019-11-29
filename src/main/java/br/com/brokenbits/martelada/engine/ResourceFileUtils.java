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
package br.com.brokenbits.martelada.engine;

import java.util.Locale;
import java.util.regex.Pattern;

public class ResourceFileUtils {
	
	public static final String FILE_EXTENSION = ".properties";
	
	// two-letter ISO 639
	public static final Pattern LANGUAGE_PATTERN = Pattern.compile("^[a-z]{2,3}$");
	
	// two-letter ISO 3166 or UN M.49
	public static final Pattern COUNTRY_PATTERN = Pattern.compile("^[A-Z]{2}|[0-9]{3}$");
	
	// IETF BCP 47
	public static final Pattern VARIANT_PATTERN = Pattern.compile(
			"^(([a-zA-Z][a-zA-Z0-9]{4,})|([0-9][a-zA-Z0-9]{3,}))" +
			"(_(([a-zA-Z][a-zA-Z0-9]{4,})|([0-9][a-zA-Z0-9]{3,})))*$");
	
	// ISO 15924
	public static final Pattern SCRIPT_PATTERN = Pattern.compile("^[A-Z][a-z]{3}$");
	

	/**
	 * Extracts the base name of a resource file. A valid resource file name is always
	 * composed by 'basename.properties'.
	 * 
	 * @param fileName The file name.
	 * @return The base name.
	 * @exception IllegalArgumentException If the file name is not a valid resource name.
	 */
	public static String extractBaseName(String fileName) throws IllegalArgumentException {
		
		if ((fileName.length() <= FILE_EXTENSION.length()) || (!fileName.endsWith(FILE_EXTENSION))) {
			throw new IllegalArgumentException("The given fileName is not a valid resource name.");
		}
		return fileName.substring(0, fileName.length() - FILE_EXTENSION.length()); 
	}
	
	/**
	 * Normalizes the resource file name by adding the extension if necessary.
	 * 
	 * @param fileName The file name.
	 * @return The normalized file name.
	 */
	public static String normalizeResourceFile(String fileName) {

		if (!fileName.endsWith(FILE_EXTENSION)) {
			return fileName + FILE_EXTENSION;
		} else {
			return fileName;
		}
	}

	// "_en_Cyrl_US_variant"
	// "_en_Cyrl__variant"
	// "_en_Cyrl_US"
	// "_en_Cyrl"
	// "_en_US_variant"
	// "_en__variant"
	// "_en_US"
	// "_en"
	
	/**
	 * Verifies if a given resource has a suffix. It is important to notice that the
	 * format of the suffix is not validated by this method.
	 * 
	 * <p>In order to have a suffix, the fileName must have the format 
	 * [<code>baseName + "_" + suffix + FILE_EXTENSION</code>], where suffix must have
	 * at least 2 characters in length.</p>
	 * 
	 * @param baseName The base name.
	 * @param fileName The file name.
	 * @return True if it may be related or false otherwise.
	 */
	protected static boolean hasExtractableSuffix(String baseName, String fileName) {

		int minimumSize = baseName.length() + 1 + 2 + FILE_EXTENSION.length();
		if (fileName.length() < minimumSize) {
			return false;
		} else {
			return fileName.startsWith(baseName) && 
					(fileName.charAt(baseName.length()) == '_') &&
					fileName.endsWith(FILE_EXTENSION);
		}
	}

	/**
	 * Extracts the suffix of the file. It is important to notice that the
	 * format of the suffix is not validated by this method.
	 * 
	 * <p>In order to have a suffix, the fileName must have the format 
	 * [<code>baseName + "_" + suffix + FILE_EXTENSION</code>], where suffix must have
	 * at least 2 characters in length.</p> 
	 * 
	 * @param baseName The base name.
	 * @param fileName The file name.
	 * @return The extracted suffix of null if it cannot be extracted.
	 */
	protected static String extractSuffix(String baseName, String fileName) {
		
		if (hasExtractableSuffix(baseName, fileName)) {
			return fileName.substring(baseName.length() + 1, 
					fileName.length() - FILE_EXTENSION.length());
		} else {
			return null;
		}
	}
	
	private static final int LANGUAGE_IDX = 0;
	private static final int SCRIPT_IDX = 1;
	private static final int COUNTRY_IDX = 2;
	private static final int VARIANT_IDX = 3;
	
	protected static String [] parseLocaleParts(String suffix) throws IllegalArgumentException {
		
		if (suffix.isEmpty()) {
			throw new IllegalArgumentException("The suffix cannot be empty.");	
		}
		String [] parts = new String[4];
		SuffixTokenizer tokenizer = new SuffixTokenizer(suffix);
		
		// Language
		String token = tokenizer.nextToken();
		if (LANGUAGE_PATTERN.matcher(token).matches()) {
			parts[LANGUAGE_IDX] = token;
		} else {
			throw new IllegalArgumentException(String.format("Invalid language %1$s.", token));	
		}

		// Script
		token = tokenizer.nextToken();
		if (token != null) {
			if (SCRIPT_PATTERN.matcher(token).matches()) {
				parts[SCRIPT_IDX] = token;
				token = tokenizer.nextToken();
			}
			
			// Country
			if (token != null) {
				if (token.isEmpty()) {
					token = tokenizer.nextToken();
					if ((token == null) || (token.isEmpty())) {
						throw new IllegalArgumentException("An empty contry must be followed by a valid variant.");
					}
				} else if (COUNTRY_PATTERN.matcher(token).matches()) {
					parts[COUNTRY_IDX] = token;
					token = tokenizer.nextToken();
				} else {
					throw new IllegalArgumentException(String.format("Invalid country %1$s.", token));	
				}

				// Variant
				if (token != null) {
					StringBuilder variant = new StringBuilder(token);
					if (tokenizer.hasMoreTokens()) {
						variant.append(SuffixTokenizer.SEPARATOR).append(tokenizer.getRemaining());
					}
					token = variant.toString();
					if (VARIANT_PATTERN.matcher(token).matches()) {
						parts[VARIANT_IDX] = token.replace('_', '-');
					} else {
						throw new IllegalArgumentException(String.format("Invalid variant %1$s.", token));
					}
				}
			}
		}
		return parts;
	}
	
	/**
	 * Creates a new locale
	 * @param parts The 4 parts of the locale in the following order:
	 * @return
	 */
	public static Locale partsToLocale(String language, String script, String country, String variant) {
		
		Locale.Builder builder = new Locale.Builder();
		if (language != null) {
			builder.setLanguage(language);
		}
		if (script != null) {
			builder.setScript(script);
		}
		if (country != null) {
			builder.setRegion(country);
		}
		if (variant != null) {
			builder.setVariant(variant);
		}		
		return builder.build();		
	}
	
	public static Locale parseLocaleSuffix(String suffix) throws IllegalArgumentException {
		
		if ((suffix == null) || (suffix.isEmpty())) {
			return null;
		} else {
			String [] parts = parseLocaleParts(suffix);
			return partsToLocale(
					parts[LANGUAGE_IDX],
					parts[SCRIPT_IDX],
					parts[COUNTRY_IDX],
					parts[VARIANT_IDX]);
		}
	}	
	
	protected static boolean hasComponent(String component) {
		return (component != null) && (!component.isEmpty());
	}
	
	protected static String formatComponent(String component) {
		return (component == null)? "": component;
	}
	
	public static String createLocaleSuffix(Locale locale) {
		
		if (locale != null) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("_").append(locale.getLanguage());
			if (hasComponent(locale.getScript())) {
				sb.append("_").append(formatComponent(locale.getScript()));
			}
			if (hasComponent(locale.getVariant())) {
				sb.append("_").append(formatComponent(locale.getCountry()));
				sb.append("_").append(formatComponent(locale.getVariant()));
			} else {
				if (hasComponent(locale.getCountry())) {
					sb.append("_").append(formatComponent(locale.getCountry()));
				}				
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
