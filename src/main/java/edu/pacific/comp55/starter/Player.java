package edu.pacific.comp55.starter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for arraylist

import acm.graphics.GImage;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class Player extends Character {
	private static final int ITEM_SIZE = 25;
	private static final int HEART_SIZE = 50;
	private Weapon weapon;
	private ArrayList<Item> inventory;
	private boolean dashAvailable;
	private int dashCooldown;
	private boolean attackAvailable;
	private boolean bulletTraveling;
	private int bulletDistance;
	private GImage bulletSprite;
	private int attackDisplayCount;
	
	public Player(GImage sprite, int health) {
		super(sprite, health);
		inventory = new ArrayList<Item>();
		dashAvailable = true;
		attackAvailable = false;
		bulletTraveling = false;
		bulletDistance = 0;
		bulletSprite = new GImage("lightningBallSprite.png", getSprite().getX() + getSprite().getWidth() / 2, getSprite().getY() + getSprite().getHeight() / 2);
		bulletSprite.setVisible(false); 
		dashCooldown = 500; // set to 500 by default
		super.setAttackCooldown(200); // set to 200 by default
	}
	
	public ArrayList<GImage> displayInventory() {
		ArrayList<GImage> inventoryDisplay = new ArrayList<GImage>(); 
		for (int x = 0; x < inventory.size(); x++) { //add items based on player inventory
			inventoryDisplay.add(inventory.get(x).getSprite()); 
		}
		return inventoryDisplay;
	}
	
	public ArrayList<GImage> displayHealth() {
		ArrayList<GImage> playerHealth = new ArrayList<GImage>(); 
		for (int x = 0; x < getHealth(); x++) { //add hearts based on player health
			// if health > 10, place the next row of hearts right beneath original row of hearts
			playerHealth.add(new GImage("Heart.png", x % 10 * HEART_SIZE, 5 * (x / 10)));
		}
		return playerHealth;
	}
	
	public void printInventory() { //print player inventory
		if (inventory.size() > 0) {
			System.out.println("Items in player inventory: ");
			for (int x = 0; x < inventory.size(); x++) {
				System.out.println(inventory.get(x).getItemType());
			}
		}
		else {
			System.out.println("No items in inventory");
		}
	}
	
	public void removeFromInventory(int ind) {
		inventory.remove(ind);
	}
	
	public void addToInventory(Item item) {
		inventory.add(item);
	}
	
	public Item nearestItem(ArrayList<Item> items) {
		double lowestDistance = distanceToItem(items.get(0));
		Item i = items.get(0);
		for (int x = 0 ; x < items.size(); x++) {
			if (distanceToItem(items.get(x)) < lowestDistance) {
				lowestDistance = distanceToItem(items.get(x));
				i = items.get(x);
			}
		}	
		return i;
	}
	
	public double distanceToItem(Item i) {
		double x = Math.abs(i.getSprite().getX()- super.getSprite().getX()); // find difference in x coordinates
		double y = Math.abs(i.getSprite().getY() - super.getSprite().getY()); // find difference in y coordinates
		return Math.sqrt(x * x + y * y);
		
	}

	public Boolean canInteract(double x, double y) {
		double xDiff = Math.abs(x - super.getSprite().getX()); // find difference in x coordinates
		double yDiff = Math.abs(y - super.getSprite().getY()); // find difference in y coordinates
		return xDiff <= 50 && yDiff <= 50; //returns true if x,y coordinates are within 50 in x direction and y direction
	}
	
	public Boolean hasKey() {
		for (Item i: inventory) {
			if (i.getItemType() == "key") {
				return true;
			}
		}
		return false;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		// if default range for close range has not been set, set weapon range
		if (weapon.getItemType() == "close range weapon" && weapon.getRange() == 200) { 
			weapon.setRange(25);
		}
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void displayInventoryBox(GRect inventoryBox) {
		int offsetFromHealth = 15;
		if (inventory.size() > 0) {
			for (int x = 0; x < inventory.size(); x++) {
				Item i = inventory.get(x);
				i.getSprite().setLocation(HEART_SIZE * Math.min(getHealth(), 10) + offsetFromHealth + x % 10 * ITEM_SIZE, ITEM_SIZE * (x / 10));
			}
			int inventoryBoxNum = 1; // increase height of box
			if (inventory.size() % 10 == 0) {
				inventoryBoxNum = 0; // when inventory size divisible by 10, do not increase height of box
			}
			// set size of inventory box based on number of items in inventory
			inventoryBox.setSize(ITEM_SIZE * Math.min(inventory.size(), 10), ITEM_SIZE * (inventoryBoxNum + inventory.size() / 10));
			// set location of inventory box
			inventoryBox.setLocation(offsetFromHealth + HEART_SIZE * Math.min(getHealth(), 10), 0); 
			inventoryBox.setVisible(true); // show inventory box
			inventoryBox.setFillColor(Color.white);
			inventoryBox.setFilled(true);
		}
		else {
			inventoryBox.setVisible(false); // make inventory box invisible
		}
	}
	
	public int searchItemIndex(Player player, int removeIndex, String itemType) {
		for (int x = 0; x < player.getInventory().size(); x++) {
			if (player.getInventory().get(x).getItemType() == itemType) { //Check if itemType is in the inventory.
				removeIndex = x;
			}
		}
		return removeIndex;
	}
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}

	public boolean isDashAvailable() {
		return dashAvailable;
	}

	public void setDashAvailable(boolean dashAvailable) {
		this.dashAvailable = dashAvailable;
	}

	public boolean isAttackAvailable() {
		return attackAvailable;
	}

	public void setAttackAvailable(boolean attackAvailable) {
		this.attackAvailable = attackAvailable;
	}

	public boolean isBulletTraveling() {
		return bulletTraveling;
	}

	public void setBulletTraveling(boolean bulletTraveling) {
		this.bulletTraveling = bulletTraveling;
	}

	public int getBulletDistance() {
		return bulletDistance;
	}

	public void setBulletDistance(int bulletDistance) {
		this.bulletDistance = bulletDistance;
	}

	public GImage getBulletSprite() {
		return bulletSprite;
	}

	public void setBulletSprite(GImage bulletSprite) {
		this.bulletSprite = bulletSprite;
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}
	
	public GImage moveBullet(GImage bulletSpirte) {
		bulletSprite = getBulletSprite();
		setBulletDistance(getBulletDistance() + 1);
		bulletSprite.movePolar(1, getWeapon().getAngle());
		return bulletSprite;
	}
	
	public void playerInvincibility() {
		setInvincibilityCounter(getInvincibilityCounter() + 1); //player is invincible for a time.
		if (getInvincibilityCounter() > 10000) { //player is not invincible.
			setDamaged(false);
			setInvincibilityCounter(0); 
			System.out.println("player vulnerable");
		}
	}
	
	public void randomizeXLocation(double maxX, double y) { // randomizes x location
		getSprite().setLocation((maxX - 1.75 * getSprite().getWidth()) * Math.random(), y - 2.25 * getSprite().getHeight());
	}
	
	public void setAttackDisplayCount (int timerCount) {
		attackDisplayCount = timerCount;
	}
	
	public int getAttackDisplayCount () {
		return attackDisplayCount;
	}
	
	@Override
	public void move(double x, double y) {
		getSprite().move(x, y);
		weapon.getSprite().move(x,y);
	}
	
	@Override
	public void movePolar(double r, double theta) {
		getSprite().movePolar(r, theta);
		weapon.getSprite().movePolar(r, theta);
	}
	
	public static void main(String[] args) {
		
	}
}
