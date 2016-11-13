package Lab7;

import java.util.Scanner;

import Lab7.Boardgame;
import Lab7.MemoryModel;

class Text15 {
	
	private final static int nRows = 4;
	private final static int nCols = 4;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Boardgame thegame = new MemoryModel(false);                 
		System.out.println("\nWelcome to Memoryspelet!\n");

		
		while (true) {
			// Skriv ut aktuell ställning
			for (int i=0; i<nRows; i++) {
				for (int j=0; j<nCols; j++) {
					System.out.print("  " + thegame.getStatus(i,j)); 
				}					
				System.out.println();
			}
			// Printout if game is finished
			if (thegame.gameEnded()) {
				System.out.println("");
				System.out.println("The game is finished!");
				break;
			}

			// Tills draget är giltigt
			while (true) {
				System.out.println();
				System.out.print("Ditt drag: ");
				try {
					int i = scan.nextInt();  // hämta heltal från terminalfönstret
					int j = scan.nextInt();
					
					// If invalid move 
					if (thegame.move(i,j) == "invalid") {
						System.out.println();
						System.out.print("Uhhu! Are you trying to cheat? You cannot pick a turned card or a card outside the bord. (Hint: range 0 - 3)");
						continue;
					}
					break;						// OM allt rätt -  gå ur loopen, fortsätt
				} catch (Exception e) {			// Exception if int not given
					scan.nextLine();			// To be able to continue
					System.out.println("\nGive an int [0, 3]"); 	
				}				
			}

			//Shows the move
			for (int i=0; i<nRows; i++) {
				for (int j=0; j<nCols; j++) {
					System.out.print("  " + thegame.getStatus(i,j)); // getStatus
				}					
				System.out.println();
			}

			System.out.println();
			System.out.println(thegame.getMessage());	     // getMessage
			System.out.println();

			// Press ENTER to continue - pause the game after two turned cards to see the cards
			if (thegame.nextMove()) {
				System.out.print("Press ENTER to continue.");
				try{System.in.read();}
				catch(Exception e){}
				System.out.println();
			}
		}
	}
}

