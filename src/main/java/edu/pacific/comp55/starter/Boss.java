package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class Boss extends Enemy {
	private Weapon weaponDrop;
	
	public Boss(GImage image, int hp) {
		super(image, hp);
	}

	//TODO: Make Weapon Class.
	public Weapon death() {
		return weaponDrop;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
