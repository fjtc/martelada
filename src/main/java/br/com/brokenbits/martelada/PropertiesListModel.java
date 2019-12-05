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

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import br.com.brokenbits.martelada.engine.PropertiesEditor;
import br.com.brokenbits.martelada.engine.PropertiesEditorEvent;
import br.com.brokenbits.martelada.engine.PropertiesEditorListener;

public class PropertiesListModel implements ListModel<String> {
	
	private List<ListDataListener> listDataListenerList = new ArrayList<ListDataListener>();
	
	private List<String> keyList = new ArrayList<String>();
	
	private final PropertiesEditor engine;
	
	private PropertyListFilter filter;
	
	public PropertiesListModel(PropertiesEditor engine) {
		this.engine = engine;
		this.engine.addListener(new PropertiesEditorListener() {

			@Override
			public void onPropertiesEditorEvent(PropertiesEditor source, PropertiesEditorEvent e) {
				switch (e.getType()) {
				case RELOAD:
				case PROPERTY_ADDED:
				case PROPERTY_REMOVED:
					reload();
					break;
				default:
				}
				
			}	
		});
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		this.listDataListenerList.add(listener);

	}

	@Override
	public String getElementAt(int idx) {
		return keyList.get(idx);
	}

	@Override
	public int getSize() {
		return keyList.size();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		this.listDataListenerList.remove(listener);
	}
	
	private boolean acceptKey(String key) {
		if (this.filter == null) {
			return true;
		} else {
			return this.filter.accept(key);
		}
	}
	
	public void reload() {
		int oldSize = keyList.size();
		keyList.clear();
		for (String key: this.engine.getKeys()) {
			if (acceptKey(key)) {
				keyList.add(key);
			}
		}
		Collections.sort(keyList);
		this.notifyChange(new ListDataEvent(this, 
				ListDataEvent.INTERVAL_REMOVED, 
						0, Math.max(keyList.size(), oldSize)));
	}
	
	private void notifyChange(ListDataEvent e) {
		for (ListDataListener l: this.listDataListenerList) {
			l.contentsChanged(e);
		}
	}

	public PropertyListFilter getFilter() {
		return filter;
	}

	public void setFilter(PropertyListFilter filter) {
		this.filter = filter;
		this.reload();
	}
}

