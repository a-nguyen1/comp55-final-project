package edu.pacific.comp55.starter;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap;

import acm.graphics.GImage; // for GImage

public class Room {
	//has player location
	//has items
	//has enemies
	//has background tile
	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
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
		if (level == 1) { 
			backgroundTileName = "GreenTile.png";
		}
		else if (level == 2) {
			backgroundTileName = "GrayTile.png";
		}
		else if (level == 3) {
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
		if (level == 1) {
			if (room % 3 != 0) { // TODO change
				addEnemy("goblin", 300, 120, 2, "close range goblin", 100); //add enemy to ArrayList
				addEnemy("baby goblin", 530, 120, 2, "close range baby goblin", 100); //add enemy to ArrayList
				addEnemy("flying goblin", 300, 300, 2, "long range flying goblin", 300, 400); //add enemy to ArrayList
			}
			else { // add boss to screen
				addBoss(); //add enemy to ArrayList
			}
		}
		else if (level == 2) {
			
		}
		else if (level == 3) {
			
		}
		return enemies; 
	}

	private void addBoss() {
		GImage bossSprite = new GImage (sprites.get("dragon"), 300, 120);
		bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
		Boss boss = new Boss(bossSprite, 5, "long range dragon boss"); //Boss has 5 health points.
		boss.setSpeed(10);
		boss.setDetectionRange(800);
		Weapon weapon = new Weapon(new GImage(""), "mouth", 500);
		boss.setWeapon(weapon);
		GImage bullet = new GImage("fireBallSprite.png", boss.getSprite().getX(), boss.getSprite().getY());
		boss.setBulletSprite(bullet);
		enemies.add(boss);
	}

	private void addEnemy(String enemy, double x, double y, int health, String enemyType, int detectionRange, int weaponRange) {
		GImage enemySprite = new GImage(sprites.get(enemy), x, y);
		Enemy enemyToAdd = new Enemy(enemySprite, health, enemyType);
		enemyToAdd.setDetectionRange(detectionRange);
		Weapon weapon = new Weapon(new GImage(""), "", weaponRange);
		enemyToAdd.setWeapon(weapon);
		enemies.add(enemyToAdd);
	}

	private void addEnemy(String enemy, double x, double y, int health, String enemyType, int detectionRange) {
		GImage enemySprite = new GImage(sprites.get(enemy), x, y);
		Enemy enemyToAdd = new Enemy(enemySprite, health, enemyType);
		enemyToAdd.setDetectionRange(detectionRange);
		enemies.add(enemyToAdd);
	}
	
	public ArrayList<Item> getItems() {
		items = new ArrayList<Item>(); // initialize item array list
		
		// randomize values for key location
		double x = 100 + Math.random() * (width - 200); //randomize x so not at edge of screen (offset by 100)
		double y = 100 + Math.random() * (height - 200); //randomize y so not at edge of screen (offset by 100)
		
		addItem("heart", 150, 300);
		addItem("chest", 500, 200);
		addItem("door", 300, 100);
		addItem("key", x, y);
		
		return items; 
	}

	private void addItem(String itemName, double x, double y) {
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
