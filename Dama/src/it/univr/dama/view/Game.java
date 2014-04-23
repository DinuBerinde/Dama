package it.univr.dama.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import it.univr.dama.model.Board;
import it.univr.dama.controller.ComputerMove;
import it.univr.dama.controller.GameSolved;
import it.univr.dama.controller.PlayerMove;
import it.univr.dama.controller.Suggestion;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;


/**
 * Costruzione del gioco della Dama italiana
 * @author dinu
 *
 */
public class Game extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private final Board board;
	private final Elements element = new Elements();
	private final CommandWindow window = new CommandWindow();
	private final CommandWindow windowGameOver = new CommandWindow(this);
	private final Menu menu = new Menu(this);

	/**
	 * Costruttore
	 * @param board la scacchiera
	 */
	protected Game(Board board){
		super("Dama");

		this.board = board;

		setLayout(new GridLayout(8,8));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(550, 550);
		setLocation( (dim.width - 550) / 2, (dim.height - 550) / 2 );

		createMenu();
		showBoard();


		// chiudere la finestra
		this.addWindowListener(new WindowListener() {

			public void windowClosing(WindowEvent e) {
				new CheckExit().setVisible(true);
			}

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {} 

		});
	}

	/**
	 * Vediamo lo stato del gioco
	 * @return 2 se ha vinto il computer
	 * 		   3 se ha vinto l'utente
	 * 		   1 se il gioco non è ancora risolto	
	 */
	private int isSolved() {
		return new GameSolved(board).isSolved();
	}

	
	/**
	 * Mostriamo la scacchiera con le pedine 
	 */
	private void showBoard(){

		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){

				if( (x+y) % 2 == 0)
					addCell( new Cell(Color.DARK_GRAY, ' '), x, y );

				else{
					// pedina nera
					if( board.isBlackPawnAt(x, y) )	
						addCell( new Cell(Color.LIGHT_GRAY, 'n'), x, y );

					// pedina rossa
					if( board.isRedPawnAt(x, y) )
						addCell( new Cell(Color.LIGHT_GRAY, 'r'), x, y ); 

					// dama nera
					if(  board.isBlackKingAt(x, y) )
						addCell( new Cell(Color.LIGHT_GRAY, 'N'), x, y );

					// dama rossa
					if(  board.isRedKingAt(x, y) )
						addCell( new Cell(Color.LIGHT_GRAY, 'R'), x, y );

					// nessuna pedina
					if(  board.isEmptyCellAt(x, y) )
						addCell( new Cell(Color.LIGHT_GRAY, ' '), x, y );


					// una cella verde come suggerimento
					if( board.isGreenCellAt(x, y))
						addCell( new Cell(Color.GREEN, ' '), x, y );

				}
			}


		}	
	} 


	/**
	 * Aggiungiamo la cella
	 * @param cell la cella da aggiungere
	 * @param x la pos x
	 * @param y	la pos y
	 */
	private void addCell(Cell cell, final int x, final int y){
		add(cell);

		cell.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e){

				// se il gioco non è risolto continua a giocare
				if( isSolved() == 1 ){

					// se è una pedina rossa aggiungiamo un suggerimento
					if( board.isRedPawnAt(x, y) || board.isRedKingAt(x, y) )
						addSuggestion(board, x, y);

					// se è una cella verde facciamo la mossa/mangiata
					else if( board.isGreenCellAt(x, y) ){
						playerTryMoveAt(board, x, y);
						computerTryMoveAt(board, x ,y);
					}


					// il gioco è risolto dal computer
					if( isSolved() == 2){
						windowGameOver.setMessage("Sorry, computer has won!");			
						windowGameOver.setVisible(true);	
					}
					// il gioco è risolto dal utente
					else if( isSolved() == 3){
						windowGameOver.setMessage("Congratulations, you have won!");
						windowGameOver.setVisible(true);
					}
					
				}else
					return;
			}

		});
	} 



	/** Agiunggiamo un suggerimento oppure un messagio che la partita finisce con un
	 * 	pareggio se non è possibile aggiungere un suggerimento
	 *  
	 * @param board la scacchiera
	 * @param x la pos x
	 * @param y la pos y
	 */
	private void addSuggestion(Board board, int x, int y) {
		
		
		if(  new Suggestion(board, x, y).isLegal() )
			repaintBoard();
		
		else {
			windowGameOver.setMessage("Sorry, the game ends with a draw!");
			windowGameOver.setVisible(true);
		}
	}

	/** L'utente fa la mossa/mangiata legale 
	 * 
	 * @param board2 la scacchiera
	 * @param x la pos x
	 * @param y la pos y
	 */
	private void playerTryMoveAt(Board board2, int x, int y) {

		if ( new PlayerMove(board2, x, y).isLegal() )
			repaintBoard();
		else
			return;	
	}

	/** Il computer fa la mossa/mangiata legale
	 * 
	 * @param board3 la scacchiera
	 * @param x la pos x
	 * @param y la pos y
	 */
	private void computerTryMoveAt(Board board3, int x, int y){

		// se l'utente non fa una mangiata multipla allora il computer fa la mossa
		if( board3.getComputerStatus() ){
			window.setVisible(false);

			if( new ComputerMove(board3).isLegal() ){


				try {

					Thread.sleep(150);
				
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				

				repaintBoard();
			}

			// altrimenti, l'utente fa una mangiata multipla ed il computer si ferma	
		}else{

			repaintBoard();
			window.setVisible(true);
			board3.setComputerStatus(true);
		}

	}

	/**
	 * Ridisegno la scacchiera
	 */
	private void repaintBoard(){

		getContentPane().removeAll();
		showBoard();

		invalidate();
		validate();

	}


	/**
	 * Costruiamo il menu
	 */
	private void createMenu() {
		JButton menuButton = new JButton("Menu");
		Color color = new Color(0,110,0);
		java.awt.Font font = new java.awt.Font ( "URW Chancery L" , Font.ITALIC, 20 ) ;
	
		menuButton.setFont(font);
		menuButton.setBackground(Color.white);

		menuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(true);

			}


		});

		JMenuBar bar = new JMenuBar();
		bar.setBackground(color);

		bar.add(menuButton);
		bar.add(element.createTitle());
		setJMenuBar(bar);
	} 


	/**
	 * Il Main del gioco
	 * @param args
	 */
	public static void main(String[] args) {
		
		new Menu(new Game(new Board())).setVisible(true);

	}
}