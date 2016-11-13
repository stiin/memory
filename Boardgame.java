package Lab7;

/* 
 * Stiin
 * DD2385 Lab 2 MVC
 * 20160423
 */

public interface Boardgame {
	public String move(int i, int j); 		// "invalid" - if invalid move, "match" - if it's a pair, "no match" - if there is no pair
	public String getStatus(int i, int j);
	public String getMessage();
	public boolean nextMove();	      // Turn back pairs 
	public boolean gameEnded();
	public String getBoardAsString();
	public void setBoardFromString(String string);
	public void setPlayerNumber(int playerNumber);
	public boolean isMyTurn();
}

