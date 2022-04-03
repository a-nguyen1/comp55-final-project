package edu.pacific.comp55.starter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for arraylist

import acm.graphics.GImage;
import acm.program.GraphicsProgram;

public class Player extends Character {
	private Weapon weapon;
	private ArrayList<Item> inventory;
	
	public Player(GImage sprite, int health) {
		super(sprite, health);
	}

	public Boolean canInteract(double x, double y) {
		double xDiff = Math.abs(x - super.getSprite().getX()); // find difference in x coordinates
		double yDiff = Math.abs(y - super.getSprite().getY()); // find difference in y coordinates
		return xDiff <= 50 && yDiff <= 50; //returns true if x,y coordinates are within 50 in x direction and y direction
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
