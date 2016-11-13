package Lab7;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.print.attribute.standard.PrinterLocation;

public class GameServer {

	public static void main(String[] args) {
		try {
			ServerSocket sock = new ServerSocket(4713,2);
			
			Socket socket1 = sock.accept();
			Socket socket2 = sock.accept();
			
			// Båda spelarna har anslutit
			BufferedReader in1, in2;
			PrintWriter ut1, ut2;
			
			// Client 1
			in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
			ut1 = new PrintWriter(socket1.getOutputStream());
			
			// Client 2 
			in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
			ut2 = new PrintWriter(socket2.getOutputStream());

			ut1.println("you_are_player_number 1"); ut1.flush();
			ut2.println("you_are_player_number 2"); ut2.flush();

			ClientHandler clienthandler1 = new ClientHandler(in1, ut2);
			ClientHandler clienthandler2 = new ClientHandler(in2, ut1);
						
			clienthandler1.start();
			clienthandler2.start();
		}
		catch(IOException e)
		{System.err.println(e);
		}
	}
} 

class ClientHandler extends Thread {
	BufferedReader in;
	PrintWriter ut;
	public ClientHandler(BufferedReader in, PrintWriter ut) {
		this.in = in;
		this.ut = ut;
	}
	
	public void run() {
		while (true) {
			try {
				String msg = in.readLine();
				ut.println(msg);
				ut.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
