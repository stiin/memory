package Lab7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Lab7.Boardgame;
import Lab7.MemoryModel;
import Lab7.ViewControl;

public class RunGame {

	public static void main(String [] args) {	
		// socket ansluter till servern
		String serverAdress = "localhost";
		
		if (args.length > 0) {
			serverAdress = args[0];
		}

		try {
			Socket socket = new Socket(serverAdress,4713);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter ut = new PrintWriter(socket.getOutputStream());

			// MEMORY
			Boardgame game = new MemoryModel(true);
			ViewControl viewControl = new ViewControl("Memory!", game, 4, 4, ut);

			while (true) {
				String str = in.readLine();
				String[] command = str.split("\\s+");
				
				System.out.println("Fick meddelande: " + str);
				
				if (command[0].equals("gameboard")) {
					game.setBoardFromString(command[1]);
				}

				if (command[0].equals("you_are_player_number")) {
					viewControl.setPlayerNumber(Integer.parseInt(command[1])); 
				}
				
				if (command[0].equals("jag_har_spelat"))
				{
					// Motståndaren har tryckt på en knapp i j
					int i, j;
					i = Integer.parseInt(command[1]);
					j = Integer.parseInt(command[2]);
					viewControl.clickedButton(i, j);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


