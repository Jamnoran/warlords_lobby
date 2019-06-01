package game.io.objects;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class LoginResponse extends JsonResponse{
	@SerializedName("user_id")
	public Integer userId;
	@SerializedName("login_key")
	public String loginKey;

	public LoginResponse() {
	}

	public LoginResponse(Integer uId, String lKey) {
		this.userId = uId;
		this.loginKey = lKey;
		setCode("200");
		setResponseType("LOGIN_USER");
	}
}
