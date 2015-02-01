//: michalspaceinvaders/PlayerObject.java

package michalspaceinvaders;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Game object representing spaceship controlled by player. 
 * Extends GameObject.
 * Overrides: move(long deltaTime)
 * Implements: inCollision(GameObject other)
 * @author Michal Czop
 */
public class PlayerObject extends GameObject {
	
	/**
	 * Constructor.
	 * @param game - game instance
	 * @param x - position of tank on x axis
	 * @param y - position of tank on y axis
	 */
	public PlayerObject(Game game, int x, int y) {
		super(game, x, y);
		defaultSpeedX = 300;
		defaultSpeedY = 0;
		defaultColor = new Color(0, 0, 255);
		defaultShape = new Rectangle(30, 30);
		color = defaultColor;
		shape = defaultShape;
	}

	/** 
	 * Handles movement of player. 
	 * @param deltaTime - time lapsed since last iteration
	 */
	@Override
	public void move(long deltaTime) {
		if (x<30 && speedX<0) {
			speedX = 0;
		}
		if (x>740 && speedX>0) {
			speedX = 0;
		}
		super.move(deltaTime);
	}
	
	/** 
	 * Determines actions performed after collision 
	 * with other objects.
	 * @param other - GameObject which collides with this
	*/
	@Override
	public void inCollision(GameObject other) {
		if(other.getClass().getSimpleName().equals("AlienMissleObject")) {
			other.setIsDestroyed(true);
			isDestroyed = true;
			gameInstance.gameLost = true;
		}
		if(other.getClass().getSimpleName().equals("AlienObject")) {
			isDestroyed = true;
		}
	}
}
