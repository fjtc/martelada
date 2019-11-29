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

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import br.com.brokenbits.martelada.engine.LocalizedProperties;

public class LocalizedPropertiesTest {

	@Test
	public void testPropertiesFile() {
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		
		assertNotNull(p.getProperties());
		assertEquals(0, p.getProperties().size());
		
		assertEquals(ResourceLocale.DEFAULT, p.getLocale());
	}

	@Test
	public void testLoad() throws Exception {
		File file = new File("sample", "sample.properties");
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		p.load(file);
		
		assertEquals(1, p.getProperties().size());
		assertEquals("file", p.getProperties().get("file"));
	}

	@Test
	public void testSave() throws Exception {
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		File file = File.createTempFile("test", ".properties");
		p.save(file);
	}
}
