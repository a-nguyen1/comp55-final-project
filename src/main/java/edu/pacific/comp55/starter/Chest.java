package edu.pacific.comp55.starter;
import acm.graphics.GImage; // for door graphic
import java.util.ArrayList; // for arrayList

public class Chest extends Item {
	private static final int ITEM_SIZE = 25;
	private GImage openChest;
	private ArrayList<Item> items;
	private boolean isChestOpen;

	public Chest(GImage image, String name) { // image is when chest closed
		super(image, name);
		isChestOpen = false;
		//Create an open chest sprite
		openChest = new GImage("openChest.png", getSprite().getX(), getSprite().getY()); 
		openChest.setSize(ITEM_SIZE, ITEM_SIZE);
	}

	public ArrayList<Item> releaseItems() {
		items = new ArrayList<Item>(); // initialize item array list
		GImage heartSprite = new GImage ("Heart.png", super.getSprite().getX() - ITEM_SIZE, super.getSprite().getY()); //Create a new sprite for heart.
		heartSprite.setSize(ITEM_SIZE, ITEM_SIZE); //Resize sprite to make it smaller.
		PickUpItem heart = new PickUpItem(heartSprite, "heart");
		items.add(heart);
		GImage heartSprite2 = new GImage ("Heart.png", super.getSprite().getX() + ITEM_SIZE, super.getSprite().getY() + ITEM_SIZE); //Create a new sprite for heart.
		heartSprite.setSize(ITEM_SIZE, ITEM_SIZE); //Resize sprite to make it smaller.
		PickUpItem heart2 = new PickUpItem(heartSprite2, "heart");
		items.add(heart2);
		return items;
	}
	
	public static void main(String[] args) {

	}
	
	public GImage getOpenChest() {
		return openChest;
	}

	public boolean isChestOpen() {
		return isChestOpen;
	}

	public void setChestOpen(boolean isChestOpen) {
		this.isChestOpen = isChestOpen;
	}
}
