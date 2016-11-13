package Lab7;

/* 
 * Stiin
 * DD2385 Lab 2 MVC
 * 20160423
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import javax.swing.Timer;

import javax.imageio.ImageIO;
import javax.sql.RowSetMetaData;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewControl extends JFrame implements ActionListener{
	private Boardgame game;
	private JLabel messageLabel;
	private JPanel buttonPanel;
	private final int nRows;
	private final int nCols;
	private Square[][] board;
	private BufferedImage images [];
	private boolean buttonsDisabled = false;
	private PrintWriter ut;
	
	// CONSTRUCTOR
	public ViewControl (String title, Boardgame game, int rows, int cols, PrintWriter ut){  
		super(title);
		this.nRows = rows;
		this.nCols = cols;
		this.game = game;
		this.ut = ut;

		// Images
		int numberOfCardPairs = (nRows * nCols)/2;
		images = new BufferedImage [numberOfCardPairs];
		for (int i = 0; i < numberOfCardPairs ; i++){
			try {
				images[i] = ImageIO.read(new File("C:\\Users\\ministini\\Documents\\Courses KTH\\DD2385 Software Engineering\\Lab2\\src\\lab2\\pics\\" + (i+1) + ".png"));
			} catch (IOException e) {

				try {
					images[i] = ImageIO.read(new File("./" + (i+1) + ".png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		}

		// Layout
		setSize(900,1000);
		setLayout(new BorderLayout());

		buttonPanel = new JPanel();		
		this.add(buttonPanel, BorderLayout.CENTER); 

		messageLabel = new JLabel("Start by picking two cards. Game on!",JLabel.CENTER ); 
		messageLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
		messageLabel.setPreferredSize(new Dimension(150, 150));
		this.add(messageLabel, BorderLayout.PAGE_END);

		// Set colors
		buttonPanel.setOpaque(true);
		buttonPanel.setBackground(Color.pink);
		messageLabel.setOpaque(true);
		messageLabel.setBackground(Color.pink);

		// CREATE BUTTONS
		board = new Square[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				Square mySquare = new Square(i, j); 
				this.board[i][j] = mySquare;
				this.board[i][j].setPreferredSize(new Dimension(200, 200));		// Set Size of button	
				this.board[i][j].setFocusPainted(false);   						// Remove selection (tab)
				buttonPanel.add(this.board[i][j]);			 
				this.board[i][j].addActionListener(this);	
			}
		}

		// Take off!
		this.setVisible(true);  
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		updateBoard();										// Make sure the board is populated on start
	}

	public void clickedButton(int i, int j) {
		String statusForThisMove = game.move(i, j);		// e.getSource()).getI() - Get index I for the active card
		updateBoard();
		updateMessage();

		// Special for memory - needs delay when there's NO MATCH - to show the wrongly paired cards
		if (statusForThisMove == "no match") {						
			buttonsDisabled = true;								// Cannot pick another card while showing two turned cards
			Timer timer = new Timer(2000, turnBackPair);		// Waits X ms, then calls function 
			timer.setRepeats(false);
			timer.start();
		} 
	}
	
	// När man klickar på en ruta - skicka kommando att klickat samt vilken ruta det är
	public void actionPerformed(ActionEvent e){
		if (buttonsDisabled || !game.isMyTurn()) {
			messageLabel.setText("Wait for it!");		
			return;
		}
		
		clickedButton(((Square) e.getSource()).getI(), ((Square) e.getSource()).getJ());
		
		ut.println("jag_har_spelat " + ((Square) e.getSource()).getI() + " " + ((Square) e.getSource()).getJ());
		ut.flush();
		System.out.println("Skickade: " + "jag_har_spelat " + ((Square) e.getSource()).getI() + " " + ((Square) e.getSource()).getJ());
	}

	private void updateBoard() {
		for (int i = 0; i < nRows; i++) {	
			for (int j = 0; j < nCols; j++) {
				String status = game.getStatus(i, j);
				if (status == "x") {
					this.board[i][j].setIcon(null);				
				} else {
					int value = Integer.parseInt(status);
					this.board[i][j].setIcon(new ImageIcon(images[value]));
				}
			}
		}
	}

	private void updateMessage() {
		String statusText = game.getMessage();
		messageLabel.setText(statusText);
	}
	
	// Swing Timer: http://stackoverflow.com/questions/1006611/java-swing-timer
	private ActionListener turnBackPair = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			game.nextMove();			// Turn back cards (x)
			updateBoard();
			updateMessage();
			buttonsDisabled = false;	// All buttons enabled
		}
	};

	public void setPlayerNumber(int playerNumber) {
		if (playerNumber == 1) {
			ut.println("gameboard " + game.getBoardAsString());
			ut.flush();
			System.out.println("Skickade: " + "gameboard " + game.getBoardAsString());
		}
		game.setPlayerNumber(playerNumber);
		updateMessage();
	}

}


