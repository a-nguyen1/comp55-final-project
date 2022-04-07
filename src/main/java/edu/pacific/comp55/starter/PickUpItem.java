package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class PickUpItem extends Item{
	private boolean inInventory;
	
	public PickUpItem(GImage image, String name) {
		super(image, name);
		inInventory = false;
	}
	
	public void consume() { //TODO use the item
		
	}
	
	public static void main(String[] args) {

	}

	public boolean getInInventory() {
		return inInventory;
	}

	public void setInInventory(boolean inInventory) {
		this.inInventory = inInventory;
	}
}
