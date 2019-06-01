package game.io.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import game.logging.Log;
import game.vo.Message;

import java.io.Serializable;

public class JsonRequest implements Serializable {
	public static final String GAME_SLOT_AVAILABLE = "GAME_SLOT_AVAILABLE";

	private static final String TAG = JsonRequest.class.getSimpleName();
	@SerializedName("request_type")
	public String requestType;
	@SerializedName("user_id")
	public String user_id;
	@SerializedName("sign")
	public String sign = null;

	public JsonRequest() {
	}
	public JsonRequest(String requestType) {
		this.requestType = requestType;
	}

	public static JsonRequest parse(Message aMessage) {
        Gson gson = new GsonBuilder().create();
        JsonRequest request = null;
        if (aMessage != null && aMessage.getMessage() != null) {
			try {
				request = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}
		return request;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestType() {
        return requestType;
    }

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "JsonRequest{" +
				"requestType='" + requestType + '\'' +
				", user_id='" + user_id + '\'' +
				'}';
	}

	public void clearSign() {
		sign = null;
	}

	public String getSignAndClear() {
		String tempSign = sign;
		sign = null;
		return tempSign;
	}
}
