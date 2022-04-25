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

	public ArrayList<Item> releaseItems(boolean closeRange) {
		items = new ArrayList<Item>(); // initialize item array list
		double chestX = getSprite().getX();
		double chestY = getSprite().getY();
		double randNum = Math.random();
		if (randNum <= 0.05) { // 5 % chance of giving weapon upgrade
			String upgradeType = "WizardUpgrade.png"; //default weapon upgrade sprite.
			if (closeRange) {
				upgradeType = "KnightUpgrade.png";
			}
			GImage sprite = new GImage(upgradeType, chestX, chestY); //weapon upgrade sprite for knight.
			PickUpItem upgrade = new PickUpItem(sprite, "upgrade");
			items.add(upgrade);
		}
		else if (randNum <= 0.10) { // 5 % chance of giving extra life
			GImage sprite = new GImage("thugLife.png", chestX, chestY);
			PickUpItem life = new PickUpItem(sprite, "life");
			items.add(life);
		}
		else if (randNum <= 0.20) { // 10 % chance of giving two extra hearts
			addHeart(chestX, chestY, ITEM_SIZE);
			addHeart(chestX, chestY, -ITEM_SIZE);
		}
		else {
			addHeart(chestX, chestY, ITEM_SIZE); // 80 % chance of giving a heart
		}
		addHeart(chestX, chestY, 0); // always give a heart
		
		return items;
	}

	private void addHeart(double chestX, double chestY, double offset) {
		GImage heartSprite = new GImage ("Heart.png", chestX, chestY + offset); //Create a new sprite for heart.
		heartSprite.setSize(ITEM_SIZE, ITEM_SIZE); //Resize sprite to make it smaller.
		PickUpItem heart = new PickUpItem(heartSprite, "heart");
		items.add(heart);
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
