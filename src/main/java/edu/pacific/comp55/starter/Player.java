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
		// TODO Auto-generated constructor stub
		super(sprite, health);
	}

	public Boolean canInteract() {
		return true;
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
