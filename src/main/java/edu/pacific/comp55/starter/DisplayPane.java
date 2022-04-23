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
import acm.graphics.GLabel; // for GLabel
import acm.graphics.GObject; // for GObject
import acm.graphics.GRect; // for GRect

public class DisplayPane extends GraphicsPane implements ActionListener{
	private static final int HEART_SIZE = 50;

	private static final double SQRT_TWO_DIVIDED_BY_TWO = 0.7071067811865476;

	private static final int FINAL_ROOM = 12; // TODO change later

	private static final int ITEM_SIZE = 25;

	private MainApplication program;
	
	private ArrayList<GImage> backgroundTiles;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> playerInventory;
	private ArrayList<GImage> bossHealth;
	private ArrayList<Item> items; // items to display on the level.
	private HashMap<String, String> itemLabel; 
	private String displayType; // to display current game state (lose/win/playing)
	private GLabel bossLabel; // to display boss name
	private GImage attackArea; // to display player attack
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
	private AudioPlayer backgroundMusic;
	
	private int timerCount; // to keep track of timer
	private boolean dropWeaponUpgrade; // to drop weapon upgrade upon boss defeat
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		
		timer = new Timer(0, this); // create timer object
		initializeGame();
	}

	private void initializeGame() {
		currentLevel = 1; // starting level number
		currentRoom = 1; // starting room number
		
		bossHealth = new ArrayList<GImage>(); //initialize bossHealth
		playerHealth = new ArrayList<GImage>(); // initialize playerHealth
		playerInventory = new ArrayList<GImage>(); // initialize playerInventory
		items = new ArrayList<Item>(); // initialize items in room
		enemies = new ArrayList<Enemy>(); // initialize enemy array list
		
		AudioPlayer p = new AudioPlayer();
		sounds = new SoundEffect(p, ""); // initialize sound effect player
		backgroundMusic = new AudioPlayer(); // initialize background music player
		
		dropWeaponUpgrade = false; // set to false by default
		program.setPlayerWin(false); // set to false by default
		
		itemLabel = new HashMap<String, String>();
		itemLabel.put("key", "Press e to pick up key.");
		itemLabel.put("closedDoor", "Press e to unlock door.");
		itemLabel.put("openDoor", "Press e to enter next room.");
		itemLabel.put("heart", "Press e to pick up heart.");
		itemLabel.put("chest", "Press e to open chest.");
		itemLabel.put("upgrade", "Press e to upgrade your weapon.");
		itemLabel.put("life", "Press e to gain an extra life.");
		
		//create player object with knight sprite as default. 
		GImage playerSprite = new GImage ("PlayerKnightSprite.png");
		player = new Player(playerSprite, 100);
		player.randomizeXLocation(program.getWidth(), program.getHeight()); //Randomize player location at bottom of screen
		player.setSpeed(7); // initialize speed
		attackArea = new GImage(""); // initialize attack area
		
		//create inventory box
		inventoryBox = new GRect(50, 0, 0, 0);
		inventoryBox.setVisible(false);
		
		timer.restart(); // reset timer
	}

	public void setBackground(String tileFile) {
		backgroundTiles = new ArrayList<GImage>(); 
		for (int x = 0; x < program.getWidth(); x += 50) { // add tiles in x direction
			for (int y = 0; y < program.getHeight(); y += 50) { // add tiles in y direction
				backgroundTiles.add(new GImage(tileFile, x, y));
			}
		}
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
		if (program.isCloseRangeCharacter()) { // close range character selected
			Weapon weapon = new Weapon(new GImage(""), "close range weapon", 25);
			player.setWeapon(weapon);
			attackArea.setVisible(false);
			attackArea.setSize(weapon.getRange(), weapon.getRange());
			program.add(attackArea); // add attack area to the screen.
		}
		else { //long range character selected
			player.setSprite(new GImage ("PlayerWizardSprite.png"));
			Weapon weapon = new Weapon(new GImage(""), "long range weapon", 200);
			player.setWeapon(weapon);
			program.add(player.getBulletSprite()); // add bullet to the screen
		}
		for (Enemy enemy: enemies) { // loop for all enemies
			program.add(enemy.getSprite()); //Add enemy sprite to screen.
			if (enemy.getEnemyType().contains("long range")) {
				program.add(enemy.getBulletSprite());
			}
		}
		dropWeaponUpgrade = false;
		if (roomNum % 6 == 0) { // boss room reached every 6th room TODO change later
			if (program.isAudioOn()) {
				backgroundMusic.stopSound("sounds", "basic_loop.wav"); // stop background music
				backgroundMusic.playSound("sounds", "more_basic_loop.wav", true); // play boss background music
			}
			if (roomNum == 6) {
				bossLabel = new GLabel("Big Goblin");
			}
			else if(roomNum == 12) {
				bossLabel = new GLabel("Falkor");
			}
			System.out.println("Boss label: " + bossLabel);
			bossLabel.setLocation(program.getWidth() - 125, inventoryBox.getHeight() + ITEM_SIZE); // set boss label based on player inventory
			bossLabel.setFont(new Font("Serif", Font.BOLD, 20));
			program.add(bossLabel);
		}
		program.add(inventoryBox); //Add inventory box to the screen.
		updateHealth(); // update player health display
		updateInventory(); // update player inventory display
		player.randomizeXLocation(program.getWidth(), program.getHeight()); // randomize player's location at bottom of screen
		program.add(player.getSprite()); //Add player sprite to screen.
		player.getSprite().sendToFront();
	}
	
	public void updateHealth() {
		while (playerHealth.size() > 0) { // remove all player hearts from screen
			program.remove(playerHealth.get(0));
			playerHealth.remove(0);
		}
		
		playerHealth = player.displayHealth();
		for (GImage heart : playerHealth) { // display all player hearts
			heart.setSize(HEART_SIZE, HEART_SIZE);
			program.add(heart);
		}
		System.out.println("current room: " + currentRoom);
		if (currentRoom % 6 == 0) { // TODO change later
			bossLabel.sendToFront();
			System.out.println(bossHealth.size());
			while (bossHealth.size() > 0) { // remove all boss hearts from screen
				program.remove(bossHealth.get(0));
				bossHealth.remove(0);
			}
			if (enemies.size() > 0) {
				for (Enemy e: enemies) { // loop through all enemies on screen
					if (e instanceof Boss) { // check if enemy is a boss
						if (e.isDead()) { // check if boss is dead
							program.remove(bossLabel); // remove bossLabel from screen
							if (!dropWeaponUpgrade) {
								GImage upgradeSprite;
								if (program.isCloseRangeCharacter()) {
									upgradeSprite = new GImage ("KnightUpgrade.png", e.getSprite().getX(), e.getSprite().getY()); //Create a new sprite for weapon upgrade.
								}
								else {
									upgradeSprite = new GImage ("WizardUpgrade.png", e.getSprite().getX(), e.getSprite().getY()); //Create a new sprite for weapon upgrade.
								}
								upgradeSprite.setSize(ITEM_SIZE, ITEM_SIZE); //Resize sprite to make it smaller.
								Weapon upgrade = new Weapon(upgradeSprite, "upgrade");
								items.add(upgrade); // add weaponUpgrade to items list
								program.add(upgradeSprite); // add weapon upgrade to screen
								program.add(upgrade.getLabel()); //add label to screen.
								dropWeaponUpgrade = true;
								player.getSprite().sendToFront();
							}
						}
						else { // boss is alive
							bossLabel.setLocation(bossLabel.getX(), inventoryBox.getHeight() + ITEM_SIZE); // update boss label based on player inventory
							int yOffset = ITEM_SIZE * (3 + (int)(player.getInventory().size() / 10));
							bossHealth = ((Boss) e).displayHealth(yOffset);
						}
					}
				}
			}
			for (GImage heart : bossHealth) { // display all boss hearts
				heart.setSize(HEART_SIZE, HEART_SIZE);
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
			i.setSize(ITEM_SIZE, ITEM_SIZE);
			program.add(i);
		}
		player.displayInventoryBox(inventoryBox); // display inventory box accordingly
		if (currentRoom % 6 == 0) { // if in a boss room
			updateHealth(); // if inventory is updated, boss health display should be updated too
		}
	}
	
	public void gameOver() {
		if (program.isAudioOn()) {
			backgroundMusic.stopSound("sounds", "basic_loop.wav"); // stop background music
			backgroundMusic.stopSound("sounds", "more_basic_loop.wav"); // stop boss background music
			backgroundMusic.playSound("sounds", "game_over.wav", false); // play game over sound
		}
		program.removeAll(); // remove all objects from screen
		initializeGame(); // reset all game values (player win is set to false)
		program.switchTo(3); // switch to game end screen
	}
	
	public void playSound(String e, AudioPlayer p) {
		if (e.contains("big goblin")) {
			sounds.setName("boss_goblin_grunt"); //Sound effect for goblin boss getting hit.
		}
		else if (e.contains("goblin")) {
			sounds.setName("small_goblin_grunt"); //Sound effect for enemy getting hit.
		}
		else if (e.contains("dragon")) {
			sounds.setName("dragon_grunt"); //Sound effect for dragon boss getting hit.
		}
		if (program.isAudioOn()) {
			sounds.play(p); //Play enemy getting hit sound effect.
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GImage playerSprite = player.getSprite();
		AudioPlayer p = sounds.getPlayer(); //Get the audio player object to play the sound.
		ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>(); // for removing dead enemies
		for (int z = 0; z < enemies.size(); z++) { // loop through all enemies
			Enemy enemy = enemies.get(z);
			GImage enemySprite = enemy.getSprite();
			if (timerCount % 1 == 0) {
				if (player.isBulletTraveling()) {
					GImage bulletSprite = player.getBulletSprite();
					bulletSprite = player.moveBullet(bulletSprite); // move bulletSprite towards mouse click 
					/*
					if (enemy.isDamaged()) {
						System.out.println("Enemy is invincible");
						enemy.enemyInvincibility();
					}
					else {*/
						if (Collision.check(bulletSprite.getBounds(), enemy.getSprite().getBounds())) { //returns true if enemy collides with player bullet 
							playSound(enemy.getEnemyType(), p); //play enemy grunt sound.
							enemy.changeHealth(-1);
							updateHealth();
							enemy.setDamaged(true); //Enemy is damaged.
							System.out.println("enemy health: " + enemy.getHealth());
							if (enemy.isDead()) { //Enemy has no health.
								removeEnemyIndex.add(z); // add index to ArrayList
								if (enemy.getEnemyType().contains("long range")) {
									program.remove(enemy.getBulletSprite()); // remove bullet from screen
								}
								program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
								System.out.println(enemy.getEnemyType() + " is dead.");
							}
							bulletSprite.sendToFront();
							while (Collision.check(bulletSprite.getBounds(), enemy.getSprite().getBounds())) { // move bulletSprite until not touching enemy 
								bulletSprite = player.moveBullet(bulletSprite); 
							}
						}
					//}
					if (player.getBulletDistance() >= player.getWeapon().getRange()) {
						player.setBulletTraveling(false);
						bulletSprite.setLocation(playerSprite.getX() + playerSprite.getWidth() / 2 - bulletSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2);
						player.setBulletDistance(0);
						bulletSprite.setVisible(false);
					}
				}
				
				if (enemy.isBulletTraveling()) {
					GImage bulletSprite = enemy.getBulletSprite();  
					bulletSprite = enemy.moveBullet(bulletSprite); //Move the bulletSprite.
					if (player.isDamaged()) {
						player.playerInvincibility();
					}
					else {
						if (Collision.check(bulletSprite.getBounds(), player.getSprite().getBounds())) { //returns true if player collides with bullet 
							//TODO player grunts
							//playSound("player", p); //play player grunt sound.
							player.changeHealth(-1);
							updateHealth(); // update health display
							player.setDamaged(true); //player is damaged.
							System.out.println("player health: " + player.getHealth());
							if (player.isDead()) {
								int lifeIndex = -1;
								lifeIndex = player.searchItemIndex(player, lifeIndex, "life");
								if (lifeIndex >= 0) { // check if player has life item
									player.setHealth(5); // reset playerHealth
									player.removeFromInventory(lifeIndex); // remove life item
									updateHealth(); // update health
									updateInventory(); // update inventory
								}
								else {
									program.removeAll();
									while (enemies.size() > 0) { // remove all enemies from ArrayList
										enemies.remove(0);
									}
									gameOver();
								}
							}
							while (Collision.check(bulletSprite.getBounds(), player.getSprite().getBounds())) { // move bulletSprite until not touching player 
								bulletSprite = enemy.moveBullet(bulletSprite); 
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
						if (enemy.getEnemyType().contains("long range")) { // if enemy is long range
							if (enemy.getEnemyType().contains("dragon boss")) { // if enemy is long range dragon boss
								String fireSpriteFileName = "burningFireSprite.png";
								if (Math.random() <= 0.5) { // 50% chance for fire to appear mirrored
									fireSpriteFileName = "burningFireMirroredSprite.png";
								}
								summonCloseRangeEnemy(enemy, fireSpriteFileName, "fire", 1, 25, 0);
							} // enemy is long range and not a boss
							else if (enemy.getEnemyType().contains("summoner")){ // if enemy is long range summoner
								summonCloseRangeEnemy(enemy, "EnemySkeletonSprite.png", "skeleton", 1, 200, 5);
								enemySprite.movePolar(2 * enemy.getSpeed(), (180 * Math.atan2(-y, x) / Math.PI)); // move long range enemy away from player
								setInBounds(enemy); // set long range enemy in bounds
							}
							else {
								enemySprite.movePolar(2 * enemy.getSpeed(), (180 * Math.atan2(-y, x) / Math.PI)); // move long range enemy away from player
								setInBounds(enemy); // set long range enemy in bounds
							}
							
							
						}
					}
					if (Collision.check(enemy.getSprite().getBounds(), player.getSprite().getBounds())) { // player collides with enemy
						playerSprite.movePolar(Math.sqrt(x*x+y*y), (180 * Math.atan2(-y, x) / Math.PI) + 180); // player moves away from enemy
						if (enemy.getEnemyType().contains("boss")) {
							player.changeHealth(-2);
							updateHealth();
							System.out.println("Player touched by " + enemy.getEnemyType() + ". player health: " + player.getHealth());
						}
						else if (enemy.getEnemyType().contains("close range")) {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player touched by " + enemy.getEnemyType() + ". player health: " + player.getHealth());
						}
						else if (enemy.getEnemyType().contains("long range")) {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player touched by " + enemy.getEnemyType() + ". player health: " + player.getHealth());
						}
						if (player.isDead()) {
							int lifeIndex = -1;
							lifeIndex = player.searchItemIndex(player, lifeIndex, "life");
							if (lifeIndex >= 0) { // check if player has life item
								player.setHealth(5); // reset playerHealth
								player.removeFromInventory(lifeIndex); // remove life item
								updateHealth(); // update health
								updateInventory(); // update inventory
							}
							else {
								program.removeAll();
								while (enemies.size() > 0) { // remove all enemies from ArrayList
									enemies.remove(0);
								}
								gameOver();
							}
						}
					}
					if (enemy.getEnemyType().contains("long range")) {
						if (enemy.isAttackAvailable()) {
							GImage bulletSprite = enemy.getBulletSprite();
							//x is set to horizontal distance between mouse and middle of playerSprite
							x = enemySprite.getX() - ( playerSprite.getX() + (playerSprite.getWidth() / 2));
			            	//y is set to vertical distance between mouse and middle of playerSprite
			            	y = enemySprite.getY() - (playerSprite.getY() + (playerSprite.getHeight() / 2));
			            	enemy.getWeapon().setAngle(180 * Math.atan2(-y, x) / Math.PI - 180);	
							if (!enemy.isBulletTraveling()) { // set initial bullet location and enemy attack sprite when not in motion
								bulletSprite.setLocation(enemySprite.getX() + (enemySprite.getWidth() / 2) - bulletSprite.getWidth() / 2, enemySprite.getY() + (enemySprite.getHeight() / 2) - bulletSprite.getHeight() / 2);
							}
							bulletSprite.setVisible(true);
							enemy.setBulletTraveling(true); // move bulletSprite under actionPerformed() method
							enemy.setAttackAvailable(false); //enemy can't attack for a time
						}
					}
				}
			}
		}
		if (removeEnemyIndex.size() > 0) { // remove all dead enemies
			System.out.println("Removing dead enemies...");
			for (int y = removeEnemyIndex.size() - 1; y >= 0 ; y--) {
				System.out.println("Enemy index to remove: " + (int)removeEnemyIndex.get(y));
				enemies.remove((int)removeEnemyIndex.get(y));
				System.out.println("Enemy index removed: " + (int)removeEnemyIndex.get(y));
			}
			System.out.println("Enemies alive: ");
			for (Enemy ene: enemies) {
				System.out.println(ene.getEnemyType());
			}
		}
		if (enemies.size() == 0) { // if no enemies
			player.getBulletSprite().setVisible(false); // hide player bullet
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
						i.getLabel().setLocation(i.getSprite().getX() + i.getSprite().getWidth() / 2 - i.getLabel().getWidth() / 2, i.getSprite().getY());
					} else {
						i.setLabel("");
					}
				}
				if (timerCount % player.getAttackCooldown() == 0) {
					player.setAttackAvailable(true); //player can now attack
				}
				if (timerCount % player.getDashCooldown() == 0) {
					player.setDashAvailable(true); //player can now dash
				}
				if (timerCount % 500 == 0) {
					for (Enemy e1 : enemies) {
						e1.setAttackAvailable(true); //enemy can now attack
					}
				}
				
			}
		}
	}

	private void summonCloseRangeEnemy(Enemy enemy, String spriteFileName, String name, int health, int detectionRange, int speed) {
		GImage sprite = new GImage("");
		if (enemy.getEnemyType().contains("dragon")) {
			sprite = new GImage (spriteFileName, enemy.getBulletSprite().getX(), enemy.getBulletSprite().getY());
		}
		else {
			int xOffset = 0;
			if (Math.random() > 0.5) { // 50% chance
				xOffset = 300;
			}
			else {
				xOffset = -300;
			}
			double xValue = inRange(player.getSprite().getX() + xOffset, 0, program.getWidth()); // make x value on screen
			sprite = new GImage (spriteFileName, xValue, player.getSprite().getY());
		}
		Enemy newEnemy = new Enemy(sprite, health, "close range " + name);
		newEnemy.setDetectionRange(detectionRange); // so new enemy can detect player
		newEnemy.setSpeed(speed); // set new enemy speed
		program.add(newEnemy.getSprite()); // add new enemy sprite to the screen
		enemies.add(newEnemy); // add new enemy to the screen
		enemy.getSprite().sendToFront(); // send summoner to front
	}
	
	@Override
	public void showContents() {
		createRoom(currentRoom); // currentRoom is initially at 1
		if (program.isAudioOn()) {
			backgroundMusic.playSound("sounds", "basic_loop.wav", true); // play background music
		}
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
	public void mouseClicked(MouseEvent e) {  
		GImage playerSprite = player.getSprite();
		AudioPlayer p = sounds.getPlayer(); // Get the audio player object to play the sound.
		if (player.isAttackAvailable()) {
			if (program.isCloseRangeCharacter()) {
				setUpAttackArea(e, playerSprite);
				ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>();
				for (int z = 0; z < enemies.size(); z++) { // loop for all enemies
					Enemy enemy = enemies.get(z);
					if (Collision.check(attackArea.getBounds(), enemy.getSprite().getBounds())) { //player in range of enemy.
						System.out.println("Enemy is hit.");
						System.out.println("Enemy: " + enemies.get(0)); //TODO fire should not play dragon sound
						playSound(enemy.getEnemyType(), p); //play enemy grunt sound
						enemy.changeHealth(-1); //Reduce health by 1.
						updateHealth();
						if (enemy.isDead()) { //Enemy has no health.
							removeEnemyIndex.add(z); // add index to ArrayList
							if (enemy.getEnemyType().contains("long range")) {
								program.remove(enemy.getBulletSprite()); // remove bullet from screen
							}
							program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
							System.out.println(enemy.getEnemyType() + " is dead.");
						}
					}
				}
				if (removeEnemyIndex.size() > 0) { // remove all dead enemies
					for (int w = 0; w < removeEnemyIndex.size(); w++) {
						System.out.println("Enemy index to remove: " + removeEnemyIndex.get(w));
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

	private void setUpAttackArea(MouseEvent e, GImage playerSprite) {
		double x = e.getX() - (playerSprite.getX() + (playerSprite.getWidth() / 2)); //x is set to horizontal distance between mouse and middle of playerSprite
		double y = e.getY() - (playerSprite.getY() + (playerSprite.getHeight() / 2));  //y is set to vertical distance between mouse and middle of playerSprite
		double angle = 180 * Math.atan2(-y, x) / Math.PI; // calculate angle from player to mouse
		double xOffset;
		double yOffset;
		double weaponRange = player.getWeapon().getRange();
		//set attackArea image, x offset, and y offset based on angle
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
						program.remove(nearestItem.getSprite()); //Remove closed chest sprite from screen
						nearestItem.setSprite(((Chest) nearestItem).getOpenChest()); //set the sprite to the open chest.
						program.add(nearestItem.getSprite()); //Add open chest sprite to screen
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
							currentRoom++; // increase current room number
							createRoom(currentRoom); // create next room
							if (currentRoom > FINAL_ROOM) {
								program.removeAll(); // remove all objects from screen
								System.out.println("Congratulations! You escaped the dungeon!");
								if (program.isAudioOn()) {
									backgroundMusic.stopSound("sounds", "more_basic_loop.wav"); // stop boss background music
									backgroundMusic.playSound("sounds", "win.wav"); // play win music
								}
								program.setPlayerWin(true); // set player win
								program.switchTo(3); // switch to game end screen
								initializeGame(); // reset all game values
							}
						}
						int removeIndex = -1;
						if (player.getInventory().size() > 0) {
							removeIndex = player.searchItemIndex(player, removeIndex, "key");
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
				else if (nearestItem instanceof Weapon) {
					// for weapon upgrade, decrease attack cool down
					int oldAttackCooldown = player.getAttackCooldown();
					int newAttackCooldown = oldAttackCooldown - (oldAttackCooldown / 4); // reduce attack cool down by ~25%
					if (newAttackCooldown < 100) { // so attack cool down doesn't go too low
						newAttackCooldown = 100;
						// when attack cool down is low, increase weapon range
						int oldAttackRange = player.getWeapon().getRange();
						int newAttackRange = oldAttackRange + (oldAttackRange / 4); // increase weapon range by ~25%
						player.getWeapon().setRange(newAttackRange);
						System.out.println("Old attack cooldown: " + oldAttackRange);
						System.out.println("New attack cooldown: " + newAttackRange);
					}
					else {
						System.out.println("Old attack cooldown: " + oldAttackCooldown);
						System.out.println("New attack cooldown: " + newAttackCooldown);
					}
					player.setAttackCooldown(newAttackCooldown);
					
					items.remove(nearestItem); // remove nearestItem from list
					program.remove(nearestItem.getSprite()); // remove weapon upgrade from screen
					program.remove(nearestItem.getLabel()); // remove weapon label from screen
				}
			}
		}
		else if (keyCode == 82) { // r
			int removeIndex = -1;
			if (player.getInventory().size() > 0) {
				removeIndex = player.searchItemIndex(player, removeIndex, "heart");
				if (removeIndex >= 0) { //check if the player has the heart to remove
					player.removeFromInventory(removeIndex); //Remove the heart from the inventory.
					System.out.println("Heart consumed");
					player.changeHealth(1); //add one health to player for now.
				}
			}
			updateHealth(); //update health changes
			updateInventory(); //update inventory changes
		}
		// for normalizing diagonal movement
		if (Math.abs(player.getMoveX()) == 1 && Math.abs(player.getMoveY()) == 1) { // check if diagonal movement is happening
			player.setMoveX(player.getMoveX() * SQRT_TWO_DIVIDED_BY_TWO);
			player.setMoveY(player.getMoveY() * SQRT_TWO_DIVIDED_BY_TWO);
		}
		
		playerSprite.move(player.getMoveX() * player.getSpeed(), player.getMoveY() * player.getSpeed()); // move playerSprite
		
		setInBounds(player);
		
		if (!program.isPlayerWin() && keyCode != 69) { // player has not won and e not pressed
			GImage newPlayerSprite = new GImage("", playerSprite.getX(), playerSprite.getY());
			if (player.getMoveX() < 0) { // player moving left
				if (program.isCloseRangeCharacter()) {
					newPlayerSprite.setImage("PlayerKnightSprite.png");
					player.setSprite(newPlayerSprite);
				}
				else {
					newPlayerSprite.setImage("PlayerWizardSprite.png");
					player.setSprite(newPlayerSprite);
				}
			}
			else { // player moving right
				if (program.isCloseRangeCharacter()) {
					newPlayerSprite.setImage("PlayerKnightMirroredSprite.png");
					player.setSprite(newPlayerSprite);
				}
				else {
					newPlayerSprite.setImage("PlayerWizardMirroredSprite.png");
					player.setSprite(newPlayerSprite);
				}
			}
			program.remove(playerSprite); // remove previous player sprite
			program.add(newPlayerSprite); // add new player sprite
		}
	}

	private void setInBounds(Character character) { // set character sprite in bounds on the screen
		GImage sprite = character.getSprite();
		double x = sprite.getX();
		double y = sprite.getY();
		double min = 0;
		double xMax = program.getWidth() - 1.75 * sprite.getWidth();
		double yMax = program.getHeight() - 2.25 * sprite.getHeight();
		if (character instanceof Enemy) { // check if character is an enemy
			yMax = program.getHeight() - 3 * sprite.getHeight();
		}
		sprite.setLocation(inRange(x, min, xMax), inRange(y, min, yMax));
	}
	
	public double inRange(double x, double min, double max) { // return value between minimum and maximum
		if (x > min && x < max) {
			return x;
		} else if (x <= min){
			return min + 1;
		} else { // x >= max
			return max - 1;
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