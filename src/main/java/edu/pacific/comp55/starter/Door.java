package edu.pacific.comp55.starter;
import acm.graphics.GImage; // for door graphic
import java.util.ArrayList; // for arrayList

public class Door extends Item{
	private GImage doorOpen;
	private boolean locked;
	private boolean finalDoor;
	//private Player player;
	//private ArrayList<Item> items;
	//comment if need more.
	
	
	public Door(GImage image, String name) { // image is when door closed
		super(image, name);
		locked = true;
		//doorOpen = new GImage(name, 0, 0);
	}
	
	public boolean unlock(ArrayList<Item> inventory) { // TODO unlock if player has key
		return true;
	}
	
	public boolean finalDoor() {
		return finalDoor;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
