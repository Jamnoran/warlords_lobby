package game.io.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class UpdateUsernameRequest extends JsonRequest{
	@SerializedName("username")
	public String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UpdateUsernameRequest{" +
				"username='" + username + '\'' +
				'}';
	}
}
