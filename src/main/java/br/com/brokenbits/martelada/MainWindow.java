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
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.brokenbits.martelada.engine.PropertiesEditor;

public class MainWindow extends JFrame {
	
	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	
	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(MainWindow.class.getName());

	private static final long serialVersionUID = 1L;
	
	private PropertiesEditor propertyEditor = new PropertiesEditor();
	
	public MainWindow() {
		super(RESOURCES.getString("application.title"));
		buildUI();
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
		JScrollPane selectScrollPane = new JScrollPane(propertyListPanel);
		
		PropertyValuePanel propertyValuePanel = new PropertyValuePanel(this.propertyEditor); 
				
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, selectScrollPane, propertyValuePanel);
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
		
		JMenuItem copyKeyMenuItem = new JMenuItem(RESOURCES.getString("editMenuItem.copyKey"));
		copyKeyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		copyKeyMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopyKey();
			}
		});
		editMenu.add(copyKeyMenuItem);

		
		this.setJMenuBar(menuBar);
	}
	
	private void doLoadFile() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.loadFile(fileChooser.getSelectedFile());
		}
	}
	
	private void doNewFile() {
		this.propertyEditor.newFile();
	}
	
	private void loadFile(File file) {
	
		try {
			propertyEditor.load(file);
			System.out.println(file.getAbsolutePath());
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
			System.out.println(file.getAbsolutePath());
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
					String.format("Unable to save the file."), JOptionPane.ERROR_MESSAGE);
		}
	}
		
	
	private void doSave() {
		if (this.propertyEditor.isSavePossible()) {
			saveFile();
		} else {
			doSaveAs();
		}
	}
	
	private void doSaveAs() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			saveFile(fileChooser.getSelectedFile());
		}
	}
	
	private void doCopyKey() {
		String key = this.propertyEditor.getSelectedKey();

		this.getToolkit().getSystemClipboard().setContents(new StringSelection(key), null);
	}
	
	private void doExit() {
		// TODO Perform some checks later
		System.exit(0);
	}
}
