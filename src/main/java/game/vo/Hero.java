package game.vo;

import game.logging.Log;
import game.util.CalculationUtil;
import game.util.DatabaseUtil;

import java.util.ArrayList;

public class Hero {
	public static final String WARRIOR = "WARRIOR";
	public static final String PRIEST = "PRIEST";
	private static final String TAG = Hero.class.getSimpleName();

	// General stats
	public Integer id = null;
	private Integer user_id = null;
	private Integer xp = 0;
	private Integer topGameLvl = 0;
	// Need xpToLevelUp as well
	private Integer level = 1;
	private boolean alive = true;
	private float positionX = 6.0f;
	private float positionZ = 5.0f;
	private float desiredPositionX = 6.0f;
	private float desiredPositionZ = 5.0f;
	private String class_type = "WARRIOR";
	private transient ArrayList<Ability> abilities;

	// Hero stats
	private Integer hp;
	private Integer maxHp;
	private Integer resource;
	private Integer maxResource;
	private float attackRange = 3.0f;
	private transient Integer hpRegen = 0;
	private transient Integer resourceRegen = 0;
	private transient Integer strength;
	private transient Integer intelligence;
	private transient Integer stamina;
	private transient Integer dexterity;
	private transient Integer baseAttackDamage = 2;
	private transient Integer baseMaxAttackDamage = 4;
	private transient float attackStrScaling = 0.1f;
	private transient float attackIntScaling = 0.1f;
	private transient float criticalMultiplier = 2.0f;
	private transient float criticalChance = 0.25f;
	private transient boolean stairsPressed = false;
	private transient long timeLastAuto = 0;
	private transient float baseAttackSpeed = 1.0f;
	private transient int baseXpForLevel = 1000;
	private transient float xpScale = 0.1f;

	public Hero() {
	}

	public Hero(Integer user_id) {
		this.user_id = user_id;
	}

	public Hero(String user_id) {
		this.user_id = Integer.parseInt(user_id);
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getXp() {
		return xp;
	}

	public void setXp(Integer xp) {
		this.xp = xp;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getTopGameLvl() {
		return topGameLvl;
	}

	public void setTopGameLvl(Integer topGameLvl) {
		this.topGameLvl = topGameLvl;
	}

	public Integer getResource() {
		return resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}

	public Integer getMaxResource() {
		return maxResource;
	}

	public void setMaxResource(Integer maxResource) {
		this.maxResource = maxResource;
	}

	public Integer getHpRegen() {
		return hpRegen;
	}

	public void setHpRegen(Integer hpRegen) {
		this.hpRegen = hpRegen;
	}

	public float getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}

	public Integer getResourceRegen() {
		return resourceRegen;
	}

	public void setResourceRegen(Integer resourceRegen) {
		this.resourceRegen = resourceRegen;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public float getPositionX() {
		return positionX;
	}
	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public float getDesiredPositionX() {
		return desiredPositionX;
	}

	public void setDesiredPositionX(float desiredPositionX) {
		this.desiredPositionX = desiredPositionX;
	}

	public float getDesiredPositionZ() {
		return desiredPositionZ;
	}

	public void setDesiredPositionZ(float desiredPositionZ) {
		this.desiredPositionZ = desiredPositionZ;
	}

	public String getClass_type() {
		return class_type;
	}

	public void setClass_type(String class_type) {
		this.class_type = class_type;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(Integer maxHp) {
		this.maxHp = maxHp;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Integer getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(Integer intelligence) {
		this.intelligence = intelligence;
	}

	public Integer getStamina() {
		return stamina;
	}

	public void setStamina(Integer stamina) {
		this.stamina = stamina;
	}

	public Integer getDexterity() {
		return dexterity;
	}

	public void setDexterity(Integer dexterity) {
		this.dexterity = dexterity;
	}

	public void setStairsPressed() {
		stairsPressed = true;
	}

	public boolean isStairsPressed() {
		return stairsPressed;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		if (this.abilities == null) {
			this.abilities = new ArrayList<>();
		}
		this.abilities = abilities;
	}


	@Override
	public String toString() {
		return "Hero{" +
				"id=" + id +
				", user_id=" + user_id +
				", xp=" + xp +
				", level=" + level +
				", topGameLvl=" + topGameLvl +
				", positionX=" + positionX +
				", positionZ=" + positionZ +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionZ=" + desiredPositionZ +
				", class_type='" + class_type + '\'' +
				", hp=" + hp +
				", alive=" + alive +
				", attackRange=" + attackRange +
				", maxHp=" + maxHp +
				", strength=" + strength +
				", intelligence=" + intelligence +
				", stamina=" + stamina +
				", dexterity=" + dexterity +
				", baseAttackDamage=" + baseAttackDamage +
				", baseMaxAttackDamage=" + baseMaxAttackDamage +
				", attackStrScaling=" + attackStrScaling +
				", criticalMultiplier=" + criticalMultiplier +
				", criticalChance=" + criticalChance +
				'}';
	}

	public String getSqlInsertLFGQuery(String gameType, String lobbyId) {
		return "INSERT INTO `warlords`.`lfg` (`user_id`, `game_type`, `hero_id`, `lobby_id`) VALUES ('" + getUser_id() + "', '" + gameType + "', '" + getId() + "', '" + lobbyId + "')";
	}

	public String getSqlUpdateLFGQuery(LFG lfg,String gameType) {
		return "UPDATE lfg SET game_type=\'" + gameType + "\' WHERE id=\'" + lfg.getId() + "\'";
	}


	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`, `top_game_lvl`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() +  "', '" + getTopGameLvl() + "')";
	}

	public String getSqlUpdateQuery() {
		return "UPDATE `heroes` SET `level`=" + getLevel() + ",`xp`=" + getXp() + ",`top_game_lvl`=" + getTopGameLvl() + " WHERE id = " + getId();
	}
}
