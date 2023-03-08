package game.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import game.Test;
import game.io.objects.JsonResponse;
import game.io.objects.RegisterLobbyResponse;
import game.logging.Log;
import game.vo.Server;
import game.vo.ServerList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Eric on 2017-03-05.
 */
public class WebserviceCommunication {
	private static final String TAG = WebserviceCommunication.class.getSimpleName();
	private static String main = "http://jamnoran.se";
	private static String lobbyUrl = main + "/warlords_webservice/lobbys.json";
	private static String gameUrl = main + "/warlords_webservice/game_servers.json";
	private static String  registerLobby = main + "/warlords_webservice/register_lobby.php";
	private static String  removeLobby = main + "/warlords_webservice/remove_lobby.php";
	private static String  removeGameServer = main + "/warlords_webservice/remove_server.php";

	private static final String USER_AGENT = "Mozilla/5.0";
	private static String ip = "158.174.113.85";
	// private static String ip = "2.248.122.35";
	private static String version = "1";

	public static ArrayList<Server> getLobbyServers() {
		Gson gson = new Gson();
		try {
			ServerList list = gson.fromJson(readUrl(lobbyUrl), ServerList.class);
			if (list != null) {
				return list.getLobby();
			} else {
				return null;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Server> getGameServers() {
		Gson gson = new Gson();
		try {
			ServerList list = gson.fromJson(readUrl(gameUrl), ServerList.class);
			if (list != null) {
				return list.getGame();
			} else {
				return null;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RegisterLobbyResponse sendLobbyOnline(int portNumber) {
		String params;
		if(ip != null){
			params = "ip=" + ip + "&port=" + portNumber + "&version=" + version;
		}else{
			params = "port=" + portNumber + "&version=" + version;
		}
		try {
			return (RegisterLobbyResponse) sendRequest(params, registerLobby, new RegisterLobbyResponse());
		} catch (Exception e) {
			Log.i(TAG, "Could  not sendRequest to registerLobby");
			e.printStackTrace();
		}
		return null;
	}

	public static void removeLobby(String ip, int port, String id) {
		String params = "ip=" + ip + "&port=" + port + "&id=" + id;
		sendRequest(params, removeLobby, new JsonResponse());
	}

	public static void removeGameServer(String ip, int port, String id) {
		String params = "ip=" + ip + "&port=" + port + "&id=" + id;
		sendRequest(params, removeGameServer, new JsonResponse());
	}

	private static JsonResponse sendRequest(String params, String url, JsonResponse responseObject) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			Log.i(TAG, "Sending 'POST' request to URL : " + url);
			Log.i(TAG, "Post parameters : " + params);
			Log.i(TAG, "Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return new Gson().fromJson(response.toString(), responseObject.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readUrl(String urlString) {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1) {
				buffer.append(chars, 0, read);
			}
			return buffer.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

}
