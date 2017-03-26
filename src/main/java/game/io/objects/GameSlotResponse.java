package game.io.objects;

/**
 * Created by Eric on 2017-03-10.
 */
public class GameSlotResponse {

	private String response_type = "GAME_SLOT_RESPONSE";
	public boolean available = false;
	public String gameId;

	public GameSlotResponse(boolean available) {
		this.available = available;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@Override
	public String toString() {
		return "GameSlotResponse{" +
				"response_type='" + response_type + '\'' +
				", available=" + available +
				", gameId='" + gameId + '\'' +
				'}';
	}
}
