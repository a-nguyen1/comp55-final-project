package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class Enemy extends Character {
	
	public Enemy(GImage image, int hp) {
		super(image, hp);
	}
	
	public static boolean playerInRange() {
		return true;
	}
	
	public Boolean canInteract(double x, double y) {
		double xDiff = Math.abs(x - super.getSprite().getX()); // find difference in x coordinates
		double yDiff = Math.abs(y - super.getSprite().getY()); // find difference in y coordinates
		return xDiff <= 100 && yDiff <= 100; //returns true if x,y coordinates are within 50 in x direction and y direction
	}
	
	public static void main(String[] args) {

	}

}
