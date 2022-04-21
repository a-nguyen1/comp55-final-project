package edu.pacific.comp55.starter;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap;

import acm.graphics.GImage; // for GImage
import acm.graphics.GPoint;

public class Room {
	private static final int BOSS_Y = 120;
	private static final int BOSS_X = 300;
	private static final int BOSS_DETECTION_RANGE = 800;
	private static final int FLYING_GOBLIN_DETECTION_RANGE = 400;
	private static final int BABY_GOBLIN_DETECTION_RANGE = 200;
	private static final int NORMAL_GOBLIN_DETECTION_RANGE = 100;
	//has player location
	//has items
	//has enemies
	//has background tile
	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
	private ArrayList<GPoint> locations;
	private HashMap<String, String> sprites;
	private String backgroundTileName;
	private double width; // width of program
	private double height; // height of program
	private int room; // room number
	private int level; // level number 

	
	public Room(int levelNumber, int roomNumber, double w, double h) {
		level = levelNumber;
		room = roomNumber;
		//set tiles based on level
		if (room <= 6) { 
			backgroundTileName = "GreenTile.png";
		}
		else if (room <= 12) {
			backgroundTileName = "GrayTile.png";
		}
		else if (room <= 18) {
			backgroundTileName = "OrangeTile.png";
		}
		width = w;
		height = h;
		sprites = new HashMap<String, String>();
		sprites.put("goblin", "EnemyGoblinSprite.png");
		sprites.put("baby goblin", "EnemyGoblinBabySprite.png");
		sprites.put("flying goblin", "EnemyGoblinFlyingSprite.png");
		sprites.put("dragon", "dragon-sprite.png");
		sprites.put("key", "key.png");
		sprites.put("heart", "heart.png");
		sprites.put("chest", "closedChest.png");
		sprites.put("door", "closedDoor.png");
		
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
				addEnemy("goblin", randomizePoint(), 2, "close range goblin", NORMAL_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				break;
			case 2:
				addEnemy("goblin", randomizePoint(), 2, "close range goblin", NORMAL_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				addEnemy("baby goblin", randomizePoint(), 1, "close range baby goblin", BABY_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				break;
			case 3: 
				addEnemy("goblin", randomizePoint(), 2, "close range goblin", NORMAL_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				addEnemy("baby goblin", randomizePoint(), 1, "close range baby goblin", BABY_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				addEnemy("flying goblin", randomizePoint(), 2, "long range flying goblin", 300, FLYING_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				break;
			case 4:
				for (int i = 0; i < 5; i++) {
					addEnemy("baby goblin", randomizePoint(), 1, "close range baby goblin", BABY_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				}
				break;
			case 5:
				for (int i = 0; i < 3; i++) {
					addEnemy("flying goblin", randomizePoint(), 2, "long range flying goblin", 300, FLYING_GOBLIN_DETECTION_RANGE); //add enemy to ArrayList
				}
				break;
			case 6:
				addGoblinBoss(); //add enemy to ArrayList
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				addDragonBoss(); //add enemy to ArrayList
				break;
			default:
				
				
		}
			
		return enemies; 
	}

	private void addDragonBoss() {
		addLocation(BOSS_X, BOSS_Y);
		
		GImage bossSprite = new GImage (sprites.get("dragon"), BOSS_X, BOSS_Y);
		bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
		Boss boss = new Boss(bossSprite, 5, "long range dragon boss"); //Boss has 5 health points.
		boss.setSpeed(10);
		boss.setDetectionRange(BOSS_DETECTION_RANGE);
		
		Weapon weapon = new Weapon(new GImage(""), "mouth", 500);
		boss.setWeapon(weapon);
		GImage bullet = new GImage("fireBallSprite.png", boss.getSprite().getX(), boss.getSprite().getY());
		boss.setBulletSprite(bullet);
		enemies.add(boss);
	}
	
	private void addGoblinBoss() {
		addLocation(BOSS_X, BOSS_Y);
		GImage bossSprite = new GImage (sprites.get("goblin"), BOSS_X, BOSS_Y);
		bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
		Boss boss = new Boss(bossSprite, 5, "close range big goblin boss"); //Boss has 5 health points.
		boss.setSpeed(10);
		boss.setDetectionRange(BOSS_DETECTION_RANGE);
		enemies.add(boss);
	}

	private void addEnemy(String enemy, GPoint point, int health, String enemyType, int detectionRange, int weaponRange) {
		double x = point.getX();
		double y = point.getY();
		addLocation(x,y);
		
		GImage enemySprite = new GImage(sprites.get(enemy), x, y);
		Enemy enemyToAdd = new Enemy(enemySprite, health, enemyType);
		enemyToAdd.setDetectionRange(detectionRange);
		Weapon weapon = new Weapon(new GImage(""), "", weaponRange);
		enemyToAdd.setWeapon(weapon);
		enemies.add(enemyToAdd);
	}

	private void addEnemy(String enemy, GPoint point, int health, String enemyType, int detectionRange) {
		double x = point.getX();
		double y = point.getY();
		addLocation(x,y);
		
		GImage enemySprite = new GImage(sprites.get(enemy), x, y);
		Enemy enemyToAdd = new Enemy(enemySprite, health, enemyType);
		enemyToAdd.setDetectionRange(detectionRange);
		enemies.add(enemyToAdd);
	}

	private void addLocation(double x, double y) {
		GPoint addLocation = new GPoint(x,y);
		locations.add(addLocation);
	}
	
	public ArrayList<Item> getItems() {
		items = new ArrayList<Item>(); // initialize item array list
		
		addItem("heart", randomizePoint());
		addItem("chest", randomizePoint());
		addItem("door", randomizePoint());
		addItem("key", randomizePoint());
		
		return items; 
	}

	private double randomizeBetween(double min, double max) {
		return min + Math.random() * max; //randomize between min and max
	}
	
	private GPoint randomizePoint() {
		double x = randomizeBetween(100, width - 200); //randomize x so not at edge of screen (offset by 100)
		double y = randomizeBetween(100, height - 200); //randomize y so not at edge of screen (offset by 100)
		GPoint returnPoint = new GPoint(x, y);
		while (isTooClose(returnPoint)) { //Re randomize the points until point is not too close.
			x = randomizeBetween(100, width - 200); //randomize x so not at edge of screen (offset by 100)
			y = randomizeBetween(100, height - 200); //randomize y so not at edge of screen (offset by 100)
			returnPoint = new GPoint(x, y);
		}
		return returnPoint;
	}
	
	private boolean isTooClose(GPoint point) {
		boolean returnValue = false;
		for (GPoint p : locations) {
			double xDiff = Math.abs(p.getX() - point.getX()); // find difference in x coordinates
			double yDiff = Math.abs(p.getY() - point.getY()); // find difference in y coordinates
			if (xDiff <= 50 && yDiff <= 50) {
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
		if (itemName == "key" || itemName == "heart" ) {
			itemSprite.setSize(25, 25); //Resize sprite to make it smaller.
			PickUpItem pickUp = new PickUpItem(itemSprite, itemName); //Create key as Item object.
			items.add(pickUp);
		}
		else if (itemName == "chest") {
			itemSprite.setSize(25, 25);
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
