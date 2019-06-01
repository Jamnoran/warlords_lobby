package game.io.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eric on 2017-03-10.
 */
public class UsernameResponse extends JsonResponse{

	@SerializedName("username")
	private String username;

	public UsernameResponse(String username) {
		this.username = username;
		setCode("200");
		setResponseType("USERNAME_RESPONSE");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UsernameResponse{" +
				", username='" + username + '\'' +
				'}';
	}
}
