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

import java.util.Locale;

import org.junit.Test;

public class ResourceLocaleTest {

	@Test
	public void testHashCode() {
		
		assertEquals("".hashCode(), ResourceLocale.DEFAULT.hashCode());

		ResourceLocale l = new ResourceLocale(null); 
		assertEquals("".hashCode(), l.hashCode());
		
		l = new ResourceLocale(Locale.ENGLISH);
		assertEquals(Locale.ENGLISH.toString().hashCode(), l.hashCode());
	}

	@Test
	public void testResourceLocale() {
		ResourceLocale l = new ResourceLocale(null); 
		assertNull(l.getLocale());
		
		l = new ResourceLocale(Locale.ENGLISH); 
		assertNotNull(l.getLocale());
		assertEquals(Locale.ENGLISH, l.getLocale());
	}

	@Test
	public void testEqualsObject() {
		
		ResourceLocale l1 = new ResourceLocale(Locale.ENGLISH);
		ResourceLocale l2 = new ResourceLocale(Locale.ENGLISH);
		ResourceLocale l3 = new ResourceLocale(Locale.FRENCH);

		assertTrue(ResourceLocale.DEFAULT.equals(ResourceLocale.DEFAULT));
		assertTrue(l1.equals(l1));
		assertTrue(l2.equals(l2));
		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l1));
		assertTrue(l3.equals(l3));

		assertFalse(ResourceLocale.DEFAULT.equals(null));
		assertFalse(ResourceLocale.DEFAULT.equals(l1));
		assertFalse(l1.equals(ResourceLocale.DEFAULT));
		assertFalse(ResourceLocale.DEFAULT.equals(l3));
		assertFalse(l3.equals(ResourceLocale.DEFAULT));
	}

	@Test
	public void testCompareTo() {
		ResourceLocale l1 = new ResourceLocale(Locale.ENGLISH);
		ResourceLocale l2 = new ResourceLocale(Locale.FRENCH);
		ResourceLocale l3 = new ResourceLocale(Locale.FRANCE);

		assertTrue(ResourceLocale.DEFAULT.compareTo(ResourceLocale.DEFAULT) == 0);
		assertTrue(l1.compareTo(l1) == 0);
		assertTrue(l2.compareTo(l2) == 0);
		assertTrue(l3.compareTo(l3) == 0);
		assertTrue(l3.compareTo(new ResourceLocale(Locale.FRANCE)) == 0);

		// Default always comes first
		assertTrue(ResourceLocale.DEFAULT.compareTo(l1) < 0);
		assertTrue(ResourceLocale.DEFAULT.compareTo(l2) < 0);
		assertTrue(ResourceLocale.DEFAULT.compareTo(l3) < 0);
		assertTrue(l1.compareTo(ResourceLocale.DEFAULT) > 0);
		assertTrue(l2.compareTo(ResourceLocale.DEFAULT) > 0);
		assertTrue(l3.compareTo(ResourceLocale.DEFAULT) > 0);
		
		assertTrue(l1.compareTo(l2) < 0);
		assertTrue(l1.compareTo(l3) < 0);
		assertTrue(l2.compareTo(l1) > 0);
		assertTrue(l3.compareTo(l1) > 0);
		
		assertTrue(l2.compareTo(l3) < 0);
		assertTrue(l3.compareTo(l2) > 0);
	}

	@Test
	public void testToString() {
		
		assertEquals("", ResourceLocale.DEFAULT.toString());
		assertEquals(Locale.FRANCE.toString(), (new ResourceLocale(Locale.FRANCE)).toString());
	}

	@Test
	public void testGetLocale() {
		
		assertNull(ResourceLocale.DEFAULT.getLocale());
		assertEquals(Locale.FRANCE, (new ResourceLocale(Locale.FRANCE)).getLocale());
	}

}
