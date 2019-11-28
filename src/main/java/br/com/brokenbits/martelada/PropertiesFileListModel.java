package br.com.brokenbits.martelada;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import br.com.brokenbits.martelada.engine.PropertiesEditor;
import br.com.brokenbits.martelada.engine.PropertiesEditorListener;
import br.com.brokenbits.martelada.engine.ResourceLocale;

public class PropertiesFileListModel implements ListModel<String> {
	
	private List<ListDataListener> listDataListenerList = new ArrayList<ListDataListener>();
	
	private final PropertiesEditor engine;
	
	public PropertiesFileListModel(PropertiesEditor engine) {
		this.engine = engine;
		this.engine.addListener(new PropertiesEditorListener() {
			@Override
			public void propertyListChanged(PropertiesEditor source) {
				notifyChanges();	
			}

			@Override
			public void propertyChanged(PropertiesEditor source, ResourceLocale locale, String key) {
			}
			
			@Override
			public void selectedChanged(PropertiesEditor source, String selected) {
			}
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
