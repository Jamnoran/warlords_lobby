package game.io.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class JoinServerRequest extends JsonRequest {
	@SerializedName("hero_id")
	public Integer heroId;
	@SerializedName("game_type")
	public String gameType;

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
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
				"heroId='" + heroId + '\'' +
				", gameType='" + gameType + '\'' +
				'}';
	}
}
