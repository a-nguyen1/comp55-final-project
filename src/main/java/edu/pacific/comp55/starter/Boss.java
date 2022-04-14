package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class Boss extends Enemy {
	private Weapon weaponDrop;
	
	public Boss(GImage image, int hp, String bossName) {
		super(image, hp, bossName);
	}

	public Weapon death() {
		return weaponDrop;
	}

	public static void main(String[] args) {

	}
}
