package game.io.objects;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class ClientTypeRequest extends JsonRequest{
	@SerializedName("type")
	public Integer type;

	public ClientTypeRequest() {
	}
	public ClientTypeRequest(int clientType) {
		type = clientType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClientTypeRequest{" +
				"type=" + type +
				'}';
	}
}
