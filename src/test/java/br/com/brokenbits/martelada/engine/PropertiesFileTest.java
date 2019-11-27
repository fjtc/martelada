package br.com.brokenbits.martelada.engine;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import br.com.brokenbits.martelada.engine.PropertiesFile;

public class PropertiesFileTest {

	@Test
	public void testPropertiesFile() {
		PropertiesFile p = new PropertiesFile();
		
		assertNotNull(p.getProperties());
		assertEquals(0, p.getProperties().size());
		
		assertNull(p.getFile());
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
		PropertiesFile p = new PropertiesFile();
		File file = File.createTempFile("test", ".properties");
		p.setFile(file);
		p.save();
		System.out.println(file);
	}

}
