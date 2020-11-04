// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	String id;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String loginId, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		this.id = loginId; // Set the id of this client
		openConnection();

		// Send login command to server
		sendToServer("#login <" + this.id + ">");
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message
	 *            The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		// check if the message is a command
		if (message.charAt(0) == '#') {
			try {
				clientCommand(message); // send to method to see what the command is
			} catch (IOException e) {
				System.out.println("There was an error with the command");
			}
		} else { // if the message is not a command
			try {
				sendToServer(message); // send the message to the server
			} catch (IOException e) {
				clientUI.display("Could not send message to server.  Terminating client.");
				quit();
			}
		}
	}

	/**
	 * This method implements all the commands that the client can use.
	 */
	public void clientCommand(String command) throws IOException {
		String[] cmd = command.split(" "); // seperate the command with the value
		String commandType = cmd[0];
		String commandInput = cmd[1];
		// cmd [0] is the commandType "#" while cmd [1] is the command input
		if (commandType.equals("#quit")) { // client quitting
			sendToServer(commandType);
			quit();
		} else if (commandType.equals("#logoff")) { // client logging off
			// client logs off and disconnects from server
			System.out.println("Logging off. Disconnected from Server.");
			sendToServer(commandType);
			closeConnection();
		} else if (commandType.equals("#sethost")) { // client setting the host
			// client can set the host if they are currently disconnected from the server
			if (isConnected()) {
				System.out.println("Cannot set host while you are connected. First #logoff to use #sethost.");
			} else {
				// removing the "<>" from the command input
				String newHost = commandInput;
				newHost = newHost.replace("<", "");
				newHost = newHost.replace(">", "");
				System.out.println("Setting new host: " + newHost);
				// setting new host
				setHost(newHost);
			}
		} else if (commandType.equals("#setport")) { // client setting the port
			// client can set the port if they are currently disconnected form the server
			if (isConnected()) {
				System.out.println("Cannot set the port while you are connected. First #logoff to use #setport");
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
					setPort(portNum);
				} catch (Exception e) {
					System.out.println("Invalid port input");
				}
			}

		} else if (commandType.equals("#login")) { // client logs into server
			// Checks to see if they are logged in already
			if (isConnected()) {
				System.out.println("You are already logged in.");
			} else { // logs the client in if they are not already logged in
				openConnection();
				sendToServer("#login <" + this.id + ">");
			}

		} else if (commandType.equals("#gethost")) { // client wants to see the current host
			System.out.println (getHost());

		} else if (commandType.equals("#getport")) { // client want sto see the curren port
			System.out.println (getPort());
		}  else { // the client has not inputted in a valid command
			System.out.println ("Invalid command, valid commands are the following: ");
			System.out.println ("#quit");
			System.out.println ("#logoff");
			System.out.println ("#sethost <host>");
			System.out.println ("#setport <port>");
			System.out.println ("#login");
			System.out.println ("#gethost");
			System.out.println ("#getport");
		}

	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * This method prints a message when the server shuts down while a client is
	 * connected.
	 */
	@Override
	protected void connectionClosed() {
		System.out.println("Connection with the server has closed.");
	}

	/**
	 * This method prints an connection exception.
	 */
	@Override
	protected void connectionException(Exception exception) {
		System.out.println("The server has shut down. Disconnecting from the server");
	}
}
// End of ChatClient class
