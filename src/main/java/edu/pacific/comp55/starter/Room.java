package edu.pacific.comp55.starter;
import java.util.ArrayList; // for ArrayList

import acm.graphics.GImage; // for GImage

public class Room {
	//has player location
	//has items
	//has enemies
	//has background tile
	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
	private String backgroundTileName;
	private double width; // width of program
	private double height; // height of program
	private int room; // room number
	private int level; // level number 
	
	public Room(int levelNumber, int roomNumber, double w, double h) {
		level = levelNumber;
		room = roomNumber;
		if (room <= 2) {
			backgroundTileName = "GrayTile.png";
		} 
		else { // levelNumber > 1
			backgroundTileName = "OrangeTile.png";
		}
		width = w;
		height = h;
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
		if (room <= 2) {
			//create enemy object
			GImage enemySprite = new GImage ("bigger-enemy-sprite.png", 300, 120);
			Enemy enemy = new Enemy(enemySprite, 2, "close range"); //Enemy has 2 health points.
			enemy.setSpeed(5);
			enemies.add(enemy); //add enemy to ArrayList
			
			//Second enemy object
			GImage enemySprite2 = new GImage ("goblin-sprite.png", 530, 120);
			Enemy enemy2 = new Enemy(enemySprite2, 2, "close range"); //Enemy has 2 health points.
			enemy2.setSpeed(5);
			enemies.add(enemy2); //add enemy to ArrayList
			
			//Third enemy object (long range)
			GImage enemySprite3 = new GImage ("FlyingGoblinAttack.png", 300, 300);
			Enemy enemy3 = new Enemy(enemySprite3, 2, "long range"); //Enemy has 2 health points.
			enemy3.setSpeed(5);
			enemy3.setDetectionRange(300);
			GImage weaponSprite = new GImage("bow.png");
			Weapon weapon = new Weapon(weaponSprite, "mouth", 400);
			enemy3.setWeapon(weapon);
			enemies.add(enemy3); //add enemy to ArrayList
		}
		else {
			GImage bossSprite = new GImage ("bigger-enemy-sprite.png", 300, 120);
			bossSprite.setSize(bossSprite.getWidth() * 2, bossSprite.getHeight() * 2);
			Boss boss = new Boss(bossSprite, 5, "Big Goblin"); //Boss has 5 health points.
			boss.setSpeed(10);
			boss.setDetectionRange(800);
			enemies.add(boss); //add enemy to ArrayList
		}
		return enemies; 
	}
	
	public ArrayList<Item> getItems() {
		items = new ArrayList<Item>(); // initialize item array list
		
		// randomize values for key location
		double x = 100 + Math.random() * (width - 200); //randomize x so not at edge of screen (offset by 100)
		double y = 100 + Math.random() * (height - 200); //randomize y so not at edge of screen (offset by 100)
		
		GImage keySprite = new GImage ("keyImage.png", x, y); //Create a new sprite for key.
		keySprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem key = new PickUpItem(keySprite, "key"); //Create key as Item object.
		items.add(key);
		
		//create heart object
		GImage heartSprite = new GImage ("Heart.png", 150, 300); //Create a new sprite for heart.
		heartSprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem heart = new PickUpItem(heartSprite, "heart");
		items.add(heart);
				
		//create chest object
		GImage chestSprite = new GImage ("closedChest.png", 500, 200);
		chestSprite.setSize(25, 25);
		Chest chest = new Chest(chestSprite, "chest");
		items.add(chest);
				
		//create door object
		GImage doorSprite = new GImage ("closedDoor.png", 300, 100); //Create a new sprite for door.
		Door door = new Door(doorSprite, "closedDoor"); //Create door as Item object.
		items.add(door);
		
		return items; 
	}

	public static void main(String[] args) {

	}

}
