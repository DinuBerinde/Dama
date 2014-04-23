package it.univr.dama.view;

import it.univr.dama.model.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Una finestra contenente il Menu con le opzioni
 * @author dinu
 *
 */
public class Menu extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final static int HEIGHT = 400;
	private final static int WIDTH = 300;
	private final JLabel message;
	private final JLabel title;
	private final static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private final Game exitGame;
	private final static java.awt.Font font = new java.awt.Font ( "URW Chancery L" , Font.ITALIC, 20 ) ;
	private final static java.awt.Font titleFont = new java.awt.Font ( "URW Chancery L" , Font.ITALIC, 35 ) ; 
	
	
	public Menu(Game game ){
		super("Menu");

		this.exitGame = game;
		
		title = new JLabel("La Dama italiana");
		title.setFont(titleFont);
		title.setForeground(Color.RED);
		
		message = new JLabel("Please choose your option");
		message.setFont(font);
		message.setForeground(Color.white);
		message.setHorizontalAlignment(SwingConstants.CENTER);
			
		setLayout(new BorderLayout());
		setSize(WIDTH, HEIGHT);
		setLocation( (dim.width - 300) / 2, (dim.height - 400) / 2 );
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel menu = new JPanel();
		menu.setLayout(new FlowLayout(1,50,30));
		menu.setBackground(new Color(0,110,0));
		menu.add(title);
		menu.add(message);

		JButton newGame = new JButton("New Game");
		newGame.setFont(font);
		newGame.setBackground(Color.white);
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				exitGame.dispose();
				dispose();
				new Game(new Board()).setVisible(true);
				
			}
			
		});

		
		menu.add(newGame);
		
		
		JButton exitGame = new JButton("Exit Game");
		exitGame.setFont(font);
		exitGame.setBackground(Color.white);
		exitGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
			
		});
		
		menu.add(exitGame);
		
		JButton exitMenu = new JButton("Exit Menu");
		exitMenu.setFont(font);
		exitMenu.setBackground(Color.white);
		exitMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
			
			
		});
		
		menu.add(exitMenu);

		this.add(menu, BorderLayout.CENTER);
	
	
	}

	
}