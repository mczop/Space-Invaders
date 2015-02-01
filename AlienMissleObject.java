//: michalspaceinvaders/AlienMissleObject.java

package michalspaceinvaders;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Game object representing missile shot by alien. 
 * Extends GameObject.
 * Overrides: move(long deltaTime)
 * Implements: inCollision(GameObject other)
 * @author Michal Czop
 */
public class AlienMissleObject extends GameObject {
	
	/**
	 * Constructor.
	 * @param game - game instance
	 * @param x - position of tank on x axis
	 * @param y - position of tank on y axis
	 */
	public AlienMissleObject(Game game, int x, int y) {
		super(game, x, y);
		defaultSpeedX = 0;
		defaultSpeedY = 400;
		defaultColor = new Color(153, 51, 255);
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
		if(y > 650) {
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
		if(other.getClass().getSimpleName().equals("PlayerMissleObject")) {
			other.setIsDestroyed(true);
			isDestroyed=true;
		}
		if(other.getClass().getSimpleName().equals("PlayerObject")) {
			other.setIsDestroyed(true);
			isDestroyed=true;
			gameInstance.gameLost = true;
		}
	}	
}
