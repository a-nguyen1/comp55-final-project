package edu.pacific.comp55.starter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap; // for HashMap

import javax.swing.Timer; // for Timer

import acm.graphics.GImage; // for GImage
import acm.graphics.GLabel;
import acm.graphics.GObject; // for GObject
import acm.graphics.GRect; // for GRect

public class DisplayPane extends GraphicsPane implements ActionListener{
	private MainApplication program;
	
	private ArrayList<GImage> backgroundTiles;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> playerInventory;
	private ArrayList<GImage> bossHealth; // TODO implement boss health
	private ArrayList<Level> levels;
	private ArrayList<Item> items; // items to display on the level.
	private HashMap<String, String> itemLabel; 
	private String displayType; // to display current game state (lose/win/playing)
	private GImage attackArea;
	private int currentLevel;
	private int currentRoom;
	private double mouseX;
	private double mouseY;
	
	//Class objects
	private Player player;
	private ArrayList<Enemy> enemies;
	private GRect inventoryBox;
	private Timer timer;
	private SoundEffect sounds;
	
	private int timerCount;
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		
		currentLevel = 1;
		currentRoom = 1;
		
		bossHealth = new ArrayList<GImage>(); //initialize bossHealth
		playerHealth = new ArrayList<GImage>(); // initialize playerHealth
		playerInventory = new ArrayList<GImage>(); // initialize playerInventory
		items = new ArrayList<Item>(); // initialize items in room
		enemies = new ArrayList<Enemy>(); // initialize enemy array list
		AudioPlayer p = new AudioPlayer();
		sounds = new SoundEffect(p, "");
		
		
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
		attackArea = new GImage("TopRight.png"); // initialize attack area
		
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
		
		for (Item i : items) {
			program.add(i.getSprite()); //Add item sprite to the screen.
			program.add(i.getLabel()); //Add item label to the screen.
		}
		//TODO add weapon to screen and make player weapon show in player hand 
		if (program.isCloseRangeWeapon()) {
			Weapon weapon = new Weapon(new GImage("sword.png"), "close range weapon", 25);
			player.setWeapon(weapon);
			attackArea.setVisible(false);
			attackArea.setSize(weapon.getRange(), weapon.getRange());
			program.add(attackArea); // add attack area to the screen.
		}
		else { //long range weapon selected
			player.setSprite(new GImage ("wizardSprite.png", program.getWidth()/2, program.getHeight()/2));
			Weapon weapon = new Weapon(new GImage("bow.png"), "long range weapon", 200);
			player.setWeapon(weapon);
			program.add(player.getBulletSprite());
		}
		for (Enemy enemy: enemies) { // loop for all enemies
			program.add(enemy.getSprite()); //Add enemy sprite to screen.
			if (enemy.getEnemyType() == "long range") {
				program.add(enemy.getBulletSprite());
			}
		}
		if (currentRoom > 2) {
			((Boss) enemies.get(0)).setBossLabel(new GLabel("Big Goblin", 700, 25));
			program.add(((Boss) enemies.get(0)).getBossLabel());
		}
		program.add(inventoryBox); //Add inventory box to the screen.
		updateHealth(); // update player health display
		updateInventory(); // update player inventory display
		program.add(player.getSprite()); //Add player sprite to screen.
	}
	
	public void updateHealth() {
		while (playerHealth.size() > 0) { // remove all player hearts from screen
			program.remove(playerHealth.get(0));
			playerHealth.remove(0);
		}
		
		playerHealth = player.displayHealth();
		for (GImage heart : playerHealth) { // display all player hearts
			heart.setSize(50,50);
			program.add(heart);
		}
		if (currentRoom > 2) {
			while (bossHealth.size() > 0) { // remove all boss hearts from screen
				program.remove(bossHealth.get(0));
				bossHealth.remove(0);
			}
			bossHealth = ((Boss) enemies.get(0)).displayHealth();
			if (enemies.get(0).isDead()) {
				program.remove(((Boss) enemies.get(0)).getBossLabel());
			}
			for (GImage heart : bossHealth) { // display all boss hearts
				heart.setSize(50,50);
				program.add(heart);
			}
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
	
	
	public void GameOver() {
		GRect blackBackground = new GRect(0, 0, program.getWidth(), program.getHeight());
		blackBackground.setFillColor(Color.BLACK);
		blackBackground.setFilled(true);
		program.add(blackBackground);
		GLabel g= new GLabel (" G A M E  O V E R", 175,300);
		g.setFont(new Font("Merriweather", Font.BOLD, 50));
		g.setColor(Color.RED);
		program.add(g);
	}
	
	public void playSound(String e, AudioPlayer p) {
		if (e == "Big Goblin") {
			sounds.setName("boss_goblin_grunt"); //Sound effect for boss getting hit.
		}
		else if (e == "close range") {
			sounds.setName("small_goblin_grunt"); //Sound effect for enemy getting hit.
		}
		sounds.play(p); //Play enemy getting hit sound effect.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GImage playerSprite = player.getSprite();
		AudioPlayer p = sounds.getPlayer(); //Get the audio player object to play the sound.
		ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>(); // for removing dead enemies
		for (int z = 0; z < enemies.size(); z++) {
			Enemy enemy = enemies.get(z);
			GImage enemySprite = enemy.getSprite();
			if (timerCount % 1 == 0) {
				if (player.isBulletTraveling()) {
					GImage bulletSprite = player.getBulletSprite();
					player.setBulletDistance(player.getBulletDistance() + 1);
					bulletSprite.movePolar(1, player.getWeapon().getAngle()); // move towards mouse click   
					if (enemy.isDamaged()) {
						enemy.setInvincibilityCounter(enemy.getInvincibilityCounter() + 1); //enemy is invincible for a time.
						if (enemy.getInvincibilityCounter() > 100) { //enemy is not invincible.
							enemy.setDamaged(false);
							enemy.setInvincibilityCounter(0); 
						}
					}
					else {
						if (Collision.check(bulletSprite.getBounds(), enemy.getSprite().getBounds())) { //returns true if enemy collides with bullet 
							playSound(enemies.get(0).getEnemyType(), p); //play enemy grunt sound.
							enemy.changeHealth(-1);
							if (currentRoom > 2) {
								updateHealth();
							}
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
				
				if (enemy.isBulletTraveling()) {
					GImage bulletSprite = enemy.getBulletSprite();
					enemy.setBulletDistance(enemy.getBulletDistance() + 1);
					bulletSprite.movePolar(1, enemy.getWeapon().getAngle()); // move towards mouse click   
					if (player.isDamaged()) {
						player.setInvincibilityCounter(player.getInvincibilityCounter() + 1); //player is invincible for a time.
						if (player.getInvincibilityCounter() > 100) { //player is not invincible.
							player.setDamaged(false);
							player.setInvincibilityCounter(0); 
						}
					}
					else {
						if (Collision.check(bulletSprite.getBounds(), player.getSprite().getBounds())) { //returns true if enemy collides with bullet 
							//TODO player grunts
							//playSound(enemies.get(0).getEnemyType(), p); //play player grunt sound.
							player.changeHealth(-1);
							if (currentRoom > 2) {
								updateHealth();
							}
							player.setDamaged(true); //player is damaged.
							System.out.println(player.getHealth());
							if (player.isDead()) {
								program.removeAll();
								while (enemies.size() > 0) { // remove all enemies from ArrayList
									enemies.remove(0);
								}
								GameOver();
							}
						}
					}
					if (enemy.getBulletDistance() >= enemy.getWeapon().getRange()) {
						enemy.setBulletTraveling(false);
						bulletSprite.setLocation(enemySprite.getX() + enemySprite.getWidth() / 2 - bulletSprite.getWidth() / 2, enemySprite.getY() + enemySprite.getHeight() / 2 - bulletSprite.getHeight() / 2);
						enemy.setBulletDistance(0);
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
						if (enemy.getEnemyType() == "long range") {
							enemySprite.movePolar(2 * enemy.getSpeed(), (180 * Math.atan2(-y, x) / Math.PI)); // enemy moves away from player
						}
					}
					// setting bounds for enemy
					if (enemySprite.getLocation().getX() < 5) {
						enemySprite.setLocation(5, enemySprite.getY());
					} else if (enemySprite.getLocation().getY() < 5) { 
						enemySprite.setLocation(enemySprite.getX(), 5);
					} else if (enemySprite.getLocation().getX() + enemySprite.getWidth() * 1.75 > program.getWidth()) {
						enemySprite.setLocation(program.getWidth() - enemySprite.getWidth() * 1.75,enemySprite.getY());
					} else if (enemySprite.getLocation().getY() + enemySprite.getHeight() * 3 > program.getHeight()) {
						enemySprite.setLocation(enemySprite.getX(), program.getHeight() - enemySprite.getHeight() * 3);
					} 
					
					if (Collision.check(enemy.getSprite().getBounds(), player.getSprite().getBounds())) {
						playerSprite.movePolar(Math.sqrt(x*x+y*y), (180 * Math.atan2(-y, x) / Math.PI) + 180); // player moves away from enemy
						if (enemy.getEnemyType() == "close range") {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player hit by close enemy: " + player.getHealth());
						}
						else if (enemy.getEnemyType() == "long range") {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player hit by long enemy: " + player.getHealth());
						}
						else if (enemy.getEnemyType() == "Big Goblin"){
							player.changeHealth(-2);
							updateHealth();
							System.out.println("Player hit by boss: " + player.getHealth());
						}
						if (player.isDead()) {
							program.removeAll();
							while (enemies.size() > 0) { // remove all enemies from ArrayList
								enemies.remove(0);
							}
							GameOver();
						}
					}
					if (enemy.getEnemyType() == "long range" && enemy.isAttackAvailable()) {
						//TODO implement long range enemy attack
						GImage bulletSprite = enemy.getBulletSprite();
						//x is set to horizontal distance between mouse and middle of playerSprite
			            x = enemySprite.getX() - ( playerSprite.getX() + (playerSprite.getWidth() / 2));
			            //y is set to vertical distance between mouse and middle of playerSprite
			            y = enemySprite.getY() - (playerSprite.getY() + (playerSprite.getHeight() / 2));
			            enemy.getWeapon().setAngle(180 * Math.atan2(-y, x) / Math.PI - 180);	
			            System.out.println(180 * Math.atan2(-y, x) / Math.PI - 180);
						if (!enemy.isBulletTraveling()) {
							bulletSprite.setLocation(enemySprite.getX() + (enemySprite.getWidth() / 2) - bulletSprite.getWidth() / 2, enemySprite.getY() + (enemySprite.getHeight() / 2) - bulletSprite.getHeight() / 2);
						}
			            bulletSprite.setVisible(true);
						enemy.setBulletTraveling(true); // move bulletSprite under actionPerformed() method
						enemy.setAttackAvailable(false); //enemy can't attack for a time
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
		if (timerCount % 50 == 0) {
			attackArea.setVisible(false); // make attack area disappear
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
				if (timerCount % 200 == 0) {
					player.setAttackAvailable(true); //player can now attack
				}
				else if (timerCount % 400 == 0) {
					for (Enemy e2 : enemies) {
						e2.setAttackAvailable(true); //enemy can attack now.
					}
				}
				else if (timerCount % 500 == 0) {
					player.setDashAvailable(true); //player can now dash
				}
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
		GImage playerSprite = player.getSprite();
		AudioPlayer p = sounds.getPlayer(); //Get the audio player object to play the sound.
		if (player.isAttackAvailable()) {
			if (program.isCloseRangeWeapon()) {
				double x = e.getX() - (playerSprite.getX() + (playerSprite.getWidth() / 2)); //x is set to horizontal distance between mouse and middle of playerSprite
				double y = e.getY() - (playerSprite.getY() + (playerSprite.getHeight() / 2));  //y is set to vertical distance between mouse and middle of playerSprite
				double angle = 180 * Math.atan2(-y, x) / Math.PI; // calculate angle
				double xOffset;
				double yOffset;
				double weaponRange = player.getWeapon().getRange();
				if (angle >= 0) {
					if (angle > 90) {
						if (angle > 135) { // angle > 135
							attackArea.setImage("TopLeft.png");
							xOffset = - weaponRange - player.getSprite().getWidth() / 2;
							yOffset = - weaponRange;
						}
						else { // 90 < angle <= 135
							attackArea.setImage("TopLeft.png");
							xOffset = - weaponRange;
							yOffset = - weaponRange - player.getSprite().getHeight() / 2;
						}
					}
					else { 
						if (angle <= 45) { // 0 <= angle <= 45
							attackArea.setImage("TopRight.png");
							xOffset = player.getSprite().getWidth() / 2;
							yOffset = - weaponRange;
						}
						else { // 45 < angle <= 90
							attackArea.setImage("TopRight.png");
							xOffset = 0;
							yOffset = - weaponRange - player.getSprite().getHeight() / 2;
						}
					}
				}
				else { // angle < 0
					if (angle < -90) {
						if (angle < -135) { // angle < -135
							attackArea.setImage("BottomLeft.png");
							xOffset = - weaponRange - player.getSprite().getWidth() / 2;
							yOffset = 0;
						}
						else { // -90 > angle >= -135
							attackArea.setImage("BottomLeft.png");
							xOffset = - weaponRange;
							yOffset = player.getSprite().getHeight() / 2;
						}
					}
					else { 
						if (angle >= -45) { // 0 > angle >= -45
							attackArea.setImage("BottomRight.png");
							xOffset = player.getSprite().getWidth() / 2;
							yOffset = 0;
						}
						else { // -45 > angle >= -90
							attackArea.setImage("BottomRight.png");
							xOffset = 0;
							yOffset = player.getSprite().getHeight() / 2;
						}
					}
				}
				attackArea.setLocation(xOffset + playerSprite.getX() + playerSprite.getWidth() / 2, yOffset + playerSprite.getY() + playerSprite.getHeight() / 2);
				attackArea.setSize(weaponRange, weaponRange);
				attackArea.sendToFront();
				attackArea.setVisible(true);
				ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>();
				for (int z = 0; z < enemies.size(); z++) { // loop for all enemies
					Enemy enemy = enemies.get(z);
					if (Collision.check(attackArea.getBounds(), enemy.getSprite().getBounds())) { //player in range of enemy.
						System.out.println("Enemy is hit.");
						System.out.println(enemies.get(0));
						playSound(enemies.get(0).getEnemyType(), p); //play enemy grunt sound
						enemy.changeHealth(-1); //Reduce health by 1.
						if (currentRoom > 2) {
							updateHealth();
						}
						//TODO set enemy invincibility if needed
						if (enemy.isDead()) { //Enemy has no health.
							removeEnemyIndex.add(z); // add index to ArrayList
							program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
							System.out.println("Enemy is dead.");
						}
					}
				}
				if (removeEnemyIndex.size() > 0) { // remove all dead enemies
					for (int w = 0; w < removeEnemyIndex.size(); w++) {
						System.out.println(removeEnemyIndex.get(w));
						enemies.remove((int)removeEnemyIndex.get(w));
					}
				}
			}
			else { // long range attack
				GImage bulletSprite = player.getBulletSprite();
				//x is set to horizontal distance between mouse and middle of playerSprite
	            double x = e.getX() - ( playerSprite.getX() + (playerSprite.getWidth() / 2));
	            //y is set to vertical distance between mouse and middle of playerSprite
	            double y = e.getY() - (playerSprite.getY() + (playerSprite.getHeight() / 2));
	            player.getWeapon().setAngle(180 * Math.atan2(-y, x) / Math.PI);	
				bulletSprite.setVisible(true);
				player.setBulletTraveling(true); // move bulletSprite under actionPerformed() method
				bulletSprite.setLocation( playerSprite.getX() + (playerSprite.getWidth() / 2) - bulletSprite.getWidth() / 2, playerSprite.getY() + (playerSprite.getHeight() / 2) - bulletSprite.getHeight() / 2);
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
			double x = mouseX - (playerSprite.getX() + playerSprite.getWidth() / 2);
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
					if (!((Chest) nearestItem).isChestOpen()) { // if chest is not open
						GImage openChestSprite = new GImage("open-chest.png", nearestItem.getSprite().getX(), nearestItem.getSprite().getY()); //Create an open chest sprite for switch.
						openChestSprite.setSize(25, 25);
						program.remove(nearestItem.getSprite()); //Remove closed chest sprite.
						nearestItem.setSprite(openChestSprite); //set the sprite to the open chest.
						program.add(openChestSprite); //Add open chest sprite.
						items.remove(nearestItem); // remove chest from items ArrayList (so chest is not set as nearestItem)
						ArrayList<Item> itemsToShow = ((Chest) nearestItem).releaseItems();
						for (Item i : itemsToShow) { //Add the chest items to screen.
							program.add(i.getSprite()); //add items from chest to screen.
							program.add(i.getLabel()); //add label to screen.
							items.add(i); //add item from chest to items ArrayList.
						}
						((Chest) nearestItem).setChestOpen(true); //Chest is open.
						program.remove(nearestItem.getLabel()); //Remove chest label.
						playerSprite.sendToFront();
					}
				}
				else if (nearestItem instanceof Door) { //if nearest item is a Door
					boolean doorStateBefore = !((Door)nearestItem).getLocked(); // to check if door is already opened
					boolean unlockedDoor = ((Door)nearestItem).unlock(player.getInventory()); // to check if door is unlocked
					if (unlockedDoor){
						if (doorStateBefore == unlockedDoor) { // door has already been opened, so create next room
							currentRoom++;
							createRoom(currentRoom); // create next room
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
									program.remove(nearestItem.getSprite()); // remove closed door GImage from screen
									program.add(((Door)nearestItem).getOpenDoor()); // add open door GImage to screen
									((GObject)playerSprite).sendToFront(); // send player to the front of the screen
								}
							} 
						}
					}
					else {
						itemLabel.put("closedDoor", "A key is needed."); 
					}
				}
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