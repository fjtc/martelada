package br.com.brokenbits.martelada;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import br.com.brokenbits.martelada.engine.PropertiesFileEngine;
import br.com.brokenbits.martelada.engine.PropertiesFileEngineListener;

public class PropertiesFileListModel implements ListModel<String> {
	
	private List<ListDataListener> listDataListenerList = new ArrayList<ListDataListener>();
	
	private final PropertiesFileEngine engine;
	
	public PropertiesFileListModel(PropertiesFileEngine engine) {
		this.engine = engine;
		this.engine.addListener(new PropertiesFileEngineListener() {
			@Override
			public void propertyListChanged() {
				notifyChanges();	
			}

			@Override
			public void propertyChanged(Locale locale, String key) {}
			
			@Override
			public void selectedChanged(String selected) {}
		});
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		this.listDataListenerList.add(listener);

	}

	@Override
	public String getElementAt(int idx) {
		return engine.getKeys().get(idx);
	}

	@Override
	public int getSize() {
		return engine.getKeys().size();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		this.listDataListenerList.remove(listener);
	}
	
	private void notifyChanges() {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 
				0, this.engine.getKeys().size() - 1);
		for (ListDataListener l: this.listDataListenerList) {
			l.contentsChanged(e);
		}
	}
}
