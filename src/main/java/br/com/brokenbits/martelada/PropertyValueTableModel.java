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
import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import br.com.brokenbits.martelada.engine.PropertiesEditor;
import br.com.brokenbits.martelada.engine.PropertiesEditorListener;
import br.com.brokenbits.martelada.engine.ResourceLocale;

public class PropertyValueTableModel implements TableModel {

	private PropertiesEditor editor;
	
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	private List<ResourceLocale> locales = new ArrayList<ResourceLocale>(); 
	
	public PropertyValueTableModel(PropertiesEditor editor) {
		this.editor = editor;
		
		this.editor.addListener(new PropertiesEditorListener() {
			@Override
			public void selectedChanged(PropertiesEditor source, String selected) {
				 reload();
			}
			
			@Override
			public void propertyListChanged(PropertiesEditor source) {
				reload();
			}
			
			@Override
			public void propertyChanged(PropertiesEditor source, ResourceLocale locale, String key) {
			}
		});		
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
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
		return this.locales.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ResourceLocale locale = this.locales.get(rowIndex);
		if (columnIndex == 0) {
			return locale.toString();
		} else {
			return this.editor.getSelectedValue(locale);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 1);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		if (columnIndex == 1) {
			ResourceLocale locale = this.locales.get(rowIndex);
			this.editor.setSelectedValue(locale, (String)aValue);
		}
	}
	
	private void notifyChange(TableModelEvent e) {
		for (TableModelListener l: this.listeners) {
			l.tableChanged(e);
		}
	}
	
	public void reload() {
		locales.clear();
		locales.addAll(this.editor.getLocales());
		Collections.sort(locales);
		this.notifyChange(new TableModelEvent(this));
	}
}
