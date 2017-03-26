package game.io.objects;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class ServerInfoResponse extends JsonResponse{
	@SerializedName("clients")
	public String clients;


	public ServerInfoResponse() {
		setResponseType("SERVER_INFO");
		clients = "0";
	}

	public String getClients() {
		return clients;
	}

	public void setClients(String clients) {
		this.clients = clients;
	}

	@Override
	public String toString() {
		return "ServerInfoResponse{" +
				", clients='" + clients + '\'' +
				'}';
	}
}
