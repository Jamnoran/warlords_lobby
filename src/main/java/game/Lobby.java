package game;

import com.google.gson.Gson;
import game.io.*;
import game.io.objects.JsonRequest;
import game.io.objects.RegisterLobbyResponse;
import game.logging.Log;
import game.util.ConfigUtil;
import game.vo.Message;
import game.vo.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Eric on 2017-03-03.
 */
public class Lobby {
	private static final String TAG = Lobby.class.getSimpleName();
	// Configuration;
	static Integer portNumber = 2075;

	private static LobbyServerDispatcher lobbyServerDispatcher;
	private static ArrayList<Server> otherLobbys;

	public static ArrayList<Server> getOtherLobbys() {
		return otherLobbys;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.i(TAG, "Lobby is up and running!");

		//portNumber = ConfigUtil.getPort();

		Log.i(TAG, "Read this port from file: " + portNumber);

		lobbyServerDispatcher = new LobbyServerDispatcher();
		lobbyServerDispatcher.start();

		// Send request to webservice that we have started a lobby on this ip + port
		RegisterLobbyResponse response = WebserviceCommunication.sendLobbyOnline(portNumber);
		lobbyServerDispatcher.setLobbyId(response.getId());

		otherLobbys = WebserviceCommunication.getLobbyServers();
		for(Server lobby : otherLobbys){
			if (!lobby.getId().equals(response.getId())) {
				Log.i(TAG, "Added other lobby to list : " + lobby.getIp() + " : " + lobby.getPort());
				ClientInfo clientInfo = lobby.startConnection(lobbyServerDispatcher.getNextClientId(), lobbyServerDispatcher);
				if (clientInfo != null) {
					lobby.setClientInfoId(clientInfo.getId());
					lobbyServerDispatcher.addClient(clientInfo);
				}else {
					Log.i(TAG, "Removing server we could not contact " + lobby.getIp() + ":" +lobby.getPort() + " - " + lobby.getId());
					WebserviceCommunication.removeLobby(lobby.getIp(), lobby.getPort(), lobby.getId());
				}
			}
		}

		startAccepting();
	}

	private static void startAccepting() {
		// Open server socket for listening
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			Log.i(TAG, "Lobby started on port " + portNumber);
		} catch (IOException se) {
			Log.i(TAG, "Can not start listening on port " + portNumber);
			se.printStackTrace();
			System.exit(-1);
		}

		// Accept and handle client connections
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i(TAG, "Incoming connection");

			// Create the client
			ClientInfo clientInfo = createClient(socket, lobbyServerDispatcher);

			lobbyServerDispatcher.addClient(clientInfo);
			// Add client to server
			Log.i(TAG, "Added client to lobby that now has this many connections: " + lobbyServerDispatcher.getClientCount());
			lobbyServerDispatcher.dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonRequest("CLIENT_TYPE"))));
		}
	}


	private static ClientInfo createClient(Socket socket, LobbyServerDispatcher lobbyServerDispatcher) {
		try {
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.mSocket = socket;
			clientInfo.id = lobbyServerDispatcher.getNextClientId();
			ClientListener clientListener = new ClientListener(clientInfo, lobbyServerDispatcher);
			ClientSender clientSender = new ClientSender(clientInfo, lobbyServerDispatcher);
			clientInfo.clientListener = clientListener;
			clientInfo.clientSender = clientSender;
			clientListener.start();
			clientSender.start();
			return clientInfo;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}
