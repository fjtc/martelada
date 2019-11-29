package br.com.brokenbits.martelada.engine;

import static org.junit.Assert.*;

import org.junit.Test;

public class SuffixTokenizerTest {

	@Test
	public void testSuffixTokenizer() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test");
		assertTrue(t.hasMoreTokens());

		t = new SuffixTokenizer("");
		assertFalse(t.hasMoreTokens());
	}

	@Test
	public void testNextToken1() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test");
		assertTrue(t.hasMoreTokens());
		assertEquals("test", t.nextToken());
		assertFalse(t.hasMoreTokens());
		assertNull(t.nextToken());
	}

	@Test
	public void testNextToken2() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test1_test2");
		assertTrue(t.hasMoreTokens());
		assertEquals("test1", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("test2", t.nextToken());
		assertFalse(t.hasMoreTokens());
		assertNull(t.nextToken());
	}

	@Test
	public void testNextToken3() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test1__test2");
		assertTrue(t.hasMoreTokens());
		assertEquals("test1", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("test2", t.nextToken());
		assertFalse(t.hasMoreTokens());
		assertNull(t.nextToken());
	}
	
	@Test
	public void testNextToken4() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test1_test2_");
		assertTrue(t.hasMoreTokens());
		assertEquals("test1", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("test2", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.nextToken());
		assertFalse(t.hasMoreTokens());
		assertNull(t.nextToken());
	}
	
	
	@Test
	public void testNextToken5() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("__");
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.nextToken());
		assertFalse(t.hasMoreTokens());
		assertNull(t.nextToken());
	}	
	
	@Test
	public void testGetRemaining0() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("");
		assertFalse(t.hasMoreTokens());
		assertNull(t.getRemaining());
		assertNull(t.nextToken());
	}
	
	@Test
	public void testGetRemaining1() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test1_test2");
		assertTrue(t.hasMoreTokens());
		assertEquals("test1", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("test2", t.getRemaining());
		assertFalse(t.hasMoreTokens());
		assertNull(t.getRemaining());
		assertNull(t.nextToken());
	}
	
	@Test
	public void testGetRemaining2() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test1_test2");
		assertTrue(t.hasMoreTokens());
		assertEquals("test1_test2", t.getRemaining());
		assertFalse(t.hasMoreTokens());
		assertNull(t.getRemaining());
		assertNull(t.nextToken());
	}
	
	@Test
	public void testGetRemaining3() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("test1_");
		assertTrue(t.hasMoreTokens());
		assertEquals("test1", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.getRemaining());
		assertFalse(t.hasMoreTokens());
		assertNull(t.getRemaining());
		assertNull(t.nextToken());
	}
	
	@Test
	public void testGetRemaining4() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("_");
		assertTrue(t.hasMoreTokens());
		assertEquals("_", t.getRemaining());
		assertFalse(t.hasMoreTokens());
		assertNull(t.getRemaining());
		assertNull(t.nextToken());
	}
	
	@Test
	public void testGetRemaining5() {
		SuffixTokenizer t;
		
		t = new SuffixTokenizer("_");
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.nextToken());
		assertTrue(t.hasMoreTokens());
		assertEquals("", t.getRemaining());
		assertFalse(t.hasMoreTokens());
		assertNull(t.getRemaining());
		assertNull(t.nextToken());
	}
}
