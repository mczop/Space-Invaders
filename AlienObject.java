//: michalspaceinvaders/AlienObject.java

package michalspaceinvaders;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Game object representing alien. 
 * Extends GameObject.
 * Overrides: move(long deltaTime)
 * Implements: inCollision(GameObject other)
 * @author Michal Czop
 */
public class AlienObject extends GameObject {
	
	/**
	 * Constructor.
	 * @param game - game instance
	 * @param x - position of tank on x axis
	 * @param y - position of tank on y axis
	 */
	public AlienObject(Game game, int x, int y) {
		super(game, x, y);
		defaultSpeedX = 100;
		defaultSpeedY = 0;
		defaultColor = new Color(255, 0, 0);
		defaultShape = new Rectangle(25, 25);
		color = defaultColor;
		shape = defaultShape;
		speedX = defaultSpeedX;
		timesHit = 0;
	}
	
	/** 
	 * Handles movement of alien. 
	 * @param deltaTime - time lapsed since last iteration
	 */
	@Override
	public void move(long deltaTime) {
		if (x<20 && speedX<0) {
			gameInstance.logicNeeded = true;
			return;
		}
		if (x>750 && speedX>0) {
			gameInstance.logicNeeded = true;
			return;
		}
		super.move(deltaTime);
	}
	
	/** When edge of screen reached - go 15 pixels down on screen. */
	public void performLogic() {
		speedX = - speedX;
		y += 15;
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
			timesHit++;
		}
		if(other.getClass().getSimpleName().equals("PlayerObject")) {
			other.setIsDestroyed(true);
			gameInstance.gameLost = true;
		}
	}
	
}
