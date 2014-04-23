package it.univr.dama.view;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Costruzione di alcuni elementi della finestra
 * @author dinu
 *
 */
public class Elements extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private final static ImageIcon pedinaNera = new ImageIcon("images/black.png");
	private final static ImageIcon pedinaRossa = new ImageIcon("images/red.png");
	private final static java.awt.Font font = new java.awt.Font ( "URW Chancery L" , Font.ITALIC, 22 ) ;
	private final JPanel panel = new JPanel();	
	

	// costruiamo i nomi
	public JPanel createTitle(){
		panel.setBackground(new Color(0,110,0));
		panel.add( addIcon(pedinaNera) );
		panel.add( addText("Computer") );
		panel.add( addText("vs") );
		panel.add( addText("Player") );
		panel.add( addIcon(pedinaRossa) );

		return panel;

	}

	
	// aggiungiamo il text
	private JLabel addText(String string){
		JLabel text = new JLabel(string);
		text.setFont(font);
		text.setForeground(Color.white);

		return text;	

	}

	// aggiungiamo l'icona
	private JLabel addIcon(ImageIcon icon){
		JLabel img = new JLabel();

		img.setIcon(icon);	

		return img;	

	}


}