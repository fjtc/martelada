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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import br.com.brokenbits.martelada.engine.ResourceLocale;

public class PropertyValueTableModel implements TableModel {

	public static class Property {
		private final ResourceLocale locale;
		private String value;
		
		public Property(ResourceLocale locale, String value) {
			this.locale = locale;
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}

		public ResourceLocale getLocale() {
			return locale;
		}
	}
	
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	private List<Property> entries = new ArrayList<PropertyValueTableModel.Property>();
	
	private static final Class<?> [] COLUMN_TYPE = {ResourceLocale.class, String.class};	

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return COLUMN_TYPE[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "Locale";
		} else {
			return "Value";
		}
	}

	@Override
	public int getRowCount() {
		return this.entries.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Property p = this.entries.get(rowIndex);
		if (columnIndex == 0) {
			return p.locale;
		} else {
			return p.value;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex != 0);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		if (columnIndex == 1) {
			Property p = this.entries.get(rowIndex);
			p.value = (String)aValue;
			notifyChange(new TableModelEvent(this, rowIndex));
		}
	}
	
	public void add(ResourceLocale locale, String value) {
		this.add(new Property(locale, value));
	}
	
	public void add(Property prop) {
		this.entries.add(prop);
		notifyChange(new TableModelEvent(this));
	}
	
	public void clear() {
		this.entries.clear();
		notifyChange(new TableModelEvent(this));
	}
	
	public Property get(int idx) {
		return this.entries.get(idx);
	}
	
	private void notifyChange(TableModelEvent e) {
		for (TableModelListener l: this.listeners) {
			l.tableChanged(e);
		}
	}
}
