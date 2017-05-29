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
	private String herolobby1 = null;
	private Integer heroId2 = null;
	private String heroClass2 = null;
	private String herolobby2 = null;
	private Integer heroId3 = null;
	private String heroClass3 = null;
	private String herolobby3 = null;
	private Integer heroId4 = null;
	private String heroClass4 = null;
	private String herolobby4 = null;
	private String gameType = null;
	private Integer highestLevel = null;
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

	public String getHerolobby1() {
		return herolobby1;
	}

	public void setHerolobby1(String herolobby1) {
		this.herolobby1 = herolobby1;
	}

	public String getHerolobby2() {
		return herolobby2;
	}

	public void setHerolobby2(String herolobby2) {
		this.herolobby2 = herolobby2;
	}

	public String getHerolobby3() {
		return herolobby3;
	}

	public void setHerolobby3(String herolobby3) {
		this.herolobby3 = herolobby3;
	}

	public String getHerolobby4() {
		return herolobby4;
	}

	public void setHerolobby4(String herolobby4) {
		this.herolobby4 = herolobby4;
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
		return "INSERT INTO lfg set game_type=\"" + gameType + "\", hero_lobby_1=\"" + herolobby1 + "\"" + ", hero_id_1=\"" + heroId1 + "\", hero_class_1=\"" + heroClass1 + "\", max_players=\"" + maxPlayers + "\", heroes_joined=\"" + heroesJoined + "\"";
	}

	@Override
	public String toString() {
		return "LFG{" +
				"id=" + id +
				", heroId1=" + heroId1 +
				", heroClass1='" + heroClass1 + '\'' +
				", herolobby1='" + herolobby1 + '\'' +
				", heroId2=" + heroId2 +
				", heroClass2='" + heroClass2 + '\'' +
				", herolobby2='" + herolobby2 + '\'' +
				", heroId3=" + heroId3 +
				", heroClass3='" + heroClass3 + '\'' +
				", herolobby3='" + herolobby3 + '\'' +
				", heroId4=" + heroId4 +
				", heroClass4='" + heroClass4 + '\'' +
				", herolobby4='" + herolobby4 + '\'' +
				", gameType='" + gameType + '\'' +
				", highestLevel=" + highestLevel +
				", heroesJoined=" + heroesJoined +
				", maxPlayers=" + maxPlayers +
				'}';
	}
}
