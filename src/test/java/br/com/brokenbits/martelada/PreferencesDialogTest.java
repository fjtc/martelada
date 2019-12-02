package br.com.brokenbits.martelada;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class PreferencesDialogTest {


	public static void main(String[] args) {

		PreferencesDialog d = new PreferencesDialog();
		d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		d.addWindowListener( new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		d.setVisible(true);
		
	}
}
