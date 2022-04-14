package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class Enemy extends Character {
	private Weapon weapon;
	private boolean attackAvailable;
	public Enemy(GImage image, int hp) {
		super(image, hp);
	}
	
	public static boolean playerInRange() {
		return true;
	}
	
	public boolean canInteract(double x, double y) {
		double xDiff = Math.abs(x - super.getSprite().getX()); // find difference in x coordinates
		double yDiff = Math.abs(y - super.getSprite().getY()); // find difference in y coordinates
		return xDiff <= 100 && yDiff <= 100; //returns true if x,y coordinates are within 100 in x direction and y direction
	}
	
	public boolean overlapping(double x, double y, double width, double height) {
		double xDiff = Math.abs(x - super.getSprite().getX()); // find difference in x coordinates
		double yDiff = Math.abs(y - super.getSprite().getY()); // find difference in y coordinates
		return xDiff <= width && yDiff <= height; //returns true if x,y coordinates are within 100 in x direction and y direction
	} 
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
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
