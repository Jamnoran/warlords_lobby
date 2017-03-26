package game.io.objects;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class GameFoundResponse extends JsonResponse{
	@SerializedName("server_ip")
	public String serverIp;
	@SerializedName("server_port")
	public int serverPort;
	@SerializedName("server_id")
	public String serverId;
	@SerializedName("hero_id")
	public Integer heroId;
	@SerializedName("game_id")
	public String gameId;

	public GameFoundResponse() {
	}

	public GameFoundResponse(String ip, int port, String id, Integer hId, String gId) {
		this.serverIp = ip;
		this.serverPort = port;
		this.serverId = id;
		this.heroId = hId;
		this.gameId = gId;
		setResponseType("GAME_FOUND_RESPONSE");
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@Override
	public String toString() {
		return "GameFoundResponse{" +
				"serverIp='" + serverIp + '\'' +
				", serverPort='" + serverPort + '\'' +
				", serverId='" + serverId + '\'' +
				", heroId=" + heroId +
				", gameId='" + gameId + '\'' +
				'}';
	}
}
