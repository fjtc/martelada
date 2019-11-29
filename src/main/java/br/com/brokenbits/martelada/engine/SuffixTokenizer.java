package br.com.brokenbits.martelada.engine;

import java.nio.CharBuffer;

/**
 * This class implements a suffix tokenizer. It splits the
 * components of the the suffix using '_' as the separator.
 * Examples:
 * 
 * <ul>
 * 	<li>"p1" : {"p1"}</li>
 * 	<li>"p1_p2" : {"p1", "p2"}</li>
 * 	<li>"p1__p2" : {"p1", "", "p2"}</li>
 * 	<li>"p1_" : {"p1", ""}</li>
 * 	<li>"_" : {"", ""}</li>
 * </ul>
 * 
 * @author Fabio Jun Takada Chino
 */
class SuffixTokenizer {
	
	public static final char SEPARATOR = '_';
	
	private final CharBuffer buffer;
	
	private char lastChar = 0;
			
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param suffix The suffix to be parsed.
	 */
	public SuffixTokenizer(String suffix) {
		buffer = CharBuffer.wrap(suffix);
	}
	
	/**
	 * Verifies if this string has more tokens.
	 * 
	 * @return true if there is more tokens or false otherwise.
	 */
	public boolean hasMoreTokens() {
		return this.buffer.hasRemaining() || (lastChar == SEPARATOR);
	}
	
	/**
	 * Returns the next token.
	 * 
	 * @return The next token or null if it is not available.
	 */
	public String nextToken() {

		if (this.buffer.hasRemaining()) {
			StringBuilder token = new StringBuilder();
			lastChar = buffer.get();
			boolean hasMore = (lastChar != SEPARATOR);
			while (hasMore) { 
				token.append(lastChar);
				if (buffer.hasRemaining()) {
					lastChar = buffer.get();
					hasMore = (lastChar != SEPARATOR);
				} else {
					hasMore = false;
					lastChar = 0;
				}
			}
			return token.toString();
		} else {
			if (lastChar == SEPARATOR) {
				lastChar = 0;
				return "";
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Returns the remaining characters in the buffer as a single token. Since
	 * "" is a valid last token, it will return "" if it is the last token available
	 * to be read.
	 * 
	 * @return The remaining characters as a string or null if it is not available.
	 */
	public String getRemaining() {

		if (this.hasMoreTokens()) {
			lastChar = 0;
			StringBuilder token = new StringBuilder();
			while (buffer.hasRemaining()) {
				token.append(this.buffer.get());
			}
			return token.toString();
		} else {
			return null;
		}
	}
}
