package br.com.brokenbits.martelada;

import javax.swing.JFrame;

public class PreferencesDialogTest {


	public static void main(String[] args) {

		PreferencesDialog d = new PreferencesDialog();
		d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		d.setModal(true);
		d.setVisible(true);
		System.exit(0);		
	}
}
