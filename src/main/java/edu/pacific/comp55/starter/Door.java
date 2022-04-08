package edu.pacific.comp55.starter;
import acm.graphics.GImage; // for door graphic
import java.util.ArrayList; // for arrayList

public class Door extends Item{
	private GImage doorOpen;
	private boolean locked;
	private boolean finalDoor;
	
	public Door(GImage image, String name) { // image is when door closed
		super(image, name);
		setLocked(true);
		doorOpen = new GImage("openDoor.png", image.getX(), image.getY());
	}
	
	public boolean unlock(ArrayList<Item> inventory) {
		for (Item i: inventory) {
			if (i.getItemType() == "key") {
				setLocked(false);
			}
		}
		return !locked;
	}
	
	public GImage getOpenDoor() {
		return doorOpen;
	}
	
	public boolean finalDoor() {
		return finalDoor;
	}
	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public static void main(String[] args) {

	}

}
