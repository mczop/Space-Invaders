//: michalspaceinvaders/Game.java

package michalspaceinvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Gameplay class.
 * Extends Canvas.
 * @author Michal Czop
 */
public class Game extends Canvas {

	private BufferStrategy strategy;
	
	/** Objects taking part in gameplay. */
	private GameObject player;

	/** Object holding references to all objects taking part in gameplay. */
	private ObjectHolder objects;
	
	/** Holder of object to delete after current iteration. */
	private ArrayList<GameObject> objectsToDelete = new ArrayList<GameObject>();
	
	/** State machine variable. */
	private String state = "gameIntro";
	
	/** Boolean flags. */
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean firePressed = false;
	private boolean enterPressed = false;
	protected boolean gameLost = false;
	private boolean gameWon = false;
	protected boolean logicNeeded = false;
	private boolean initLevel = true;
	private boolean levelWon = false;
	private boolean gameRunning = true;
	
	/**Intensity of player's color. Changes during loading a missile. */ 
	private double playerColorIntensivity = 1;
	
	/** Variables for calculating before new shot available. */
	private long lastShotTime;
	private long lastAlienShotTime;
	private long shotDelayTime = 600;
	
	/** Amount of aliens left in game. */
	private int alienCounter;
	
	/** Chance for each alien to attack. */
	private int alienAttack;
	
	/** How many hits each alien need to receive before killed. */
	private int hitsToKill;
	
	/** Number of levels in game. */
	private int levels = 3;
	
	/** Number of current level. */
	private int currentLevel = 1;
	
	/** Variable for randomizing alien shots. */
	private Random rand = new Random();
	
	private Font bigFont = new Font("SANS_SERIF", Font.BOLD, 36);
	private Font smallFont = new Font("SANS_SERIF", Font.ITALIC, 16);

	/** Constructor. */
	public Game() {
		
		/** Setting graphics for game. */
		setBounds(0,0,800,600);
		setIgnoreRepaint(true);
		requestFocus();

		JFrame mainWindow = new JFrame("Space Invaders (written by Micha¸ Czop)");
		
		JPanel panel = (JPanel) mainWindow.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
		panel.add(this);
		
		mainWindow.pack();
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);

		/** Buffer strategy for displaying graphics. */
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		mainWindow.addKeyListener(new KeyPressedHandler());
		
		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		objects = ObjectHolder.getInstance();
	}
	
	/**
	 * Initialization of level. Setting alienAttack and hitsToKill variables. 
	 * @param currentLevel - number of initialized level.
	 */
	public void initLevel(int currentLevel) {
		switch (currentLevel) {
			case 1:	alienAttack = 1;
				hitsToKill = 1;
				break;

			case 2:	alienAttack = 3;
				hitsToKill = 1;
				break;

			case 3:	alienAttack = 2;
				hitsToKill = 2;
				break;

				default: break;
		}
		
		/** Creating objects and configuring game. */ 
		objects.clearObjects();
		player = new PlayerObject(this, 380, 550);
		objects.addObject(player);
		initAliens();
		alienCounter = 40;
	}
	
	/** Initialization of alien objects. */
	public void initAliens() {
		for (int i1 = 0; i1 < 4; i1++) {
			for(int i2 = 0; i2 < 10; i2++) {
				objects.addObject(new AlienObject(this, (i2*2+1)*25, (i1*2+1)*25));
			}
		}
	}
	
	/** Calculation of alien color, dependent on times hit. 
	 * 
	 * @param timesHit - number of hits received
	 * @param hitsTokill - how many hits each alien need to receive before killed
	 * @return current color of alien
	 */
	public Color alienColorCalc(int timesHit, int hitsTokill) {
		return new Color((int)(255*(double)(1-(double)timesHit/hitsTokill)),0,0);
	}
	
	/** Internal class for handling key events. */
	private class KeyPressedHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {leftPressed = true;}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {rightPressed = true;}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {firePressed = true;}
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {enterPressed = true;}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {System.exit(0);}
		}

		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {leftPressed = false;}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {rightPressed = false;}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {firePressed = false;}
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {enterPressed = false;}
		}
	}
	
	/** Introducing screen of game. */
	public void gameIntro() {
		while(!enterPressed) {
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setFont(bigFont);
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);
			g.setColor(Color.white);
			g.drawString("Press Enter to start", 200, 250);
			
			g.dispose();
			strategy.show();
		}
		/** Change of state machine variable. */
		state = "levelIntro";
	}
	
	/** Introducing screen of level. */
	public void levelIntro(int currentLevel) {
		
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setFont(bigFont);
		g.setColor(Color.black);
		g.fillRect(0,0,800,600);
		g.setColor(Color.white);
		g.drawString("LEVEL " + currentLevel, 320, 250);
		g.dispose();
		strategy.show();
		
		try { 
			Thread.sleep(2000); 
		} catch (Exception e) {}
		
		/** Change of state machine variable. */
		state = "game";
		
		gameRunning = true;
	}
		
	/** Main loop of game. */
	public void gameLoop() {
		
		long lastLoopTime = System.currentTimeMillis();

		while (gameRunning) {
			
			/** Calculations for loop timing. */
			long deltaTime = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			/** initialization of level. */
			if (initLevel) {
				initLevel(currentLevel);
				initLevel = false;
			}
			
			/** Preparing main window appearance. */
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);
			g.setColor(Color.white);
			
			/** Perform logic of each object. */
			if(logicNeeded) {
				for (int i = 0; i<objects.getSize(); i++) {
					objects.getObject(i).performLogic();
					logicNeeded = false;
				}
			}
			
			/** Handling of player's shots. Shot enabled after shotDelayTime passed.*/
			if (firePressed) {
				if ((System.currentTimeMillis() - lastShotTime) >= shotDelayTime) {
					objects.addObject(new PlayerMissleObject(this, (int)player.getX()+12, (int)player.getY()-50));
					lastShotTime = System.currentTimeMillis();
				}
			}
			
			/** Handling of player's color. */
			if((System.currentTimeMillis() - lastShotTime) / shotDelayTime >= 1) { playerColorIntensivity = 1; }
			else {
				playerColorIntensivity = (double)(System.currentTimeMillis() - lastShotTime) / shotDelayTime;
			}
			player.setColor(new Color((int)(255*playerColorIntensivity), (int)(255*playerColorIntensivity), (int)(255*(1 - playerColorIntensivity))));
			
			/** Handling of aliens' shots. Shot enabled after shotDelayTime passed.*/
			if ((System.currentTimeMillis() - lastAlienShotTime) >= shotDelayTime) {
				for (int i = 0; i < objects.getSize(); i++) {
					if (objects.getObject(i).getClass().getSimpleName().equals("AlienObject")) {
						if(rand.nextInt(objects.getSize()) < alienAttack) {
							objects.addObject(new AlienMissleObject(this, (int)objects.getObject(i).getX() + 12, (int)objects.getObject(i).getY() + 50));
							lastAlienShotTime = System.currentTimeMillis();
						}
					}
				}
			}
			
			/** Handling of spaceship movement.*/
			player.setSpeedX(0);
			if (leftPressed && !rightPressed) {
				player.setSpeedX(-(player.getDefaultSpeedX()));
			}
			if (!leftPressed && rightPressed) {
				player.setSpeedX(player.getDefaultSpeedX());
			}

			/** Perform movement of all GameObject's inside ObjectHolder. */
			for (int i = 0; i<objects.getSize(); i++) {
				objects.getObject(i).move(deltaTime);
			}
			
			/** Collision check between game objects. */
			for (int i1 = 0; i1 < objects.getSize(); i1++) {
				
				for(int i2 = i1+1; i2 < objects.getSize(); i2++) {
					if(objects.getObject(i1).collisionCheck(objects.getObject(i2))) {
						objects.getObject(i1).inCollision(objects.getObject(i2));
					}
				}	
			}
			
			/** Deleting of destroyed game objects. */
			for (int i = 0; i < objects.getSize(); i++) {
				if (objects.getObject(i).getTimesHit() >= hitsToKill) {
					objects.getObject(i).setIsDestroyed(true);
				}
				if (objects.getObject(i).getIsDestroyed() == true) {
					objectsToDelete.add(objects.getObject(i));
					if (objects.getObject(i).getClass().getSimpleName().equals("AlienObject")) {
						alienCounter--;
					}
					if (objects.getObject(i).getClass().getSimpleName().equals("PlayerObject")) {
						gameLost = true;
					}
				}
			}
			objects.deleteObject(objectsToDelete);
			
			/** Drawing of game objects. */
			for (int i = 0; i<objects.getSize(); i++) {
				if(objects.getObject(i).getClass().getSimpleName().equals("AlienObject")) {
					objects.getObject(i).setColor(alienColorCalc(objects.getObject(i).getTimesHit(), hitsToKill));
				}
				g.setColor(objects.getObject(i).getColor());
				objects.getObject(i).draw(g);
			}
			
			/** Drawing of graphics in buffer. */
			g.dispose();
			strategy.show();
			
			/** Checking of level won condition. */
			if (alienCounter <= 0) {
				
				/** Checking of game won condition. */
				if (currentLevel == levels) {
					gameWon = true;
					gameRunning = false;
				}
				else levelWon = true;
			}
			
			/** Setting viariables for new level initialization. */
			if (levelWon) {
				currentLevel++;
				initLevel = true;
				state = "levelIntro";
				levelWon = false;
				gameRunning = false;
			}
			
			/** End game if player was destroyed. */
			if (gameLost == true) {
				gameRunning = false;
				
			}
			
			/** Wait 20 miliseconds. */
			try { 
				Thread.sleep(20); 
			} catch (Exception e) {}
		}
		
		/** If player won the game - show final win screen. */
		if (gameWon) {
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);
			g.setColor(Color.white);
			g.setFont(bigFont);
			g.drawString("YOU WON", 310, 250);
			g.setFont(smallFont);
			g.drawString("Press Escape to exit", 320, 300);
			g.dispose();
			strategy.show();
		}
		
		/** If player won the game - show final loose screen. */
		if (gameLost) {
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);
			g.setColor(Color.white);
			g.setFont(bigFont);
			g.drawString("YOU LOST", 300, 250);
			g.setFont(smallFont);
			g.drawString("Press Escape to exit", 320, 300);
			g.dispose();
			strategy.show();
		}
		
	}

	public static void main(String argv[]) {
		Game game = new Game();
		while(!game.gameLost && !game.gameWon) {
			if(game.state.equals("gameIntro")) game.gameIntro();
			else if(game.state.equals("levelIntro")) game.levelIntro(game.currentLevel);
			else if(game.state.equals("game")) game.gameLoop();
		}
	}
}

