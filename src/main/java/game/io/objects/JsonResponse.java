package game.io.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonResponse implements Serializable {
	public static final Integer CODE_SEARCHING_FOR_GROUP = 1050;
	public static final String JOIN_GAME_RESPONSE = "JOIN_GAME_RESPONSE";
	@SerializedName("response_type")
	public String responseType;
	@SerializedName("code")
	public String code;
	@SerializedName("message")
	public String message;

	public JsonResponse() {
	}

	public JsonResponse(String resp, Integer codeToUser) {
		responseType = resp;
		code = "" + codeToUser;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public String toString() {
		return "JsonResponse{" +
				"responseType='" + responseType + '\'' +
				", code='" + code + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
