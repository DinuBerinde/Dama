package it.univr.dama.controller;

import java.util.Random;

import it.univr.dama.model.Board;

/**
 * Facciamo una mossa o una mangiata legale con il computer
 * @author dinu
 *
 */
public class ComputerMove{
	private final Board board;
	private final boolean legal;
	private boolean multiple = false;

	private final int emptyCell = 1;
	private final int blackPawn = 2;
	private final int blackKing = 8;

	private int[] pawnLegalMoves;
	private int[] pawnLegalJumps;
	private int[] kingLegalMoves;
	private int[] kingLegalJumps;
	private int[] pawnBecomesKing;

	/**
	 * Costruttore
	 * @param board la scacchiera
	 */
	public ComputerMove(Board board){

		this.board = board;
		this.legal = computerMoves();


	}


	public boolean isLegal() {
		return legal;
	}


	/** Il computer sceglie la mossa che deve fare ( tra una mossa casuale o una intelligente )
	 * 
	 * @return computerMakesRandomMove() 
	 * 		 oppure computerMakesMove()	
	 */
	private boolean computerMoves(){

		if( allInLine() )
			return computerMakesRandomMove();    // la prima mossa del computer che sarà casuale
		else
			return computerMakesMove();		 // la mossa intelligente del computer 	

	}


	/**
	 * Vediamo se il computer può mangiare o fare una mossa
	 * Quindi andiamo a scandire l'array legalJumps & legalMoves 
	 */
	private boolean computerMakesMove(){
		int xFrom;
		int yFrom;

		// inizializziamo i vari array
		kingLegalJumps();
		kingLegalMoves();
		pawnLegalJumps();
		pawnLegalMoves();
		pawnBecomesKing();

		// proviamo a fare la soffiata
		if( board.getSoffioFlag() )	
			trySoffio();

		// vediamo se la dama può mangiare
		if( kingLegalJumps.length > 0 ){

			for( int i = 0; i < kingLegalJumps.length; i += 2){
				xFrom = kingLegalJumps[i];
				yFrom = kingLegalJumps[i+1];

				// vediamo se la dama può mangiare in basso
				if( canJumpDownAtRight(xFrom, yFrom, "king") )
					return makeJumpDownAtRight(xFrom, yFrom) && tryToJumpUpAgain(xFrom + 2, yFrom + 2) && tryToJumpDownAgain(xFrom + 2, yFrom + 2, "king")
							&& tryToJumpDownAgain(xFrom + 4, yFrom + 4, "king") && tryToJumpUpAgain(xFrom + 4, yFrom + 4);

				if( canJumpDownAtLeft(xFrom, yFrom, "king") )		
					return makeJumpDownAtLeft(xFrom, yFrom) && tryToJumpUpAgain(xFrom + 2, yFrom - 2) && tryToJumpDownAgain(xFrom + 2 , yFrom - 2, "king")
							&& tryToJumpDownAgain(xFrom + 4 , yFrom - 4, "king") && tryToJumpUpAgain(xFrom + 4, yFrom - 4); 

				// vediamo se la dama può mangiare in alto
				if( canJumpUpAtRight(xFrom, yFrom) )
					return makeJumpUpAtRight(xFrom, yFrom) && tryToJumpUpAgain(xFrom - 2, yFrom + 2) && tryToJumpDownAgain(xFrom - 2, yFrom + 2, "king")
							&& tryToJumpDownAgain(xFrom - 4 , yFrom + 4, "king") && tryToJumpUpAgain(xFrom - 4, yFrom + 4);

				if( canJumpUpAtLeft(xFrom, yFrom) )		
					return makeJumpUpAtLeft(xFrom, yFrom) && tryToJumpUpAgain(xFrom - 2, yFrom - 2) && tryToJumpDownAgain(xFrom - 2 , yFrom - 2, "king")
							&& tryToJumpDownAgain(xFrom - 4 , yFrom - 4, "king") && tryToJumpUpAgain(xFrom - 4, yFrom - 4);


			}	

		} 


		// vediamo se le pedine possono mangiare
		if( pawnLegalJumps.length > 0 ){

			for( int i = 0; i < pawnLegalJumps.length; i += 2){
				xFrom = pawnLegalJumps[i];
				yFrom = pawnLegalJumps[i+1];

				if( canJumpDownAtRight(xFrom, yFrom, "pawn") )
					return makeJumpDownAtRight(xFrom, yFrom) && tryToJumpDownAgain(xFrom + 2, yFrom + 2, "pawn")
							&& tryToJumpDownAgain(xFrom + 4, yFrom + 4, "pawn");

				if( canJumpDownAtLeft(xFrom, yFrom, "pawn") )	
					return makeJumpDownAtLeft(xFrom, yFrom) && tryToJumpDownAgain(xFrom + 2, yFrom - 2, "pawn")
							&& tryToJumpDownAgain(xFrom + 4, yFrom - 4, "pawn");


			}
		}


		// vediamo se c'è qualche pedina che può diventare damone
		if( pawnBecomesKing.length > 0 ){

			for( int j = 0; j <  pawnBecomesKing.length; j += 2){
				xFrom = pawnBecomesKing[j];
				yFrom = pawnBecomesKing[j+1];

				if( canMoveDownAtRight(xFrom, yFrom) )
					return makeMoveDownAtRight(xFrom, yFrom);

				if( canMoveDownAtLeft(xFrom, yFrom) )
					return makeMoveDownAtLeft(xFrom, yFrom);

			}
		}


		// a questo punto la dama non può mangiare quindi vediamo se può fare una mossa
		if( kingLegalMoves.length > 0 ){

			for( int j = 0; j < kingLegalMoves.length; j += 2){
				xFrom = kingLegalMoves[j];
				yFrom = kingLegalMoves[j+1];

				// andiamo a seguire la pedina del utente in alto
				if( kingGoForTheRedAtLeft(xFrom,yFrom, "up") ){
					if( canMoveUpAtLeft(xFrom, yFrom) )
						return makeMoveUpAtLeft(xFrom, yFrom);
				}
				if( kingGoForTheRedAtRight(xFrom,yFrom, "up") ){
					if( canMoveUpAtRight(xFrom, yFrom) )
						return makeMoveUpAtRight(xFrom, yFrom);
				}

				// andiamo a seguire la pedina del utente in basso
				if( kingGoForTheRedAtLeft(xFrom,yFrom, "down") ){
					if( canMoveDownAtLeft(xFrom, yFrom) )
						return makeMoveDownAtLeft(xFrom, yFrom);
				}
				if( kingGoForTheRedAtRight(xFrom,yFrom, "down") ){
					if( canMoveDownAtRight(xFrom, yFrom) )
						return makeMoveDownAtRight(xFrom, yFrom);
				}
				
			} 

		}

		// a questo punto le pedine non possono mangiare quindi vediamo se possono fare una mossa
		if( pawnLegalMoves.length > 0 ){

			//scandisco l'array per vedere quale pedina è vicina ad una pedina del utente
			//se ne trovo una facciamo una mossa con quella pedina (per non essere mangiata)
			for( int j = 0; j < pawnLegalMoves.length; j += 2){
				xFrom = pawnLegalMoves[j];
				yFrom = pawnLegalMoves[j+1];

				if( !redIsCloseToBlackAtRight(xFrom, yFrom, "up")){
					if( canMoveDownAtRight(xFrom, yFrom) )
						return makeMoveDownAtRight(xFrom, yFrom);
				}

				if( !redIsCloseToBlackAtLeft(xFrom, yFrom, "up") ){	
					if( canMoveDownAtLeft(xFrom, yFrom) )
						return makeMoveDownAtLeft(xFrom, yFrom);

				}
			}

			//altrimenti, se non ci sono pedine del utente vicino alla nostra pedina 
			//facciamo una mossa legale
			for( int j = 0; j < pawnLegalMoves.length; j += 2){
				xFrom = pawnLegalMoves[j];
				yFrom = pawnLegalMoves[j+1];

				if( canMoveDownAtRight(xFrom, yFrom) )
					return makeMoveDownAtRight(xFrom, yFrom);

				if( canMoveDownAtLeft(xFrom, yFrom) )
					return makeMoveDownAtLeft(xFrom, yFrom);

			}
		}


		// la pedina o la dama non può mangiare o fare una mossa
		return false;

	}


	/** Vediamo se la pedina o dama può fare una mossa legale in basso 
	 *
	 * @param xFrom la pos x
	 * @param yFrom	la pos y
	 * @return true possiamo fare una mossa legale
	 *  	   false altrimenti 	   
	 */	
	private boolean isLegalMoveDown(int xFrom, int yFrom){

		return canMoveDownAtRight(xFrom,yFrom) || canMoveDownAtLeft(xFrom,yFrom);

	}

	/** Vediamo se la pedina o dama può fare una mossa legale in alto 
	 *
	 * @param xFrom la pos x
	 * @param yFrom	la pos y
	 * @return true se possiamo fare una mossa legale
	 *  	   false altrimenti 	   
	 */	
	private boolean isLegalMoveUp(int xFrom, int yFrom){

		return canMoveUpAtLeft(xFrom,yFrom) || canMoveUpAtRight(xFrom,yFrom);
	}



	/** Vediamo se la dama si può fare una mossa in alto a Sinistra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può
	 * 		   false altrimenti		
	 */
	private boolean canMoveUpAtLeft(int xFrom, int yFrom){

		// se non è occupato a Dx
		if( board.inLimitColumn(yFrom-1) && board.inLimitRow(xFrom-1) ){
			if( !board.occupiedAt(xFrom-1, yFrom-1) )
				return true;
		}

		// non si può fare una mossa
		return false;
	}


	/** Vediamo se la dama si può fare una mossa in alto a Destra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return	true se può
	 * 			false altrimenti
	 */
	private boolean canMoveUpAtRight(int xFrom, int yFrom){

		// se non è occupato a Dx
		if( board.inLimitColumn(yFrom+1) && board.inLimitRow(xFrom-1) ){
			if( !board.occupiedAt(xFrom-1, yFrom+1) )
				return true;
		}

		// non si può fare una mossa
		return false;
	}


	/** Vediamo se la pedina o la dama può fare una mossa in basso a Destra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return	true se può
	 * 			false altrimenti
	 */
	private boolean canMoveDownAtRight(int xFrom, int yFrom){

		// se non è occupato a Dx
		if( board.inLimitColumn(yFrom+1) && board.inLimitRow(xFrom+1) ){
			if( !board.occupiedAt(xFrom+1, yFrom+1) )
				return true;
		}
		// non si può fare una mossa
		return false;
	}


	/** Vediamo se la pedina o la dama può fare una mossa in basso a Sinistra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return	true se può
	 * 			false altrimenti
	 */
	private boolean canMoveDownAtLeft(int xFrom, int yFrom){

		// se non è occupato a Sx
		if( board.inLimitColumn(yFrom-1) && board.inLimitRow(xFrom+1) ){
			if( !board.occupiedAt(xFrom+1, yFrom-1) )
				return true;
		}
		// non si può fare una mossa
		return false;
	}


	/** La dama fa la mossa in alto a Sinistra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 */
	private boolean makeMoveUpAtLeft(int xFrom, int yFrom){  

		board.setPawnAt(xFrom, yFrom, emptyCell);
		board.setPawnAt(xFrom-1, yFrom-1, blackKing);

		return true;
	}

	/** La dama fa la mossa in alto a Destra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 */
	private boolean makeMoveUpAtRight(int xFrom, int yFrom){

		board.setPawnAt(xFrom, yFrom, emptyCell);
		board.setPawnAt(xFrom-1, yFrom+1, blackKing); 

		return true;
	}


	/** La pedina o la dama fa la mossa in basso a Destra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 */
	private boolean makeMoveDownAtRight(int xFrom, int yFrom){

		// se arriviamo alla riga 7 la pedina diventa dama
		if( xFrom+1 == 7){
			board.setPawnAt(xFrom, yFrom, emptyCell);
			board.setPawnAt(xFrom+1, yFrom+1, blackKing); 

			// se la pos x,y è gia una dama
		}else if( board.isBlackKingAt(xFrom, yFrom) ){
			board.setPawnAt(xFrom, yFrom, emptyCell);
			board.setPawnAt(xFrom+1, yFrom+1, blackKing); 

			// altrimenti rimane pedina
		}else{
			board.setPawnAt(xFrom, yFrom, emptyCell);
			board.setPawnAt(xFrom+1, yFrom+1, blackPawn); 
		}
		
		return true;
	}


	/** La pedina o la dama fa la mossa in basso a Sinistra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 */
	private boolean makeMoveDownAtLeft(int xFrom, int yFrom){  

		// se arriviamo alla riga 7 la pedina diventa dama
		if( xFrom+1 == 7){
			board.setPawnAt(xFrom, yFrom, emptyCell);
			board.setPawnAt(xFrom+1, yFrom-1, blackKing);

			// se la pos x,y è gia una dama
		}else if( board.isBlackKingAt(xFrom, yFrom) ){
			board.setPawnAt(xFrom, yFrom, emptyCell);
			board.setPawnAt(xFrom+1, yFrom-1, blackKing);

			// altrimenti rimane pedina
		}else{

			board.setPawnAt(xFrom, yFrom, emptyCell);
			board.setPawnAt(xFrom+1, yFrom-1, blackPawn);

		}

		return true;
	}

	/*** mangiate ***/

	/** Vediamo se la dama può mangiare in alto a Destra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true può 
	 * 		   false altrimenti
	 */
	private boolean canJumpUpAtRight(int xFrom, int yFrom){

		// si può mangiare in alto a Dx
		if( board.inLimitColumn(yFrom+2) && board.inLimitRow(xFrom-2 )){
			if( ( board.isRedPawnAt(xFrom-1, yFrom+1) || board.isRedKingAt(xFrom-1, yFrom+1)) && board.isEmptyCellAt(xFrom-2, yFrom+2) )
				return true;
		}
		//non si può mangiare
		return false;

	}

	/** Vediamo se la la dama può mangiare in alto a Sinistra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom a pos y
	 * @return true se può
	 * 		   false altrimenti	
	 */
	private boolean canJumpUpAtLeft(int xFrom, int yFrom){

		// si può mangiare in alto a Sx
		if( board.inLimitColumn(yFrom-2) && board.inLimitRow(xFrom-2) ){
			if( (board.isRedPawnAt(xFrom-1, yFrom-1) || board.isRedKingAt(xFrom-1, yFrom-1)) && board.isEmptyCellAt(xFrom-2, yFrom-2) )
				return true;
		}
		//non si può mangiare
		return false;
	}


	/** Vediamo se la pedina o la dama può mangiare in basso a Destra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @param turn che specifica di chi è il turno; dama o pedina
	 * @return true se può 
	 * 		   false altrimenti
	 */
	private boolean canJumpDownAtRight(int xFrom, int yFrom, String turn){

		// si può mangiare in basso a Dx
		if( turn.equals("pawn") && !multiple ){
			if( board.inLimitColumn(yFrom+2) && board.inLimitRow(xFrom+2) ){
				if( board.isRedPawnAt(xFrom+1, yFrom+1) && board.isEmptyCellAt(xFrom+2, yFrom+2) )
					return true;
			}
		}

		// si può mangiare in basso a Dx
		if( turn.equals("king") || multiple ){
			if( board.inLimitColumn(yFrom+2) && board.inLimitRow(xFrom+2) ){
				if( (board.isRedPawnAt(xFrom+1, yFrom+1) || board.isRedKingAt(xFrom+1, yFrom+1)) && board.isEmptyCellAt(xFrom+2, yFrom+2) )
					return true;
			}
		}

		// non si può mangiare
		return false;
	}


	/** Vediamo se la pedina o la dama può mangiare in basso a Sinistra
	 * 
	 * @param xFrom la pos x
	 * @param yFrom a pos y
	 * @param turn che specifica di chi è il turno; dama o pedina
	 * @return true se può
	 * 		   false altrimenti	
	 */
	private boolean canJumpDownAtLeft(int xFrom, int yFrom, String turn){
		
		// si può mangiare in basso a Sx
		if( turn.equals("pawn") && !multiple ){
			if( board.inLimitColumn(yFrom-2) && board.inLimitRow(xFrom+2) ){
				if( board.isRedPawnAt(xFrom+1, yFrom-1) && board.isEmptyCellAt(xFrom+2, yFrom-2) )
					return true;
			}
		}

		// si può mangiare in basso a Sx
		if( turn.equals("king") || multiple ){
			if( board.inLimitColumn(yFrom-2) && board.inLimitRow(xFrom+2) ){
				if( (board.isRedPawnAt(xFrom+1, yFrom-1) || board.isRedKingAt(xFrom+1, yFrom-1)) && board.isEmptyCellAt(xFrom+2, yFrom-2) )
					return true;
			}
		}

		//non si può mangiare
		return false;
	}


	/** Il computer mangia la pedina del utente in basso ( mangiata Destra )
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 */
	private boolean makeJumpDownAtRight(int xFrom, int yFrom){

		// se arriviamo alla riga 7 la pedina diventa dama
		if( xFrom+2 == 7){   
			board.setPawnAt(xFrom+1, yFrom+1, emptyCell );
			board.setPawnAt(xFrom, yFrom, emptyCell );
			board.setPawnAt(xFrom+2, yFrom+2, blackKing );

			// se la pos x,y è già una dama
		}else if( board.isBlackKingAt(xFrom, yFrom) ){
			board.setPawnAt(xFrom+1, yFrom+1, emptyCell );
			board.setPawnAt(xFrom, yFrom, emptyCell );
			board.setPawnAt(xFrom+2, yFrom+2, blackKing );

			// altrimenti rimane pedina	
		}else{
			board.setPawnAt(xFrom+1, yFrom+1, emptyCell );
			board.setPawnAt(xFrom, yFrom, emptyCell );
			board.setPawnAt(xFrom+2, yFrom+2, blackPawn );

		}

		return true;

	}

	/** Il computer mangia la pedina del utente int basso( mangiata Sinistra )
	 * 
	 * @param xFrom la pos x 
	 * @param yFrom la pos y
	 * return true
	 */
	private boolean makeJumpDownAtLeft(int xFrom, int yFrom){

		// se arriviamo alla riga 7 la pedina diventa dama
		if( xFrom+2 == 7){
			board.setPawnAt(xFrom+1, yFrom-1, emptyCell );
			board.setPawnAt(xFrom, yFrom, emptyCell );
			board.setPawnAt(xFrom+2, yFrom-2, blackKing );

			// se la pos x,y è già una dama 
		}else if( board.isBlackKingAt(xFrom, yFrom) ){
			board.setPawnAt(xFrom+1, yFrom-1, emptyCell );
			board.setPawnAt(xFrom, yFrom, emptyCell );
			board.setPawnAt(xFrom+2, yFrom-2, blackKing );

			// altrimenti rimane pedina	
		}else{
			board.setPawnAt(xFrom+1, yFrom-1, emptyCell );
			board.setPawnAt(xFrom, yFrom, emptyCell );
			board.setPawnAt(xFrom+2, yFrom-2, blackPawn );

		}

		return true;
	}


	/** La dama mangia la pedina del utente in alto  a Sinistra 
	 * 
	 * @param xFrom la pos x 
	 * @param yFrom la pos y
	 * return true
	 */
	private boolean makeJumpUpAtLeft(int xFrom, int yFrom){

		board.setPawnAt(xFrom-1, yFrom-1, emptyCell );
		board.setPawnAt(xFrom, yFrom, emptyCell );
		board.setPawnAt(xFrom-2, yFrom-2, blackKing );

		return true;
	}


	/** La dama mangia la pedina del utente in alto a Destra 
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 */
	private boolean makeJumpUpAtRight(int xFrom, int yFrom){

		board.setPawnAt(xFrom-1, yFrom+1, emptyCell );
		board.setPawnAt(xFrom, yFrom, emptyCell );
		board.setPawnAt(xFrom-2, yFrom+2, blackKing );

		return true;
	}

	/**
	 * Proviamo a fare le mangiate multiple in basso con il computer 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @param turn specifica di chi è il turno ( dama o pedina )
	 * @return  makeJumpDownAtRight oppure  makeJumpDownAtRight oppure true
	 */
	private boolean tryToJumpDownAgain(int xFrom, int yFrom, String turn){
		multiple = true;

		if( board.inLimitColumn(yFrom) && board.inLimitRow(xFrom)){

			if( canJumpDownAtRight(xFrom, yFrom, turn) )
				return makeJumpDownAtRight(xFrom, yFrom);

			if( canJumpDownAtLeft(xFrom, yFrom, turn) )
				return makeJumpDownAtLeft(xFrom, yFrom);
		}

		return true;
	}

	/**
	 * Proviamo a fare le mangiate multiple in alto con il computer 
	 * @param xFrom la pos x iniziale
	 * @param yFrom la pos y iniziale
	 * @param turn specifica di chi è il turno ( dama o pedina )
	 * @return  makeJumpUpAtRight oppure  makeJumpUpAtRight oppure true
	 */
	private boolean tryToJumpUpAgain(int xFrom, int yFrom){

		if( board.inLimitColumn(yFrom) && board.inLimitRow(xFrom)){
			if( canJumpUpAtLeft(xFrom, yFrom) )
				return makeJumpUpAtLeft(xFrom, yFrom); 

			if( canJumpUpAtRight(xFrom, yFrom) )
				return makeJumpUpAtRight(xFrom, yFrom); 
		}

		return true;
	}


	/** Vediamo se la pedina può mangiare in basso 
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può 
	 * 		   false atrimenti
	 */
	private boolean isLegalJumpDownPawn(int xFrom, int yFrom) {

		return canJumpDownAtLeft(xFrom,yFrom, "pawn") || canJumpDownAtRight(xFrom,yFrom, "pawn");
	}

	/** Vediamo la dama può mangiare in basso 
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può 
	 * 		   false atrimenti
	 */
	private boolean isLegalJumpDownKing(int xFrom, int yFrom) {

		return canJumpDownAtLeft(xFrom,yFrom, "king") || canJumpDownAtRight(xFrom,yFrom, "king");			
	}

	/** Vediamo se la dama può mangiare in alto
	 * 
	 * @param xFrom la pos x
	 * @param yFrom la pos y
	 * @return true se può 
	 * 		   false atrimenti
	 */
	private boolean isLegalJumpUp(int xFrom, int yFrom) {

		return canJumpUpAtRight(xFrom,yFrom) || canJumpUpAtLeft(xFrom,yFrom) ;
	}

	/** 
	 *  Costruisco un'array con le coordinate delle pedine che possono mangiare una pedina del player
	 */
	private void pawnLegalJumps(){
		int len = 0;
		int pos = 0;

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackPawnAt(x,y) ){

					if( isLegalJumpDownPawn(x,y) )
						len++;
				}

			}
		}

		pawnLegalJumps = new int[len*2];

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackPawnAt(x,y) ){

					if( isLegalJumpDownPawn(x,y) ){

						pawnLegalJumps[pos] = x; 
						pawnLegalJumps[pos+1] = y;
						pos += 2;

					}
				}
			}
		} 

	}

	/** 
	 *  Costruisco un'array con le coordinate delle pedine che possono fare una mossa legale
	 */
	private void pawnLegalMoves(){
		int len = 0;
		int pos = 0;

		// scandisco la scacchiera per vedere quante pedine nere possono fare una mossa legale
		// poi costruisco l'array di mosse legale
		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackPawnAt(x,y) ){

					if( isLegalMoveDown(x,y) )

						len++; 
				}
			}
		}

		pawnLegalMoves = new int[len*2];  // costruisco l'array

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackPawnAt(x,y) ){

					if( isLegalMoveDown(x,y) ){

						pawnLegalMoves[pos] = x; 
						pawnLegalMoves[pos+1] = y;
						pos += 2;

					}
				}
			}
		} 

	}

	/** 
	 *  Costruisco un'array con le coordinate delle dame che possono fare una mossa legale
	 */
	private void kingLegalMoves(){
		int len = 0;
		int pos = 0;

		// scandisco la scacchiera per vedere quante pedine nere possono fare una mossa legale
		// poi costruisco l'array di mosse legale
		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackKingAt(x,y) ){

					if( isLegalMoveDown(x,y) )
						len++; 

					if( isLegalMoveUp(x,y) )
						len++;
				}
			}
		}

		kingLegalMoves = new int[len*2];  // costruisco l'array

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackKingAt(x,y) ){

					if( isLegalMoveDown(x,y) ){

						kingLegalMoves[pos] = x; 
						kingLegalMoves[pos+1] = y;
						pos += 2;

					}

					if( isLegalMoveUp(x,y) ){

						kingLegalMoves[pos] = x; 
						kingLegalMoves[pos+1] = y;
						pos += 2;

					}
				}
			}
		} 

	}

	/** 
	 *  Costruisco un'array con le coordinate delle dame che possono mangiare una pedina del player
	 */
	private void kingLegalJumps(){
		int len = 0;
		int pos = 0;

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackKingAt(x,y) ){

					if( isLegalJumpDownKing(x,y) )
						len++;

					if( isLegalJumpUp(x,y))
						len++;
				}

			}
		}

		kingLegalJumps = new int[len*2];

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( board.isBlackKingAt(x,y) ){
					if( isLegalJumpDownKing(x,y)  ){

						kingLegalJumps[pos] = x; 
						kingLegalJumps[pos+1] = y;
						pos += 2;
					}

					if( isLegalJumpUp(x,y) ){

						kingLegalJumps[pos] = x; 
						kingLegalJumps[pos+1] = y;
						pos += 2;
					}
				}
			}
		} 

	}

	/**
	 * Costruiamo l'array con le coordinate delle pedine che possono diventare damone
	 * ovvero quelle che sono nella riga 6
	 */
	private void pawnBecomesKing(){
		int len = 0;
		int pos = 0;

		for(int j = 0; j < 8; j++){
			if( board.isBlackPawnAt(6, j) ){
				len++;
			}
		}

		pawnBecomesKing = new int [2*len];

		for(int j = 0; j < 8; j++){
			if( board.isBlackPawnAt(6, j) ){
				pawnBecomesKing[pos] = 6;
				pawnBecomesKing[pos+1] = j;

				pos += 2;

			}

		}


	}

	/**  Il computer fa la prima mossa (iniziale) che sarà casuale
	 * 
	 * @return true se ci riesce
	 * 		   false altrimenti
	 */
	private boolean computerMakesRandomMove(){
		int col = returnRandomColumn();

		if( !board.occupiedAt(3,col) ){
			board.setPawnAt(3, col, blackPawn);
			board.setPawnAt(3-1,col+1, emptyCell);

			return true;
		}
		else
			return false;

	}


	/** Mi ritorna un numero a caso pari fra 0-7 ch serve per la mossa iniziale
	 * del computer
	 * 
	 * @return il numero a caso 
	 */
	private int returnRandomColumn(){
		Random randomMove = new Random();
		int col;

		do{

			col = randomMove.nextInt(8); // prendiamo solo il numero pari

		}while( col % 2 != 0 );

		return col;
	}

	/** Vediamo se le pedine nere sono nelle pos iniziali, perché 
	 * se ci sono significa che il computer deve fare la prima mossa che sarà casuale
	 *  
	 * @return true se ci sono
	 * 		   false altrimenti  
	 */
	private boolean allInLine() {

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( (x % 2 != y % 2) && x < 3 ){
					if( !board.isBlackPawnAt(x, y) )
						return false;
				}

			}
		}

		return true;
	}

	/**
	 * Andiamo a prendere la pedina del utente a destra
	 * @return true
	 * 			false altrimenti
	 *@param row la riga
	 *param col la colonna
	 */
	private boolean kingGoForTheRedAtRight(int row, int col, String where){

		if( where.equals("up") )
			return board.getXFrom() < row && board.getYFrom() > col;
		else //down
			return board.getXFrom() > row && board.getYFrom() > col;
	}

	/**
	 *  Andiamo a prendere la pedina del utente a sinistras
	 * @return true
	 * 			false altrimenti
	 *@param row la riga
	 *@param col la colonna
	 */
	private boolean kingGoForTheRedAtLeft(int row, int col, String where){

		if( where.equals("up") )
			return board.getXFrom() < row && board.getYFrom() < col;
		else  //down
			return board.getXFrom() > row && board.getYFrom() < col;
	}

	/**
	 * Vediamo se la pedina o dama rossa sono vicine alla nostra pedina nera (sinistra)
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se la pedina o dama rossa sono vicine alla nostra pedina nera
	 * 			false altrimenti
	 */
	private boolean redIsCloseToBlackAtLeft(int x, int y, String where ){

		//in alto
		if( where.equals("up") ){
			//Lx
			if( board.inLimitColumn(y-2) && board.inLimitRow(x+2)){
				if( board.isRedKingAt(x+2, y-2) || board.isRedPawnAt(x+2, y-2) )
					return true;
			}
		}

		// int basso
		if( where.equals("down") ){
			//Lx
			if( board.inLimitColumn(y-2) && board.inLimitRow(x-2)){
				if( board.isRedKingAt(x-2, y-2) || board.isRedPawnAt(x-2, y-2) )
					return true;
			}

		}

		return false;
	}


	/**
	 * Vediamo se la pedina o dama rossa sono vicine alla nostra pedina nera ( destra)
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se la pedina o dama rossa sono vicine alla nostra pedina nera
	 * 			false altrimenti
	 */
	private boolean redIsCloseToBlackAtRight(int x, int y, String where ){

		//in alto
		if( where.equals("up") ){
			//Rx
			if( board.inLimitColumn(y+2) && board.inLimitRow(x+2)){
				if( board.isRedKingAt(x+2, y+2) || board.isRedPawnAt(x+2, y+2) )
					return true;

			}

		}

		// int basso
		if( where.equals("down") ){
			//Rx
			if( board.inLimitColumn(y+2) && board.inLimitRow(x-2)){
				if( board.isRedKingAt(x-2, y+2) || board.isRedPawnAt(x-2, y+2) )
					return true;
			}

		}

		return false;
	}

	/**
	 * Proviamo a fare la soffiata
	 */
	private void trySoffio(){

		board.setPawnAt(board.getSoffioOriginX(), board.getSoffioOriginY(), emptyCell);
		board.setSoffioFlag(false);

	}

}