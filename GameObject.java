//: michalspaceinvaders/GameObject.java

package michalspaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/** 
 * Base class for most objects in game.
 * @author Michal Czop
 */
abstract public class GameObject {
	
	/** Instance of current Game used for communication. */
	protected Game gameInstance;

	/** Unique id number for each object. */
	protected static int idCounter;
	protected final int id = idCounter++;
	
	/** Number of hits the object received. */
	protected int timesHit;
	
	/** 
	 * Coordinates of object. Double type 
	 * secures good resolution of motion.
	 */
	protected double x;
	protected double y;
	
	/** Default speed of Game Objects in pixels/second. */
	protected int defaultSpeedX;
	protected int defaultSpeedY;
	
	/** Speed of Game Objects in pixels/second. */
	protected int speedX;
	protected int speedY;
	
	/** Color and shape of object. */
	protected Color defaultColor;
	protected Color color;
	protected Rectangle defaultShape;
	protected Rectangle shape;
	
	/** Shapes used for collision check. */
	protected Rectangle myShape = new Rectangle();
	protected Rectangle otherShape = new Rectangle();
	
	/** 
	 * Status of object set by collision check. If destroyed,
	 * object should be removed from ObjectHolder.
	 */
	protected boolean isDestroyed = false;
	
	/** 
	 * Constructor. 
	 * @param game - game instance
	 * @param x - position on x axis
	 * @param y - position on y axis
	 */
	public GameObject(Game game, int x, int y) {
		this.gameInstance = game;
		this.x = x;
		this.y = y;
	}
	
	/** Performs logic actions when needed. */
	public void performLogic() {}
	
	/** 
	 * Calculates coordinates dependent on time passed.
	 * @param deltaTime - time lapsed since last iteration
	 */
	public void move(long deltaTime) {
		x += (speedX*deltaTime)/1000;
		y += (speedY*deltaTime)/1000;
	}
	
	/** 
	 * Performs collision check between game objects.
	 * @param other - second object
	 */
	public boolean collisionCheck (GameObject other) {
		myShape.setBounds((int)x, (int)y, (int)shape.getWidth(), (int)shape.getHeight());
		otherShape.setBounds((int)other.getX(), (int)other.getY(), (int)other.getShape().getWidth(), (int)other.getShape().getHeight());
		return myShape.intersects(otherShape);
	}
	
	/**
	 * Draws object to g.
	 * @param g - graphics object in game
	 */
	public void draw(Graphics2D g) {
		g.fillRect((int)x, (int)y, (int)shape.getWidth(), (int)shape.getHeight());
	}
	
	/** 
	 * Determines actions performed after collision 
	 * with other objects.
	 * @param other - GameObject which collides with this
	*/
	abstract void inCollision(GameObject other);

	/** Setters and getters for cordinates. */
	public void setX (double x) {
		this.x = x;
	}
	public void setY (double y) {
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	/** Setters and getters for speed. */
	public void setDefaultSpeedX (int defaultSpeedX) {
		this.defaultSpeedX = defaultSpeedX;
	}
	public void setDefaultSpeedY (int defaultSpeedY) {
		this.defaultSpeedY = defaultSpeedY;
	}
	public int getDefaultSpeedX() {
		return defaultSpeedX;
	}
	public int getDefaultSpeedY() {
		return defaultSpeedY;
	}
	public void setSpeedX (int speedX) {
		this.speedX = speedX;
	}
	public void setSpeedY (int speedY) {
		this.speedY = speedY;
	}
	public int getSpeedX() {
		return speedX;
	}
	public int getSpeedY() {
		return speedY;
	}
	
	/** Setters and getters for attributes. */
	public void setShape(Rectangle shape) {
		this.shape = shape;
	}
	public Rectangle getShape() {
		return shape;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	
	public void setIsDestroyed (boolean status) {
		this.isDestroyed = status;
	}
	public boolean getIsDestroyed () {
		return isDestroyed;
	}
	
	public void setTimesHit(int timesHit) {
		this.timesHit = timesHit;
	}
	
	public int getTimesHit() {
		return timesHit;
	}
}