package game.util;

import com.google.gson.Gson;
import game.io.WebserviceCommunication;
import game.io.objects.GameSlotRequest;
import game.io.objects.GameSlotResponse;
import game.io.objects.JsonRequest;
import game.io.objects.JsonResponse;
import game.logging.Log;
import game.vo.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Eric on 2017-03-10.
 */
public class GameServerUtil {

	private static final String TAG = GameServerUtil.class.getSimpleName();

	public static Server getGameServer(String gameType) {
		ArrayList<Server> lobbyServers = WebserviceCommunication.getGameServers();
		if (lobbyServers != null) {
			for (Server server : lobbyServers) {
				System.out.println(server.getId() + " Ip : " + server.getIp() + " : " + server.getPort());
				Log.i(TAG, "Connect and see if available for creating a games");
				GameSlotResponse gameSlotResponse = GameServerUtil.checkGameServer(server, gameType);
				if (gameSlotResponse.isAvailable()) {
					Log.i(TAG, "This server Have room! Join it!");
					server.setGameId(gameSlotResponse.getGameId());
					return server;
				} else {
					Log.i(TAG, "This server did not have room for a game");
				}
			}
		}else{
			Log.i(TAG, "Could not get servers");
		}
		return null;
	}


	private static GameSlotResponse checkGameServer(Server server, String gameType) {
		BufferedReader in;
		PrintWriter out;
		try {
			// Connect to Server
			Socket socket = new Socket(server.getIp(),server.getPort());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("Connected to server " + server.getIp() + ":" + server.getPort());

			out.println(new Gson().toJson(new GameSlotRequest(gameType)));
			out.flush();

			try {
				// Read messages from the server and print them
				String message;
				while ((message = in.readLine()) != null) {
					System.out.println(message);
					Gson gson = new Gson();
					JsonResponse response = gson.fromJson(message, JsonResponse.class);
					Log.i(TAG, "Response : " + response.toString());
					GameSlotResponse gameSlotResponse = gson.fromJson(message, GameSlotResponse.class);
					Log.i(TAG, "GameSlotResponse : " + gameSlotResponse.toString());
					socket.close();
					return gameSlotResponse;
				}
			} catch (IOException ioe) {
				System.err.println("Connection to server broken.");
				ioe.printStackTrace();
				WebserviceCommunication.removeGameServer(server.getIp(),server.getPort(), server.getId());
				return null;
			}
		} catch (IOException ioe) {
			Log.i(TAG, "Server is not online, remove it from list");
			WebserviceCommunication.removeGameServer(server.getIp(),server.getPort(), server.getId());
		}
		return null;
	}

}
