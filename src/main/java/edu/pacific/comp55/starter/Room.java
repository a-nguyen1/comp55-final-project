package edu.pacific.comp55.starter;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap;

import acm.graphics.GImage; // for GImage
import acm.graphics.GPoint;

public class Room {
	
	private static final int NORMAL_SIZE = 50;

	// minimum distance (between 2 x values and 2 y values) for difference between 2 points
	private static final int MINIMUM_DISTANCE = 50; 
	
	private static final int OFFSET = 100; // offset from sides of screen
	
	private static final int SLOW_SPEED = 3;
	private static final int NORMAL_SPEED = 5;
	private static final int FAST_SPEED = 10;
	private static final int SUPER_FAST_SPEED = 15;
	
	private static final int ITEM_SIZE = 25;
	
	private static final int BOSS_Y = 120;
	private static final int BOSS_X = 300;
	private static final int BOSS_HEALTH = 5;
	private static final int BOSS_WEAPON_RANGE = 500;
	private static final int BOSS_DETECTION_RANGE = 800;
	
	private static final int WIZARD_DRAGON_WEAPON_RANGE = 300;
	private static final int WIZARD_DRAGON_DETECTION_RANGE = 200;

	private static final int DEMON_MAGICIAN_WEAPON_RANGE = 400;
	private static final int DEMON_MAGICIAN_DETECTION_RANGE = 250;
	
	private static final int SKELETON_SUMMONER_WEAPON_RANGE = 450;
	private static final int SKELETON_SUMMONER_DETECTION_RANGE = 350;
	
	private static final int SKELETON_DETECTION_RANGE = 250;
	private static final int HEARTLESS_SKELETON_DETECTION_RANGE = BOSS_WEAPON_RANGE; // 500
	
	private static final int FLYING_GOBLIN_WEAPON_RANGE = 400;
	private static final int FLYING_GOBLIN_DETECTION_RANGE = 300;
	
	private static final int BABY_GOBLIN_DETECTION_RANGE = 200;
	private static final int NORMAL_GOBLIN_DETECTION_RANGE = 100;
	
	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
	private ArrayList<GPoint> locations;
	private HashMap<String, String> sprites;
	private String backgroundTileName;
	
	private double width; // width of program
	private double height; // height of program
	private int room; // room number
	
	
	public Room(int roomNumber, double w, double h) {
		room = roomNumber;
		//set tiles based on room number
		
		if (room <= 6) { 
			backgroundTileName = ImageFolder.get() + "GreenTile.png";
		}
		else if (room <= 12) {
			backgroundTileName = ImageFolder.get() + "GrayTile.png";
		}
		else {
			backgroundTileName = ImageFolder.get() + "OrangeTile.png";
		}
		width = w;
		height = h;
		sprites = new HashMap<String, String>();
		
		//enemy sprites
		sprites.put("skeleton", ImageFolder.get() + "EnemySkeletonSprite.png"); 
		sprites.put("heartless skeleton", ImageFolder.get() + "EnemyHeartlessSkeletonSprite.png");
		sprites.put("skeleton summoner", ImageFolder.get() + "EnemySkeletonSummonerSprite.png");
		sprites.put("goblin", ImageFolder.get() + "EnemyGoblinSprite.png");
		sprites.put("baby goblin", ImageFolder.get() + "EnemyGoblinBabySprite.png");
		sprites.put("flying goblin", ImageFolder.get() + "EnemyGoblinFlyingSprite.png");
		sprites.put("dragon", ImageFolder.get() + "EnemyDragonSprite.png");
		sprites.put("wizard", ImageFolder.get() + "EnemyWizardSprite.png");
		sprites.put("demon magician", ImageFolder.get() + "EnemyDemonMagicianSprite.png");
		
		//item sprites
		sprites.put("key", ImageFolder.get() + "key.png");
		sprites.put("heart", ImageFolder.get() + "heart.png");
		sprites.put("chest", ImageFolder.get() + "closedChest.png");
		sprites.put("door", ImageFolder.get() + "closedDoor.png");
		sprites.put("life", ImageFolder.get() + "thugLife.png");
		
		locations = new ArrayList<GPoint>();
		items = new ArrayList<Item>();
		enemies = new ArrayList<Enemy>();
	}
	
	public void setPlayerLocation(Player p, double x, double y) {
		p.getSprite().setLocation(x, y);
	}
	
	public void setItemLocation(Item i, double x, double y) {
		i.getSprite().setLocation(x, y);
	}
	
	public String getTileName() {
		return backgroundTileName;
	}
	
	public ArrayList<Enemy> getEnemies() {
		enemies = new ArrayList<Enemy>(); // initialize enemy array list
		switch(room) {
			case 1:
				addEnemy("goblin", randomizePoint(), 2, "close range goblin", NORMAL_GOBLIN_DETECTION_RANGE, NORMAL_SPEED); 
				break;
			case 2:
				addEnemy("goblin", randomizePoint(), 2, "close range goblin", NORMAL_GOBLIN_DETECTION_RANGE, NORMAL_SPEED); 
				addEnemy("baby goblin", randomizePoint(), 1, "close range baby goblin", BABY_GOBLIN_DETECTION_RANGE, FAST_SPEED); 
				break;
			case 3: 
				addEnemy("goblin", randomizePoint(), 2, "close range goblin", NORMAL_GOBLIN_DETECTION_RANGE, NORMAL_SPEED); 
				addEnemy("baby goblin", randomizePoint(), 1, "close range baby goblin", BABY_GOBLIN_DETECTION_RANGE, FAST_SPEED); 
				addEnemy("flying goblin", randomizePoint(), 2, "long range flying goblin", FLYING_GOBLIN_DETECTION_RANGE, FLYING_GOBLIN_WEAPON_RANGE, ImageFolder.get() + "poisonBallSprite.png", NORMAL_SPEED); 
				break;
			case 4:
				for (int i = 0; i < 5; i++) {
					addEnemy("baby goblin", randomizePoint(), 1, "close range baby goblin", BABY_GOBLIN_DETECTION_RANGE, FAST_SPEED);
				}
				break;
			case 5:
				for (int i = 0; i < 3; i++) {
					addEnemy("flying goblin", randomizePoint(), 2, "long range flying goblin", FLYING_GOBLIN_DETECTION_RANGE, FLYING_GOBLIN_WEAPON_RANGE, ImageFolder.get() + "poisonBallSprite.png", NORMAL_SPEED);
				}
				break;
			case 6:
				addBoss("goblin", BOSS_HEALTH, "close range big goblin boss", FAST_SPEED); //add GoblinBoss
				break;
			case 7:
				for (int i = 0; i <= 1; i++) {
					addEnemy("dragon", randomizePoint(), 3, "long range dragon", WIZARD_DRAGON_DETECTION_RANGE, WIZARD_DRAGON_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				}
				break;
			case 8:
				for (int i = 0; i <= 2; i++) {
					addEnemy("dragon", randomizePoint(), 3, "long range dragon", WIZARD_DRAGON_DETECTION_RANGE, WIZARD_DRAGON_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				}
				break;
			case 9:
				addEnemy("wizard", randomizePoint(), 3, "long range wizard", WIZARD_DRAGON_DETECTION_RANGE, WIZARD_DRAGON_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				break;
			case 10:
				addEnemy("wizard", randomizePoint(), 3, "long range wizard", WIZARD_DRAGON_DETECTION_RANGE, WIZARD_DRAGON_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED);
				addEnemy("dragon", randomizePoint(), 3, "long range dragon", WIZARD_DRAGON_DETECTION_RANGE, WIZARD_DRAGON_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED);
				break;
			case 11:
				for (int i = 0; i < 2; i++) {
					addEnemy("wizard", randomizePoint(), 3, "long range wizard", WIZARD_DRAGON_DETECTION_RANGE, WIZARD_DRAGON_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				}
				break;
			case 12:
				addBoss("dragon", BOSS_HEALTH * 2, "long range dragon boss", BOSS_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", FAST_SPEED); //add DragonBoss
				break;
			case 13:
				addEnemy("demon magician", randomizePoint(), 3, "long range demon magician", DEMON_MAGICIAN_DETECTION_RANGE, DEMON_MAGICIAN_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				addEnemy("skeleton", randomizePoint(), 1, "close range skeleton", SKELETON_DETECTION_RANGE, NORMAL_SPEED);
				addEnemy("heartless skeleton", randomizePoint(), 4, "close range heartless skeleton", HEARTLESS_SKELETON_DETECTION_RANGE, SUPER_FAST_SPEED);
				break;
			case 14:
				for (int i = 0; i < 2; i++) {
					addEnemy("demon magician", randomizePoint(), 3, "long range demon magician", DEMON_MAGICIAN_DETECTION_RANGE, DEMON_MAGICIAN_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				}
				addEnemy("skeleton summoner", randomizePoint(), 3, "long range skeleton summoner", SKELETON_SUMMONER_DETECTION_RANGE, SKELETON_SUMMONER_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", SLOW_SPEED); 
				break;
			case 15:
				for (int i = 0; i < 2; i++) {
					addEnemy("demon magician", randomizePoint(), 3, "long range demon magician", DEMON_MAGICIAN_DETECTION_RANGE, DEMON_MAGICIAN_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				}
				for (int i = 0; i < 7; i++) {
					addEnemy("skeleton", randomizePoint(), 1, "close range skeleton", SKELETON_DETECTION_RANGE, NORMAL_SPEED);
				}
				break;
			case 16:
				for (int i = 0; i < 2; i++) {
					addEnemy("demon magician", randomizePoint(), 3, "long range demon magician", DEMON_MAGICIAN_DETECTION_RANGE, DEMON_MAGICIAN_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", NORMAL_SPEED); 
				}
				for (int i = 0; i < 2; i++) {
					addEnemy("heartless skeleton", randomizePoint(), 4, "close range heartless skeleton", HEARTLESS_SKELETON_DETECTION_RANGE, SUPER_FAST_SPEED);
				}
				break;
			case 17:
				for (int i = 0; i < 2; i++) {
					addEnemy("skeleton summoner", randomizePoint(), 3, "long range skeleton summoner", SKELETON_SUMMONER_DETECTION_RANGE, SKELETON_SUMMONER_WEAPON_RANGE, ImageFolder.get() + "fireBallSprite.png", SLOW_SPEED); 
				}
				break;
			case 18:
				addBoss("wizard", BOSS_HEALTH * 3, "long range wizard boss summoner", BOSS_WEAPON_RANGE, ImageFolder.get() + "fireSpiralSprite.png", FAST_SPEED); //add WizardBoss
				break;
		}
			
		return enemies; 
	}

	//adding long range boss
	private void addBoss(String bossName, int health, String enemyType, int weaponRange, String bulletName, double speed) {
		addLocation(BOSS_X, BOSS_Y);
		
		GImage bossSprite = new GImage (sprites.get(bossName), BOSS_X, BOSS_Y);
		bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
		Boss boss = new Boss(bossSprite, health, enemyType); 
		boss.setSpeed(speed);
		boss.setDetectionRange(BOSS_DETECTION_RANGE);
		
		Weapon weapon = new Weapon(new GImage(""), "", BOSS_WEAPON_RANGE);
		boss.setWeapon(weapon);
		GImage bullet = new GImage(bulletName, boss.getSprite().getX(), boss.getSprite().getY());
		boss.setBulletSprite(bullet);
		enemies.add(boss);
	}
	
	//adding close range boss
	private void addBoss(String bossName, int health, String enemyType, double speed) {
		addLocation(BOSS_X, BOSS_Y);
		GImage bossSprite = new GImage (sprites.get(bossName), BOSS_X, BOSS_Y);
		bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
		Boss boss = new Boss(bossSprite, health, enemyType);
		boss.setSpeed(speed);
		boss.setDetectionRange(BOSS_DETECTION_RANGE);
		enemies.add(boss);
	}

	//adding long range enemy
	private void addEnemy(String enemy, GPoint point, int health, String enemyType, int detectionRange, int weaponRange, String bulletName, double speed) {
		double x = point.getX();
		double y = point.getY();
		addLocation(x,y);
		
		GImage enemySprite = new GImage(sprites.get(enemy), x, y);
		if(enemyType.contains("demon")) {
			enemySprite.setSize(NORMAL_SIZE, NORMAL_SIZE); //Shrink demon enemy down.
		}
		Enemy enemyToAdd = new Enemy(enemySprite, health, enemyType);
		enemyToAdd.setDetectionRange(detectionRange);
		enemyToAdd.setSpeed(speed);
		Weapon weapon = new Weapon(new GImage(""), "", weaponRange);
		enemyToAdd.setWeapon(weapon);
		GImage bullet = new GImage(bulletName, enemyToAdd.getSprite().getX() + enemyToAdd.getSprite().getWidth() / 2, enemyToAdd.getSprite().getY() + enemyToAdd.getSprite().getHeight() / 2);
		enemyToAdd.setBulletSprite(bullet);
		enemies.add(enemyToAdd);
	}

	//adding close range enemy
	private void addEnemy(String enemy, GPoint point, int health, String enemyType, int detectionRange, double speed) {
		double x = point.getX();
		double y = point.getY();
		addLocation(x,y);
		
		GImage enemySprite = new GImage(sprites.get(enemy), x, y);
		Enemy enemyToAdd = new Enemy(enemySprite, health, enemyType);
		enemyToAdd.setDetectionRange(detectionRange);
		enemyToAdd.setSpeed(speed);
		enemies.add(enemyToAdd);
	}

	private void addLocation(double x, double y) {
		GPoint addLocation = new GPoint(x,y);
		locations.add(addLocation);
	}
	
	public ArrayList<Item> getItems() {
		items = new ArrayList<Item>(); // initialize item array list
		
		double randNum = Math.random();
		
		addItem("door", randomizePoint()); //always add door
		addItem("key", randomizePoint()); // always add key
		
		if (randNum <= 0.05) { // 5 % chance to appear
			addItem("life", randomizePoint()); // add life
		}
		else if (randNum <= 0.35) { // 30 % chance to appear
			addItem("chest", randomizePoint()); 
		}
		else if (randNum <= 0.45) { // 10 % chance to appear
			addItem("heart", randomizePoint()); 
			addItem("heart", randomizePoint()); 
			addItem("heart", randomizePoint()); 
		}
		else if (randNum <= 0.65) { // 20 % chance to appear
			addItem("heart", randomizePoint()); 
			addItem("heart", randomizePoint()); 
		}
		else {
			addItem("heart", randomizePoint()); // 35 % chance to appear
		}
		
		return items; 
	}

	private double randomizeBetween(double min, double max) {
		return min + Math.random() * max; //randomize between min and max
	}
	
	private GPoint randomizePoint() {
		double x = randomizeBetween(OFFSET, width - 2*OFFSET); //randomize x so not at edge of screen (there is an offset)
		double y = randomizeBetween(OFFSET, height - 2*OFFSET); //randomize y so not at edge of screen (there is an offset)
		GPoint returnPoint = new GPoint(x, y);
		while (isTooClose(returnPoint)) { //randomize the points until point is not too close.
			x = randomizeBetween(OFFSET, width - 2*OFFSET); //randomize x so not at edge of screen (there is an offset)
			y = randomizeBetween(OFFSET, height - 2*OFFSET); //randomize y so not at edge of screen (there is an offset)
			returnPoint = new GPoint(x, y);
		}
		return returnPoint;
	}
	
	private boolean isTooClose(GPoint point) {
		boolean returnValue = false;
		for (GPoint p : locations) {
			double xDiff = Math.abs(p.getX() - point.getX()); // find difference in x coordinates
			double yDiff = Math.abs(p.getY() - point.getY()); // find difference in y coordinates
			if (xDiff <= MINIMUM_DISTANCE && yDiff <= MINIMUM_DISTANCE) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	private void addItem(String itemName, GPoint point) {
		double x = point.getX();
		double y = point.getY();
		addLocation(x,y);
		
		GImage itemSprite = new GImage (sprites.get(itemName), x, y); //Create a new sprite for key.
		if (itemName == "key" || itemName == "heart"  || itemName == "life") {
			itemSprite.setSize(ITEM_SIZE, ITEM_SIZE); //Resize sprite to make it smaller.
			PickUpItem pickUp = new PickUpItem(itemSprite, itemName); //Create key as Item object.
			items.add(pickUp);
		}
		else if (itemName == "chest") {
			itemSprite.setSize(ITEM_SIZE, ITEM_SIZE);
			Chest chest = new Chest(itemSprite, "chest");
			items.add(chest);
		}
		else if (itemName == "door") {
			Door door = new Door(itemSprite, "closedDoor"); //Create door as Item object.
			items.add(door);
		}
	}

	public static void main(String[] args) {

	}

}
