package br.com.brokenbits.martelada.engine;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class ResourceFileUtilsTest {


	
	@Test
	public void testExtractBaseName() {
		assertEquals("t", ResourceFileUtils.extractBaseName("t.properties"));
		assertEquals("test", ResourceFileUtils.extractBaseName("test.properties"));
		assertEquals("test.properties", ResourceFileUtils.extractBaseName("test.properties.properties"));
		try {
			ResourceFileUtils.extractBaseName(".properties");
			fail();
		} catch (IllegalArgumentException e) {}
		try {
			ResourceFileUtils.extractBaseName("test");
			fail();
		} catch (IllegalArgumentException e) {}
	}
	
	@Test
	public void testNormalizeResourceFile(){
		assertEquals("t.properties", ResourceFileUtils.normalizeResourceFile("t"));
		assertEquals("test.properties", ResourceFileUtils.normalizeResourceFile("test"));
		assertEquals("test.properties", ResourceFileUtils.normalizeResourceFile("test.properties"));		
	}
	
	@Test
	public void testExtractVariantString() {
		
		assertEquals("en", ResourceFileUtils.extractVariantString("base", "base_en.properties"));
		assertEquals("en_whatever", ResourceFileUtils.extractVariantString("base", "base_en_whatever.properties"));
		
		assertNull(ResourceFileUtils.extractVariantString("base", "base.properties"));
		assertNull(ResourceFileUtils.extractVariantString("base", "base2_en.propertie"));
		assertNull(ResourceFileUtils.extractVariantString("base2", "base_en.propertie"));
	}
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	@Test
	public void testHasComponent() {
		
		assertTrue(ResourceFileUtils.hasComponent("123"));
		assertFalse(ResourceFileUtils.hasComponent(null));
		assertFalse(ResourceFileUtils.hasComponent(""));
	}
	
	@Test
	public void testFormatComponent() {
		assertEquals("", ResourceFileUtils.formatComponent(null));
		assertEquals("", ResourceFileUtils.formatComponent(""));
		assertEquals("123", ResourceFileUtils.formatComponent("123"));
	}

	@Test
	public void testCreateLocaleSuffix() {

		assertEquals("", ResourceFileUtils.createLocaleSuffix(null));
		
		assertEquals("_en_Cyrl_US_variant", ResourceFileUtils.createLocaleSuffix(
				(new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setRegion("US")
				.setVariant("variant")
				.build()));
		assertEquals("_en_Cyrl__variant", ResourceFileUtils.createLocaleSuffix(
				(new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setVariant("variant")
				.build()));		
		assertEquals("_en_Cyrl_US", ResourceFileUtils.createLocaleSuffix(
				(new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setRegion("US")
				.build()));
		assertEquals("_en_Cyrl", ResourceFileUtils.createLocaleSuffix(
				(new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.build()));
		assertEquals("_en_US_variant", ResourceFileUtils.createLocaleSuffix((new Locale.Builder())
				.setLanguage("en")
				.setRegion("US")
				.setVariant("variant")
				.build()));
		assertEquals("_en__variant", ResourceFileUtils.createLocaleSuffix((new Locale.Builder())
				.setLanguage("en")
				.setVariant("variant")
				.build()));		
		assertEquals("_en_US", ResourceFileUtils.createLocaleSuffix((new Locale.Builder())
				.setLanguage("en")
				.setRegion("US")
				.build()));	
		assertEquals("_en", ResourceFileUtils.createLocaleSuffix((new Locale.Builder())
				.setLanguage("en")
				.build()));
	}	
}
