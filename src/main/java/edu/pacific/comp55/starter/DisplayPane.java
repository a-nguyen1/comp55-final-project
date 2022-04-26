package edu.pacific.comp55.starter;
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
	
	private static final int PLAYER_STARTING_LONG_RANGE = 200;
	private static final int PLAYER_STARTING_CLOSE_RANGE = 25;
	private static final int PLAYER_STARTING_SPEED = 7;
	private static final int PLAYER_STARTING_HEALTH = 10;
	
	private static final int KNIGHT_ATTACK_DISPLAY_INTERVAL = 50;
	private static final int INTERACT_INTERVAL = 100;
	private static final int LONG_RANGE_ENEMY_ATTACK_INTERVAL = 500;
	private static final int WIZARD_TELEPORT_INTERVAL = 1000;
	
	private static final double SQRT_TWO_DIVIDED_BY_TWO = 0.7071067811865476;
	private static final int BACKGROUND_TILE_SIZE = 50;
	private static final int HEART_SIZE = 50;
	private static final int ITEM_SIZE = 25;
	private static final int FINAL_ROOM = 18;
	
	private MainApplication program;
	
	private ArrayList<GImage> backgroundTiles;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> playerInventory;
	private ArrayList<GImage> bossHealth;
	private ArrayList<Item> items; // items to display on the level.
	private HashMap<String, String> itemLabel; // hash map to link items with labels
	private GLabel bossLabel; // to display boss name
	private GImage attackArea; // to display player attack
	private int currentRoom; // to display current room
	private double mouseX; // to keep track of mouse x location
	private double mouseY; // to keep track of mouse y location
	
	//main game objects
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Integer> removeEnemyIndex; // to keep track of enemy indexes to remove
	private GRect inventoryBox;
	private Timer timer;
	private SoundEffect soundEffect;
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
		currentRoom = 1; // starting room number
		
		bossHealth = new ArrayList<GImage>(); //initialize bossHealth
		playerHealth = new ArrayList<GImage>(); // initialize playerHealth
		playerInventory = new ArrayList<GImage>(); // initialize playerInventory
		items = new ArrayList<Item>(); // initialize items in room
		enemies = new ArrayList<Enemy>(); // initialize enemy array list
		removeEnemyIndex = new ArrayList<Integer>(); // initialize array list for indexes of dead enemies
		
		soundEffect = new SoundEffect(AudioPlayer.getInstance(), ""); // initialize sound effect player
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
		player = new Player(playerSprite, PLAYER_STARTING_HEALTH);
		player.randomizeXLocation(program.getWidth(), program.getHeight()); //Randomize player location at bottom of screen
		player.setSpeed(PLAYER_STARTING_SPEED); // initialize speed
		attackArea = new GImage(""); // initialize attack area
		
		//create inventory box
		inventoryBox = new GRect(0, 0, 0, 0);
		inventoryBox.setVisible(false);
		
		timer.restart(); // reset timer
	}

	public void setBackground(String tileFile) {
		backgroundTiles = new ArrayList<GImage>(); 
		for (int x = 0; x < program.getWidth(); x += BACKGROUND_TILE_SIZE) { // add tiles in x direction
			for (int y = 0; y < program.getHeight(); y += BACKGROUND_TILE_SIZE) { // add tiles in y direction
				backgroundTiles.add(new GImage(tileFile, x, y));
			}
		}
	}
	
	public void createRoom(int roomNum) {
		Room newRoom = new Room(roomNum, program.getWidth(), program.getHeight());
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
		
		Weapon weapon;
		if (program.isCloseRangeCharacter()) { // close range character selected
			if (player.getWeapon() == null) { // if weapon not initialized yet
				weapon = new Weapon(new GImage(""), "close range weapon", PLAYER_STARTING_CLOSE_RANGE); 
				player.setWeapon(weapon);
			}
			attackArea.setVisible(false);
			attackArea.setSize(player.getWeapon().getRange(), player.getWeapon().getRange());
			program.add(attackArea); // add attack area to the screen.
		}
		else { //long range character selected
			if (player.getWeapon() == null) { // if weapon not initialized yet
				weapon = new Weapon(new GImage(""), "long range weapon", PLAYER_STARTING_LONG_RANGE); 
				player.setWeapon(weapon);
			}
			player.setSprite(new GImage ("PlayerWizardSprite.png"));
			program.add(player.getBulletSprite()); // add bullet to the screen
		}
		
		for (Enemy enemy: enemies) { // loop for all enemies
			program.add(enemy.getSprite()); //Add enemy sprite to screen.
			if (enemy.getEnemyType().contains("long range")) {
				program.add(enemy.getBulletSprite());
			}
		}
		dropWeaponUpgrade = false;
	
		if (roomNum % 6 == 0) { // every sixth room is a boss room
			if (program.isAudioOn()) {
				stopBackgroundMusic();
				backgroundMusic.playSound("sounds", "most_basic_loop.wav", true); // play boss background music
			}
			if (roomNum == 6) {
				bossLabel = new GLabel("Big Goblin");
			}
			else if(roomNum == 12) {
				bossLabel = new GLabel("Falkor");
			}
			else if (roomNum == 18) {
				bossLabel = new GLabel("Evil Wizard");
			}
			bossLabel.setLocation(program.getWidth() - 2.25 * bossLabel.getWidth(), inventoryBox.getHeight() + ITEM_SIZE); // set boss label based on player inventory
			bossLabel.setFont(new Font("Serif", Font.BOLD, 20));
			program.add(bossLabel);
		}
		else if (roomNum >= 7 && program.isAudioOn()) { // change music after 1st boss room
			stopBackgroundMusic();
			backgroundMusic.playSound("sounds", "more_basic_loop.wav", true); // play background music
		}
		
		program.add(inventoryBox); //Add inventory box to the screen.
		updateHealth(); // update player health display
		updateInventory(); // update player inventory display
		player.randomizeXLocation(program.getWidth(), program.getHeight()); // randomize player's location at bottom of screen
		program.add(player.getSprite()); //Add player sprite to screen.
		player.getSprite().sendToFront(); //send player sprite to front.
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
		if (currentRoom % 6 == 0) { // every sixth room is a boss room
			bossLabel.sendToFront();
			while (bossHealth.size() > 0) { // remove all boss hearts from screen
				program.remove(bossHealth.get(0));
				bossHealth.remove(0);
			}
			if (enemies.size() > 0) {
				for (Enemy e: enemies) { // loop through all enemies on screen
					if (e instanceof Boss) { // check if enemy is a boss
						if (e.isDead()) { // check if boss is dead
							program.remove(bossLabel); // remove bossLabel from screen
							if (!dropWeaponUpgrade) { // if weapon has not been dropped yet
								String upgradeType;
								if (program.isCloseRangeCharacter()) {
									upgradeType = "KnightUpgrade.png";
								}
								else {
									upgradeType = "WizardUpgrade.png";
								}
								GImage upgradeSprite = new GImage (upgradeType, e.getSprite().getX(), e.getSprite().getY() + ITEM_SIZE); //Create a new sprite for weapon upgrade.
								Weapon upgrade = new Weapon(upgradeSprite, "upgrade");
								items.add(upgrade); // add weaponUpgrade to items list
								program.add(upgradeSprite); // add weapon upgrade to screen
								program.add(upgrade.getLabel()); //add label to screen.
								player.getSprite().sendToFront(); // send player sprite to front
								dropWeaponUpgrade = true; // weapon has been dropped
								addHeart(e, 0, ITEM_SIZE * 2);
								addHeart(e, 0, -ITEM_SIZE);
							}
						}
						else { // boss is alive
							bossLabel.setLocation(bossLabel.getX(), inventoryBox.getHeight() + ITEM_SIZE); // update boss label based on player inventory
							int xOffset = (int)program.getWidth() - (int)(HEART_SIZE * 1.5);
							int yOffset = ITEM_SIZE * (3 + (int)(player.getInventory().size() / 10));
							bossHealth = ((Boss) e).displayHealth(xOffset, yOffset);
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
			stopBackgroundMusic();
			backgroundMusic.playSound("sounds", "game_over.wav", false); // play game over sound
		}
		System.out.println("Player is dead. Game Over.");
		program.removeAll(); // remove all objects from screen
		initializeGame(); // reset all game values (player win is set to false)
		program.switchTo(3); // switch to game end screen
	}

	private void stopBackgroundMusic() {
		backgroundMusic.stopSound("sounds", "basic_loop.wav"); // stop background music
		backgroundMusic.stopSound("sounds", "more_basic_loop.wav"); // stop background music
		backgroundMusic.stopSound("sounds", "most_basic_loop.wav"); // stop boss background music
	}
	
	public void playSound(String e, AudioPlayer p) {
		if (e.contains("big goblin")) {
			soundEffect.setName("boss_goblin_grunt"); //Sound effect for goblin boss getting hit.
		}
		else if (e.contains("goblin")) {
			soundEffect.setName("small_goblin_grunt"); //Sound effect for goblin getting hit.
		}
		else if (e.contains("dragon")) {
			soundEffect.setName("dragon_grunt"); //Sound effect for dragon getting hit.
		}
		else if (e.contains("skeleton")){
			soundEffect.setName("skeleton");
			if (e.contains("skeleton summoner")) {
				soundEffect.setName("skeleton_summoner");
			}
		}
		else if (e.contains("wizard")) {
			soundEffect.setName("wizard");
		}
		else if (e.contains("fire")) {
			soundEffect.setName("fire");
		}
		else if (e.contains("demon magician")){
			soundEffect.setName("demon_magician");
		}
		else if (e.contains("player")) {
			soundEffect.setName("player");
		}
		if (program.isAudioOn()) {
			soundEffect.play(p); //Play sound effect.
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GImage playerSprite = player.getSprite();
		removeEnemyIndex = new ArrayList<Integer>(); // initialize array list for indexes of dead enemies
		for (int z = 0; z < enemies.size(); z++) { // loop through all enemies
			Enemy enemy = enemies.get(z);
			GImage enemySprite = enemy.getSprite();
			if (player.isBulletTraveling()) {
				GImage bulletSprite = player.getBulletSprite();
				bulletSprite = player.moveBullet(bulletSprite); // move bulletSprite towards mouse click 
				if (Collision.check(bulletSprite.getBounds(), enemy.getSprite().getBounds())) { //enemy collides with player bullet 
					playSound(enemy.getEnemyType(), soundEffect.getPlayer()); //play enemy grunt sound.
					enemy.changeHealth(-1);
					if (enemy instanceof Boss) {
						updateHealth(); //update boss health
					}
					enemy.setDamaged(true); //Enemy is damaged.
					System.out.println("Enemy health: " + enemy.getHealth());
					if (enemy.isDead()) { //Enemy has no health.
						removeEnemyIndex.add(z); // add index to ArrayList
						if (enemy.getEnemyType().contains("long range")) {
							program.remove(enemy.getBulletSprite()); // remove bullet from screen
						}
						program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
						System.out.println(enemy.getEnemyType() + " is dead.");
						
						addHeart(enemy, 0, 0);
						
					}
					bulletSprite.sendToFront();
					while (Collision.check(bulletSprite.getBounds(), enemy.getSprite().getBounds())) { // move bulletSprite until not touching enemy 
						bulletSprite = player.moveBullet(bulletSprite);
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
				bulletSprite = enemy.moveBullet(bulletSprite); //Move the bulletSprite.
				if (player.isDamaged()) {
					player.playerInvincibility();
				}
				else {
					if (Collision.check(bulletSprite.getBounds(), player.getSprite().getBounds())) { //returns true if player collides with bullet 
						playSound("player", AudioPlayer.getInstance()); //play player grunt sound.
						player.changeHealth(-1);
						updateHealth(); // update health display
						player.setDamaged(true); //player is damaged.
						if (player.isDead()) {
							int lifeIndex = -1;
							lifeIndex = player.searchItemIndex(player, lifeIndex, "life");
							if (lifeIndex >= 0) { // check if player has life item
								player.setHealth(PLAYER_STARTING_HEALTH); // reset playerHealth
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
				if (timerCount % INTERACT_INTERVAL == 0) {
					enemySprite.movePolar(enemy.getSpeed(), angle(enemySprite, playerSprite) + 180); // close range enemy or dragon moves towards player
					if (enemy.getEnemyType().contains("long range")) { // if enemy is long range
						if (enemy.getEnemyType().contains("dragon")) { // if enemy is long range dragon
								if (enemy.getEnemyType().contains("dragon boss")) { // if enemy is long range dragon boss
								String fireSpriteFileName = "burningFireSprite.png";
								if (Math.random() <= 0.5) { // 50% chance for fire to appear mirrored
									fireSpriteFileName = "burningFireMirroredSprite.png";
								}
								summonEnemy(enemy, fireSpriteFileName, "fire", 1, ITEM_SIZE, 0); // canInteract/damage range set to ITEM_SIZE
							}
						} // enemy is long range and not a dragon
						else {
							enemySprite.movePolar(2 * enemy.getSpeed(), angle(enemySprite, playerSprite)); // long range enemy moves away from player
							setInBounds(enemy); // set long range enemy in bounds
							if (enemy.getEnemyType().contains("summoner")){ // if enemy is long range summoner
								int numSummoners = 0; // for counting the number of summoners
								int numSummoned = 0; // for counting the number of summoned enemies
								for (int ind = 0; ind < enemies.size(); ind++) { // loop over all enemies
									Enemy temp = enemies.get(ind);
									if (temp.getEnemyType().contains("summoner")) { // count summoner enemies
										numSummoners++;
									}
									if (temp.getEnemyType().contains("summoned")) { // count summoned enemies
										numSummoned++;
									}
								}
								int summonRatio = 15; // number of summoned enemies per summoner
								if (numSummoned / numSummoners < summonRatio) { // maintain summon ratio
									if (enemy.getEnemyType().contains("wizard boss")) {
										if (timerCount % WIZARD_TELEPORT_INTERVAL == 0) { // teleport interval
											double newX = Math.random() * program.getWidth();
											double newY = Math.random() * program.getHeight();
											while (Collision.check(playerSprite.getBounds(), enemySprite.getBounds())) { // randomize location until enemy sprite will not touch player
												newX = Math.random() * program.getWidth();
												newY = Math.random() * program.getHeight();
											}
											enemySprite.setLocation(newX, newY);
										}
										setInBounds(enemy); // set wizard boss in bounds
										summonEnemy(enemy, "EnemySkeletonSummonerSprite.png", "summoned skeleton summoner", 3, 450, 350, "fireballSprite.png", 5);
									}
									else {
										summonEnemy(enemy, "EnemyHeartlessSkeletonSprite.png", "summoned skeleton", 1, 300, 5);
									}
								}
							}
						}
					}
					enemySprite.sendToFront(); // send enemy sprite to front
				}
				if (Collision.check(enemy.getSprite().getBounds(), player.getSprite().getBounds())) { // player collides with enemy
					playSound("player", AudioPlayer.getInstance()); // player is damaged
					double x = (enemySprite.getX() + (enemySprite.getWidth() / 2)) - (playerSprite.getX() + (playerSprite.getWidth() / 2)); //x is set to horizontal distance between enemy and player
					double y = (enemySprite.getY() + (enemySprite.getHeight() / 2)) - (playerSprite.getY() + (playerSprite.getHeight() / 2));  //y is set to vertical distance between enemy and player
					playerSprite.movePolar(Math.sqrt(x*x+y*y), angle(enemySprite, playerSprite) + 180); // player moves away from enemy
					if (enemy.getEnemyType().contains("boss")) {
						player.changeHealth(-2);
						updateHealth();
					}
					else {
						player.changeHealth(-1);
						updateHealth();
					}
					System.out.println("Player hit by " + enemy.getEnemyType() + ". player health: " + player.getHealth());
					if (player.isDead()) {
						int lifeIndex = -1;
						lifeIndex = player.searchItemIndex(player, lifeIndex, "life");
						if (lifeIndex >= 0) { // check if player has life item
							player.setHealth(PLAYER_STARTING_HEALTH); // reset playerHealth
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
			           	enemy.getWeapon().setAngle(angle(enemySprite, playerSprite) + 180);	
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
		if (removeEnemyIndex.size() > 0) { // remove all dead enemies
			removeAllDeadEnemies();
		}
		if (enemies.size() == 0) { // if no enemies
			player.getBulletSprite().setVisible(false); // hide player bullet
		}
		timerCount++;
		if (Math.abs(player.getAttackDisplayCount() - timerCount) > KNIGHT_ATTACK_DISPLAY_INTERVAL) {
			attackArea.setVisible(false); // make attack area disappear
		}
		if (timerCount % INTERACT_INTERVAL == 0) {
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
			if (timerCount % LONG_RANGE_ENEMY_ATTACK_INTERVAL == 0) {
				for (Enemy e1 : enemies) {
					e1.setAttackAvailable(true); //enemy can now attack
				}
			}
				
		}
	}

	private void addHeart(Enemy enemy, int xOffset, int yOffset) {
		if (enemy instanceof Boss || Math.random() < 0.50 + 2 * ((double)currentRoom / 100.0)) { // if enemy is boss or (50 + (2 to 36)) % chance
			GImage heartSprite = new GImage ("Heart.png", enemy.getSprite().getX() + xOffset, enemy.getSprite().getY() + yOffset); //Create a new sprite for heart.
			heartSprite.setSize(ITEM_SIZE, ITEM_SIZE); //Resize sprite to make it smaller.
			PickUpItem heart = new PickUpItem(heartSprite, "heart");
			items.add(heart);
			program.add(heartSprite);
			program.add(heart.getLabel());
		}
	}

	private void removeAllDeadEnemies() {
		for (int y = removeEnemyIndex.size() - 1; y >= 0 ; y--) {
			enemies.remove((int)removeEnemyIndex.get(y));
		}
	}

	private void summonEnemy(Enemy enemy, String spriteFileName, String name, int health, int detectionRange, int speed) {
		GImage sprite = new GImage("");
		if (enemy.getEnemyType().contains("dragon boss")) {
			sprite = new GImage (spriteFileName, enemy.getBulletSprite().getX(), enemy.getBulletSprite().getY());
		}
		else {
			int xOffset = 0;
			if (Math.random() > 0.5) { // 50% chance
				xOffset = detectionRange;
			}
			else {
				xOffset = -detectionRange;
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
	
	private void summonEnemy(Enemy enemy, String spriteFileName, String name, int health, int detectionRange, int weaponRange, String bulletName, int speed) {
		GImage sprite = new GImage("");
		int xOffset = 0;
		if (Math.random() > 0.5) { // 50% chance
			xOffset = detectionRange;
		}
		else {
			xOffset = -detectionRange;
		}
		double xValue = inRange(player.getSprite().getX() + xOffset, 0, program.getWidth()); // make x value on screen
		sprite = new GImage (spriteFileName, xValue, player.getSprite().getY());
	
		Enemy newEnemy = new Enemy(sprite, health, "long range " + name);
		newEnemy.setDetectionRange(detectionRange); // so new enemy can detect player
		newEnemy.setSpeed(speed); // set new enemy speed
		Weapon weapon = new Weapon(new GImage(""), "", weaponRange);
		newEnemy.setWeapon(weapon);
		GImage bullet = new GImage(bulletName, newEnemy.getSprite().getX() + newEnemy.getSprite().getWidth() / 2, newEnemy.getSprite().getY() + newEnemy.getSprite().getHeight() / 2);
		newEnemy.setBulletSprite(bullet);
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
		else {
			stopBackgroundMusic();
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
		if (player.isAttackAvailable()) {
			if (program.isCloseRangeCharacter()) {
				setUpAttackArea(e, playerSprite);
				player.setAttackDisplayCount(timerCount);
				removeEnemyIndex = new ArrayList<Integer>(); // initialize array list for indexes of dead enemies
				for (int z = 0; z < enemies.size(); z++) { // loop for all enemies
					Enemy enemy = enemies.get(z);
					if (Collision.check(attackArea.getBounds(), enemy.getSprite().getBounds())) { //player hits enemy.
						playSound(enemy.getEnemyType(), soundEffect.getPlayer()); // play enemy grunt sound
						System.out.println(enemy.getEnemyType() + " is hit.");
						enemy.changeHealth(-1); //Reduce health by 1.
						if (enemy instanceof Boss) {
							updateHealth(); // update boss health
						}
						if (enemy.isDead()) { //Enemy has no health.
							removeEnemyIndex.add(z); // add index to ArrayList
							if (enemy.getEnemyType().contains("long range")) {
								program.remove(enemy.getBulletSprite()); // remove bullet from screen
							}
							program.remove(enemy.getSprite()); //Remove enemy sprite from the screen since it is dead.
							System.out.println(enemy.getEnemyType() + " is dead.");
							
							addHeart(enemy, 0, 0);
						}
					}
				}
				removeAllDeadEnemies();
				
			}
			else { // long range attack
				GImage bulletSprite = player.getBulletSprite();
	            player.getWeapon().setAngle(angle(playerSprite));	
				bulletSprite.setVisible(true);
				player.setBulletTraveling(true); // move bulletSprite under actionPerformed() method
				bulletSprite.setLocation(playerSprite.getX() + (playerSprite.getWidth() / 2) - bulletSprite.getWidth() / 2, playerSprite.getY() + (playerSprite.getHeight() / 2) - bulletSprite.getHeight() / 2);
			}
		}
		player.setAttackAvailable(false); // Initiate attack cool down.
	}

	private void setUpAttackArea(MouseEvent e, GImage playerSprite) {
		double angle = angle(playerSprite);
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
	
	private double angle(GImage enemySprite, GImage playerSprite) { // return angle between player and enemy
		double x = (enemySprite.getX() + (enemySprite.getWidth() / 2)) - (playerSprite.getX() + (playerSprite.getWidth() / 2)); //x is set to horizontal distance between enemy and player
		double y = (enemySprite.getY() + (enemySprite.getHeight() / 2)) - (playerSprite.getY() + (playerSprite.getHeight() / 2));  //y is set to vertical distance between enemy and player
		double angle = 180 * Math.atan2(-y, x) / Math.PI; // calculate angle from player to enemy
		return angle;
	}
	
	private double angle(GImage playerSprite) { // return angle between player and mouse
		double x = mouseX - (playerSprite.getX() + (playerSprite.getWidth() / 2)); //x is set to horizontal distance between mouse and middle of playerSprite
		double y = mouseY - (playerSprite.getY() + (playerSprite.getHeight() / 2));  //y is set to vertical distance between mouse and middle of playerSprite
		double angle = 180 * Math.atan2(-y, x) / Math.PI; // calculate angle from player to mouse
		return angle;
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
			playerSprite.movePolar(player.getSpeed() * player.getSpeed() * 2, angle(playerSprite)); // dash in direction of mouse
			timer.start();
		} else if (keyCode == 69) { // e
			Item nearestItem = player.nearestItem(items); //check for item nearest to player
			if (player.canInteract(nearestItem.getSprite().getX(), nearestItem.getSprite().getY())) {
				//if nearest item is a PickUpItem
				if (nearestItem instanceof PickUpItem && !((PickUpItem) nearestItem).getInInventory()) { // check if PickUpItem and if not in inventory
					player.addToInventory(nearestItem); // add item to player inventory
					((PickUpItem) nearestItem).setInInventory(true); // set item in inventory
					program.remove(nearestItem.getLabel()); // remove item label
					updateInventory();
				}
				else if (nearestItem instanceof Chest) {
					if (!((Chest) nearestItem).isChestOpen()) { // if chest is not open
						program.remove(nearestItem.getSprite()); //Remove closed chest sprite from screen
						nearestItem.setSprite(((Chest) nearestItem).getOpenChest()); //set the sprite to the open chest.
						program.add(nearestItem.getSprite()); //Add open chest sprite to screen
						items.remove(nearestItem); // remove chest from items ArrayList (so chest is not set as nearestItem)
						ArrayList<Item> itemsToShow = ((Chest) nearestItem).releaseItems(program.isCloseRangeCharacter());
						for (Item i : itemsToShow) { //Add the chest items to screen.
							program.add(i.getSprite()); //add items from chest to screen.
							program.add(i.getLabel()); //add label to screen.
							items.add(i); //add item from chest to items ArrayList.
						}
						((Chest) nearestItem).setChestOpen(true); //Chest is open.
						program.remove(nearestItem.getLabel()); //Remove chest label.
						playerSprite.sendToFront(); //move player to front
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
									stopBackgroundMusic();
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
					else { // door is locked
						itemLabel.put("closedDoor", "A key is needed."); 
					}
				}
				else if (nearestItem instanceof Weapon) { // for weapon upgrade, increase weapon range
					int oldAttackRange = player.getWeapon().getRange(); 
					int newAttackRange = oldAttackRange + (oldAttackRange / 4); // increase weapon range by ~25%
					player.getWeapon().setRange(newAttackRange);
					System.out.println("Your weapon range has increased by: " + (oldAttackRange / 4));
					items.remove(nearestItem); // remove weapon upgrade from list
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
					player.changeHealth(1); //add one health to player.
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
		sprite.setLocation(inRange(x, min, xMax), inRange(y, min, yMax)); // set location of sprite in bounds
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