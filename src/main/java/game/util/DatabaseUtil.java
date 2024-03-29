package game.util;

import game.logging.Log;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Rogue;
import game.vo.classes.Warlock;
import game.vo.classes.Warrior;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class DatabaseUtil {
	private static final String TAG = DatabaseUtil.class.getSimpleName();
	private static String ip = "jamnoran.se";
	private static String port = "9906";
	private static String user = "warlord_clients";
	private static String password = "bosse45&";


	public static User createUser(User user) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(user.getSqlInsertQuery());
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				Log.i(TAG, "Got user_id : " + autoIncKeyFromApi);
				user.setId(autoIncKeyFromApi);
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return user;
	}

	public static User getUser(Integer id) {
		User user = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, username, email, password, login_key FROM users where id = " + id);
				while (rs.next()) {
					user = new User();
					//Retrieve by column name
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setLoginKey(rs.getString("login_key"));
					//Display values
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return user;
	}

	public static User getUser(String userName) {
		User user = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, username, email, password, login_key FROM users where username = " + userName);
				while (rs.next()) {
					user = new User();
					//Retrieve by column name
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setLoginKey(rs.getString("login_key"));
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return user;
	}

	public static User getUser(String email, String password) {
		User user = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, username, email, password FROM users where email = \"" + email + "\" and password = \"" + password + "\"");
				while (rs.next()) {
					user = new User();
					//Retrieve by column name
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					//Display values
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		// Create loginKey and update database
		if (user != null) {
			user.setLoginKey(generateLoginKey());
			updateUser(user);
		}
		return user;
	}

	public static String generateLoginKey() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyGen.init(128); // for example
		SecretKey secretKey = keyGen.generateKey();
		return CalculationUtil.bytesToHex(secretKey.getEncoded());
	}

	public static User updateUser(User user) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				// stmt.executeUpdate(user.getSqlUpdateQuery());
				Log.i(TAG, "Update user with new loginKey userId: " + user.getId() + " key: " + user.getLoginKey());
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return user;
	}

	public static Hero createHero(Integer userId, String classType) {
		Connection connection = getConnection();
		Hero hero = new Hero(userId);
		hero.setClass_type(classType.trim());
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(hero.getSqlInsertQuery());
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				Log.i(TAG, "Got hero_id : " + autoIncKeyFromApi);
				hero.setId(autoIncKeyFromApi);
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return hero;
	}

	public static ArrayList<Hero> getHeroes(Integer userId) {
		ArrayList<Hero> heroes = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM heroes where user_id = " + userId);
				while (rs.next()) {
					Hero hero = new Hero(userId);
					//Retrieve by column name
					hero.setId(rs.getInt("id"));
					hero.setXp(rs.getInt("xp"));
					hero.setClass_type(rs.getString("class_type"));
					hero.setLevel(rs.getInt("level"));
					hero.setTopGameLvl(rs.getInt("top_game_lvl"));
					//Display values
					heroes.add(hero);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return heroes;
	}


	public static Hero getHero(Integer heroId) {
		Hero hero = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM heroes where id = " + heroId);
				while (rs.next()) {
					//Log.i(TAG, "Class : " + rs.getString("class_type"));
					if(rs.getString("class_type").equals(Hero.WARRIOR)){
						hero = new Warrior();
					}else if(rs.getString("class_type").equals(Hero.PRIEST)){
						hero = new Priest();
					}else if(rs.getString("class_type").equals(Hero.WARLOCK)){
						hero = new Warlock();
					}else if(rs.getString("class_type").equals(Hero.ROGUE)){
						hero = new Rogue();
					}else{
						Log.i(TAG, "Cant find class: [" + rs.getString("class_type") + "]");
					}
					//Retrieve by column name
					hero.setId(rs.getInt("id"));
					hero.setXp(rs.getInt("xp"));
					hero.setUser_id(rs.getInt("user_id"));
					hero.setClass_type(rs.getString("class_type"));
					hero.setLevel(rs.getInt("level"));
					hero.setTopGameLvl(rs.getInt("top_game_lvl"));
					//Display values
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return hero;
	}


	public static ArrayList<Ability> getAllAbilities(String classType){
		ArrayList<Ability> abilities = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM abilities where class_type LIKE \'" + classType + "\'");
				while (rs.next()) {
					Ability ability = new Ability();
					//Retrieve by column name
					ability.setId(rs.getInt("id"));
					ability.setName(rs.getString("name"));
					ability.setBaseDamage(rs.getInt("base_damage"));
					ability.setTopDamage(rs.getInt("top_damage"));
					ability.setClassType(rs.getString("class_type"));
					ability.setCrittable(rs.getInt("crittable"));
					ability.setLevelReq(rs.getInt("level_req"));
					ability.setTargetType(rs.getString("target_type"));
					ability.setImage(rs.getString("image"));
					ability.setDescription(rs.getString("description"));
					ability.setDamageType(rs.getString("damage_type"));
					ability.setBaseCD(rs.getInt("base_cd"));
					//Display values
					abilities.add(ability);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}

		// Add Auto Attack ability at id 0
		Ability ability = new Ability();
		ability.setId(0);
		ability.setName("Auto Attack");
		ability.setImage("auto");
		abilities.add(ability);

		return abilities;
	}

	public static Hero updateHero(Hero hero) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(hero.getSqlUpdateQuery());
				Log.i(TAG, "Update hero_id : " + hero.getId());
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return hero;
	}


	public static void updateUsername(int heroId, String username) {
		// Check first if username is taken

		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate("UPDATE users set username = \"" + username + "\" where id = " + heroId);
				Log.i(TAG, "Update username to : " + username);
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
	}

	public static ArrayList<LFG> getLFG() {
		ArrayList<LFG> lfgs = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from lfg");
				while (rs.next()) {
					LFG lfg = new LFG();
					//Retrieve by column name
					lfg.setId(rs.getInt("id"));
					lfg.setGameType(rs.getString("game_type"));
					lfg.setHeroId1(rs.getInt("hero_id_1"));
					lfg.setHeroClass1(rs.getString("hero_class_1"));
					lfg.setHerolobby1(rs.getString("hero_lobby_1"));
					lfg.setHeroId2(rs.getInt("hero_id_2"));
					lfg.setHeroClass2(rs.getString("hero_class_2"));
					lfg.setHerolobby2(rs.getString("hero_lobby_2"));
					lfg.setHeroId3(rs.getInt("hero_id_3"));
					lfg.setHeroClass3(rs.getString("hero_class_3"));
					lfg.setHerolobby3(rs.getString("hero_lobby_3"));
					lfg.setHeroId4(rs.getInt("hero_id_4"));
					lfg.setHeroClass4(rs.getString("hero_class_4"));
					lfg.setHerolobby4(rs.getString("hero_lobby_4"));
					lfg.setMaxPlayers(rs.getInt("max_players"));
					lfg.setHeroesJoined(rs.getInt("heroes_joined"));
					lfgs.add(lfg);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return lfgs;
	}

	public static void addLFG(LFG lfg, String gameType) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				Log.i(TAG , "Mysql query: " + lfg.getSqlInsertLFGQuery(gameType));
				stmt.executeUpdate(lfg.getSqlInsertLFGQuery(gameType));
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				lfg.setId(autoIncKeyFromApi);
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
	}


	public static void updateLFG(LFG group) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				Log.i(TAG , "Mysql query: " + group.getSqlUpdateLFGQuery());
				stmt.executeUpdate(group.getSqlUpdateLFGQuery());
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
	}

	public static LFG deleteHeroLFG(Integer heroId) {
		LFG lfgToRemoveFrom = null;
		int position = 0;

		ArrayList<LFG> groups = getLFG();
		for (LFG lfg : groups){
			if(lfg.getHeroId1() == heroId){
				position = 1;
				lfgToRemoveFrom = lfg;
			}else if(lfg.getHeroId2() == heroId){
				position = 2;
				lfgToRemoveFrom = lfg;
			}else if(lfg.getHeroId3() == heroId){
				position = 3;
				lfgToRemoveFrom = lfg;
			}else if(lfg.getHeroId4() == heroId){
				position = 4;
				lfgToRemoveFrom = lfg;
			}
		}

		if(lfgToRemoveFrom != null){
			Connection connection = getConnection();
			Log.i(TAG, "Deleting from lfg, heroId: " + heroId);
			String SQL;
			if(lfgToRemoveFrom.getHeroesJoined() == 1){
				SQL = "DELETE from lfg where id = " + lfgToRemoveFrom.getId();
			}else{
				SQL = "UPDATE lfg set heroes_joined = heroes_joined - 1, hero_id_" + position + "=null, hero_lobby_" + position + "=null, hero_class_" + position + "=null where id=\"" + lfgToRemoveFrom.getId() + "\"";
			}

			Log.i(TAG, "Sql query [" + SQL + "]");
			PreparedStatement pstmt;
			try {
				pstmt = connection.prepareStatement(SQL);
				pstmt.executeUpdate();
				pstmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return lfgToRemoveFrom;
		}
		return null;
	}



	public static void addChatMessage(int userId, String message, int groupId, long time){
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				String query = "INSERT INTO `warlord`.`chat` (`user_id`, `message`, `group_id`, `time`) VALUES (" + userId + ", '" + message + "', " + groupId + ", " + time + ")";
				stmt.executeUpdate(query);
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
//				message.setId(autoIncKeyFromApi);
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
	}

	public static ArrayList<ChatMessage> getMessagesLastHour(){
		ArrayList<ChatMessage> messages = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, user_id, message, group_id, time FROM chat");
				while (rs.next()) {
					ChatMessage message = new ChatMessage();
					//Retrieve by column name
					message.setId(rs.getInt("id"));
					message.setMessage(rs.getString("message"));
					message.setUserId(rs.getInt("user_id"));
					message.setGroupId(rs.getInt("group_id"));
					message.setTime(rs.getInt("time"));
					messages.add(message);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return messages;
	}



	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}
		try {
			return DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/warlord", user, password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
	}
}