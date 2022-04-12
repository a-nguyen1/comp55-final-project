package edu.pacific.comp55.starter;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap;

import javax.swing.Timer; // for Timer

import acm.graphics.GImage; // for GImage
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

public class DisplayPane extends GraphicsPane implements ActionListener{
	private MainApplication program;
	
	private ArrayList<GImage> backgroundTiles;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> bossHealth;
	private String displayType; // to display current game state (lose/win/playing)
	private ArrayList<Level> levels;
	private ArrayList<Item> items; // items to display on the level.
	private HashMap<String, String> itemLabel; 
	private int currentLevel;
	private int currentRoom;
	
	//Class objects
	private Player player;
	private boolean bulletTraveling;
	private int bulletDistance;
	private Enemy enemy;
	private GRect inventoryBox;
	
	private Timer timer;
	private int timerCount;
	
	private GImage bulletSprite;
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		
		//add background tiles
		backgroundTiles = new ArrayList<GImage>(); 
		for (int x = 0; x < program.getWidth(); x+=50) { // add tiles in x direction
			for (int y = 0; y < program.getHeight(); y+=50) { // add tiles in y direction
				backgroundTiles.add(new GImage("GrayTile.png", x, y));
			}
		}
		
		items = new ArrayList<Item>();
		itemLabel = new HashMap<String, String>();
		itemLabel.put("key", "Press e to pick up key.");
		itemLabel.put("closedDoor", "Press e to unlock door.");
		itemLabel.put("openDoor", "Press e to enter next room.");
		itemLabel.put("heart", "Press e to pick up heart.");
		
		//create player object
		GImage playerSprite = new GImage ("knight-sprite-with-sword.png", program.getWidth()/2, program.getHeight()/2);
		player = new Player(playerSprite, 3);
		player.setSpeed(7);
		//TODO change bulletSprite to actual bullet
		bulletSprite = new GImage("door.png", player.getSprite().getX() - player.getSprite().getWidth() / 2, player.getSprite().getY() - player.getSprite().getHeight() / 2);
		
		//create enemy object
		GImage enemySprite = new GImage ("bigger-enemy-sprite.png", 300, 50);
		enemy = new Enemy(enemySprite, 2); //Enemy has 2 health points.
		enemy.setSpeed(5);
		
		//create inventory box
		inventoryBox = new GRect(50, 0, 0, 0);
		inventoryBox.setVisible(false);
		
		//create key object
		GImage keySprite = new GImage ("keyImage.png", 200, 200); //Create a new sprite for key.
		keySprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem key = new PickUpItem(keySprite, "key"); //Create key as Item object.
		items.add(key);
		
		//create door object
		GImage doorSprite = new GImage ("closedDoor.png", 100, 100); //Create a new sprite for door.
		Door door = new Door(doorSprite, "closedDoor"); //Create door as Item object.
		items.add(door);
		
		//initialize playerHealth
		playerHealth = new ArrayList<GImage>(); 
		for (int x = 0; x < player.getHealth(); x++) { //add hearts based on player health
			playerHealth.add(new GImage("Heart.png", x*50, 0)); 
		}
		
		//create timer object and start timer
		timer = new Timer(0, this);
		timer.start();
	}

	public void setBackground(String b) { //TODO set background
		
	}
	
	public void createLevel(int l) { //TODO create level
		
	}
	
	public void createRoom(int r) { //TODO create room
		
	}
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GImage playerSprite = player.getSprite();
		GImage enemySprite = enemy.getSprite();
		timerCount++;
		if (timerCount % 300 == 0) {
			player.setAttackAvailable(true); //player can now attack
		}
		if (timerCount % 500 == 0) {
			player.setDashAvailable(true); //TODO show player that dash is available
		}
		if (timerCount % 100 == 0) {
			for (Item i : items) {
				if (player.canInteract(i.getSprite().getX(), i.getSprite().getY())) {
					if (player.hasKey() && i.getItemType() == "closedDoor") {
						itemLabel.put("closedDoor", "Press e to unlock door.");
					}
					String s = itemLabel.get(i.getItemType());
					i.setLabel(s);
					i.getLabel().setLocation(i.getSprite().getX(), i.getSprite().getY());
				} else {
					i.setLabel("");
				}
			}
		}
		if (bulletTraveling) {
			if (timerCount % 1 == 0) {
			bulletDistance++;
            bulletSprite.movePolar(1, player.getWeapon().getAngle()); // move towards mouse click    
            	if (bulletDistance >= player.getWeapon().getRange()) {
            		bulletTraveling = false;
            		bulletSprite.setLocation(playerSprite.getX(), playerSprite.getY());
            		bulletDistance = 0;
            		bulletSprite.setVisible(false);
            	}
			}
		}
		if (enemy.canInteract(playerSprite.getX(), playerSprite.getY())) {
			// x is set to horizontal distance between enemy and middle of playerSprite
			double x = (enemySprite.getX() - enemySprite.getWidth() / 2) - (playerSprite.getX() - playerSprite.getWidth() / 2);
			// y is set to vertical distance between enemy and middle of playerSprite
			double y = (enemySprite.getY() - enemySprite.getHeight() / 2) - (playerSprite.getY() - playerSprite.getHeight() / 2);
			if (timerCount % 100 == 0) {
				enemySprite.movePolar(enemy.getSpeed(), (180 * Math.atan2(-y, x) / Math.PI) + 180); // enemy move towards player
			}
			if (enemy.overlapping(player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getWidth(), player.getSprite().getHeight())) {
				playerSprite.movePolar(Math.sqrt(x*x+y*y), (180 * Math.atan2(-y, x) / Math.PI) + 180); // player move away from enemy
			}
		}
		if (!player.healthIsZero()) { //player is alive
			
		} else { //player is dead
			
		}
	}

	@Override
	public void showContents() {
		Weapon weapon = new Weapon(new GImage("sword.png"), "close range weapon"); //TODO change so image shows up near player hand
		if (program.isCloseRangeWeapon()) {
			weapon.setRange(50);
			player.setWeapon(weapon);
			}
		else {
			weapon.setItemType("long range weapon");
			weapon.setRange(200);
			player.setWeapon(weapon);
		}
		for (GImage tile: backgroundTiles) { // add all tiles to display
			program.add(tile);
		}
		for (Item i : items) {
			program.add(i.getSprite()); //Add item sprite to the screen.
			program.add(i.getLabel()); //Add item label to the screen.
		}
		for (GImage heart: playerHealth) { // add all hearts to display
			heart.setSize(50,50);
			program.add(heart);
		}
		program.add(player.getSprite()); //Add player sprite to screen.
		program.add(enemy.getSprite()); //Add enemy sprite to screen.
		program.add(inventoryBox); //Add inventory box to the screen.
		program.add(bulletSprite);
	}

	@Override
	public void hideContents() {
		for (Item i : items) {
			program.remove(i.getSprite()); //remove item sprite from the screen.
			program.remove(i.getLabel()); //remove item label from the screen.
		}
		program.remove(player.getSprite());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { //TODO implement close range attack
		if (program.isCloseRangeWeapon()) {
			if (player.canInteract(enemy.getSprite().getX(), enemy.getSprite().getY())) { //player in range of enemy.
				System.out.println("Enemy is hit.");
				enemy.healthChanged(-1); //Reduce health by 1.
				if (enemy.healthIsZero()) { //Enemy has no health.
					program.remove(enemy.getSprite()); //Remove enemy from the screen since he is dead.
					System.out.println("Enemy is dead.");
				}
			}
		}
		else { // long range attack
			if (player.isAttackAvailable()) {
				//x is set to horizontal distance between mouse and middle of playerSprite
	            double x = MouseInfo.getPointerInfo().getLocation().getX() - bulletSprite.getX() - bulletSprite.getWidth() / 2;
	            //y is set to vertical distance between mouse and middle of playerSprite
	            double y = MouseInfo.getPointerInfo().getLocation().getY() - bulletSprite.getY() - bulletSprite.getHeight() / 2;
	            player.getWeapon().setAngle(180 * Math.atan2(-y, x) / Math.PI);	
				bulletSprite.setVisible(true);
				bulletTraveling = true;
				bulletSprite.setLocation(player.getSprite().getX(), player.getSprite().getY());
				}
			}
		player.setAttackAvailable(false); // Initiate attack cool down.
		}
	
	@Override
	public void keyPressed(KeyEvent e) {
		GImage playerSprite = player.getSprite();
		int keyCode = e.getKeyCode();
		if (keyCode == 87) { // w
			player.setMoveY(-1);
		} else if (keyCode == 65) { // a
			player.setMoveX(-1);
		} else if (keyCode == 83) { // s
			player.setMoveY(1);
		} else if (keyCode == 68) { // d
			player.setMoveX(1);
		} else if (keyCode == 16 && player.isDashAvailable()) { // SHIFT
			timer.stop();
			player.setDashAvailable(false);
			// x is set to horizontal distance between mouse and middle of playerSprite
			double x = MouseInfo.getPointerInfo().getLocation().getX() - playerSprite.getX() - playerSprite.getWidth() / 2;
			// y is set to vertical distance between mouse and middle of playerSprite
			double y = MouseInfo.getPointerInfo().getLocation().getY() - playerSprite.getY() - playerSprite.getHeight() / 2;
			playerSprite.movePolar(player.getSpeed() * player.getSpeed() * 2, 180 * Math.atan2(-y, x) / Math.PI); // dash in direction of mouse
			timer.start();
		} else if (keyCode == 69) { // e
			Item nearestItem = player.nearestItem(items);
			//if nearest item is a PickUpItem, add to player inventory
			if (player.canInteract(nearestItem.getSprite().getX(), nearestItem.getSprite().getY())) {
				if (nearestItem instanceof PickUpItem && !((PickUpItem) nearestItem).getInInventory()) {
					player.addToInventory(nearestItem);
					nearestItem.getSprite().setLocation(50 * player.getHealth(), 12.5); //TODO change after hearts are implemented
					inventoryBox.setVisible(true); 
					inventoryBox.setSize(25*player.getInventory().size(), 25);
					inventoryBox.setLocation(50 * player.getHealth(), 12.5);
					((PickUpItem) nearestItem).setInInventory(true);
					program.remove(nearestItem.getLabel()); // remove key label
				}
				else if (nearestItem instanceof Door) {
					boolean doorStateBefore = !((Door)nearestItem).getLocked();
					boolean unlockedDoor = ((Door)nearestItem).unlock(player.getInventory());
					if (unlockedDoor){
						if (doorStateBefore == unlockedDoor) { // door has already been opened
							program.removeAll(); //remove all to create next room
							for (GImage tile: backgroundTiles) { // add background tiles
								program.add(tile);
							}
							for (Item i : items) { 
								if (i.getItemType() == "key") { //reset key to default values
									i.getSprite().setLocation(200,200);
									((PickUpItem)i).setInInventory(false);
								}
								else if (i.getItemType() == "openDoor") { //reset door to default values
									i.setItemType("closedDoor");
									((Door)i).setLocked(true);
									itemLabel.put("closedDoor", "Press e to unlock door.");
								}
								program.add(i.getSprite()); //Add item sprite to the screen.
								program.add(i.getLabel()); //Add item label to the screen.
							}
							for (GImage heart: playerHealth) { // add all hearts to display
								heart.setSize(50,50);
								program.add(heart);
							}
							program.add(player.getSprite()); //Add player sprite to screen.
							program.add(enemy.getSprite()); //Add enemy sprite to screen.
							inventoryBox.setSize(25*player.getInventory().size(), 25);
							if (player.getInventory().size() == 0) {
								inventoryBox.setVisible(false);
							}
							program.add(inventoryBox); //Add inventory box to the screen.
							program.add(bulletSprite);
						}
						int removeIndex = -1;
						if (player.getInventory().size() > 0) {
							for (int x = 0; x < player.getInventory().size(); x++) {
								if (player.getInventory().get(x).getItemType() == "key") { // check for key in player inventory
									removeIndex = x;
								}
							}
							if (removeIndex >= 0) {
								player.removeFromInventory(removeIndex); // remove key from player inventory
							} 
							if (nearestItem.getItemType() == "closedDoor") { // change door to open door
								nearestItem.setItemType("openDoor");
								program.add(((Door)nearestItem).getOpenDoor()); // add open door GImage to screen
								((GObject)player.getSprite()).sendToFront(); // send player to the front of the screen
							}
						}
					}
					else {
						itemLabel.put("closedDoor", "A key is needed."); 
					}
				}
				//if nearest item is a Chest, open the chest
				//if nearest item is a Weapon, swap player's current weapon with new weapon
			}
			
		}
		// for normalizing diagonal movement
		if (Math.abs(player.getMoveX()) == 1 && Math.abs(player.getMoveY()) == 1) { // check if diagonal movement is happening
			player.setMoveX(player.getMoveX() * 0.7071067811865476);
			player.setMoveY(player.getMoveY() * 0.7071067811865476);
		}
		
		playerSprite.move(player.getMoveX() * player.getSpeed(), player.getMoveY() * player.getSpeed()); // move playerSprite
		
		// setting bounds for player
		if (playerSprite.getLocation().getX() < 0) {
			playerSprite.setLocation(5, playerSprite.getY());
		} else if (playerSprite.getLocation().getY() < 0) {
			playerSprite.setLocation(playerSprite.getX(), 5);
		} else if (playerSprite.getLocation().getX() + playerSprite.getWidth() * 1.75 > program.getWidth()) {
			playerSprite.setLocation(program.getWidth() - playerSprite.getWidth() * 1.75,playerSprite.getY());
		} else if (playerSprite.getLocation().getY() + playerSprite.getHeight() * 2.25 > program.getHeight()) {
			playerSprite.setLocation(playerSprite.getX(), program.getHeight()-playerSprite.getHeight() * 2.25);
		} 
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == 87 || keyCode == 83) { // w or s -> reset vertical movement when released
			player.setMoveY(0);
		} else if (keyCode == 65 || keyCode == 68) { // a or d -> reset horizontal movement when released
			player.setMoveX(0);
		}
	}
}
