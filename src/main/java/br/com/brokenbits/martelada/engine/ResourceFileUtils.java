package br.com.brokenbits.martelada.engine;

import java.util.Locale;
import java.util.regex.Pattern;

public class ResourceFileUtils {
	
	public static final String FILE_SUFFIX = ".properties";
	
	// two-letter ISO 639
	public static final Pattern LANGUAGE_PATTERN = Pattern.compile("[a-z]{2,3}");
	
	// two-letter ISO 3166 
	public static final Pattern COUNTRY_PATTERN = Pattern.compile("[A-Z]{2}");
	
	// IETF BCP 47
	public static final Pattern VARIANT_PATTERN = Pattern.compile("[a-zA-Z0-9]{5,8}(_[a-zA-Z0-9]{5,8})*");
	
	// ISO 15924
	public static final Pattern SCRIPT_PATTERN = Pattern.compile("[A-Z][a-z]{3}");
	

	/**
	 * Extracts the base name of a resource file. A valid resource file name is always
	 * composed by 'basename.properties'.
	 * 
	 * @param fileName The file name.
	 * @return The base name.
	 * @exception IllegalArgumentException If the file name is not a valid resource name.
	 */
	public static String extractBaseName(String fileName) throws IllegalArgumentException {
		
		if ((fileName.length() <= FILE_SUFFIX.length()) || (!fileName.endsWith(FILE_SUFFIX))) {
			throw new IllegalArgumentException("The given fileName is not a valid resource name.");
		}
		return fileName.substring(0, fileName.length() - FILE_SUFFIX.length()); 
	}
	
	/**
	 * Normalizes the resource file name by adding the extension if necessary.
	 * 
	 * @param fileName The file name.
	 * @return The normalized file name.
	 */
	public static String normalizeResourceFile(String fileName) {

		if (!fileName.endsWith(FILE_SUFFIX)) {
			return fileName + FILE_SUFFIX;
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
	 * Extracts the variant string from the given file name.
	 * 
	 * @param baseName The base name.
	 * @param fileName
	 * @return
	 */
	protected static String extractVariantString(String baseName, String fileName) {
		int variantLen = fileName.length() - (baseName.length() + 1 + FILE_SUFFIX.length());
		if (variantLen < 2) {
			return null;
		} else {
			if (fileName.startsWith(baseName + "_") && fileName.endsWith(FILE_SUFFIX)) {
				return fileName.substring(baseName.length() + 1, baseName.length() + 1 + variantLen);
			} else {
				return null;
			}
		}
	}
	
	
	private static final int LANGUAGE_IDX = 0;
	private static final int SCRIPT_IDX = 1;
	private static final int COUNTRY_IDX = 2;
	private static final int VARIANT_IDX = 3;
	
	protected static String [] parseLocaleParts(String suffix) throws IllegalArgumentException {
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
						parts[VARIANT_IDX] = token;
					} else {
						throw new IllegalArgumentException(String.format("Invalid variant %1$s.", token));
					}
				}
			}
		}
		return parts;
	}
	
	public static Locale parseLocaleSuffix(String suffix) throws IllegalArgumentException {
		
		if ((suffix == null) || (suffix.isEmpty())) {
			return null;
		} else {
			String [] parts = parseLocaleParts(suffix);
			
			Locale.Builder builder = new Locale.Builder();
			if (parts[LANGUAGE_IDX] != null) {
				builder.setLanguage(parts[LANGUAGE_IDX]);
			}
			
			return builder.build();
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
