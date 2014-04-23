package it.univr.dama.controller;

import it.univr.dama.model.Board;

/**
 * Vediamo se c'è un vincitore tra l'utente ed il computer
 * @author dinu
 *
 */
public class GameSolved {
	private final int isSolved;
	private final int black = 2;
	private final int red = 3;
	private final int notSolved = 1;
	
	/**
	 * Costruttore
	 * @param board
	 */
	public GameSolved(Board board) {

		this.isSolved = checkSolved(board); 

	}

	/**
	 * Vediamo chi a vinto
	 * @return un intero che rappresenta il vincitore
	 */
	public int isSolved() {
		return isSolved;
	}

	/**Vediamo chi ha vinto tra computer e l'utente
	 * Contiamo quante pedine nere(computer) e rosse(utente) ci sono sulla scacchiera
	 * 
	 * @param board la scacchiera
	 * @return black se ha vinto il computer
	 *   	   red se ha vinto l'utente
	 * 		   1 se il gioco non è ancora risolto
	 */
	private int checkSolved(Board board) {
		int countBlack = 0;	
		int countRed = 0;

		
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){
				if( board.isBlackPawnAt(x, y) || board.isBlackKingAt(x, y) )

					countBlack++;
			}
		}

		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){
				if( board.isRedPawnAt(x, y) || board.isRedKingAt(x, y)  )
			
					countRed++;
			}
		}

		if( countBlack > 0 && countRed == 0 )
			return black;
		
		else if( countRed > 0 && countBlack == 0)
			return red;
		
		else
			return notSolved;
	}

	
}