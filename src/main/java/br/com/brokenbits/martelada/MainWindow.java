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

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.brokenbits.martelada.engine.BaseResourceFile;
import br.com.brokenbits.martelada.engine.PropertiesEditor;

public class MainWindow extends JFrame {
	
	private static final String RECENT_FILE_ID_PROPERTY = "br.com.brokenbits.martelada.MainWindow.recentFileId";

	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	
	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(MainWindow.class.getName());

	private static final long serialVersionUID = 1L;
	
	private PropertiesEditor propertyEditor = new PropertiesEditor();
	
	private JMenu recentFilesMenu;
	
	private ActionListener recentFilesMenuActionListener;
	
	public MainWindow() {
		super();
		buildUI();

		this.doNewFile();
	}

	private void buildUI() {
		
		this.setSize(600, 480);
		this.getContentPane().setLayout(new BorderLayout());
		
		this.setDropTarget(new DropTarget() {

			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void dragEnter(DropTargetDragEvent dtde) {
				
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
				} else {
					dtde.rejectDrag();
				}
			}
			
			@Override
			public synchronized void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
	            Transferable t = dtde.getTransferable();
                try {
                    Object object = t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (object instanceof List) {
                    	List<?> files = (List<?>)object;
                    	loadFile((File)files.get(0));
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                }
			}
		});
		
		this.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) {
			}
			
			public void windowIconified(WindowEvent e) {
			}
			
			public void windowDeiconified(WindowEvent e) {
			}
			
			public void windowDeactivated(WindowEvent e) {
			}
			
			public void windowClosing(WindowEvent e) {
				doExit();
			}
			
			public void windowClosed(WindowEvent e) {
			}
			
			public void windowActivated(WindowEvent e) {
			}
		});

		PropertyListPanel propertyListPanel = new PropertyListPanel(this.propertyEditor);
		
		PropertyValuePanel propertyValuePanel = new PropertyValuePanel(this.propertyEditor); 
				
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, propertyListPanel, propertyValuePanel);
		splitPane.setDividerLocation(200);
		this.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		// Menu
		JMenuBar menuBar = new JMenuBar();
		
		// File menu
		JMenu fileMenu = new JMenu(RESOURCES.getString("fileMenu.title"));
		menuBar.add(fileMenu);
		
		JMenuItem newMenuItem = new JMenuItem(RESOURCES.getString("fileMenuItem.new"));
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
				InputEvent.SHIFT_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doNewFile();
			}
		});
		fileMenu.add(newMenuItem);
		
		JMenuItem openMenuItem = new JMenuItem(RESOURCES.getString("fileMenuItem.open"));
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLoadFile();
			}
		});
		fileMenu.add(openMenuItem);
		
		recentFilesMenu = new JMenu(RESOURCES.getString("fileMenuItem.openRecent"));
		fileMenu.add(recentFilesMenu);
		recentFilesMenuActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer id = (Integer)((JComponent)e.getSource()).getClientProperty(RECENT_FILE_ID_PROPERTY);
				loadRecent(id);
			}
		};

		JMenuItem saveMenuItem = new JMenuItem(RESOURCES.getString("fileMenuItem.save"));
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		fileMenu.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem(RESOURCES.getString("fileMenuItem.saveAs"));
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSaveAs();
			}
		});
		fileMenu.add(saveAsMenuItem);
		
		fileMenu.addSeparator();
		JMenuItem exitMenuItem = new JMenuItem(RESOURCES.getString("fileMenuItem.exit"));
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		});
		fileMenu.add(exitMenuItem);
		
		// File menu
		JMenu editMenu = new JMenu(RESOURCES.getString("editMenu.title"));
		menuBar.add(editMenu);
		updateRecentFiles();
		
		JMenuItem copyKeyMenuItem = new JMenuItem(RESOURCES.getString("editMenuItem.copyKey"));
		copyKeyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		copyKeyMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopyKey();
			}
		});
		editMenu.add(copyKeyMenuItem);
		
		JMenuItem copyKeyWithPatternMenuItem = new JMenuItem(RESOURCES.getString("editMenuItem.copyKeyWithPattern"));
		copyKeyWithPatternMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 
				InputEvent.CTRL_DOWN_MASK |InputEvent.ALT_DOWN_MASK));
		copyKeyWithPatternMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopyKeyWithPattern();
			}
		});
		editMenu.add(copyKeyWithPatternMenuItem);
		
		editMenu.addSeparator();
		JMenuItem addNewLocaleMenuItem = new JMenuItem(RESOURCES.getString("editMenuItem.addLocale"));
		addNewLocaleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddNewLocale();
			}
		});
		editMenu.add(addNewLocaleMenuItem);				

		editMenu.addSeparator();
		JMenuItem preferencesMenuItem = new JMenuItem(RESOURCES.getString("editMenuItem.preferences"));
		preferencesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPreferences();
			}
		});
		editMenu.add(preferencesMenuItem);
		
		this.setJMenuBar(menuBar);
	}
	
	private void doLoadFile() {
		JFileChooser fileChooser = createFileChooser();
		fileChooser.setFileFilter(new PropertiesFileFilter());
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.loadFile(fileChooser.getSelectedFile());
		}
	}
	
	private void doNewFile() {
		this.propertyEditor.newFile();
		this.updateTitle();
	}
	
	private void loadFile(File file) {
	
		try {
			propertyEditor.load(file);
			this.updateTitle();
			AppPreferences.getPreferences().addRecent(file);
			updateRecentFiles();
		} catch (IOException e) {
			logger.error("Unable to load the file {}.", file.getAbsolutePath(), e);
			JOptionPane.showMessageDialog(this, 
					RESOURCES.getString("saveResultDialog.title"), 
					String.format(""), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void saveFile(File file) {
		
		try {
			propertyEditor.save(file);
			this.updateTitle();
			AppPreferences.getPreferences().addRecent(file);
			updateRecentFiles();
		} catch (IOException e) {
			logger.error("Unable to save the file.", e);
			JOptionPane.showMessageDialog(this, 
					RESOURCES.getString("saveResultDialog.title"), 
					String.format(""), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void saveFile() {
		try {
			this.propertyEditor.save();
		} catch (IOException e) {
			logger.error("Unable to save the file.", e);
			JOptionPane.showMessageDialog(this, this.getTitle(), 
					String.format(RESOURCES.getString("messages.unableToSave")), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JFileChooser createFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		if (AppPreferences.getPreferences().getLastDirectory() != null) {
			fileChooser.setCurrentDirectory(AppPreferences.getPreferences().getLastDirectory());
		}	
		return fileChooser;
	}
	
	private void doSave() {
		if (this.propertyEditor.isSavePossible()) {
			saveFile();
		} else {
			doSaveAs();
		}
	}
	
	private void doSaveAs() {
		JFileChooser fileChooser = createFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			saveFile(fileChooser.getSelectedFile());
		}
	}
	
	private void doCopyKey() {
		String key = this.propertyEditor.getSelectedKey();

		this.getToolkit().getSystemClipboard().setContents(new StringSelection(key), null);
	}
	
	private void doCopyKeyWithPattern() {
		String key = this.propertyEditor.getSelectedKey();

		if (AppPreferences.getPreferences().getCopyPattern() != null) {
			key = AppPreferences.getPreferences().getCopyPattern().format(key);
		}
		this.getToolkit().getSystemClipboard().setContents(new StringSelection(key), null);
	}
	
	private void doExit() {
		AppPreferences.getPreferences().save();
		System.exit(0);
	}
	
	private void updateTitle() {
		String titlePattern = RESOURCES.getString("mainWindow.title");
		BaseResourceFile f = this.propertyEditor.getBaseResourceFile();
		String fileName;
		if (f != null) {
			fileName = f.getBaseFile().getAbsolutePath();
		} else {
			fileName = RESOURCES.getString("mainWindow.title.noFile");
		}
		this.setTitle(String.format(titlePattern, fileName));
	}
	
	private void doPreferences() {
		PreferencesDialog preferencesDialog = new PreferencesDialog();
		preferencesDialog.setModal(true);
		preferencesDialog.setVisible(true);
	}
	
	private void loadRecent(int i) {
		if (i < AppPreferences.getPreferences().getRecentFiles().size()) {
			this.loadFile(AppPreferences.getPreferences().getRecentFiles().get(i));
		}
	}
	
	private void updateRecentFiles() {
		
		String recentPattern = RESOURCES.getString("fileMenuItem.openRecent.itemPattern");
		this.recentFilesMenu.removeAll();
		int i = 0;
		for (File f: AppPreferences.getPreferences().getRecentFiles()) {
			JMenuItem menuItem = new JMenuItem(String.format(recentPattern,
					i + 1, f.getAbsolutePath()));
			menuItem.putClientProperty(RECENT_FILE_ID_PROPERTY, i);
			menuItem.addActionListener(recentFilesMenuActionListener);
			this.recentFilesMenu.add(menuItem);
			i++;
		}
	}
	
	private void doAddNewLocale() {
		LocaleDialog dialog = new LocaleDialog();
		dialog.setModal(true);
		dialog.setVisible(true);
		Locale l = dialog.getLocale();
		if (l != null) {
			if (this.propertyEditor.addLocale(l)) {
				JOptionPane.showMessageDialog(this, 
						RESOURCES.getString("saveResultDialog.title"), 
						this.getTitle(), JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, 
						RESOURCES.getString("saveResultDialog.title"), 
						this.getTitle(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
