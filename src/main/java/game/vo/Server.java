package game.vo;

import game.io.ClientInfo;
import game.io.ClientListener;
import game.io.ClientSender;
import game.io.LobbyServerDispatcher;
import game.logging.Log;

import java.io.*;
import java.net.Socket;

/**
 * Created by Eric on 2017-03-03.
 */
public class Server {
	private String id;
	private String ip;
	private int port;
	private String version;
	private int clientInfoId = -1;
	private String gameId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean canCreateNewGame() {
		return false;
	}

	public int getClientInfoId() {
		return clientInfoId;
	}

	public void setClientInfoId(int clientInfoId) {
		this.clientInfoId = clientInfoId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public ClientInfo startConnection(int id, LobbyServerDispatcher lobbyServerDispatcher) {
		try {
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.setTypeOfConnect(ClientInfo.LOBBY);
			Socket socket = new Socket(getIp(), getPort());
			clientInfo.mSocket = socket;
			clientInfo.id = id;
			clientInfo.clientSender = new ClientSender(clientInfo, lobbyServerDispatcher);
			clientInfo.clientListener = new ClientListener(clientInfo, lobbyServerDispatcher);
			clientInfo.clientListener.start();
			clientInfo.clientSender.start();
			setClientInfoId(id);
			return clientInfo;
		} catch (IOException ioe) {
			//ioe.printStackTrace();
			Log.i(Server.class.getSimpleName(), "Could not contact this lobby");
		}
		return null;
	}



	@Override
	public String toString() {
		return "Server{" +
				"id='" + id + '\'' +
				", ip='" + ip + '\'' +
				", port='" + port + '\'' +
				", version='" + version + '\'' +
				", clientInfoId=" + clientInfoId +
				'}';
	}
}
