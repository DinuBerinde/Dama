package it.univr.dama.controller;

import it.univr.dama.model.Board;

/**
 * Facciamo una mossa o una mangiata legale con l'utente
 * @author dinu
 *
 */
public class PlayerMove {
	private final Board board;
	private final boolean legal;

	private final static int emptyCell = 1;
	private final static int redPawn = 3;
	private final static int redKing = 7;


	/**
	 * Costruttore
	 * @param board la scacchiera
	 * @param xTo la pos x dove vogliamo andare
	 * @param yTo la pos y dove vogliamo andare
	 */
	public PlayerMove(Board board, int xTo, int yTo) {
		this.board = board;
		this.legal = makeMoveAt(xTo, yTo);

	}

	/**
	 * Ritorniamo legal che ci dice se facciamo una mossa/mangiata legale
	 * @return true se possiamo fare una mossa/mangiata
	 * 			false altrimenti
	 */
	public boolean isLegal() {
		return legal;
	}


	/** Facciamo una mossa a Destra o a Sinistra oppure mangiamo a Destra o a Sinistra
	 *  con la dama o la pedina
	 *  
	 * @param xTo la posizione x 
	 * @param yTo la posizione y
	 * @return true se riusciamo
	 * 		   false altrimenti
	 */
	private boolean makeMoveAt(int xTo, int yTo) {
		// la posizione iniziale della pedina o dama
		int yFrom = board.getYFrom();   
		int xFrom = board.getXFrom();

		// gestico la dama ( che si muove/mangia in alto e in basso )
		if( board.isRedKingAt(xFrom, yFrom) ){ 

			if( board.isGreenCellAt(xTo,yTo) && yTo+2 == yFrom && xTo+2 == xFrom )
				return makeLeftJumpUp(xTo,yTo,redKing);

			else if( board.isGreenCellAt(xTo,yTo) && yTo-2 == yFrom && xTo+2 == xFrom)
				return makeRightJumpUp(xTo,yTo,redKing);

			else if( board.isGreenCellAt(xTo,yTo) && yTo+2 == yFrom && xTo-2 == xFrom)
				return makeLeftJumpDown(xTo,yTo);

			else if( board.isGreenCellAt(xTo,yTo) && yTo-2 == yFrom && xTo-2 == xFrom)
				return makeRightJumpDown(xTo,yTo);

			else if( board.isGreenCellAt(xTo,yTo) && yTo+1 == yFrom && xTo+1 == xFrom )			
				return makeLeftMoveUp(xTo,yTo,redKing);

			else if( board.isGreenCellAt(xTo,yTo) &&  yTo-1 == yFrom && xTo+1 == xFrom )
				return makeRightMoveUp(xTo,yTo,redKing);

			else if( board.isGreenCellAt(xTo,yTo) &&  yTo+1 == yFrom && xTo-1 == xFrom)
				return makeLeftMoveDown(xTo,yTo);

			else if( board.isGreenCellAt(xTo,yTo) &&  yTo-1 == yFrom && xTo-1 == xFrom)
				return makeRightMoveDown(xTo,yTo);


		}


		// gestisco la pedina  (che si muove/mangia in alto )
		if( board.isGreenCellAt(xTo,yTo) && yTo+2 == yFrom )
			return makeLeftJumpUp(xTo,yTo,redPawn);

		if( board.isGreenCellAt(xTo,yTo) && yTo-2 == yFrom )
			return makeRightJumpUp(xTo,yTo,redPawn);

		if( board.isGreenCellAt(xTo,yTo) && yTo+1 == yFrom ){			
			trySoffio();
			return makeLeftMoveUp(xTo,yTo,redPawn);
		}

		if( board.isGreenCellAt(xTo,yTo) &&  yTo-1 == yFrom ){
			trySoffio();
			return makeRightMoveUp(xTo,yTo,redPawn);
		}


		// non si può fare una mossa o una mangiata
		return false;
	}


	/** Facciamo una mossa a Sinistra in alto 		
	 * 
	 * @param xTo la pos x 
	 * @param yTo la pos y
	 * @param turn specifica di chi è il turno: dama o pedina
	 * @return true se riusciamo
	 * 			
	 */
	private boolean makeLeftMoveUp(int xTo, int yTo, int turn) {

		// se x è uguale a 0, la pedina diventa dama
		if( xTo == 0){
			board.setPawnAt(xTo+1, yTo+1, emptyCell);
			board.setPawnAt(xTo, yTo, redKing); 

			// altrimenti non cambia	
		}else{
			board.setPawnAt(xTo+1, yTo+1, emptyCell);    // la posizione iniziale la mettiamo vuota 
			board.setPawnAt(xTo, yTo, turn);  			 // la pos corrente viene occupata dalla pedina
		}

		putEmptyWhereIsGreen();	

		return true;

	}



	/** Facciamo una mossa a Destra in alto		
	 *
	 * @param turn specifica di chi è il turno: dama o pedina
	 * @param xTo la pos x 
	 * @param yTo la pos y
	 * @return true se riusciamo
	 * 			
	 */
	private boolean makeRightMoveUp(int xTo, int yTo, int turn) {

		// se x è uguale a 0, la pedina diventa dama
		if( xTo == 0){
			board.setPawnAt(xTo+1, yTo-1, emptyCell);
			board.setPawnAt(xTo, yTo, redKing); 

			// altrimenti non cambia	
		}else{
			board.setPawnAt(xTo+1, yTo-1, emptyCell);    // la posizione iniziale la mettiamo vuota 
			board.setPawnAt(xTo, yTo, turn);   			// la pos corrente viene occupata dalla pedina
		}

		putEmptyWhereIsGreen();

		return true;
	}


	/** Facciamo un salto a Sinistra, cioè mangiamo la pedina del computer
	 * 
	 * @param turn specifica di chi è il turno: dama o pedina
	 * @param xTo la pos x
	 * @param yTo la pos y
	 * @return true se riusciamo
	 *  	  
	 * */ 
	private boolean makeLeftJumpUp(int xTo, int yTo, int turn) {

		// vediamo se possiamo mangiare ancora, se si, allora mettiamo il stato del computer 
		// a false
		if( xTo != 0){
			if( canJumpUpAgain(xTo,yTo) ){
				board.setComputerStatus(false);
				board.setPawnMultiple(true);
			}
			else board.setPawnMultiple(false);
		}	


		// se x è uguale a 0, la pedina diventa dama
		if( xTo == 0){
			board.setPawnAt(xTo+1, yTo+1, emptyCell);
			board.setPawnAt(xTo+2, yTo+2, emptyCell);
			board.setPawnAt(xTo, yTo, redKing);

			// altrimenti non cambia
		}else{
			board.setPawnAt(xTo+1, yTo+1, emptyCell);
			board.setPawnAt(xTo+2, yTo+2, emptyCell);
			board.setPawnAt(xTo, yTo, turn);
		}


		putEmptyWhereIsGreen();

		return true;
	}


	/** Facciamo un salto a Destra, cioè mangiamo la pedina del computer
	 * 
	 * @param turn specifica di chi è il turno: dama o pedina
	 * @param xTo la pos x
	 * @param yTo la pos y
	 * @return true se riusciamo
	 *  	 
	 * */ 
	private boolean makeRightJumpUp(int xTo, int yTo, int turn) {

		// vediamo se possiamo mangiare ancora, se si, allora mettiamo il stato del computer 
		// a false
		if( xTo!= 0){
			if( canJumpUpAgain(xTo,yTo) ){
				board.setComputerStatus(false);
				board.setPawnMultiple(true);
			}
			else board.setPawnMultiple(false);
		}

		// se x è uguale a 0, la pedina diventa dama
		if( xTo == 0){
			board.setPawnAt(xTo+1, yTo-1, emptyCell);
			board.setPawnAt(xTo+2, yTo-2, emptyCell);
			board.setPawnAt(xTo, yTo, redKing);

			// altrimenti non cambia
		}else{
			board.setPawnAt(xTo+1, yTo-1, emptyCell);
			board.setPawnAt(xTo+2, yTo-2, emptyCell);
			board.setPawnAt(xTo, yTo, turn);
		}

		putEmptyWhereIsGreen();

		return true;
	}

	/** Facciamo un salto a Sinistra con la Dama, cioè mangiamo la pedina del computer( in basso )
	 * 
	 * @param xTo la pos x
	 * @param yTo la pos y
	 * @return true se riusciamo
	 *  	  
	 * */ 
	private boolean makeLeftJumpDown(int xTo, int yTo) {

		// vediamo se possiamo mangiare ancora, se si, allora mettiamo il stato del computer 
		// a false
		if( xTo != 0){
			if( canJumpUpAgain(xTo,yTo) )
				board.setComputerStatus(false);

			else if( canJumpDownAgain(xTo,yTo))
				board.setComputerStatus(false);
		}

		board.setPawnAt(xTo-1, yTo+1, emptyCell);
		board.setPawnAt(xTo-2, yTo+2, emptyCell);
		board.setPawnAt(xTo, yTo, redKing);

		putEmptyWhereIsGreen();

		return true;
	}

	/** Facciamo un salto a Destra con la Dama, cioè mangiamo la pedina del computer( in basso )
	 * 
	 * @param xTo la pos x
	 * @param yTo la pos y
	 * @return true se riusciamo
	 *  	 
	 * */ 
	private boolean makeRightJumpDown(int xTo, int yTo) {

		// vediamo se possiamo mangiare ancora, se si, allora mettiamo il stato del computer 
		// a false
		if( xTo != 0){
			if( canJumpUpAgain(xTo,yTo) )
				board.setComputerStatus(false);

			else if( canJumpDownAgain(xTo,yTo))
				board.setComputerStatus(false);
		}

		board.setPawnAt(xTo-1, yTo-1, emptyCell);
		board.setPawnAt(xTo-2, yTo-2, emptyCell);
		board.setPawnAt(xTo, yTo, redKing);

		putEmptyWhereIsGreen();

		return true;
	}



	/** Facciamo una mossa a Sinistra ( in basso ) con la Dama		
	 *
	 * @param xTo la pos x 
	 * @param yTo la pos y
	 * @return true se riusciamo
	 * 			
	 */
	private boolean makeLeftMoveDown(int xTo, int yTo) {

		board.setPawnAt(xTo-1, yTo+1, emptyCell);    // la posizione iniziale la mettiamo vuota 
		board.setPawnAt(xTo, yTo, redKing);  		 // la pos corrente viene occupata dalla pedina

		putEmptyWhereIsGreen();

		return true;
	}

	/** Facciamo una mossa a Destra ( in basso ) con la Dama		
	 *
	 * @param xTo la pos x 
	 * @param yTo la pos y
	 * @return true se riusciamo
	 * 			
	 */
	private boolean makeRightMoveDown(int xTo, int yTo) {

		board.setPawnAt(xTo-1, yTo-1, emptyCell);    // la posizione iniziale la mettiamo vuota 
		board.setPawnAt(xTo, yTo, redKing);   		 // la pos corrente viene occupata dalla pedina

		putEmptyWhereIsGreen();

		return true;
	}


	/** Vediamo se possiamo mangiare ancora in alto
	 * 
	 * @param xTo la pos x
	 * @param yTo la pos y
	 * @return true se possiamo mangiare ancora 
	 * 			false altrimenti
	 */
	private boolean canJumpUpAgain(int xTo, int yTo){

		// Rx
		if( board.inLimitRow(xTo-2) &&  board.inLimitColumn(yTo+2) ){
			if( !board.isBlackPawnAt(xTo, yTo) ){
				if( ( board.isBlackPawnAt(xTo-1, yTo+1) || board.isBlackKingAt(xTo-1, yTo+1) ) && board.isEmptyCellAt(xTo-2, yTo+2) )			
					return true;
			}
		}

		// Lx
		if( board.inLimitRow(xTo-2)  &&  board.inLimitColumn(yTo-2) ){
			if ( !board.isBlackPawnAt(xTo, yTo) ){
				if(  (board.isBlackPawnAt(xTo-1, yTo-1) || board.isBlackKingAt(xTo-1, yTo-1) ) && board.isEmptyCellAt(xTo-2, yTo-2)  )		
					return true;
			}
		}

		return false;
	}

	/** Vediamo se possiamo mangiare ancora in basso
	 * 
	 * @param xTo la pos x 
	 * @param yTo la pos y
	 * @return true se possiamo mangiare ancora 
	 * 			false altrimenti
	 */
	private boolean canJumpDownAgain(int xTo, int yTo){

		// Rx
		if( board.inLimitRow(xTo+2) &&  board.inLimitColumn(yTo+2) ){
			if( !board.isBlackPawnAt(xTo, yTo) ){
				if( ( board.isBlackPawnAt(xTo+1, yTo+1) || board.isBlackKingAt(xTo+1, yTo+1) ) && board.isEmptyCellAt(xTo+2, yTo+2) )			
					return true;
			}
		}

		// Lx
		if( board.inLimitRow(xTo+2)  &&  board.inLimitColumn(yTo-2) ){
			if ( !board.isBlackPawnAt(xTo, yTo) ){
				if( (board.isBlackPawnAt(xTo+1, yTo-1) || board.isBlackKingAt(xTo+1, yTo-1) ) && board.isEmptyCellAt(xTo+2, yTo-2)  )		
					return true;

			}
		}

		return false;
	}


	/**
	 * Priviamo a vedere se si può fare la soffiato. Se la possiamo fare, impostiamo un flag a true 
	 * @param xFrom la pos x inziale
	 * @param yFrom la pos y iniziale
	 */
	private void trySoffio(){

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isRedPawnAt(x, y) ){

					if( canJump(x,y) ){
						board.setSoffioOrigin(x, y);
						board.setSoffioFlag(true);
					}
				}
			}
		}

	}

	/**
	 * Vediamo se la pedina può mangiare
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può
	 * 			false altrimenti
	 */
	private boolean canJump(int xFrom, int yFrom) {

		// Rx
		if( board.inLimitRow(xFrom-2) && board.inLimitColumn(yFrom+2) ){
			if( board.isBlackPawnAt(xFrom-1, yFrom+1) && (board.isEmptyCellAt(xFrom-2, yFrom+2) || board.isGreenCellAt(xFrom-2, yFrom+2)) )			
				return true;
		}

		// Lx
		if( board.inLimitRow(xFrom-2) && board.inLimitColumn(yFrom-2) ){
			if( board.isBlackPawnAt(xFrom-1, yFrom-1) && (board.isEmptyCellAt(xFrom-2, yFrom-2) || board.isGreenCellAt(xFrom-2, yFrom-2))  )		
				return true;
		}

		return false;
	}


	/** Vediamo se c'è già un suggerimeto da qualche parte,
	 *	se c'e, la togliamo per far spazio a quello nuovo
	 * 
	 * @return void
	 */
	private void putEmptyWhereIsGreen(){

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if( board.isGreenCellAt(i,j) )

					board.setPawnAt(i, j, emptyCell);

			}
		}
	}
}