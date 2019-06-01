package game.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.io.objects.JsonRequest;
import game.logging.Log;
import game.vo.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AuthenticationService {

	private static final String TAG = AuthenticationService.class.getSimpleName();

	public static boolean checkAuthentication(JsonRequest request, User user, String macFromClient){
//		String result = "56EFD0189020ACD9194E34B046A0F169750AE89CD2A08AEDC6CC666397984C66";
//		String key = "6667F8F45AB4187AD420D1ACF64B1B8B";

		// Need to have null values shown since C# can't do this
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();
		Gson gsonWithNullValues = gsonBuilder.create();
		String convertedJson = gsonWithNullValues.toJson(request);

		Log.i(TAG, "Removed json without sign : " + convertedJson);
		String calculation = null;
		try {
			calculation = encode(user.getLoginKey(), convertedJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "Result : " + calculation);
		return(macFromClient.equals(calculation));
	}


	private static String encode(String key, String data) throws Exception {
		Mac sha256HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256HMAC.init(secret_key);

		return bytesToHex(sha256HMAC.doFinal(data.getBytes("UTF-8")));
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
