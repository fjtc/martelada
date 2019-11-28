package br.com.brokenbits.martelada.engine;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import br.com.brokenbits.martelada.engine.LocalizedProperties;

public class LocalizedPropertiesTest {

	@Test
	public void testPropertiesFile() {
		LocalizedProperties p = new LocalizedProperties();
		
		assertNotNull(p.getProperties());
		assertEquals(0, p.getProperties().size());
		
		assertNull(p.getLocale());
	}

	@Test
	public void testGetFile() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetFile() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetProperties() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetProperties() {
		//fail("Not yet implemented");
	}

	@Test
	public void testLoad() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSave() throws Exception {
		LocalizedProperties p = new LocalizedProperties();
		File file = File.createTempFile("test", ".properties");
		p.save(file);
		System.out.println(file);
	}
}
