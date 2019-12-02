package br.com.brokenbits.martelada;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class JValidatedTextFieldTest {

	@Test
	public void testJValidatedTextField() {
		Pattern pattern = Pattern.compile("[a-z]+");
		JValidatedTextField t = new JValidatedTextField(pattern, true);
		assertEquals(pattern, t.getPattern());
		assertTrue(t.isEmptyAllowed());
		
		t = new JValidatedTextField(pattern, false);
		assertEquals(pattern, t.getPattern());
		assertFalse(t.isEmptyAllowed());
	}

	@Test
	public void testIsTextValid() {

		Pattern pattern = Pattern.compile("[a-z]+");
		JValidatedTextField t = new JValidatedTextField(pattern, true);
		assertTrue(t.isTextValid());
		t.setText("a");
		assertTrue(t.isTextValid());
		t.setText("ab");
		assertTrue(t.isTextValid());
		t.setText("1");
		assertFalse(t.isTextValid());
		
		t = new JValidatedTextField(pattern, false);
		assertFalse(t.isTextValid());
		t.setText("a");
		assertTrue(t.isTextValid());
		t.setText("ab");
		assertTrue(t.isTextValid());
		t.setText("1");
		assertFalse(t.isTextValid());
	}

	@Test
	public void testSetGetPattern() {
		Pattern pattern1 = Pattern.compile("[a-z]+");
		Pattern pattern2 = Pattern.compile("[a-z]+");
		JValidatedTextField t = new JValidatedTextField(pattern1, true);
		assertEquals(pattern1, t.getPattern());

		t.setPattern(pattern2);
		assertEquals(pattern2, t.getPattern());
	}

	@Test
	public void testSetIsEmptyAllowed() {

		Pattern pattern = Pattern.compile("[a-z]+");
		JValidatedTextField t = new JValidatedTextField(pattern, true);
		assertTrue(t.isEmptyAllowed());
		
		t.setEmptyAllowed(false);
		assertFalse(t.isEmptyAllowed());
		
		t.setEmptyAllowed(true);
		assertTrue(t.isEmptyAllowed());
	}
}
