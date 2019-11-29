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
		assertEquals("sample", p.getProperties().get("sample"));
	}

	@Test
	public void testSave() throws Exception {
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		File file = File.createTempFile("test", ".properties");
		p.save(file);
	}
}
