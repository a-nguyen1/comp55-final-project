package edu.pacific.comp55.starter;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for arraylist

import acm.program.GraphicsProgram;

public class Player extends Character {
	private Weapon weapon;
	private ArrayList<Item> inventory;
	
	public Player() {
		// TODO Auto-generated constructor stub
	}

	public Boolean canInteract() {
		return true;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public void getWeapon() {
		return weapon;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void keyTyped(MouseEvent e) {
		
	}
}
