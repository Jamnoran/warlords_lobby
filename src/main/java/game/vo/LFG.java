package game.vo;

import game.logging.Log;
import game.util.CalculationUtil;
import game.util.DatabaseUtil;

import java.util.ArrayList;

public class LFG {
	private static final String TAG = LFG.class.getSimpleName();

	// General stats
	public Integer id = null;
	private Integer heroId1 = null;
	private String heroClass1 = null;
	private Integer heroId2 = null;
	private String heroClass2 = null;
	private Integer heroId3 = null;
	private String heroClass3 = null;
	private Integer heroId4 = null;
	private String heroClass4 = null;
	private String gameType = null;
	private Integer highestLevel = null;
	private String lobbyId = null;
	private int heroesJoined = 0;
	private int maxPlayers = 4;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHeroId1() {
		return heroId1;
	}

	public void setHeroId1(Integer heroId1) {
		this.heroId1 = heroId1;
	}

	public Integer getHeroId2() {
		return heroId2;
	}

	public void setHeroId2(Integer heroId2) {
		this.heroId2 = heroId2;
	}

	public Integer getHeroId3() {
		return heroId3;
	}

	public void setHeroId3(Integer heroId3) {
		this.heroId3 = heroId3;
	}

	public Integer getHeroId4() {
		return heroId4;
	}

	public void setHeroId4(Integer heroId4) {
		this.heroId4 = heroId4;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getHeroClass1() {
		return heroClass1;
	}

	public void setHeroClass1(String heroClass1) {
		this.heroClass1 = heroClass1;
	}

	public String getHeroClass2() {
		return heroClass2;
	}

	public void setHeroClass2(String heroClass2) {
		this.heroClass2 = heroClass2;
	}

	public String getHeroClass3() {
		return heroClass3;
	}

	public void setHeroClass3(String heroClass3) {
		this.heroClass3 = heroClass3;
	}

	public String getHeroClass4() {
		return heroClass4;
	}

	public void setHeroClass4(String heroClass4) {
		this.heroClass4 = heroClass4;
	}

	public Integer getHighestLevel() {
		return highestLevel;
	}

	public void setHighestLevel(Integer highestLevel) {
		this.highestLevel = highestLevel;
	}

	public String getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(String lobbyId) {
		this.lobbyId = lobbyId;
	}

	public int getHeroesJoined() {
		return heroesJoined;
	}

	public void setHeroesJoined(int heroesJoined) {
		this.heroesJoined = heroesJoined;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getSqlInsertLFGQuery(String gameType) {
		return "INSERT INTO lfg set game_type=\"" + gameType + "\", lobby_id=\"" + lobbyId + "\"" + ", hero_id_1=\"" + heroId1 + "\", hero_class_1=\"" + heroClass1 + "\", max_players=\"" + maxPlayers + "\", heroes_joined=\"" + heroesJoined + "\"";
	}

	@Override
	public String toString() {
		return "LFG{" +
				"id=" + id +
				", heroId1=" + heroId1 +
				", heroClass1='" + heroClass1 + '\'' +
				", heroId2=" + heroId2 +
				", heroClass2='" + heroClass2 + '\'' +
				", heroId3=" + heroId3 +
				", heroClass3='" + heroClass3 + '\'' +
				", heroId4=" + heroId4 +
				", heroClass4='" + heroClass4 + '\'' +
				", gameType='" + gameType + '\'' +
				", highestLevel=" + highestLevel +
				", lobbyId='" + lobbyId + '\'' +
				", heroesJoined=" + heroesJoined +
				", maxPlayers=" + maxPlayers +
				'}';
	}
}
