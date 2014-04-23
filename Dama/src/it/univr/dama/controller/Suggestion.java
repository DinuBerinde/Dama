package it.univr.dama.controller;

import it.univr.dama.model.Board;

/**
 * Una classe che mette un suggerimento dove si può fare una mangiata/mossa legale
 * @author dinu
 *
 */
public class Suggestion {
	private final Board board;
	private final boolean legal;
	private boolean move = false;

	private final static int empty = 1;
	private final static int greenCell = 5;


	/**
	 * Costruttore
	 * @param board la scacchiera
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 */
	public Suggestion(Board board, int xFrom, int yFrom){

		this.board = board;
		this.legal = chooseSuggestion(xFrom, yFrom);


	}
	
	/**
	 * Ritorniamo legal un boolean
	 * @return legal, il flag che ci dice se è un suggerimento legale
	 */
	public boolean isLegal() {
		return legal;
	}

	

	/** Scegliamo il suggerimento giusto ( tra una mangiata e una mossa)
	 * 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @return move, il flag che ci dice se abbiamo messo qualche suggerimento 
	 * 			
	 */
	private boolean chooseSuggestion(int xFrom, int yFrom){
		// la pos iniziale della pedina o dama
		board.setYFrom(yFrom);		
		board.setXFrom(xFrom);

		// dama
		if( board.isRedKingAt(xFrom, yFrom) ){

			if( isLegalJumpForKingAt(xFrom,yFrom) )
				kingMakesJumpAt(xFrom,yFrom);
			else
				addSuggestionForKingMove(xFrom,yFrom);

		}else{

			if( isLegalJumpForPawnAt(xFrom,yFrom) )
				pawnMakesJumpAt(xFrom, yFrom);	
			else
				addSuggestionForPawnMove(xFrom, yFrom);
		}

		
		return move;	

	}

	/** Vediamo se la dama può fare una mangiata a Sinstra o a Destra ( in basso o in alto )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può fare una mnagiata a Destra o a Sinistra ( in bassoo in alto)
	 * 		   false altrimenti
	 */
	private boolean isLegalJumpForKingAt(int xFrom, int yFrom){

		if(  canJumpUpAtRight(xFrom, yFrom, 'k') )
			return true;

		if( canJumpUpAtLeft(xFrom, yFrom, 'k') )
			return true;

		if(  canJumpDownAtRight(xFrom,yFrom) )
			return true;

		if( canJumpDownAtLeft(xFrom,yFrom) )
			return true;


		return false;
	}



	/** Aggiungiamo un suggerimento dove si può per la mangiata della dama
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos
	 */
	private void kingMakesJumpAt(int xFrom, int yFrom){

		if(  canJumpUpAtRight(xFrom, yFrom, 'k') )
			addSuggestionForJumpUpAtRight(xFrom, yFrom);

		if( canJumpUpAtLeft(xFrom, yFrom, 'k') )
			addSuggestionForJumpUpAtLeft(xFrom, yFrom);

		if(  canJumpDownAtRight(xFrom,yFrom) )
			addSuggestionForJumpDownAtRight(xFrom, yFrom);

		if( canJumpDownAtLeft(xFrom,yFrom) )
			addSuggestionForJumpDownAtLeft(xFrom, yFrom);

	}


	/** Vediamo se la pedina può fare una mangiata a Sinstra o a Destra ( in alto )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può fare una mangiata a Destra o a Sinistra ( in alto )
	 * 		   false altrimenti
	 */
	private boolean isLegalJumpForPawnAt(int xFrom, int yFrom){

		if( canJumpUpAtRight(xFrom, yFrom, 'p') )
			return true;

		if( canJumpUpAtLeft(xFrom, yFrom, 'p') )
			return true;

		return false; 
	}


	/** Aggiungiamo un suggerimento dove si può per la mangiata della pedina
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos
	 */
	private void pawnMakesJumpAt(int xFrom, int yFrom){

		if(  canJumpUpAtRight(xFrom, yFrom, 'p') )
			addSuggestionForJumpUpAtRight(xFrom, yFrom);

		if( canJumpUpAtLeft(xFrom, yFrom, 'p') )
			addSuggestionForJumpUpAtLeft(xFrom, yFrom);

	}

	/** Aggiungiamo un suggerimento
	 *  
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pox y iniziale
	 */
	private void putSuggestion(int xFrom, int yFrom) {
		board.setPawnAt(xFrom, yFrom, greenCell);
		move = true;
	}


	/**Aggiungiamo un suggerimento per la mangiata della pedina o dama ( in alto ) a Destra
	 * 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @return void
	 */
	private void addSuggestionForJumpUpAtRight(int xFrom, int yFrom){
		putSuggestion(xFrom-2,yFrom+2);
	}


	/**Aggiungiamo un suggerimento per la mangiata della pedina o dama ( in alto ) a Sinistra
	 * 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @return void
	 */
	private void addSuggestionForJumpUpAtLeft(int xFrom, int yFrom){
		putSuggestion(xFrom-2,yFrom-2);
	}

	/**Aggiungiamo un suggerimento per la mossa della pedina
	 * 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @return void
	 */
	private void addSuggestionForPawnMove(int xFrom, int yFrom){

		putEmptyWhereIsGreen();

		// si puo aggiungere un sugerimento a Sx 
		if( board.inLimitColumn(yFrom-1) && board.inLimitRow(xFrom-1) ){
			if( !board.occupiedAt(xFrom-1, yFrom-1) )	
				putSuggestion(xFrom-1, yFrom-1);
		}

		// si puo aggiungere un suggerimento a Dx 
		if( board.inLimitColumn(yFrom+1) && board.inLimitRow(xFrom-1) ){
			if( !board.occupiedAt(xFrom-1, yFrom+1) )
				putSuggestion(xFrom-1, yFrom+1);
		}	
	}


	/** Aggiungiamo suggerimenti pe la mossa della dama
	 *  
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @return void
	 */		  	
	private void addSuggestionForKingMove(int xFrom, int yFrom) {

		putEmptyWhereIsGreen();

		// up at Lx
		if( board.inLimitRow(xFrom-1) && board.inLimitColumn(yFrom-1) ){
			if( !board.occupiedAt(xFrom-1, yFrom-1) ) 
				putSuggestion(xFrom-1, yFrom-1); 
		}

		// up at Rx
		if( board.inLimitRow(xFrom-1) && board.inLimitColumn(yFrom+1) ){
			if( !board.occupiedAt(xFrom-1, yFrom+1) ) 
				putSuggestion(xFrom-1, yFrom+1); 
		}

		// down at Lx
		if( board.inLimitRow(xFrom+1) && board.inLimitColumn(yFrom-1) ){
			if( !board.occupiedAt(xFrom+1, yFrom-1) ) 
				putSuggestion(xFrom+1, yFrom-1); 
		}

		// up at Rx
		if( board.inLimitRow(xFrom+1) && board.inLimitColumn(yFrom+1) ){
			if( !board.occupiedAt(xFrom+1, yFrom+1) ) 
				putSuggestion(xFrom+1, yFrom+1); 
		}

	}

	/**Vediamo se la dama o la pedina possono fare una mangiata ( in alto )
	 * 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @param turn che specifica di chi è il turno ( dama o pedina )
	 * @return true se riusciamo
	 * 		   false altrimenti  	
	 */
	private boolean canJumpUpAtLeft(int xFrom, int yFrom, char turn){

		// Lx //pedina
		if( turn == 'p'){  
			if( board.inLimitRow(xFrom-2)  &&  board.inLimitColumn(yFrom-2) ){
				if (board.getPawnMultiple()){

					if ( (board.isBlackPawnAt(xFrom-1, yFrom-1) || board.isBlackKingAt(xFrom-1, yFrom-1))
							&& board.isEmptyCellAt(xFrom-2, yFrom-2) )	
						return true;

				}else if( board.isBlackPawnAt(xFrom-1, yFrom-1) && board.isEmptyCellAt(xFrom-2, yFrom-2)  )	
					return true;

			}

		}

		// Lx //dama
		else if( turn == 'k'){
			if( board.inLimitRow(xFrom-2)  &&  board.inLimitColumn(yFrom-2) ){
				if(  (board.isBlackPawnAt(xFrom-1, yFrom-1) || board.isBlackKingAt(xFrom-1, yFrom-1) ) && board.isEmptyCellAt(xFrom-2, yFrom-2)  )		
					return true;
			}
		}

		return false;

	}


	/**Vediamo se la dama o la pedina possono fare una mangiata ( in alto )
	 * 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @param turn che specifica di chi è il turno ( dama o pedina )
	 * @return true se riusciamo
	 * 		   false altrimenti  	
	 */
	private boolean canJumpUpAtRight(int xFrom, int yFrom, char turn){

		// Rx //pedina
		if( turn == 'p'){
			if( board.inLimitRow(xFrom-2) &&  board.inLimitColumn(yFrom+2) ){
				if (board.getPawnMultiple()){

					if ( (board.isBlackPawnAt(xFrom-1, yFrom+1) || board.isBlackKingAt(xFrom-1, yFrom+1)) 
							&& board.isEmptyCellAt(xFrom-2, yFrom+2) )
						return true;

				}else if( board.isBlackPawnAt(xFrom-1, yFrom+1) && board.isEmptyCellAt(xFrom-2, yFrom+2) )
					return true;
			}
		}

		// Rx // dama
		else if( turn == 'k'){
			if( board.inLimitRow(xFrom-2) &&  board.inLimitColumn(yFrom+2) ){
				if( ( board.isBlackPawnAt(xFrom-1, yFrom+1) || board.isBlackKingAt(xFrom-1, yFrom+1) ) && board.isEmptyCellAt(xFrom-2, yFrom+2) )			
					return true;
			}
		}

		return false;

	}


	/**Aggiungiamo un suggerimento per la mangiata della dama ( in basso )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se riesce
	 * 		   false altrimenti	
	 */
	private void addSuggestionForJumpDownAtRight(int xFrom, int yFrom) {
		putSuggestion(xFrom+2,yFrom+2);
	}


	/**Aggiungiamo un suggerimento per la mangiata della dama ( in basso )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se riesce
	 * 		   false altrimenti	
	 */
	private void addSuggestionForJumpDownAtLeft(int xFrom, int yFrom) {
		putSuggestion(xFrom+2,yFrom-2);
	}


	/**Vediamo se la dama può fare una mangiata ( in basso )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se riesce
	 * 		   false altrimenti  	
	 */
	private boolean canJumpDownAtLeft(int xFrom, int yFrom) {

		// Lx
		if( board.inLimitRow(xFrom+2) && board.inLimitColumn(yFrom-2) ){
			if( ( board.isBlackPawnAt(xFrom+1, yFrom-1) || board.isBlackKingAt(xFrom+1, yFrom-1) ) && board.isEmptyCellAt(xFrom+2, yFrom-2)  )
				return true;
		}

		return false;

	}

	/**Vediamo se la dama può fare una mangiata ( in basso )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se riesce
	 * 		   false altrimenti  	
	 */
	private boolean canJumpDownAtRight(int xFrom, int yFrom) {

		// Rx
		if( board.inLimitRow(xFrom+2) && board.inLimitColumn(yFrom+2) ){
			if( (board.isBlackPawnAt(xFrom+1, yFrom+1) || board.isBlackKingAt(xFrom+1, yFrom+1) ) && board.isEmptyCellAt(xFrom+2, yFrom+2) )
				return true;
		}

		return false;

	}


	/** Vediamo se c'è già un suggerimeto da qualche parte
	 *	se c'e, la togliamo per far spazio a quello nuovo
	 * 
	 * @return
	 */
	private void putEmptyWhereIsGreen(){

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if( board.isGreenCellAt(i,j) )

					board.setPawnAt(i, j, empty);


			}
		}
	}


}