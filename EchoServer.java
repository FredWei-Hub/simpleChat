
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String message = String.valueOf(msg);
		String[] cmd = message.split(" "); // seperate the command with the value
		String commandType = cmd[0];
		String commandInput = cmd[1];
		if (commandType.equals("#login")) { // client logs on
			String setId = commandInput;
			setId = setId.replace("<", "");
			setId = setId.replace(">", "");
			// Output client message
			System.out.println("A new client is attempting to connect to the server.");
			System.out.println("Message received: " + message + " from" + client.getInfo("id"));
			// Set the id of the client
			client.setInfo("id", setId);
			System.out.println("<" + setId + "> has logged on");
		} else {
			System.out.println("<" + client.getInfo("id") + "> sent a message: " + message);
			this.sendToAllClients("<" + client.getInfo("id") + "> sent a message: " + message);
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0]
	 *            The port number to listen on. Defaults to 5555 if no argument is
	 *            entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	/**
	 * This method prints a message when a client connects.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("A client has connected to the server");
	}

	/**
	 * This method prints a message when a client disconnects.
	 */
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("A client has disconncted from the server");
	}

	/**
	 * This method implements the commands that the client can use.
	 */
	public void clientCommand(String command) {
		if (command.charAt(0) == '#') {

		}
		if (command.equals("#quit")) {

		} else if (command.equals("#stop")) {

		} else if (command.equals("#close")) {

		} else if (command.equals("#setport")) {

		} else if (command.equals("#start")) {

		} else if (command.equals("#getport")) {

		}
	}
}
// End of EchoServer class
