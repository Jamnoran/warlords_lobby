package game.io;

import game.vo.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientListener extends Thread {
	private LobbyServerDispatcher lobbyServerDispatcher;
    private ClientInfo clientInfo;
    private BufferedReader in;
 
    public ClientListener(ClientInfo aClientInfo, LobbyServerDispatcher lobbyServerDispatcher) throws IOException {
        clientInfo = aClientInfo;
	    this.lobbyServerDispatcher = lobbyServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

	/**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run(){
        try {
           while (!isInterrupted()) {
               String message = in.readLine();
               if (message == null) {
				   break;
			   }
			   lobbyServerDispatcher.handleClientRequest(clientInfo, new Message(message));
           }
        } catch (IOException ioex) {
           // Problem reading from socket (communication is broken)
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        clientInfo.clientSender.interrupt();
	    if (lobbyServerDispatcher != null) {
		    lobbyServerDispatcher.deleteClient(clientInfo);
	    }
    }
 
}