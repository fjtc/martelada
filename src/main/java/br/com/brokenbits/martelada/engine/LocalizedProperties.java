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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class LocalizedProperties {

	private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");
	
	private final Properties properties = new Properties();
	
	private final ResourceLocale locale;
	
	public LocalizedProperties(ResourceLocale locale) {
		this.locale = locale;
	}

	public Properties getProperties() {
		return properties;
	}
	
	public void load(File file) throws IOException {
		try (FileInputStream in = new FileInputStream(file)) {
			this.properties.clear();
			this.properties.load(in);
		}
	}
	
	
	protected List<String> saveToLines(File file) throws IOException {
		StringWriter writer = new StringWriter();
		this.properties.store(writer, file.getName());
		writer.close();
		
		List<String> lines = new ArrayList<>();
		try (LineNumberReader reader = new LineNumberReader(new StringReader(writer.toString()))){
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		}
		return lines;
	}
	
	public void save(File file) throws IOException {
		
		// NOTE: This implementation uses the original
		// Properties.store() to generate the file. However,
		// to keep it sorted, it converts the file into a list
		// of lines and sorts the lines before writing the actual
		// file. This approach works because each entry will be
		// in a single line, as stated in the documentation 
		// "Then every entry in this Properties table is written out, one per line.".
		// Thus, no multiple line entries are expected.
		
		// Serialize all entries to a list of lines.
		List<String> lines = saveToLines(file);
		
		// Extract the header because we don't want to sort its lines
		List<String> header = new ArrayList<>();
		while ((lines.size() > 0) && (lines.get(0).startsWith("#"))) {
			header.add(lines.get(0));
			lines.remove(0);
		}
		
		// Sort the lines
		Collections.sort(lines);

		// Save the actual file
		try (FileWriter writer = new FileWriter(file, DEFAULT_CHARSET)) {
			for (String line: header) {
				writer.write(line);
				writer.write("\n");
			}
			for (String line: lines) {
				writer.write(line);
				writer.write("\n");
			}
		}
	}

	public ResourceLocale getLocale() {
		return locale;
	}
}
