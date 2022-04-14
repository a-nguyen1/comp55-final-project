package edu.pacific.comp55.starter;
import java.util.ArrayList; // for arraylist
import javax.swing.*; // for timer

public class Room {
	private String roomType;
	private ArrayList<Item> items;
	
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
