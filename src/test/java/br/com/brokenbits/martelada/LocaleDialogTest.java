package br.com.brokenbits.martelada;

import javax.swing.JFrame;

public class LocaleDialogTest {

	public static void main(String[] args) {

		LocaleDialog d = new LocaleDialog();
		d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		d.setModal(true);
		d.setVisible(true);
		System.out.println(d.getLocale());
		System.exit(0);
	}
}
