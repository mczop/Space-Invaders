//: michalspaceinvaders/PlayerMissleObject.java

package michalspaceinvaders;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Game object representing missile shot by player. 
 * Extends GameObject.
 * Overrides: move(long deltaTime)
 * Implements: inCollision(GameObject other)
 * @author Michal Czop
 */
public class PlayerMissleObject extends GameObject {
	
	/**
	 * Constructor.
	 * @param game - game instance
	 * @param x - position of tank on x axis
	 * @param y - position of tank on y axis
	 */
	public PlayerMissleObject(Game game, int x, int y) {
		super(game, x, y);
		defaultSpeedX = 0;
		defaultSpeedY = -400;
		defaultColor = new Color(0, 255, 0);
		defaultShape = new Rectangle(5, 20);
		color = defaultColor;
		shape = defaultShape;
		speedY = defaultSpeedY;
	}
	
	/** 
	 * Handles movement of missile. 
	 * @param deltaTime - time lapsed since last iteration
	 */
	@Override
	public void move(long deltaTime) {
		if(y < (-50)) {
			isDestroyed = true;
			return;
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
			isDestroyed=true;
		}
	}
}
