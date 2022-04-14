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
	private GImage backgroundTile;
	
	public Room(String roomName) {
		
	}
	
	public void setPlayerLocation(Player p, double x, double y) {
		p.getSprite().setLocation(x, y);
	}
	
	public void setItemLocation(Item i, double x, double y) {
		i.getSprite().setLocation(x, y);
	}
	
	public void addEnemies() { //TODO add enemies to room
		
	}
	
	public void addItems() { //TODO add items to room
		
	}
	
	public void nextRoom() { //TODO move to next room
		
	}

	public static void main(String[] args) {

	}

}
