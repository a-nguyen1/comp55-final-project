package edu.pacific.comp55.starter;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for ArrayList

import javax.swing.Timer; // for Timer

import acm.graphics.GImage; // for GImage
import acm.graphics.GLabel;
import acm.graphics.GObject;

public class DisplayPane extends GraphicsPane implements ActionListener{
	private MainApplication program;
	
	private GImage background;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> bossHealth;
	private String displayType; // to display current game state (lose/win/playing)
	private ArrayList<Level> levels;
	private ArrayList<Item> items;
	private int currentLevel;
	
	//Class objects
	private Player player;

	private double dx = 0;
	private double dy = 0;
	private double speed = 7;
	
	private Timer timer;
	private boolean dashAvailable = true;
	private int timerCount;

	private PickUpItem key; 
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		//background = new GImage("bow.png", program.getWidth() * 2 / 3, 200);
		
		items = new ArrayList<Item>();
		
		//Add playerSprite to the screen and create player object
		GImage playerSprite = new GImage ("knight-sprite4.png", program.getWidth()/2, program.getHeight()/2);
		player = new Player(playerSprite, 5);
		
		//Add key item to the screen.
		GImage keySprite = new GImage ("keyImage.png", 200, 200); //Create a new sprite for key.
		keySprite.setSize(25, 25); //Resize sprite to make it smaller.
		key = new PickUpItem(keySprite, "key"); //Create key as Item object.
		
		items.add(key);
		
		//Add player health to the screen.
		GImage playerHPSprite = new GImage("heartImage.png", 0, 0); //Create a new sprite for player HP.
		playerHPSprite.setSize(50, 50); //Resize sprite to make it smaller.
		//playerHPSprite.move(0, 0);
		playerHealth = new ArrayList<GImage>(); 
		playerHealth.add(playerHPSprite); //Add sprite to playerHealth arraylist.
		
		//create timer object and start timer
		timer = new Timer(0, this);
		timer.start();
	}

	public void setBackground(String b) { //TODO set background
		//background = newGImage(b, program.getWidth() / 2, program.getHeight() / 2);
	}
	
	public void createLevel(int l) { //TODO create level
		
	}
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timerCount++;
		if (timerCount % 500 == 0) {
			dashAvailable = true; //TODO show to player that dash is available
		}
		if (timerCount % 100 == 0) {
			if (player.canInteract(key.getImage().getX(), key.getImage().getY())) { //TODO make player able to do this check for all items
				String s = "press e to pick up key";
				key.setLabel(s);
				key.getLabel().setLocation(key.getImage().getX(), key.getImage().getY());
			} else {
				key.setLabel("");
			}
		}
	}

	@Override
	public void showContents() {
		//program.add(background);
		program.add(key.getImage()); //Add key sprite to the screen.
		program.add(playerHealth.get(0)); //Add first element of playerHealth (initial amount of health).
		program.add(player.getSprite()); //Add player sprite to screen.
		program.add(key.getLabel()); //Add key label to the screen.
		
	}

	@Override
	public void hideContents() {
		program.remove(player.getSprite());
		program.remove(key.getImage());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { //TODO implement attack

	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		GImage playerSprite = player.getSprite();
		int keyCode = e.getKeyCode();
		if (keyCode == 87) { // w
			dy = -1;
		} else if (keyCode == 65) { // a
			dx = -1;
		} else if (keyCode == 83) { // s
			dy = 1;
		} else if (keyCode == 68) { // d
			dx = 1;
		} else if (keyCode == 16 && dashAvailable) { // SHIFT
			timer.stop();
			dashAvailable = false;
			// x is set to horizontal distance between mouse and middle of playerSprite
			double x = MouseInfo.getPointerInfo().getLocation().getX() - playerSprite.getX() - playerSprite.getWidth() / 2;
			// y is set to vertical distance between mouse and middle of playerSprite
			double y = MouseInfo.getPointerInfo().getLocation().getY() - playerSprite.getY() - playerSprite.getHeight() / 2;
			playerSprite.movePolar(speed * speed * 2, 180 * Math.atan2(-y, x) / Math.PI); // dash in direction of mouse
			timer.start();
		} else if (keyCode == 69) { // e
			Item nearestItem = player.nearestItem(items);
			//if nearest item is a PickUpItem, add to player inventory
			if (player.canInteract(nearestItem.getImage().getX(), nearestItem.getImage().getY())) {
				if (nearestItem instanceof  PickUpItem) {
					//player.addToInventory(nearestItem);
					System.out.println("player can pick up item");
				}
				//if nearest item is a Door, if player has key, unlock. if player has no key, set item label to indicate that a key is needed
				//if nearest item is a Chest, open the chest
				//if nearest item is a Weapon, swap player's current weapon with new weapon
			}
			
		}
		// for normalizing diagonal movement
		if (Math.abs(dx) == 1 && Math.abs(dy) == 1) { // check if diagonal movement is happening
			dx = dx * 0.7071067811865476;
			dy = dy * 0.7071067811865476;
		}
		
		playerSprite.move(dx * speed, dy * speed); // move playerSprite
		
		// setting bounds for player
		if (playerSprite.getLocation().getX() < 0) {
			playerSprite.setLocation(0,playerSprite.getY());
		} else if (playerSprite.getLocation().getY() < 0) {
			playerSprite.setLocation(playerSprite.getX(),0);
		} else if (playerSprite.getLocation().getX()+ playerSprite.getWidth() > program.getWidth()) {
			playerSprite.setLocation(program.getWidth()-playerSprite.getWidth(),playerSprite.getY());
		} else if (playerSprite.getLocation().getY()+ playerSprite.getHeight() * 2 > program.getHeight()) {
			playerSprite.setLocation(playerSprite.getX(), program.getHeight()-playerSprite.getHeight() * 2);
		} 
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == 87 || keyCode == 83) { // w or s -> reset vertical movement when released
			dy = 0;
		} else if (keyCode == 65 || keyCode == 68) { // a or d -> reset horizontal movement when released
			dx = 0;
		}
	}
}
