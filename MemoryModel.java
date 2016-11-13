package Lab7;

import java.util.ArrayList;

public class MemoryModel implements Boardgame {

	private final static int nRows = 4;
	private final static int nCols = 4;
	private final int numberOfCardPairs = (nRows * nCols)/2;	// Number of card pairs
	private String [][] status = new String [nRows][nCols];		// Spelplanen som den ses av spelarna
	private int [][] facit = new int [nRows][nCols];			// Spelplanen - facit

	private ArrayList<Integer> cards;
	private int [] firstCardIndex = new int [2];				// Indexes for the first card
	private int []  secondCardIndex = new int [2];				// Indexes for the second card

	private int playerTurn = 1;
	private int myPlayerNumber = 1;
	private boolean wasAMatch = true;
	private int user1Score = 0;
	private int user2Score = 0;
	private boolean isFirstMove = true;

	private boolean lastMoveWasInvalid = false;

	private boolean hasGui;

	private enum NextMove {							
		FirstCard, 
		SecondCard
	};
	NextMove nextMove = NextMove.FirstCard;

	// CONSTRUCTOR
	public MemoryModel(boolean hasGui) {
		this.hasGui = hasGui;
		populateBoard();
	}

	// GET STATUS
	public String getStatus(int i, int j) {
		return status[i][j];
	}

	// MOVE
	public String move(int i, int j) {
		isFirstMove =  false;

		lastMoveWasInvalid = true;
		if (i < 0 || i > nRows-1 || j < 0 || j > nCols-1) {		// Cannot pick a card outside the board
			return "invalid";
		}

		if (! status[i][j].equals("x")){						// Cannot pick an already turned card
			return "invalid";
		}

		lastMoveWasInvalid = false;
		status[i][j] = Integer.toString(facit[i][j]);			// Vänder upp kortet
		if (nextMove == NextMove.FirstCard) {
			// FIRST DRAW
			firstCardIndex[0] = i;
			firstCardIndex[1] = j;
			nextMove = NextMove.SecondCard;
			return "valid";

		} else {
			// SECOND DRAW			
			secondCardIndex[0] = i;
			secondCardIndex[1] = j;
			nextMove = NextMove.FirstCard;

			if (facit[firstCardIndex[0]][firstCardIndex[1]] == facit[secondCardIndex[0]][secondCardIndex[1]]) {		// If first card == second card
				wasAMatch = true;																				// If a match: it is the same user's turn
				if (playerTurn == 1) {
					user1Score++;
				} else {
					user2Score++;
				}
				return "match"; 		//"It's a match!

			} else {					// "No match!"
				wasAMatch = false;		// If no match: the other user's turn
				if (playerTurn == 1) {
					playerTurn = 2;
				} else {
					playerTurn = 1;
				}
			}
		}
		return "no match";
	}

	// GAME ENDED
	public boolean gameEnded() {
		for (int i = 0; i<nRows; i++) {
			for (int j = 0; j<nCols; j++) {
				if (status[i][j].equals("x")) { 	// If one card is marked as x: the game is not ended
					return false;
				} 				
			}
		}
		return true;								// If no card is x: the game is ended
	}

	// GET MESSAGE
	// Prints which player's turn it is + if it was a match or not
	public String getMessage() {

		// With GUI
		if (hasGui) {			
			String message = "<HTML>";
			if (isFirstMove) {
				if (playerTurn == myPlayerNumber) {
					message += "It's your turn!";
				} else {
					message += "It's your opponent's turn!";
				}
			} else if (gameEnded()) {
				message += "Game is over!";
				if (myPlayerNumber == 1) {
					message += "<Br>YOUR SCORE: " + user1Score;
					message += "<Br>OPPONENT'S SCORE: " + user2Score;
				} else {
					message += "<Br>YOUR SCORE: " + user2Score;
					message += "<Br>OPPONENT'S SCORE: " + user1Score;
				}
				
			} else if (lastMoveWasInvalid) {
				if (playerTurn == myPlayerNumber) {
					message += "Uhhu! Are you trying to cheat?";
				} else {

				}
					
			}else if (nextMove == NextMove.FirstCard && wasAMatch == true) {
				if (playerTurn == myPlayerNumber) {
					message += "You had a match! It's your turn again.";
				} else {
					message += "Your opponent had a match! It's your opponent's turn again.";
					
				}
				
				if (myPlayerNumber == 1) {
					message += "<Br>YOUR SCORE: " + user1Score;
					message += "<Br>OPPONENT'S SCORE: " + user2Score;
				} else {
					message += "<Br>YOUR SCORE: " + user2Score;
					message += "<Br>OPPONENT'S SCORE: " + user1Score;
				}
			} else if (nextMove == NextMove.FirstCard && wasAMatch == false) {
				
				if (playerTurn == myPlayerNumber) {
					message += "It was not a match! It's your turn!";
				} else {
					message += "It was not a match! Now it's your opponent's turn.";
				}
			} else {

				if (playerTurn == myPlayerNumber) {
					message += "Pick another card!";
				} else {
					message += "";
				}
			}
			return message + "</HTML>";
		} else {
			// Without GUI
			String message = "";
			if (nextMove == NextMove.FirstCard && wasAMatch == true) {
				message += "You picked card " + firstCardIndex[0] +"," + firstCardIndex[1] + " with value: " + facit[firstCardIndex[0]][firstCardIndex[1]] + ". \nYou had a match! It's your turn again.";
				message += "\n\nUSER 1 SCORE: " + user1Score;
				message += "\nUSER 2 SCORE: " + user2Score;
			} else if (nextMove == NextMove.FirstCard && wasAMatch == false) {
				return("You picked card " + firstCardIndex[0] +"," + firstCardIndex[1] + " with value: " + facit[firstCardIndex[0]][firstCardIndex[1]] + ". \nIt was not a match! Now it's your opponent's turn.");
			} else {
				return("You picked card " + firstCardIndex[0] +"," + firstCardIndex[1] + " with value: " + facit[firstCardIndex[0]][firstCardIndex[1]] + ". Now pick another card!");
			}
			return message;
		}
	}

	// NEXT MOVE - turn back cards
	public boolean nextMove() {
		if (nextMove == NextMove.FirstCard && wasAMatch == false){	// If there is no match - turn the cards again
			status[firstCardIndex[0]][firstCardIndex[1]] = "x";
			status[secondCardIndex[0]][secondCardIndex[1]] = "x";
			return true;
		} else {
			return false;
		}
	}

	// CREATE CARDS AND POPULATE BOARD
	private void populateBoard (){
		int i; int j;
		// CREATE CARDS 0-numberOfCardPairs 
		cards = new ArrayList<Integer>();		
		for (i = 0; i < numberOfCardPairs; i++) {	 
			cards.add(i);
			cards.add(i);
		}		
		// POPULATE BOARD
		for (i = 0; i<nRows; i++) {
			for (j=0; j<nCols; j++) {
				int randomIndex = (int) (Math.random() * cards.size()) ;
				facit[i][j] = cards.get(randomIndex);
				cards.remove(randomIndex);
				status[i][j] = "x";						
			}
		}
	}

	public void setBoardFromString(String str) {
		System.out.println("setBoardFromString: " + str);
		int i; int j;
		String[] boardString = str.split(",");
		for (i = 0; i<nRows; i++) {
			for (j=0; j<nCols; j++) {
				facit[i][j] = Integer.parseInt(boardString[j + i * nCols]);
			}
		}
	}

	public String getBoardAsString() {
		String str = "";
		int i; int j;
		for (i = 0; i<nRows; i++) {
			for (j=0; j<nCols; j++) {
				str += facit[i][j] + ",";
			}
		}
		System.out.println("getBoardAsString: " + str);
		return str;
	}
	
	public void setPlayerNumber(int i) {
		System.out.println("setPlayerNumber: " + i);
		if (i == 1) {
			myPlayerNumber = 1;			
			wasAMatch = true;
		} else {
			myPlayerNumber = 2;
			wasAMatch = false;
		}
		
		playerTurn = 1;
	}

	public boolean isMyTurn() {
		return myPlayerNumber == playerTurn;
	}
}


