package game.io;

import com.google.gson.Gson;
import game.Lobby;
import game.io.objects.*;
import game.logging.Log;
import game.util.DatabaseUtil;
import game.util.GameServerUtil;
import game.vo.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class LobbyServerDispatcher extends Thread {
	private static final String TAG = LobbyServerDispatcher.class.getSimpleName();
	private Vector mMessageQueue = new Vector();
	private static Vector mClients = new Vector();
	private String lobbyId;

	public int getClientCount(){
		return mClients.size();
	}

	public String getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(String lobbyId) {
		this.lobbyId = lobbyId;
	}


	public synchronized void handleClientRequest(ClientInfo clientInfo, Message aMessage) {
		Gson gson = new Gson();
		JsonRequest request = null;
		if (aMessage != null && aMessage.getMessage() != null) {
			request = JsonRequest.parse(aMessage);
			if (request != null && request.getRequestType() != null) {
				Log.i(TAG, "Got this request" + request.toString());
				if(request.getRequestType().equals("CLIENT_TYPE")){
					Log.i(TAG, "Sending this message : " + new Gson().toJson(new ClientTypeResponse(ClientInfo.LOBBY)));
					dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new ClientTypeResponse(ClientInfo.LOBBY))));
					Log.i(TAG, "Status of clients: " + getCountOfTypesOfClients());
				}else if(request.getRequestType().equals("CLIENT_TYPE_RESPONSE")){
					ClientTypeResponse clientTypeRequest = gson.fromJson(aMessage.getMessage(), ClientTypeResponse.class);
					clientInfo.setTypeOfConnect(clientTypeRequest.getType());
					Log.i(TAG, "Status of clients: " + getCountOfTypesOfClients());
//					if(clientInfo.getTypeOfConnect() == ClientInfo.CLIENT){
//						dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new ServerInfoResponse())));
//					}
				}else if(request.getRequestType().equals("JOIN_GAME")){
					JoinServerRequest joinServerRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
					clientInfo.setHeroId(joinServerRequest.getHeroId());
					Log.i(TAG, "Join game request : " + joinServerRequest.toString());
					clientJoinServer(clientInfo, joinServerRequest.getGameType());
				}else if (request.getRequestType().equals("CREATE_HERO")){
					CreateHeroRequest createHeroRequest = gson.fromJson(aMessage.getMessage(), CreateHeroRequest.class);
					Log.i(TAG, "User is trying to create class: " + createHeroRequest.toString());
					Hero hero = DatabaseUtil.createHero(Integer.parseInt(createHeroRequest.getUser_id()), createHeroRequest.getClass_type());
					Log.i(TAG, "Created hero : " + hero.toString());
				}else if (request.getRequestType().equals("CREATE_USER")){
					CreateUserRequest createUserRequest = gson.fromJson(aMessage.getMessage(), CreateUserRequest.class);
					Log.i(TAG, "User is trying to create user: " + createUserRequest.toString());
					User user = DatabaseUtil.createUser(new User(createUserRequest.getUsername(), createUserRequest.getEmail(), createUserRequest.getPassword()));
					Log.i(TAG, "Created user with this is: " + user.getId() + " We need to send that back to client");
					dispatchMessage(new Message(clientInfo.getId(), "{\"response_type\":\"CREATE_USER\", \"user_id\" : \"" + user.getId() + "\"}"));
				}else if (request.getRequestType().equals("LOGIN_USER")){
					CreateUserRequest createUserRequest = gson.fromJson(aMessage.getMessage(), CreateUserRequest.class);
					Log.i(TAG, "User is trying to login: " + createUserRequest.toString());
					User user = DatabaseUtil.getUser(createUserRequest.getEmail(), createUserRequest.getPassword());
					if (user != null) {
						Log.i(TAG, "Logged in user with this is: " + user.getId() + " We need to send that back to client");
						dispatchMessage(new Message(clientInfo.getId(), "{\"response_type\":\"LOGIN_USER\", \"user_id\" : \"" + user.getId() + "\"}"));
					} else {
						Log.i(TAG, "Did not find this user, send error back");
					}
				}else if (request.getRequestType().equals("GET_HEROES")){
					Log.i(TAG, "User wants his heroes: " + request.toString());
					String heroesJson = "";
					ArrayList<Hero> heroes = DatabaseUtil.getHeroes(Integer.parseInt(request.getUser_id()));
					if (heroes.size() > 0){
						for (Hero hero : heroes){
							if(heroesJson.length() > 2){
								heroesJson = heroesJson + ",";
							}
							heroesJson = heroesJson + "{";

							heroesJson = heroesJson + "\"id\": " + hero.getId() + ", \"level\": " + hero.getLevel() + ", \"class_type\": \"" + hero.getClass_type() + "\"";
							heroesJson = heroesJson + "}";
						}
					}else {
						heroesJson = "{}";
					}
					dispatchMessage(new Message(clientInfo.getId(), "{\"response_type\":\"HEROES\", \"heroes\" : [" + heroesJson + "]}"));
				}
			}
		}
		if (request != null) {
			notify();
		}
	}


	private void clientJoinServer(ClientInfo clientInfo, String gameType){
		LFG groupUserIsIn = new LFG();
		if(gameType.equals("CUSTOM")){
			// Send to this lobbys users as well
			if (clientInfo != null && clientInfo.getId() != null) {
				Server server = GameServerUtil.getGameServer(gameType);
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new GameFoundResponse(server.getIp(), server.getPort(), server.getId(), clientInfo.getHeroId(), server.getGameId()))));
				Log.i(TAG, "User wanted to play custom game, sending server");
			} else {
				Log.i(TAG, "Client is null, what happened?");
			}
		}else{
			Hero hero = DatabaseUtil.getHero(clientInfo.heroId);
			ArrayList<LFG> groups = DatabaseUtil.getLFG();

			boolean alreadySearching = false;
			boolean foundGroup = false;
			Iterator<LFG> lfgIterator = groups.iterator();
			while (lfgIterator.hasNext()) {
				LFG group = lfgIterator.next(); // must be called before you can call i.remove()
				if(group.getHeroesJoined() < group.getMaxPlayers()){
					boolean partyHasClassAlready = false;
					if((group.getHeroClass1() != null && group.getHeroClass1().equals(hero.getClass_type())) ||
							(group.getHeroClass2() != null && group.getHeroClass2().equals(hero.getClass_type())) ||
							(group.getHeroClass3() != null && group.getHeroClass3().equals(hero.getClass_type())) ||
							(group.getHeroClass4() != null && group.getHeroClass4().equals(hero.getClass_type()))){
						partyHasClassAlready = true;
					}
					if(group.getHeroId1() == clientInfo.heroId
							|| group.getHeroId2() == clientInfo.heroId
							|| group.getHeroId3() == clientInfo.heroId
							|| group.getHeroId4() == clientInfo.heroId){
						alreadySearching = true;
						groupUserIsIn = group;
					}
					if(!partyHasClassAlready){
						if(group.getHeroId1() == null){
							group.setHeroId1(hero.getId());
							group.setHeroClass1(hero.getClass_type());
						}else if (group.getHeroId2() == null){
							group.setHeroId2(hero.getId());
							group.setHeroClass2(hero.getClass_type());
						} else if (group.getHeroId3() == null){
							group.setHeroId3(hero.getId());
							group.setHeroClass3(hero.getClass_type());
						} else if (group.getHeroId4() == null){
							group.setHeroId4(hero.getId());
							group.setHeroClass4(hero.getClass_type());
						}
						group.setHeroesJoined(group.getHeroesJoined() + 1);
						if(group.getHeroesJoined() == group.getMaxPlayers()){
							lfgIterator.remove();
						}

						// Send message to all in group.

						foundGroup = true;
						break;
					}
				}
			}
			if(!foundGroup && !alreadySearching){
				groupUserIsIn.setHeroId1(hero.getId());
				groupUserIsIn.setHeroClass1(hero.getClass_type());
				groupUserIsIn.setHeroesJoined(groupUserIsIn.getHeroesJoined() + 1);
				groups.add(groupUserIsIn);
				groupUserIsIn.setHerolobby1(lobbyId);
				Log.i(TAG, "Lobby id : " + lobbyId);
				DatabaseUtil.addLFG(groupUserIsIn, gameType);
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse(JsonResponse.JOIN_GAME_RESPONSE, JsonResponse.CODE_SEARCHING_FOR_GROUP))));
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new LFGResponse(groupUserIsIn))));
			}else if (!foundGroup && alreadySearching){
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse(JsonResponse.JOIN_GAME_RESPONSE, JsonResponse.CODE_SEARCHING_FOR_GROUP))));
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new LFGResponse(groupUserIsIn))));
			}
		}
	}


//	/**
//	 * Send ip + port + gameId to client then let client handle joining a game server
//	 * @param clientInfo
//	 * @param gameType (QUICK, CUSTOM)
//	 */
//	private void clientJoinServer(ClientInfo clientInfo, String gameType) {
//		int playersForParty = 2;
//		if (gameType.equals("CUSTOM")){
//			playersForParty = 1;
//		}
////		// Get a server that the client can join.
//		// Get all users in LFG table in db then check if we got full group
//		LFG heroInDatabase = null;
//		ArrayList<LFG> group = new ArrayList<>();
//		ArrayList<LFG> heroes = DatabaseUtil.getLFG();
//		Hero myHero = DatabaseUtil.getHero(clientInfo.heroId);
//		for (LFG hero : heroes){
////			Log.i(TAG, "Hero looking for group " + hero.getClassType() + " highest level  " + hero.getHighestLevel());
////			if (hero.getHeroId() != myHero.getId()) {
////				if(!hero.getClassType().equals(myHero.getClass_type())){
////					// TODO: Check if hero is still online
////					group.add(hero);
////				}else {
////					Log.i(TAG, "Don't want a hero with the same class");
////				}
////			} else {
////				Log.i(TAG, "Found previous lfg of this user, update this instead of inserting new");
////				heroInDatabase = hero;
////			}
//		}
//		if ((group.size() + 1) == playersForParty) {
//			Log.i(TAG, "Found group to play with");
//
//			// Get all game servers and check if one has room for a new game
//			Server server = GameServerUtil.getGameServer(gameType);
//			if (server != null) {
//				// Send message to all lobbys to send out to users that we have a game
////				for(LFG lfg : group){
////					ClientInfo cInfo = getClientByUserId(lfg.getHeroId());
////					// Send to this lobbys users as well
////					if (cInfo != null && cInfo.getId() != null) {
////						dispatchMessage(new Message(cInfo.getId(), new Gson().toJson(new GameFoundResponse(server.getIp(), server.getPort(), server.getId(), cInfo.getHeroId(), server.getGameId()))));
////					} else {
////						Log.i(TAG, "Client is null, what happened?");
////					}
////				}
//				if (clientInfo != null && clientInfo.getId() != null) {
//					dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new GameFoundResponse(server.getIp(), server.getPort(), server.getId(), clientInfo.getHeroId(), server.getGameId()))));
//				} else {
//					Log.i(TAG, "Client is null, what happened?");
//				}
//
//			}else{
//				Log.i(TAG, "Did not find a server to connect to.... What to doooo?");
//			}
//
//			// Remove from database
////			for(LFG heroesInGroup : group){
////				DatabaseUtil.deleteHeroLFG(heroesInGroup.getHeroId(), heroesInGroup.getUserId());
////			}
//		} else {
//			Log.i(TAG, "Did not find group");
//			if (heroInDatabase != null) {
//				DatabaseUtil.updateHeroLfg(heroInDatabase, myHero, gameType);
//			} else {
//				DatabaseUtil.addHeroLFG(myHero, gameType, getLobbyId());
//			}
//			dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse(JsonResponse.JOIN_GAME_RESPONSE, JsonResponse.CODE_SEARCHING_FOR_GROUP))));
//		}
//	}

	private ClientInfo getClientByUserId(Integer heroId) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if(clientInfo.getHeroId() == heroId){
				return clientInfo;
			}
		}
		return null;
	}


	private String getCountOfTypesOfClients() {
		int lobbys = 1;
		int game = 0;
		int clients = 0;
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if(clientInfo.getTypeOfConnect() == 1){
				lobbys++;
			}else if(clientInfo.getTypeOfConnect() == 2){
				game++;
			}else if(clientInfo.getTypeOfConnect() == 3){
				clients++;
			}
		}
		return "Lobbys[" + lobbys + "], GameServers[" + game + "], Clients[" + clients + "]";
	}


	/**
	 * @return and deletes the next message from the message queue. If there is
	 *         no messages in the queue, falls in sleep until notified by
	 *         dispatchMessage method.
	 */
	private synchronized Message getNextMessageFromQueue() throws InterruptedException {
		while (mMessageQueue.size() == 0)
			wait();
		Message message = (Message) mMessageQueue.get(0);
		mMessageQueue.removeElementAt(0);
		return message;
	}

	/**
	 * Sends given message to all clients in the client list. Actually the
	 * message is added to the client sender thread's message queue and this
	 * client sender thread is notified.
	 */
	private synchronized void sendMessageToAllClients(Message aMessage) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if (clientInfo != null) {
				if ((aMessage.getRecipient() == null || (aMessage.getRecipient() != null && clientInfo.id == aMessage.getRecipient()))
						|| aMessage.getRecipient() != null &&  aMessage.getRecipient() == -1 && i > 0 ) {
					clientInfo.clientSender.sendMessage(aMessage);
				}
			}
		}
	}

	/**
	 * Adds given client to the server's client list.
	 */
	public static synchronized void addClient(ClientInfo aClientInfo) {
		mClients.add(aClientInfo);
	}

	/**
	 * Deletes given client from the server's client list if the client is in
	 * the list.
	 */
	public synchronized void deleteClient(ClientInfo clientInfo) {
		int clientIndex = mClients.indexOf(clientInfo);
		if (clientIndex != -1){
			mClients.removeElementAt(clientIndex);
			Log.i(TAG, "Client left, size: " + mClients.size());
		}
		if(clientInfo.getTypeOfConnect() != null && clientInfo.getTypeOfConnect() == ClientInfo.LOBBY){
			// Send remove request to webservice
			for(Server lobby : Lobby.getOtherLobbys()){
				if(clientInfo.getId() == lobby.getClientInfoId()){
					WebserviceCommunication.removeLobby(lobby.getIp(), lobby.getPort(), lobby.getId());
					// Remove from Lobby list getOtherLobbys()

				}
			}
		}
	}

	/**
	 * Adds given message to the dispatcher's message queue and notifies this
	 * thread to wake up the message queue reader (getNextMessageFromQueue
	 * method). dispatchMessage method is called by other threads
	 * (warlords.game.io.ClientListener) when a message is arrived.
	 */
	public synchronized void dispatchMessage(Message aMessage) {
		mMessageQueue.add(aMessage);
		notify();
	}

	/**
	 * Infinitely reads messages from the queue and dispatch them to all clients
	 * connected to the server.
	 */
	public void run() {
		try {
			while (true) {
				Message message = getNextMessageFromQueue();
				sendMessageToAllClients(message);
			}
		} catch (InterruptedException ie) {
			// Thread interrupted. Stop its execution
		}
	}

	public Integer getNextClientId() {
		int id = 1;
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if (clientInfo != null) {
				if(clientInfo.id <= id){
					id++;
				}
			}
		}
		return id;
	}

}
