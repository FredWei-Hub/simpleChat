
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF {
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ServerConsole.
	 */
	EchoServer server;

	/**
	 * Scanner to read from the console
	 */
	Scanner fromConsole;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ServerConsole UI.
	 *
	 * @param host
	 *            The host to connect to.
	 * @param port
	 *            The port to connect on.
	 */
	public ServerConsole(int port) {
		server = new EchoServer(port);
		try {
			server.listen(); // checks if there are any connections
		} catch (Exception e) {
			
		}
		
	    // Read from console
	    fromConsole = new Scanner(System.in);
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console.
	 */
	public void accept() {
		try {

			String message;

			while (true) {
				message = fromConsole.nextLine();
				display(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the server and to all clients.
	 *
	 * @param message
	 *            The string to be displayed.
	 */
	@Override
	public void display(String message) {
		if (message.charAt(0) == '#') {
			try {
				serverCommand(message);
			} catch (IOException e) {
				System.out.println("There was an error with the command");
			}
		} else {
			System.out.println("SERVER MSG> " + message);
			server.sendToAllClients("SERVER MSG> " + message);
		}
	}

	/**
	 * This method implements all the commands that the server can use.
	 */
	public void serverCommand(String command) throws IOException {
		String[] cmd = command.split(" "); // seperate the command with the value
		String commandType = cmd[0];
		String commandInput = cmd[1];
		// cmd [0] is the commandType "#" while cmd [1] is the command input
		if (commandType.equals("#quit")) { // server quitting
			System.exit(0);
		} else if (commandType.equals("#stop")) { // server stopping
			// the server stops listening and informs all clients
			System.out.println("The server has stopped listening for new clients");
			server.sendToAllClients("The server has stopped listening for new clients");
			server.stopListening();
		} else if (commandType.equals("#close")) { // server closes
			// the server stops listening to new clients and closes
			System.out.println("The server has closed");
			server.sendToAllClients("The server has closed");
			server.stopListening();
			server.close();
		} else if (commandType.equals("#setport")) { // server setting the port
			// server can set the port if it is currently closed
			if (server.isListening()) {
				System.out.println("Can only set the port if the server is closed");
			} else {
				// removing the "<>" from the command input
				int portNum;
				String newPort = commandInput;
				newPort = newPort.replace("<", "");
				newPort = newPort.replace(">", "");
				// checking to see if the port input value is a valid number
				try {
					portNum = Integer.parseInt(newPort);
					System.out.println("Setting new port: " + portNum);
					server.setPort(portNum);
				} catch (Exception e) {
					System.out.println("Invalid port input");
				}
			}

		} else if (commandType.equals("#start")) { // server starts listening for clients
			// Checks to see if the server is already listening
			if (server.isListening()) {
				System.out.println("You are already listening");
			} else { // the server starts listening
				server.listen();
				System.out.println("The server has started listening for new clients");
				server.sendToAllClients("The server has started listening for new clients");
			}

		} else if (commandType.equals("#getport")) { // server wants to see current port
			System.out.println(server.getPort());
		} else { // the server has not inputted in a valid command
			System.out.println("Invalid command, valid commands are the following: ");
			System.out.println("#quit");
			System.out.println("#stop");
			System.out.println("#close");
			System.out.println("#setport <port>");
			System.out.println("#start");
			System.out.println("#getport");
		}
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args[0]
	 *            The host to connect to.
	 */
	public static void main(String[] args) {
		// Variables
		int port = 0;

		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = DEFAULT_PORT;
		}
		// creating new server console
		ServerConsole serv = new ServerConsole(port);
		serv.accept(); // Wait for console data
	}
}
// End of ConsoleChat class
