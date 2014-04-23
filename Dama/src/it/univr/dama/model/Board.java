package it.univr.dama.model;

/**
 * Costruzione della scacchiera
 * @author dinu
 *
 */
public class Board {
	private int[][] cells;
	private final int row = 8;
	private final int col = 8;

	private final static int emptyCell = 1;
	private final static int blackPawn = 2;
	private final static int redPawn = 3;
	private final static int redKing = 7;
	private final static int blackKing = 8;
	private final static int greenCell = 5;

	private int columnPos;
	private int rowPos;

	private boolean computerStatus = true;
	private int soffioOriginX;
	private int soffioOriginY;
	private boolean soffio = false;
	private boolean multiple;

	
	/**
	 * Costruttore
	 */
	public Board(){

		buildBoard();

	}

	/**
	 * Creiamo la scacchiera
	 */
	public void buildBoard(){
		cells = new int[row][col];

		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if( (x % 2 != y % 2) && x < 3 )
					setPawnAt(x, y, blackPawn );
				else if( (x % 2 != y % 2) && x > 4 )
					setPawnAt(x, y, redPawn );
				else	
					setPawnAt(x, y, emptyCell );

			}
		}
	}

	/**
	 * Ritorna il contenuto della cella a x,y
	 * @param x la pos x
	 * @param y la pos y
	 * @return il contenuto della cella ( pedina nera/rossa, dama nera/rossa
	 *  empty, cella verde)
	 */
	public int getCellAt(int x, int y) {
		return cells[x][y];
	}

	/**
	 * Inserisce una pedina alla pos x,y
	 * @param x la pos x
	 * @param y la pos y
	 * @param pawn la pedina da inserire
	 */
	public void setPawnAt(int x, int y, int pawn) {
		cells[x][y] = pawn;
	}


	/**
	 * Impostiamo lo stato del computer
	 * @param flag
	 */
	public void setComputerStatus(boolean flag){

		computerStatus = flag;
	}

	/**
	 * Ritorna lo stato del computer
	 * @return
	 */
	public boolean getComputerStatus(){

		return computerStatus;
	}


	/**
	 * Mettiamo la posizone corrente della colonna
	 * @param y
	 */
	public void setYFrom(int y){
		columnPos = y;
	}

	/**
	 * Ritorniamo la posizione corrente della colonna
	 * @return
	 */
	public int getYFrom(){

		return columnPos;
	}

	/**
	 * Mettiamo la posizone corrente della riga
	 * @param x
	 */
	public void setXFrom(int x){
		rowPos = x;
	}

	/**
	 * Ritorniamo la posizione corrente della riga
	 * @return
	 */
	public int getXFrom(){

		return rowPos;
	}


	/**
	 * Vediamo se c'è una cella verde alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se c'è
	 * 			false altrimenti
	 */
	public boolean isGreenCellAt(int x, int y) {

		return cells[x][y] == greenCell;
	}


	/**
	 * Vediamo se ci siamo nelle pos (0-7) inclusi per le colonne
	 * @param y la pos y (colonna)
	 * @return true se ci siamo 
	 * 			false altrimenti
	 */
	public boolean inLimitColumn(int y){

		return ( y >= 0 && y <= 7 );

	}


	/**
	 * Vediamo se ci siamo nelle pos (0-7) inclusi per le righe
	 * @param x la pos x (riga)
	 * @return true se ci siamo 
	 * 			false altrimenti
	 */
	public boolean inLimitRow(int x) {

		return ( x >= 0 && x <= 7);
	}

	/**
	 * Vediamo se è occupato alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se è occupato 
	 * 			false altrimenti
	 */
	public boolean occupiedAt(int x, int y) {

		return ( cells[x][y] == redPawn ) || ( cells[x][y] == blackPawn )
				|| ( cells[x][y] == redKing ) ||  ( cells[x][y] == blackKing );
	}


	/**
	 * Vediamo se c'è una pedina rossa alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se c'è
	 * 			false altrimenti
	 */
	public boolean isRedPawnAt(int x, int y) {

		return cells[x][y] == redPawn;
	}

	/**
	 * Vediamo se c'è una pedina nera alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se c'è
	 * 			false altrimenti
	 */
	public boolean isBlackPawnAt(int x, int y) {

		return cells[x][y] == blackPawn;
	}

	/**
	 * Vediamo se c'è una pedina empty alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se c'è
	 * 			false altrimenti
	 */
	public boolean isEmptyCellAt(int x, int y) {

		return cells[x][y] == emptyCell;
	}

	/**
	 * Vediamo se c'è una dama nera alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se c'è
	 * 			false altrimenti
	 */
	public boolean isBlackKingAt(int x, int y) {

		return cells[x][y] == blackKing;
	}

	/**
	 * Vediamo se c'è una dama rossa alla pos x,y 
	 * @param x la pos x
	 * @param y la pos y
	 * @return true se c'è
	 * 			false altrimenti
	 */
	public boolean isRedKingAt(int x, int y) {

		return cells[x][y] == redKing;
	}

	
	
	/**
	 * Impostiamo le coordinate x,y della pedina che deve essere soffiata
	 * @param x la pos x iniziale
	 * @param y la pos y iniziale
	 */
	public void setSoffioOrigin(int x, int y) {
		soffioOriginX = x;
		soffioOriginY = y;
		
	}
	
	/**
	 * 
	 * @return la coordinata x iniziale della pedina 
	 */
	public int getSoffioOriginX(){
		return soffioOriginX;
		
	}
	
	/**
	 * 
	 * @return la coordinata y iniziale della pedina 
	 */
	public int getSoffioOriginY(){
		return soffioOriginY;
		
	}

	/**
	 * 
	 * @return il flag del soffio
	 */
	public boolean getSoffioFlag(){
		return soffio;
	}


	/**
	 * 
	 * @param flag un boolean per capire quando possiamo fare la soffiata
	 */
	public void setSoffioFlag(boolean flag) {
		soffio = flag;
		
	}

		
	/**
	 * 
	 * @return il flag 
	 */
	public boolean getPawnMultiple() {
		
		return multiple;
	}

	/**
	 * Impostiamo il flag che ci serve nel caso in cui la pedina del utente a mangiato 
	 * e nella mangiata succesiva può mangiare una dama nera
	 * @param flag da impostare
	 */
	public void setPawnMultiple(boolean flag) {
		
		multiple = flag;
		
	}


}