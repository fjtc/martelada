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
