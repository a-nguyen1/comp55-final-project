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
				GImage enemySprite = new GImage(sprites.get("goblin"), 300, 120);
				Enemy goblin = new Enemy(enemySprite, 2, "close range goblin");
				enemies.add(goblin); //add enemy to ArrayList
				
				GImage enemySprite2 = new GImage(sprites.get("baby goblin"), 530, 120);
				Enemy babyGoblin = new Enemy(enemySprite2, 2, "close range baby goblin");
				enemies.add(babyGoblin); //add enemy2 to ArrayList
				
				GImage enemySprite3 = new GImage (sprites.get("flying goblin"), 300, 300);
				Enemy enemy3 = new Enemy(enemySprite3, 2, "long range flying goblin"); //Enemy has 2 health points.
				enemy3.setDetectionRange(300);
				Weapon weapon = new Weapon(new GImage(""), "mouth", 400);
				enemy3.setWeapon(weapon);
				enemies.add(enemy3); //add enemy to ArrayList
			}
			else { // add boss to screen
				GImage bossSprite = new GImage (sprites.get("dragon"), 300, 120);
				bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
				Boss boss = new Boss(bossSprite, 5, "long range dragon boss"); //Boss has 5 health points.
				boss.setSpeed(10);
				boss.setDetectionRange(800);
				Weapon weapon = new Weapon(new GImage(""), "mouth", 500);
				boss.setWeapon(weapon);
				GImage bullet = new GImage("fireBallSprite.png", boss.getSprite().getX(), boss.getSprite().getY());
				boss.setBulletSprite(bullet);
				enemies.add(boss); //add enemy to ArrayList
			}
		}
		else if (level == 2) {
			
		}
		else if (level == 3) {
			
		}
		return enemies; 
	}
	
	public ArrayList<Item> getItems() {
		items = new ArrayList<Item>(); // initialize item array list
		
		// randomize values for key location
		double x = 100 + Math.random() * (width - 200); //randomize x so not at edge of screen (offset by 100)
		double y = 100 + Math.random() * (height - 200); //randomize y so not at edge of screen (offset by 100)
		
		GImage keySprite = new GImage (sprites.get("key"), x, y); //Create a new sprite for key.
		keySprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem key = new PickUpItem(keySprite, "key"); //Create key as Item object.
		items.add(key);
		
		//create heart object
		GImage heartSprite = new GImage (sprites.get("heart"), 150, 300); //Create a new sprite for heart.
		heartSprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem heart = new PickUpItem(heartSprite, "heart");
		items.add(heart);
				
		//create chest object
		GImage chestSprite = new GImage (sprites.get("chest"), 500, 200);
		chestSprite.setSize(25, 25);
		Chest chest = new Chest(chestSprite, "chest");
		items.add(chest);
		
		//create door object
		GImage doorSprite = new GImage (sprites.get("door"), 300, 100); //Create a new sprite for door.
		Door door = new Door(doorSprite, "closedDoor"); //Create door as Item object.
		items.add(door);
		
		return items; 
	}

	public static void main(String[] args) {

	}

}
