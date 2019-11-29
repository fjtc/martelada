package br.com.brokenbits.martelada.engine;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class ResourceFileUtilsTest {

	@Test
	public void testLanguagePattern() {
		
		assertTrue(ResourceFileUtils.LANGUAGE_PATTERN.matcher("aa").matches());
		assertTrue(ResourceFileUtils.LANGUAGE_PATTERN.matcher("zz").matches());
		assertTrue(ResourceFileUtils.LANGUAGE_PATTERN.matcher("aaa").matches());
		assertTrue(ResourceFileUtils.LANGUAGE_PATTERN.matcher("zzz").matches());
		
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("").matches());
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("a").matches());
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("aaaa").matches());
		
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("12").matches());
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("--").matches());
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("AA").matches());
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("AAA").matches());
		assertFalse(ResourceFileUtils.LANGUAGE_PATTERN.matcher("ZZZ").matches());
	}

	@Test
	public void testScriptPattern() {
		
		assertTrue(ResourceFileUtils.SCRIPT_PATTERN.matcher("Aaaa").matches());
		assertTrue(ResourceFileUtils.SCRIPT_PATTERN.matcher("Zzzz").matches());

		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("").matches());
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("Z").matches());
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("Zzzzz").matches());
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("zzzz").matches());
		
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("1zzz").matches());
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("Z1zz").matches());
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("Zz1z").matches());
		assertFalse(ResourceFileUtils.SCRIPT_PATTERN.matcher("Zzz1").matches());
	}

	@Test
	public void testCountryPattern() {
		
		assertTrue(ResourceFileUtils.COUNTRY_PATTERN.matcher("AA").matches());
		assertTrue(ResourceFileUtils.COUNTRY_PATTERN.matcher("ZZ").matches());
		assertTrue(ResourceFileUtils.COUNTRY_PATTERN.matcher("000").matches());
		assertTrue(ResourceFileUtils.COUNTRY_PATTERN.matcher("999").matches());
		
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("A").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("AAA").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("0").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("00").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("0000").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("aA").matches());
		assertFalse(ResourceFileUtils.COUNTRY_PATTERN.matcher("Aa").matches());
	}

	@Test
	public void testVariantPattern() {

		// Starting with a letter
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("aaaaa").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("zzzzz").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("AAAAA").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("ZZZZZ").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("abcdefghijklmnopqrstuvwxyz0123456789").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("AbCdEfGhIjKlMnOpQrStUvWxYz0123456789").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("a0123456789bcdefghijklmnopqrstuvwxyz").matches());
		
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("Z").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("ab").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("asd").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("ZZZZ").matches());
	
		// Starting with a number
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("1111").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("9999").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("9aaa").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("01234567890").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("0abcdefghijklmnopqrstuvwxyz123456789").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("9AbCdEfGhIjKlMnOpQrStUvWxYz123456789").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("0").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("01").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("012").matches());
		
		// Multipart
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("aaaaa_1234").matches());
		assertTrue(ResourceFileUtils.VARIANT_PATTERN.matcher("1234_aaaaa_12345").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("123_aaaaa_12345").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("1234_").matches());
		assertFalse(ResourceFileUtils.VARIANT_PATTERN.matcher("1234_aaaa_12345").matches());
	}
	
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
	public void testPartsToLocale() {
		
		assertEquals((new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setRegion("US")
				.setVariant("variant")
				.build(),
				ResourceFileUtils.partsToLocale("en", "Cyrl", "US", "variant")); 
		
		assertEquals((new Locale.Builder())
				.setScript("Cyrl")
				.setRegion("US")
				.setVariant("variant")
				.build(),
				ResourceFileUtils.partsToLocale(null, "Cyrl", "US", "variant")); 		

		assertEquals((new Locale.Builder())
				.setLanguage("en")
				.setRegion("US")
				.setVariant("variant")
				.build(),
				ResourceFileUtils.partsToLocale("en", null, "US", "variant"));
		
		assertEquals((new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setVariant("variant")
				.build(),
				ResourceFileUtils.partsToLocale("en", "Cyrl", null, "variant"));
		
		assertEquals((new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setRegion("US")
				.build(),
				ResourceFileUtils.partsToLocale("en", "Cyrl", "US", null));
		
		assertEquals((new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setVariant("variant-tester")
				.build(),
				ResourceFileUtils.partsToLocale("en", "Cyrl", null, "variant-tester"));		
	}
	
	@Test
	public void testParseLocaleParts() {
		
		assertArrayEquals(
				new String[] {"en", "Latn", "US", "WINDOWS-VISTA"},
				ResourceFileUtils.parseLocaleParts("en_Latn_US_WINDOWS_VISTA"));
		assertArrayEquals(
				new String[] {"en", "Latn", "US", "WINDOWS"},
				ResourceFileUtils.parseLocaleParts("en_Latn_US_WINDOWS"));
		assertArrayEquals(
				new String[] {"en", "Latn", "US", null},
				ResourceFileUtils.parseLocaleParts("en_Latn_US"));
		assertArrayEquals(
				new String[] {"en", "Latn", null, null},
				ResourceFileUtils.parseLocaleParts("en_Latn"));		
		assertArrayEquals(
				new String[] {"en", null, "US", "WINDOWS-VISTA"},
				ResourceFileUtils.parseLocaleParts("en_US_WINDOWS_VISTA"));
		assertArrayEquals(
				new String[] {"en", null, "US", "WINDOWS"},
				ResourceFileUtils.parseLocaleParts("en_US_WINDOWS"));
		assertArrayEquals(
				new String[] {"en", null, "US", null},
				ResourceFileUtils.parseLocaleParts("en_US"));
		assertArrayEquals(
				new String[] {"en", null, null, null},
				ResourceFileUtils.parseLocaleParts("en"));
		assertArrayEquals(
				new String[] {"de", null, null, "JAVAC"},
				ResourceFileUtils.parseLocaleParts("de__JAVAC"));
		
		// TODO Find more invalid cases to be tested later
		for (String invalid: new String[] {
				"",
				"AA",
				"aaaa",
				"123",
				"aa_aaaa",
				"aa_Aaa",
				"aa_AAAA",
				"aa_Aaaa_aa",
				"aa_Aaaa_AA_AAAA",
				"aa_Aaaa_AA_111",
				"aa_aa",
				"aa_",
				"aa_Aaaa_",
				"aa_Aaaa_AA_",
				}) {
			try {
				ResourceFileUtils.parseLocaleParts(invalid);
				fail(invalid);
			} catch (IllegalArgumentException e) {}
		}
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
		assertEquals("_en_Cyrl_US_variant_1234", ResourceFileUtils.createLocaleSuffix(
				(new Locale.Builder())
				.setLanguage("en")
				.setScript("Cyrl")
				.setRegion("US")
				.setVariant("variant-1234")
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
