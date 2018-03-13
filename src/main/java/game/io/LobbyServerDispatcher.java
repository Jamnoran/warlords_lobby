package game.io;

import com.google.gson.Gson;
import game.Lobby;
import game.io.objects.*;
import game.logging.Log;
import game.util.DatabaseUtil;
import game.util.GameServerUtil;
import game.vo.*;

import java.awt.image.DataBuffer;
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
				}else if (request.getRequestType().equals("UPDATE_USERNAME")){
					UpdateUsernameRequest updateUsernameRequest = gson.fromJson(aMessage.getMessage(), UpdateUsernameRequest.class);
					Log.i(TAG, "Updating username " + updateUsernameRequest.toString());
					updateUsername(updateUsernameRequest);
				}else if (request.getRequestType().equals("LOGIN_USER")){
					CreateUserRequest createUserRequest = gson.fromJson(aMessage.getMessage(), CreateUserRequest.class);
					Log.i(TAG, "User is trying to login: " + createUserRequest.toString());
					User user = DatabaseUtil.getUser(createUserRequest.getEmail(), createUserRequest.getPassword());
					if (user != null) {
						Log.i(TAG, "Logged in user with this is: " + user.getId() + " We need to send that back to client");
						dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new LoginResponse(user.getId()))));
					} else {
						Log.i(TAG, "Did not find this user, send error back to the user");

						dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse("LOGIN_USER","Wrong username or password", JsonResponse.WRONG_EMAIL_OR_PASSWORD))));
					}
				}else if (request.getRequestType().equals("GET_HEROES")){
					returnHeroes(request, clientInfo);
				}else if (request.getRequestType().equals("LFG_RESPONSE")){
					LFGResponse lfgResponse = gson.fromJson(aMessage.getMessage(), LFGResponse.class);
					handleLfgResponse(lfgResponse);
				}else if(request.getRequestType().equals("START_GAME")){
					LFGResponse lfgResponse = gson.fromJson(aMessage.getMessage(), LFGResponse.class);
					Log.i(TAG, "Wanting to start game early : " + lfgResponse.toString());
					handleStartGame(clientInfo, lfgResponse.getLfg());
				}else if(request.getRequestType().equals("CANCEL_SEARCH_GAME")){
					LFGResponse lfgResponse = gson.fromJson(aMessage.getMessage(), LFGResponse.class);
					Log.i(TAG, "Wanting to cancel game search : " + lfgResponse.toString());
					handleCancelGame(clientInfo, lfgResponse.getLfg());
				}else if (request.getRequestType().equals("GET_USERNAME")) {
					returnUsername(request, clientInfo);
				}
			}else{
				Log.i(TAG, "Could not parse request " + aMessage.getMessage());
			}
		}
		if (request != null) {
			notify();
		}
	}


	private void returnUsername(JsonRequest request, ClientInfo clientInfo) {
		User user = DatabaseUtil.getUser(Integer.parseInt(request.user_id));
		String data = new Gson().toJson(new UsernameResponse(user.getUsername()));
		Log.i(TAG, "Return username : " + user.getUsername());
		dispatchMessage(new Message(clientInfo.getId(), data));
	}

	private void updateUsername(UpdateUsernameRequest updateUsernameRequest) {
		Log.i(TAG, "Updating username to " + updateUsernameRequest.getUsername() + " on userId: " + updateUsernameRequest.getUser_id());
		DatabaseUtil.updateUsername(Integer.parseInt(updateUsernameRequest.getUser_id()), updateUsernameRequest.getUsername());
	}


	private void returnHeroes(JsonRequest request, ClientInfo clientInfo) {
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


	private void clientJoinServer(ClientInfo clientInfo, String gameType){
		if(gameType.equals("CUSTOM")){
			// Send to this lobbys users as well
			if (clientInfo != null && clientInfo.getId() != null) {
				Server server = GameServerUtil.getGameServer(gameType);
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new GameFoundResponse(server.getIp(), server.getPort(), server.getId(), clientInfo.getHeroId(), server.getGameId()))));
				Log.i(TAG, "User wanted to play custom game, sending server");
			} else {
				Log.i(TAG, "Client is null, what happened?");
			}
		}else if (gameType.equals("QUICK")){
			joinQuickGame(clientInfo, gameType);
		}
	}

	private void joinQuickGame(ClientInfo clientInfo, String gameType) {
		LFG groupUserIsIn = new LFG();
		Hero hero = DatabaseUtil.getHero(clientInfo.heroId);
		ArrayList<LFG> groups = DatabaseUtil.getLFG();

		Log.i(TAG, "Hero wants to join a quick game : " + hero.getId() + " class : " + hero.getClass_type());

		boolean alreadySearching = false;
		boolean foundGroup = false;
		Iterator<LFG> lfgIterator = groups.iterator();
		while (lfgIterator.hasNext()) {
			LFG group = lfgIterator.next(); // must be called before you can call i.remove()
			if(group.getHeroesJoined() < group.getMaxPlayers()){
				Log.i(TAG, "Found a group that is not full");
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
				Log.i(TAG, "This hero is already searching " + alreadySearching + " Group already has this class : " + partyHasClassAlready);
				if(!partyHasClassAlready){
					if(group.getHeroId1() == null || group.getHeroId1() == 0){
						group.setHeroId1(hero.getId());
						group.setHeroClass1(hero.getClass_type());
						group.setHerolobby1(lobbyId);
						Log.i(TAG, "Adding hero to position 1");
					}else if (group.getHeroId2() == null || group.getHeroId2() == 0){
						group.setHeroId2(hero.getId());
						group.setHeroClass2(hero.getClass_type());
						group.setHerolobby2(lobbyId);
						Log.i(TAG, "Adding hero to position 2");
					} else if (group.getHeroId3() == null || group.getHeroId3() == 0){
						group.setHeroId3(hero.getId());
						group.setHeroClass3(hero.getClass_type());
						group.setHerolobby3(lobbyId);
						Log.i(TAG, "Adding hero to position 3");
					} else if (group.getHeroId4() == null || group.getHeroId4() == 0){
						group.setHeroId4(hero.getId());
						group.setHeroClass4(hero.getClass_type());
						group.setHerolobby4(lobbyId);
						Log.i(TAG, "Adding hero to position 4");
					}
					group.setHeroesJoined(group.getHeroesJoined() + 1);

					findServerForGroup(clientInfo, group, gameType, false);

					// Update database
					DatabaseUtil.updateLFG(group);

					groupUserIsIn = group;

					foundGroup = true;
					break;
				}
			}
		}

		Log.i(TAG, "Sending group: " + groupUserIsIn.toString() + " Found group : " + foundGroup + " Already searching : " + alreadySearching);
		if(!foundGroup && !alreadySearching){
			groupUserIsIn.setHeroId1(hero.getId());
			groupUserIsIn.setHeroClass1(hero.getClass_type());
			groupUserIsIn.setHeroesJoined(groupUserIsIn.getHeroesJoined() + 1);
			groupUserIsIn.setHerolobby1(lobbyId);
			Log.i(TAG, "Lobby id : " + lobbyId);
			DatabaseUtil.addLFG(groupUserIsIn, gameType);
			dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse(JsonResponse.JOIN_GAME_RESPONSE, JsonResponse.CODE_SEARCHING_FOR_GROUP))));
			dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new LFGResponse(groupUserIsIn))));
		} else {
			dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse(JsonResponse.JOIN_GAME_RESPONSE, JsonResponse.CODE_SEARCHING_FOR_GROUP))));
			dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new LFGResponse(groupUserIsIn))));
		}
	}

	private void handleStartGame(ClientInfo clientInfo, LFG group) {
		findServerForGroup(clientInfo, group, "QUICK", true);
	}


	private void handleCancelGame(ClientInfo clientInfo, LFG group) {
		DatabaseUtil.deleteHeroLFG(clientInfo.getHeroId());
		sendMessageToLFG(clientInfo, group, new Gson().toJson(new LFGResponse(group)));
	}

	private void findServerForGroup(ClientInfo clientInfo, LFG group, String gameType, boolean forceStart) {
		Log.i(TAG, "Starting to find a game for the client of gametype : " + gameType + " User id : " + clientInfo.getId());
		String gameFoundResponse = null;
		if(group.getHeroesJoined() == group.getMaxPlayers() || forceStart){
			Server server = GameServerUtil.getGameServer(gameType);
			gameFoundResponse = new Gson().toJson(new GameFoundResponse(server.getIp(), server.getPort(), server.getId(), clientInfo.getHeroId(), server.getGameId()));
			dispatchMessage(new Message(clientInfo.getId(), gameFoundResponse));
		}
		sendMessageToLFG(clientInfo, group, gameFoundResponse);

		// Update database that this group have found a game, we can now remove it from list
		Log.i(TAG, "Sent to all clients that we have found a game for this group");
	}

	private void sendMessageToLFG(ClientInfo clientInfo, LFG group, String message) {
		// Send message to all in group.
		if (!group.getHeroId1().equals(clientInfo.getHeroId()) && group.getHerolobby1() != null) {
			if (!group.getHerolobby1().equals(lobbyId)) {
				sendMessageToLobby(group.getHerolobby1(), new Gson().toJson(new LFGResponse(group)));
				if(message != null){ sendMessageToLobby(group.getHerolobby1(), message); }
			}else {
				dispatchMessage(new Message(getClientByHeroId(group.getHeroId1()).getId(), new Gson().toJson(new LFGResponse(group))));
				if(message != null){ dispatchMessage(new Message(getClientByHeroId(group.getHeroId1()).getId(), message)); }
			}
		}
		if (!group.getHeroId2().equals(clientInfo.getHeroId()) && group.getHerolobby2() != null) {
			if (!group.getHerolobby2().equals(lobbyId)) {
				sendMessageToLobby(group.getHerolobby2(), new Gson().toJson(new LFGResponse(group)));
				if(message != null){ sendMessageToLobby(group.getHerolobby1(), message); }
			} else {
				dispatchMessage(new Message(getClientByHeroId(group.getHeroId2()).getId(), new Gson().toJson(new LFGResponse(group))));
				if(message != null){ dispatchMessage(new Message(getClientByHeroId(group.getHeroId2()).getId(), message)); }
			}
		}
		if (!group.getHeroId3().equals(clientInfo.getHeroId()) && group.getHerolobby3() != null) {
			if (!group.getHerolobby3().equals(lobbyId)) {
				sendMessageToLobby(group.getHerolobby3(), new Gson().toJson(new LFGResponse(group)));
				if(message != null){ sendMessageToLobby(group.getHerolobby1(), message); }
			} else {
				dispatchMessage(new Message(getClientByHeroId(group.getHeroId3()).getId(), new Gson().toJson(new LFGResponse(group))));
				if(message != null){ dispatchMessage(new Message(getClientByHeroId(group.getHeroId3()).getId(), message)); }
			}
		}
		if (!group.getHeroId4().equals(clientInfo.getHeroId()) && group.getHerolobby4() != null) {
			if (!group.getHerolobby4().equals(lobbyId)) {
				sendMessageToLobby(group.getHerolobby4(), new Gson().toJson(new LFGResponse(group)));
				if(message != null){ sendMessageToLobby(group.getHerolobby1(), message); }
			} else {
				dispatchMessage(new Message(getClientByHeroId(group.getHeroId4()).getId(), new Gson().toJson(new LFGResponse(group))));
				if(message != null){ dispatchMessage(new Message(getClientByHeroId(group.getHeroId4()).getId(), message)); }
			}
		}
	}



	private void sendMessageToLobby(String lobbyId, String message) {
		for(Server server : Lobby.getOtherLobbys()){
			if(server.getId().equals(lobbyId)){
				ClientInfo client = getClientById(server.getClientInfoId());
				if (client != null && client.getId() != null) {
					dispatchMessage(new Message(client.getId(), message));
				} else {
					Log.i(TAG, "Couldn't not find clientInfo with this clientInfoId : " + server.getClientInfoId());
				}
			}
		}
	}


	private void handleLfgResponse(LFGResponse lfgResponse) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo cInfo = (ClientInfo) mClients.get(i);
			if(cInfo.getHeroId() == lfgResponse.getLfg().getHeroId1()){
				Log.i(TAG, "Sending lfg update to this hero : " + lfgResponse.getLfg().getHeroId1());
				dispatchMessage(new Message(getClientByHeroId(cInfo.getHeroId()).getId(), new Gson().toJson(lfgResponse)));
			}
			if(cInfo.getHeroId() == lfgResponse.getLfg().getHeroId2()){
				Log.i(TAG, "Sending lfg update to this hero : " + lfgResponse.getLfg().getHeroId2());
				dispatchMessage(new Message(getClientByHeroId(cInfo.getHeroId()).getId(), new Gson().toJson(lfgResponse)));
			}
			if(cInfo.getHeroId() == lfgResponse.getLfg().getHeroId3()){
				Log.i(TAG, "Sending lfg update to this hero : " + lfgResponse.getLfg().getHeroId3());
				dispatchMessage(new Message(getClientByHeroId(cInfo.getHeroId()).getId(), new Gson().toJson(lfgResponse)));
			}
			if(cInfo.getHeroId() == lfgResponse.getLfg().getHeroId4()){
				Log.i(TAG, "Sending lfg update to this hero : " + lfgResponse.getLfg().getHeroId4());
				dispatchMessage(new Message(getClientByHeroId(cInfo.getHeroId()).getId(), new Gson().toJson(lfgResponse)));
			}
		}
	}

	private ClientInfo getClientByHeroId(Integer heroId) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if(clientInfo.getHeroId() == heroId){
				return clientInfo;
			}
		}
		return null;
	}

	private ClientInfo getClientById(Integer clientInfoId) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if(clientInfo.getId() == clientInfoId){
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
		}else if(clientInfo.getTypeOfConnect() != null && clientInfo.getTypeOfConnect() == ClientInfo.CLIENT){
			LFG lfg = DatabaseUtil.deleteHeroLFG(clientInfo.getHeroId());
			if(lfg != null){
				sendMessageToLFG(clientInfo, lfg, new Gson().toJson(new LFGResponse(lfg)));
			}
		}
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
