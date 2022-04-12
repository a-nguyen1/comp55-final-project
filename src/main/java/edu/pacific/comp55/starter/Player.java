package edu.pacific.comp55.starter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for arraylist

import acm.graphics.GImage;
import acm.program.GraphicsProgram;

public class Player extends Character {
	private Weapon weapon;
	private ArrayList<Item> inventory;
	private boolean dashAvailable;
	private boolean attackAvailable;
	
	public Player(GImage sprite, int health) {
		super(sprite, health);
		inventory = new ArrayList<Item>();
		dashAvailable = true;
		attackAvailable = true;
	}
	
	public void printInventory() {
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
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void displayInventory() {
		
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

	public static void main(String[] args) {
		
	}
}
