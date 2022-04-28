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
		openChest = new GImage(ImageFolder.get() + "openChest.png", getSprite().getX(), getSprite().getY()); 
		openChest.setSize(ITEM_SIZE, ITEM_SIZE);
	}

	public ArrayList<Item> releaseItems(boolean closeRange) {
		items = new ArrayList<Item>(); // initialize item array list
		double chestX = getSprite().getX();
		double chestY = getSprite().getY();
		double randNum = Math.random();
		if (randNum <= 0.1) { // 10 % chance of giving weapon upgrade
			String upgradeType = ImageFolder.get() + "WizardUpgrade.png"; //default weapon upgrade sprite.
			if (closeRange) {
				upgradeType = ImageFolder.get() + "KnightUpgrade.png";
			}
			GImage sprite = new GImage(upgradeType, chestX, chestY + ITEM_SIZE); //weapon upgrade sprite for knight.
			Weapon upgrade = new Weapon(sprite, "upgrade");
			items.add(upgrade);
		}
		else if (randNum <= 0.20) { // 10 % chance of giving extra life
			GImage sprite = new GImage(ImageFolder.get() + "thugLife.png", chestX, chestY + ITEM_SIZE);
			PickUpItem life = new PickUpItem(sprite, "life");
			items.add(life);
		}
		else if (randNum <= 0.30) { // 10 % chance of giving three extra hearts
			addHeart(chestX, chestY + 2 * ITEM_SIZE);
			addHeart(chestX, chestY + ITEM_SIZE);
			addHeart(chestX, chestY - ITEM_SIZE);
		}
		else if (randNum <= 0.50) { // 20 % chance of giving two extra hearts
			addHeart(chestX, chestY + ITEM_SIZE);
			addHeart(chestX, chestY - ITEM_SIZE);
		}
		else {
			addHeart(chestX, chestY + ITEM_SIZE); // 50 % chance of giving a heart
		}
		addHeart(chestX, chestY); // always give a heart
		
		return items;
	}

	private void addHeart(double chestX, double chestY) {
		GImage heartSprite = new GImage (ImageFolder.get() + "Heart.png", chestX, chestY); //Create a new sprite for heart.
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
