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
	private boolean dropWeapon; // to drop weapon upgrade upon boss defeat
	
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
		backgroundMusic = new AudioPlayer();
		dropWeapon = false; // set to false by default
		
		itemLabel = new HashMap<String, String>();
		itemLabel.put("key", "Press e to pick up key.");
		itemLabel.put("closedDoor", "Press e to unlock door.");
		itemLabel.put("openDoor", "Press e to enter next room.");
		itemLabel.put("heart", "Press e to pick up heart.");
		itemLabel.put("chest", "Press e to open chest.");
		itemLabel.put("upgrade", "Press e to upgrade your weapon.");
		
		//create player object with knight sprite as default.
		GImage playerSprite = new GImage ("PlayerKnightSprite.png", program.getWidth()/2, program.getHeight()/2);
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
		if (program.isCloseRangeWeapon()) { // close range weapon selected
			Weapon weapon = new Weapon(new GImage(""), "close range weapon", 25);
			player.setWeapon(weapon);
			attackArea.setVisible(false);
			attackArea.setSize(weapon.getRange(), weapon.getRange());
			program.add(attackArea); // add attack area to the screen.
		}
		else { //long range weapon selected
			player.setSprite(new GImage ("PlayerWizardSprite.png", program.getWidth()/2, program.getHeight()/2));
			Weapon weapon = new Weapon(new GImage(""), "long range weapon", 200);
			player.setWeapon(weapon);
			program.add(player.getBulletSprite());
		}
		for (Enemy enemy: enemies) { // loop for all enemies
			program.add(enemy.getSprite()); //Add enemy sprite to screen.
			if (enemy.getEnemyType().contains("long range")) {
				program.add(enemy.getBulletSprite());
			}
		}
		dropWeapon = false;
		if (currentRoom % 3 == 0) { // boss room reached (every 3rd room) TODO change later
			if (program.isAudioOn()) {
				backgroundMusic.stopSound("sounds", "basic_loop.wav"); // stop background music
				backgroundMusic.playSound("sounds", "more_basic_loop.wav", true); // play boss background music
			}
			bossLabel = new GLabel("Falkor", program.getWidth() - 125, 25);
			bossLabel.setFont(new Font("Serif", Font.BOLD, 20));
			program.add(bossLabel);
		}
		program.add(inventoryBox); //Add inventory box to the screen.
		updateHealth(); // update player health display
		updateInventory(); // update player inventory display
		player.getSprite().setLocation((program.getWidth() - 1.75 * player.getSprite().getWidth()) * Math.random(), program.getHeight() - 2.25 * player.getSprite().getHeight());
		program.add(player.getSprite()); //Add player sprite to screen.
		
		if (currentRoom >= 4) { // TODO change so this is the room after the final boss room
			program.removeAll();
			while (enemies.size() > 0) { // remove all enemies from ArrayList
				enemies.remove(0);
			}
			while (items.size() > 0) { // remove all items from ArrayList
				items.remove(0);
			}
			timer.stop(); // stop timer
			System.out.println("Congratulations! You escaped the dungeon!");
			if (program.isAudioOn()) {
				backgroundMusic.stopSound("sounds", "more_basic_loop.wav"); // stop boss background music
				backgroundMusic.playSound("sounds", "win.wav"); // play win music
			}
			program.setPlayerWin(true);
			program.switchTo(3);
		}
		
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
		if (currentRoom % 3 == 0) { // TODO change later
			while (bossHealth.size() > 0) { // remove all boss hearts from screen
				program.remove(bossHealth.get(0));
				bossHealth.remove(0);
			}
			if (enemies.size() > 0) {
				for (Enemy e: enemies) { // loop through all enemies on screen
					if (e instanceof Boss) { // check if enemy is a boss
						if (e.isDead()) { // check if boss is dead
							program.remove(bossLabel); // remove bossLabel from screen
							if (!dropWeapon) {
								GImage upgradeSprite;
								if (program.isCloseRangeWeapon()) {
									upgradeSprite = new GImage ("KnightUpgrade.png", e.getSprite().getX(), e.getSprite().getY()); //Create a new sprite for weapon upgrade.
								}
								else {
									upgradeSprite = new GImage ("WizardUpgrade.png", e.getSprite().getX(), e.getSprite().getY()); //Create a new sprite for weapon upgrade.
								}
								upgradeSprite.setSize(25, 25); //Resize sprite to make it smaller.
								Weapon upgrade = new Weapon(upgradeSprite, "upgrade");
								items.add(upgrade); // add weaponUpgrade to items list
								program.add(upgradeSprite); // add weapon upgrade to screen
								program.add(upgrade.getLabel()); //add label to screen.
								dropWeapon = true;
								player.getSprite().sendToFront();
							}
						}
						else { // boss is alive
							bossHealth = ((Boss) e).displayHealth();
						}
					}
				}
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
	
	public void gameOver() {
		if (program.isAudioOn()) {
			backgroundMusic.stopSound("sounds", "basic_loop.wav"); // stop background music
			backgroundMusic.stopSound("sounds", "more_basic_loop.wav"); // stop boss background music
			backgroundMusic.playSound("sounds", "game_over.wav", false); // play game over sound
		}
		program.switchTo(3);
	}
	
	public void playSound(String e, AudioPlayer p) {
		if (e.contains("big goblin")) {
			sounds.setName("boss_goblin_grunt"); //Sound effect for boss getting hit.
		}
		else if (e.contains("goblin")) {
			sounds.setName("small_goblin_grunt"); //Sound effect for enemy getting hit.
		}
		else if (e.contains("dragon")) {
			sounds.setName("dragon_grunt");
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
		for (int z = 0; z < enemies.size(); z++) {
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
							playSound(enemies.get(0).getEnemyType(), p); //play enemy grunt sound.
							enemy.changeHealth(-1);
							updateHealth();
							enemy.setDamaged(true); //Enemy is damaged.
							System.out.println(enemy.getHealth());
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
							//playSound(enemies.get(0).getEnemyType(), p); //play player grunt sound.
							player.changeHealth(-1);
							updateHealth(); // update health display
							player.setDamaged(true); //player is damaged.
							System.out.println("player health: " + player.getHealth());
							if (player.isDead()) {
								program.removeAll();
								while (enemies.size() > 0) { // remove all enemies from ArrayList
									enemies.remove(0);
								}
								gameOver();
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
						if (enemy.getEnemyType().contains("long range")) {
							if (enemy.getEnemyType().contains("dragon boss")) {
								String fireSpriteFile = "burningFireSprite.png";
								if (Math.random() <= 0.5) { // 50% chance for fire to appear mirrored
									fireSpriteFile = "burningFireMirroredSprite.png";
								}
								GImage fireSprite = new GImage (fireSpriteFile, enemy.getBulletSprite().getX(), enemy.getBulletSprite().getY());
								Enemy fire = new Enemy(fireSprite, 1, "close range fire");
								fire.setDetectionRange(25); // so fire can hurt player
								fire.setSpeed(0); // so fire does not move
								program.add(fire.getSprite()); // add fire sprite to the screen
								enemies.add(fire); // add fire enemy to the screen
								enemy.getSprite().sendToFront(); // send dragon to front
							}
							else {
								enemySprite.movePolar(2 * enemy.getSpeed(), (180 * Math.atan2(-y, x) / Math.PI)); // enemy moves away from player
							}
						}
					}
					// setting bounds for enemy //TODO set proper bounds for dragon
					if (enemySprite.getLocation().getX() < 5) {
						enemySprite.setLocation(5, enemySprite.getY());
					} else if (enemySprite.getLocation().getY() < 5) { 
						enemySprite.setLocation(enemySprite.getX(), 5);
					} else if (enemySprite.getLocation().getX() + enemySprite.getWidth() * 1.75 > program.getWidth()) {
						enemySprite.setLocation(program.getWidth() - enemySprite.getWidth() * 1.75,enemySprite.getY());
					} else if (enemySprite.getLocation().getY() + enemySprite.getHeight() * 3 > program.getHeight()) {
						enemySprite.setLocation(enemySprite.getX(), program.getHeight() - enemySprite.getHeight() * 3);
					} 
					
					if (Collision.check(enemy.getSprite().getBounds(), player.getSprite().getBounds())) { // player collides with enemy
						playerSprite.movePolar(Math.sqrt(x*x+y*y), (180 * Math.atan2(-y, x) / Math.PI) + 180); // player moves away from enemy
						if (enemy.getEnemyType().contains("boss")) {
							player.changeHealth(-2);
							updateHealth();
							System.out.println("Player touched by boss: " + player.getHealth());
						}
						else if (enemy.getEnemyType().contains("close range")) {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player touched by close range enemy: " + player.getHealth());
						}
						else if (enemy.getEnemyType().contains("long range")) {
							player.changeHealth(-1);
							updateHealth();
							System.out.println("Player touched by long range enemy: " + player.getHealth());
						}
						if (player.isDead()) {
							program.removeAll();
							while (enemies.size() > 0) { // remove all enemies from ArrayList
								enemies.remove(0);
							}
							gameOver();
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
			for (int y = 0; y < removeEnemyIndex.size(); y++) {
				System.out.println("Enemy index to remove: " + removeEnemyIndex.get(y));
				enemies.remove((int)removeEnemyIndex.get(y));
			}
			System.out.println("Enemies alive: " + enemies.size());
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
						i.getLabel().setLocation(i.getSprite().getX(), i.getSprite().getY());
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
			if (program.isCloseRangeWeapon()) {
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
				ArrayList<Integer> removeEnemyIndex = new ArrayList<Integer>();
				for (int z = 0; z < enemies.size(); z++) { // loop for all enemies
					Enemy enemy = enemies.get(z);
					if (Collision.check(attackArea.getBounds(), enemy.getSprite().getBounds())) { //player in range of enemy.
						System.out.println("Enemy is hit.");
						System.out.println("Enemy: " + enemies.get(0));
						playSound(enemies.get(0).getEnemyType(), p); //play enemy grunt sound
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
							currentRoom++;
							createRoom(currentRoom); // create next room
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
					if (newAttackCooldown < 100) { // so attack cool down doesn't go to low
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
		//Player revival if there are hearts in the inventory.
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