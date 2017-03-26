package game.io.objects;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class ClientTypeResponse extends JsonRequest{
	@SerializedName("type")
	public Integer type;

	public ClientTypeResponse() {
	}

	public ClientTypeResponse(int clientType) {
		type = clientType;
		setRequestType("CLIENT_TYPE_RESPONSE");
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClientTypeResponse{" +
				"type=" + type +
				'}';
	}
}
