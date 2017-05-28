package game;

import game.logging.Log;
import game.util.DatabaseUtil;
import game.vo.Hero;
<<<<<<< HEAD
import game.vo.LFG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
=======
>>>>>>> a0a39f016fde6f86f7aa92bb65e738aff514fd97

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
<<<<<<< HEAD
//		String json = "{\"request_type\":\"CLIENT_TYPE\"}";
//		System.out.println("Text: " + json);
//		String responseTypeString = "\"request_type\":\"";
//		String newJson = json.substring(json.indexOf(responseTypeString) + responseTypeString.length());
//		System.out.println(newJson.substring(0, newJson.indexOf("\"")));

//		Log.i(TAG, "Hero : " + DatabaseUtil.getHero(16));

//
//		Random rand = new Random();
//		int max = 10;
//		int min = 1;
//
//		int classMax = 4;
//		int classMin = 1;
//
//		int filledGroups = 0;
//
//		long millis = System.currentTimeMillis();
//
//		ArrayList<LFG> groups = new ArrayList<>();
////		ArrayList<LFG> heroes = DatabaseUtil.getLFG();
//
//		for(int i = 0 ; i < 10000 ; i++){
//			Hero hero = new Hero();
//			int randomNum = rand.nextInt((max - min) + 1) + min;
//			hero.setLevel(randomNum);
//
//			int randomId = rand.nextInt((1000 - 1) + 1) + 1;
//			hero.setId(randomId);
//
//
//			int classRandom = rand.nextInt((classMax - classMin) + 1) + classMin;
//			if(classRandom == 1){
//				hero.setClass_type("WARRIOR");
//			}else if(classRandom == 2){
//				hero.setClass_type("PRIEST");
//			}else if(classRandom == 3){
//				hero.setClass_type("WARLOCK");
//			}else if(classRandom == 4){
//				hero.setClass_type("ROUGUE");
//			}
////			Log.i(TAG, "Hero generated : " + hero.toString());
//
//
//
//
//
//			boolean foundGroup = false;
//			Iterator<LFG> lfgIterator = groups.iterator();
//			while (lfgIterator.hasNext()) {
//				LFG group = lfgIterator.next(); // must be called before you can call i.remove()
//
//				if(group.getHeroesJoined() < group.getMaxPlayers()){
//					boolean partyHasClassAlready = false;
//					if((group.getHeroClass1() != null && group.getHeroClass1().equals(hero.getClass_type())) ||
//							(group.getHeroClass2() != null && group.getHeroClass2().equals(hero.getClass_type())) ||
//							(group.getHeroClass3() != null && group.getHeroClass3().equals(hero.getClass_type())) ||
//							(group.getHeroClass4() != null && group.getHeroClass4().equals(hero.getClass_type()))){
//						partyHasClassAlready = true;
//					}
//					if(!partyHasClassAlready){
//						if(group.getHeroId1() == null){
//							group.setHeroId1(hero.getId());
//							group.setHeroClass1(hero.getClass_type());
//						}else if (group.getHeroId2() == null){
//							group.setHeroId2(hero.getId());
//							group.setHeroClass2(hero.getClass_type());
//						} else if (group.getHeroId3() == null){
//							group.setHeroId3(hero.getId());
//							group.setHeroClass3(hero.getClass_type());
//						} else if (group.getHeroId4() == null){
//							group.setHeroId4(hero.getId());
//							group.setHeroClass4(hero.getClass_type());
//						}
//						group.setHeroesJoined(group.getHeroesJoined() + 1);
//						if(group.getHeroesJoined() == group.getMaxPlayers()){
//							filledGroups++;
//							lfgIterator.remove();
//						}
//						foundGroup = true;
//					}
//				}
//				if(foundGroup){
//					break;
//				}
//
//
//			}
//			if(!foundGroup){
//				LFG group = new LFG();
//				group.setHeroId1(hero.getId());
//				group.setHeroClass1(hero.getClass_type());
//				group.setHeroesJoined(group.getHeroesJoined() + 1);
//				groups.add(group);
//			}
//		}


//		for(LFG group : groups){
//			Log.i(TAG, "Group : " + group.toString());
//		}

//		Log.i(TAG, "Filled " + filledGroups + " groups, time : " + (System.currentTimeMillis() - millis));
=======
		//String json = "{\"request_type\":\"CLIENT_TYPE\"}";
		//System.out.println("Text: " + json);
		//String responseTypeString = "\"request_type\":\"";
		//String newJson = json.substring(json.indexOf(responseTypeString) + responseTypeString.length());
		//System.out.println(newJson.substring(0, newJson.indexOf("\"")));
		Hero hero = DatabaseUtil.getHero(16);
		Log.i(TAG, "Hero : " + hero.toString());
>>>>>>> a0a39f016fde6f86f7aa92bb65e738aff514fd97
	}
}
