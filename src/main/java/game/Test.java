package game;

import game.logging.Log;
import game.util.DatabaseUtil;
import game.vo.Hero;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		//String json = "{\"request_type\":\"CLIENT_TYPE\"}";
		//System.out.println("Text: " + json);
		//String responseTypeString = "\"request_type\":\"";
		//String newJson = json.substring(json.indexOf(responseTypeString) + responseTypeString.length());
		//System.out.println(newJson.substring(0, newJson.indexOf("\"")));
		Hero hero = DatabaseUtil.getHero(16);
		Log.i(TAG, "Hero : " + hero.toString());
	}
}
