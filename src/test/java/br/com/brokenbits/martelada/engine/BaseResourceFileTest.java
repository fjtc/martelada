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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class BaseResourceFileTest {

	private static final File SAMPLE_FILE = new File("sample", "sample.properties");
	
	@Test
	public void testBaseResourceFile() {
		BaseResourceFile f = new BaseResourceFile(SAMPLE_FILE);
		assertEquals("sample", f.getBaseName());
		assertEquals(SAMPLE_FILE, f.getBaseFile());
	}

	@Test
	public void testGetFile() {
		BaseResourceFile f = new BaseResourceFile(SAMPLE_FILE);
		
		assertEquals(new File(SAMPLE_FILE.getParentFile(), "sample_en.properties"),
				f.getFile(Locale.ENGLISH));
		assertEquals(new File(SAMPLE_FILE.getParentFile(), "sample_fr_CA.properties"),
				f.getFile(Locale.CANADA_FRENCH));
		Locale l = (new Locale.Builder())
				.setLanguage("en")
				.setScript("Latn")
				.setRegion("UK")
				.setVariant("Allan-Turing-1912")
				.build();
		assertEquals(new File(SAMPLE_FILE.getParentFile(), "sample_en_Latn_UK_Allan_Turing_1912.properties"),
				f.getFile(l));		
	}
	
	private static final String [] CANDIDATE_FILES = {
			"sample_pt_BR-fd.properties",
			"sample_pt_BR.properties",
			"sample_pt_Latn_PT_Original.properties"};

	@Test
	public void testListRelatedFiles() {
		BaseResourceFile f = new BaseResourceFile(SAMPLE_FILE);
		
		List<File> l = f.listRelatedFiles();
		assertEquals(CANDIDATE_FILES.length, l.size());
		
		HashSet<String> candidates = new HashSet<String>();
		Collections.addAll(candidates, CANDIDATE_FILES);
		for (File cf: l) {
			assertTrue(cf.exists());
			assertTrue(cf.isFile());
			assertEquals(SAMPLE_FILE.getParentFile(), cf.getParentFile());
			assertTrue(candidates.contains(cf.getName()));
		}
	}

}
