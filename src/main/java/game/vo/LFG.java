package game.vo;

import game.logging.Log;
import game.util.CalculationUtil;
import game.util.DatabaseUtil;

import java.util.ArrayList;

public class LFG {
	private static final String TAG = LFG.class.getSimpleName();

	// General stats
	public Integer id = null;
	private Integer userId = null;
	private String gameType = null;
	private Integer heroId = null;
	private String classType = null;
	private Integer highestLevel = null;
	private String lobbyId = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}


	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Integer getHighestLevel() {
		return highestLevel;
	}

	public void setHighestLevel(Integer highestLevel) {
		this.highestLevel = highestLevel;
	}

	public String getSqlInsertLFGQuery(String gameType) {
		return "INSERT INTO `warlords`.`lfg` (`id`, `user_id`, `game_type`, `hero_id`) VALUES (NULL, '" + getUserId() + "', '" + gameType + "', '" + getId() + "')";
	}
}
