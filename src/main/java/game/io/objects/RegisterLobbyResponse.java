package game.io.objects;

import com.google.gson.annotations.SerializedName;

public class RegisterLobbyResponse extends JsonResponse {

	@SerializedName("id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RegisterLobbyResponse{" +
				", id='" + id + '\'' +
				'}';
	}
}
