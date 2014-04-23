package it.univr.dama.view;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *  Costruzione della cella 
 *  
 * @author dinu
 *
 */
public class Cell extends JButton{

	private static final long serialVersionUID = 1L;
	private final static ImageIcon pedinaNera = new ImageIcon("images/black.png");
	private final static ImageIcon pedinaRossa = new ImageIcon("images/red.png");
	private final static ImageIcon damaNera = new ImageIcon("images/blackking.png");
	private final static ImageIcon damaRossa = new ImageIcon("images/redking.png");
	

	/**
	 * Costruttore
	 * @param color il colore
	 * @param pawn la pedina
	 */
	public Cell(Color color, char pawn){
		
		setCell(color);
		setPedina(pawn);
		
	}

	/**
	 * Impostiamo le pedine
	 * @param pawn la pedina
	 */
	public void setPedina(char pawn){

		switch (pawn){
		case 'n':
			this.setIcon(pedinaNera);
			break;
		case 'r':
			this.setIcon(pedinaRossa);
			break;
		case 'N':
			this.setIcon(damaNera);
			break;
		case 'R':
			this.setIcon(damaRossa);
			break;
		case ' ':
			this.setIcon(null);
		}
	}

	/**
	 * Impostiamo il colore della cella
	 * @param color
	 */
	public void setCell(Color color){

		this.setBackground(color);

	}


	 	
}