package edu.pacific.comp55.starter;
import java.util.ArrayList; // for ArrayList

import acm.graphics.GImage; // for GImage

public class Room {
	//has player location
	//has items
	//has enemies
	//has background tile
	private String roomType;
	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
	private String backgroundTileName;
	
	public Room(int num) {
		if (num == 0) {
			backgroundTileName = "GrayTile.png";
		} 
		else {
			backgroundTileName = "OrangeTile.png";
		}
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
		return enemies; //TODO add enemies to room
	}
	
	public ArrayList<Item> getItems() {
		items = new ArrayList<Item>(); // initialize item array
		
		//create key object
		GImage keySprite = new GImage ("keyImage.png", 200, 200); //Create a new sprite for key.
		keySprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem key = new PickUpItem(keySprite, "key"); //Create key as Item object.
		items.add(key);
		
		//create heart object
		GImage heartSprite = new GImage ("Heart.png", 150, 300); //Create a new sprite for heart.
		heartSprite.setSize(25, 25); //Resize sprite to make it smaller.
		PickUpItem heart = new PickUpItem(heartSprite, "heart");
		items.add(heart);
				
		//create chest object
		GImage chestSprite = new GImage ("chest.png", 500, 200);
		chestSprite.setSize(25, 25);
		Chest chest = new Chest(chestSprite, "chest");
		items.add(chest);
				
		//create door object
		GImage doorSprite = new GImage ("closedDoor.png", 300, 100); //Create a new sprite for door.
		Door door = new Door(doorSprite, "closedDoor"); //Create door as Item object.
		items.add(door);
		return items; //TODO add items to room
	}
	
	public void nextRoom() { //TODO move to next room
		
	}

	public static void main(String[] args) {

	}

}
