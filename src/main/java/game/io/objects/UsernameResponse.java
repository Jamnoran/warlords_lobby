package game.io.objects;

/**
 * Created by Eric on 2017-03-10.
 */
public class UsernameResponse {

	private String response_type = "USERNAME_RESPONSE";
	public String username;

	public UsernameResponse(String username) {
		this.username = username;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
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
				"response_type='" + response_type + '\'' +
				", username='" + username + '\'' +
				'}';
	}
}
