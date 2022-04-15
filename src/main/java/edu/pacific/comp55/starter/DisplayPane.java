package edu.pacific.comp55.starter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap; // for HashMap

import javax.swing.Timer; // for Timer

import acm.graphics.GImage; // for GImage
import acm.graphics.GObject; // for GObject
import acm.graphics.GRect; // for GRect

public class DisplayPane extends GraphicsPane implements ActionListener{
	private MainApplication program;
	
	private ArrayList<GImage> backgroundTiles;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> playerInventory;
	private ArrayList<GImage> bossHealth; // TODO implement boss health
	private String displayType; // to display current game state (lose/win/playing)
	private ArrayList<Level> levels;
	private ArrayList<Item> items; // items to display on the level.
	private HashMap<String, String> itemLabel; 
	private int currentLevel;
	private int currentRoom;
	private double mouseX;
	private double mouseY;
	private boolean isChestOpen;
	
	//Class objects
	private Player player;
	private ArrayList<Enemy> enemies;
	private GRect inventoryBox;
	private Timer timer;
	
	private int timerCount;
	//GImage openChestSprite = new GImage("open-chest.png", 500, 200); //Create an open chest sprite for switch.
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		
		currentLevel = 1;
		currentRoom = 1;
		isChestOpen = false;
		
		playerHealth = new ArrayList<GImage>(); // initialize playerHealth
		playerInventory = new ArrayList<GImage>(); // initialize playerInventory
		items = new ArrayList<Item>(); // initialize items in room
		enemies = new ArrayList<Enemy>(); // initialize enemy array list
		
		itemLabel = new HashMap<String, String>();
		itemLabel.put("key", "Press e to pick up key.");
		itemLabel.put("closedDoor", "Press e to unlock door.");
		itemLabel.put("openDoor", "Press e to enter next room.");
		itemLabel.put("heart", "Press e to pick up heart.");
		itemLabel.put("chest", "Press e to open chest.");
		
		//create player object with knight sprite as default.
		GImage playerSprite = new GImage ("knight-sprite-with-sword.png", program.getWidth()/2, program.getHeight()/2);
		player = new Player(playerSprite, 5);
		player.setSpeed(7);
		
		//create inventory box
		inventoryBox = new GRect(50, 0, 0, 0);
		inventoryBox.setVisible(false);
		
		timer = new Timer(0, this); // create timer object 
		timer.start(); // start timer
	}

	public void setBackground(String tileFile) {
		backgroundTiles = new ArrayList<GImage>(); 
		for (int x = 0; x < program.getWidth(); x += 50) { // add tiles in x direction
			for (int y = 0; y < program.getHeight(); y += 50) { // add tiles in y direction
				backgroundTiles.add(new GImage(tileFile, x, y));
			}
		}
	}
	
	public void createLevel(int l) { //TODO create level
		
	}
	
	public void createRoom(int roomNum) {
		Room newRoom = new Room(currentLevel, roomNum, program.getWidth(), program.getHeight());
		items = newRoom.getItems();
		enemies = newRoom.getEnemies();
		
		program.removeAll();
		
		setBackground(newRoom.getTileName()); //Set background tile
		for (GImage tile: backgroundTiles) { //Add all tiles to the screen.
			program.add(tile);
		}
		/*
		for (Item i : items) {
			if (i.getItemType() == "key") { //reset key to default values
				((PickUpItem)i).setInInventory(false);
			}
			else if (i.getItemType() == "openDoor") { //reset door to default values
				i.setItemType("closedDoor");
				((Door)i).setLocked(true);
				i.setSprite(new GImage ("closedDoor.png", 300, 100));
				itemLabel.put("closedDoor", "Press e to unlock door.");
				program.add(i.getLabel());
			}
			if (i instanceof PickUpItem && !((PickUpItem)i).getInInventory()) { // check if item is not in player inventory
				program.add(i.getLabel()); //Add PickUp item label to the screen.
			}
			program.add(i.getSprite()); //Add item sprite to the screen.
		}
		*/
		
		for (Item i : items) {
			program.add(i.getSprite()); //Add item sprite to the screen.
			program.add(i.getLabel()); //Add item label to the screen.
		}
		//TODO add weapon to screen and make player weapon show in player hand 
		if (program.isCloseRangeWeapon()) {
			Weapon weapon = new Weapon(new GImage("sword.png"), "close range weapon"); 
			weapon.setRange(50);
			player.setWeapon(weapon);
		}
		else { //long range weapon selected
			player.setSprite(new GImage ("wizardSprite.png", program.getWidth()/2, program.getHeight()/2));
			Weapon weapon = new Weapon(new GImage("bow.png"), "long range weapon");
			weapon.setRange(200);
			player.setWeapon(weapon);
			program.add(player.getBulletSprite());
		}
		for (Enemy enemy: enemies) { // loop for all enemies
			program.add(enemy.getSprite()); //Add enemy sprite to screen.
		}
		program.add(inventoryBox); //Add inventory box to the screen.
		updateHealth(); // update player health display
		updateInventory(); // update player inventory display
		program.add(player.getSprite()); //Add player sprite to screen.
	}
	
	public void updateHealth() {
		while (playerHealth.size() > 0) { // remove all hearts from screen
			program.remove(playerHealth.get(0));
			playerHealth.remove(0);
		}
		
		playerHealth = player.displayHealth();
		for (GImage heart : playerHealth) { // display all hearts
			heart.setSize(50,50);
			program.add(heart);
		}
	}
	
	public void updateInventory() { 
		while (playerInventory.size() > 0) { // remove all inventory items from screen
			program.remove(playerInventory.get(0));
			playerInventory.remove(0);
		}
		playerInventory = player.displayInventory();
		for (GImage i : playerInventory) { // display all inventory items
			i.setSize(25, 25);
			program.add(i);
		}
		player.displayInventoryBox(inventoryBox); // display inventory box accordingly
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GImage playerSprite = player.getSprite();
		ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>(); // for removing dead enemies
		for (int z = 0; z < enemies.size(); z++) {
			Enemy enemy = enemies.get(z);
			GImage enemySprite = enemy.getSprite();
			if (timerCount % 1 == 0) {
				if (player.isBulletTraveling()) {
					GImage bulletSprite = player.getBulletSprite();
					player.setBulletDistance(player.getBulletDistance() + 1);
					bulletSprite.movePolar(1, player.getWeapon().getAngle()); // move towards mouse click   
					double xDiff = Math.abs(bulletSprite.getX() + bulletSprite.getWidth()/2 - (enemySprite.getX() + enemySprite.getWidth() / 2)); // find difference in x coordinates
					double yDiff = Math.abs(bulletSprite.getY() + bulletSprite.getHeight()/2 - (enemySprite.getY() + enemySprite.getHeight() / 2)); // find difference in y coordinates
					if (enemy.isDamaged()) {
						enemy.setInvincibilityCounter(enemy.getInvincibilityCounter() + 1); //enemy is invincible for a time.
						if (enemy.getInvincibilityCounter() > 100) { //enemy is not invincible.
							enemy.setDamaged(false);
							enemy.setInvincibilityCounter(0); 
						}
					}
					else {
						if (xDiff <= enemySprite.getWidth() && yDiff <= enemySprite.getHeight()) { //returns true if x,y coordinates are within enemy
							enemy.changeHealth(-1);
							enemy.setDamaged(true); //Enemy is damaged.
							System.out.println(enemy.getHealth());
							if (enemy.isDead()) { //Enemy has no health.
								removeEnemyIndex.add(z); // add index to ArrayList
								program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
								System.out.println("Enemy is dead.");
							}
						}
					}
					if (player.getBulletDistance() >= player.getWeapon().getRange()) {
						player.setBulletTraveling(false);
						bulletSprite.setLocation(playerSprite.getX() + playerSprite.getWidth() / 2 - bulletSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2);
						player.setBulletDistance(0);
						bulletSprite.setVisible(false);
					}
				}
				if (enemy.canInteract(playerSprite.getX(), playerSprite.getY())) { //enemy detects player
					// x is set to horizontal distance between enemy and player
					double x = (enemySprite.getX() + enemySprite.getWidth() / 2) - (playerSprite.getX() + playerSprite.getWidth() / 2);
					// y is set to vertical distance between enemy and player
					double y = (enemySprite.getY() + enemySprite.getHeight() / 2) - (playerSprite.getY() + playerSprite.getHeight() / 2);
					if (timerCount % 100 == 0) {
						enemySprite.movePolar(enemy.getSpeed(), (180 * Math.atan2(-y, x) / Math.PI) + 180); // enemy moves towards player
					}
					if (enemy.overlapping(player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getWidth(), player.getSprite().getHeight())) {
						playerSprite.movePolar(Math.sqrt(x*x+y*y), (180 * Math.atan2(-y, x) / Math.PI) + 180); // player moves away from enemy
						if (enemy.getEnemyType() == "close range") {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player hit: " + player.getHealth());
						}
						else {
							System.out.println("Player not hit: " + player.getHealth());
						}
						//if (player.isDead()) {
							//program.remove(playerSprite);
						//}
					}
				}
			}
		}
		if (removeEnemyIndex.size() > 0) { // remove all dead enemies
			for (int y = 0; y < removeEnemyIndex.size(); y++) {
				System.out.println(removeEnemyIndex.get(y));
				enemies.remove((int)removeEnemyIndex.get(y));
			}
		}
		if (enemies.size() == 0) {
			player.getBulletSprite().setVisible(false); // hide bullet if no enemies
		}
		timerCount++;
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
			if (timerCount % 300 == 0) {
				player.setAttackAvailable(true); //player can now attack
			}
			else if (timerCount % 500 == 0) {
				player.setDashAvailable(true); //player can now dash
			}
		}
	}
	
	@Override
	public void showContents() {
		createRoom(currentRoom); // currentRoom is initially at 1
	}

	@Override
	public void hideContents() {
		program.removeAll();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { //TODO implement close range attack
		if (program.isCloseRangeWeapon()) {
			ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>();
			for (int x = 0; x < enemies.size(); x++) {
				Enemy enemy = enemies.get(x);
				if (player.canInteract(enemy.getSprite().getX(), enemy.getSprite().getY())) { //player in range of enemy.
					System.out.println("Enemy is hit.");
					enemy.changeHealth(-1); //Reduce health by 1.
					if (enemy.isDead()) { //Enemy has no health.
						removeEnemyIndex.add(x); // add index to ArrayList
						program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
						System.out.println("Enemy is dead.");
					}
				}
			}
			if (removeEnemyIndex.size() > 0) { // remove all dead enemies
				for (int y = 0; y < removeEnemyIndex.size(); y++) {
					System.out.println(removeEnemyIndex.get(y));
					enemies.remove((int)removeEnemyIndex.get(y));
				}
			}
		}
		else { // long range attack
			if (player.isAttackAvailable()) {
				GImage bulletSprite = player.getBulletSprite();
				GImage playerSprite = player.getSprite();
				//x is set to horizontal distance between mouse and middle of playerSprite
	            double x = e.getX() - ( playerSprite.getX() + (player.getSprite().getWidth() / 2));
	            //y is set to vertical distance between mouse and middle of playerSprite
	            double y = e.getY() - (playerSprite.getY() + (player.getSprite().getHeight() / 2));
	            y = - y;
	            player.getWeapon().setAngle(180 * Math.atan2(y, x) / Math.PI);	
				bulletSprite.setVisible(true);
				player.setBulletTraveling(true);
				bulletSprite.setLocation( playerSprite.getX() + (player.getSprite().getWidth() / 2) - bulletSprite.getWidth() / 2, playerSprite.getY() + (player.getSprite().getHeight() / 2) - bulletSprite.getHeight() / 2);
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
			double x = mouseX - (playerSprite.getX() +playerSprite.getWidth() / 2);
			// y is set to vertical distance between mouse and middle of playerSprite
			double y = mouseY - (playerSprite.getY() + playerSprite.getHeight() / 2);
			playerSprite.movePolar(player.getSpeed() * player.getSpeed() * 2, 180 * Math.atan2(-y, x) / Math.PI); // dash in direction of mouse
			timer.start();
		} else if (keyCode == 69) { // e
			Item nearestItem = player.nearestItem(items); //check for item nearest to player
			if (player.canInteract(nearestItem.getSprite().getX(), nearestItem.getSprite().getY())) {
				//if nearest item is a PickUpItem
				if (nearestItem instanceof PickUpItem && !((PickUpItem) nearestItem).getInInventory()) { // check if PickUpItem and if not in inventory
					player.addToInventory(nearestItem); // add item to player inventory
					((PickUpItem) nearestItem).setInInventory(true);
					program.remove(nearestItem.getLabel()); // remove item label
					updateInventory();
				}
				else if (nearestItem instanceof Chest) {
					if (!isChestOpen) {
						GImage openChestSprite = new GImage("open-chest.png", 500, 200); //Create an open chest sprite for switch.
						openChestSprite.setSize(25, 25);
						program.remove(items.get(2).getSprite()); //Remove closed chest sprite.
						items.get(2).setSprite(openChestSprite); //set the sprite to the open chest.
						program.add(openChestSprite); //Add open chest sprite.
						//GImage heartSprite2 = new GImage("") 
					}
					isChestOpen = true;
					
				}
				else if (nearestItem instanceof Door) { //if nearest item is a Door
					boolean doorStateBefore = !((Door)nearestItem).getLocked(); // to check if door is already opened
					boolean unlockedDoor = ((Door)nearestItem).unlock(player.getInventory()); // to check if door is unlocked
					if (unlockedDoor){
						if (doorStateBefore == unlockedDoor) { // door has already been opened, so create next room
							createRoom(currentRoom + 1); // create next room
						}
						int removeIndex = -1;
						if (player.getInventory().size() > 0) {
							for (int x = 0; x < player.getInventory().size(); x++) {
								if (player.getInventory().get(x).getItemType() == "key") { // check for key in player inventory
									removeIndex = x;
								}
							}
							if (removeIndex >= 0) { // check if player has key to remove
								player.removeFromInventory(removeIndex); // remove key from player inventory
								if (nearestItem.getItemType() == "closedDoor") { // change door to open door
									nearestItem.setItemType("openDoor");
									program.add(((Door)nearestItem).getOpenDoor()); // add open door GImage to screen
									((GObject)player.getSprite()).sendToFront(); // send player to the front of the screen
								}
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
		if (playerSprite.getLocation().getX() < 5) {
			playerSprite.setLocation(5, playerSprite.getY());
		} else if (playerSprite.getLocation().getY() < 5) { 
			playerSprite.setLocation(playerSprite.getX(), 5);
		} else if (playerSprite.getLocation().getX() + playerSprite.getWidth() * 1.75 > program.getWidth()) {
			playerSprite.setLocation(program.getWidth() - playerSprite.getWidth() * 1.75,playerSprite.getY());
		} else if (playerSprite.getLocation().getY() + playerSprite.getHeight() * 2.25 > program.getHeight()) {
			playerSprite.setLocation(playerSprite.getX(), program.getHeight() - playerSprite.getHeight() * 2.25);
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
	
	public static void main(String[] args) {
		
	}
}