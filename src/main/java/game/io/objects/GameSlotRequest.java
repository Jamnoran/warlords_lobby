package game.io.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameSlotRequest extends JsonRequest {
	@SerializedName("game_type")
	public String gameType;

	public GameSlotRequest(String gameType) {
		setRequestType(JsonRequest.GAME_SLOT_AVAILABLE);
		this.gameType = gameType;
	}


	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	@Override
	public String toString() {
		return "JoinServerRequest{" +
				"gameType='" + gameType + '\'' +
				'}';
	}
}
