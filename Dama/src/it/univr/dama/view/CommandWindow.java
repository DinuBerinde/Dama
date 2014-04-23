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
 * Una finestra per mostrare un messaggio al utente
 * @author dinu
 *
 */
public class CommandWindow extends JFrame {

	private static JLabel message; 
	private static JLabel message2; 
	private static JLabel string = new JLabel();
	private static final long serialVersionUID = 1L;
	private final static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private final static java.awt.Font font = new java.awt.Font ( "URW Chancery L" , Font.ITALIC, 20 ) ;
	private Game exit;

	public CommandWindow(Game game) {
		
		this.exit = game;

		setLayout(new BorderLayout());
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation( (dim.width - 300) / 2, (dim.height - 300) / 2 );

		string.setForeground(Color.white);
		string.setFont(font);
		string.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel menu = new JPanel();
		menu.setLayout(new FlowLayout(1,10,50));
		menu.setBackground(new Color(0,110,0));
		menu.add(string);


		JButton newGame = new JButton("New Game");
		newGame.setFont(font);
		newGame.setBackground(Color.white);
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exit.dispose();
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

		add(menu, BorderLayout.CENTER);

	}

	public void setMessage(String text) {
		string.setText(text);
	}
	
	public CommandWindow() {
		Color c = new Color(0,110,0);
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 400, 300, 130);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(1,4,4));
		panel.setBackground(c);
		setBackground(c);
				
		message = new JLabel("You can eat again !");
		message.setFont(font);
		message2 = new JLabel("Click one more time on the pawn !");
		message2.setFont(font);
		message.setForeground(Color.white);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message2.setForeground(Color.white);
		message2.setHorizontalAlignment(SwingConstants.CENTER);
			
		panel.add(message);
		panel.add(message2);
		
		add(panel, BorderLayout.CENTER);
		
	}


}