package it.univr.dama.view;


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
 * Costruzione della finestra che ci chiede se vogliamo uscire o no 
 * @author dinu
 *
 */
public class CheckExit extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private final static java.awt.Font font = new java.awt.Font ( "URW Chancery L" , Font.ITALIC, 20 ) ;
	private static JLabel message;
	
	
	
	/***
	 * Costruttore della finestra Exit
	 * Aggiungiamo anche 2 buttoni
	 */
	public CheckExit() {
		super("Are you sure?");

		
		setLocation( (dim.width - 300) / 2, (dim.height - 150) / 2 );
		setLayout(new BorderLayout());
		setSize(300,150);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		message = new JLabel("Are you sure?");
		message.setFont(font);
		message.setForeground(Color.white);
		message.setHorizontalAlignment(SwingConstants.CENTER);


		JPanel siNo = new JPanel();
		siNo.setLayout(new FlowLayout(1,70,30));
		siNo.setBackground(new Color(0,110,0));

		siNo.add(message);

		JButton yes = new JButton("Yes");
		yes.setFont(font);
		yes.setBackground(Color.white);
		yes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		siNo.add(yes);

		JButton no = new JButton("No");
		no.setFont(font);
		no.setBackground(Color.white);
		no.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();

			}	
		});

		siNo.add(no);


		this.add(siNo, BorderLayout.CENTER);



	}
}