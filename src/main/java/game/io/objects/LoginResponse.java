package game.io.objects;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class LoginResponse extends JsonResponse{
	@SerializedName("user_id")
	public Integer userId;

	public LoginResponse() {
	}

	public LoginResponse(Integer uId) {
		this.userId = uId;
		setResponseType("LOGIN_USER");
	}

}
