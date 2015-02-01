//: michalspaceinvaders/ObjectHolder.java

package michalspaceinvaders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Container class for game objects which extend GameObject class.
 * Singleton pattern.
 * @author Michal Czop
 */

public class ObjectHolder {
	
	/** Singleton pattern. */
	private ObjectHolder() {}
	
	private static ObjectHolder instance = new ObjectHolder();
	
	public static ObjectHolder getInstance() {
		return instance;
	}
	
	/** ArrayList containing all game objects. */
	private List<GameObject> gameObjects = new ArrayList<GameObject>();
	
	/** 
	 * Adds new object to ArrayList. 
	 * @param gameObject - object to add.
	 */
	public void addObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}
	
	/** Returns GameObject under i index. */
	public GameObject getObject(int i) {
		return gameObjects.get(i);
	}
	
	/** Deletes GameObject under i index. */
	public void deleteObject(int index) {
		gameObjects.remove(index);
	}
	
	/** Deletes GameObjects. Takes collection as parameter. */
	public void deleteObject(Collection<?> c) {
		gameObjects.removeAll(c);
	}
	
	/** Deletes all objects in ArrayList. */
	public void clearObjects() {
		gameObjects.clear();
	}
	
	/** Returns size of the collection. */
	public int getSize() {
		return gameObjects.size();
	}
}
