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

import java.util.HashSet;

import javax.swing.JFrame;

public class PropertyNameDialogTest {

	public static void main(String[] args) {

		HashSet<String> keys = new HashSet<>();
		keys.add("123");
		
		PropertyNameDialog d = new PropertyNameDialog();
		d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		System.out.println(d.showDialog("Test title", "old-value", keys));
		System.exit(0);
	}
}
