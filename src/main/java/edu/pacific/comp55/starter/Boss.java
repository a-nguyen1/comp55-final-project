package edu.pacific.comp55.starter;

import java.util.ArrayList;

import acm.graphics.GImage;

public class Boss extends Enemy {
	private Weapon weaponDrop;
	
	public Boss(GImage image, int hp, String bossName) {
		super(image, hp, bossName);
	}

	public Weapon death() {
		return weaponDrop;
	}
	
	public ArrayList<GImage> displayHealth() {
		ArrayList<GImage> bossHealth = new ArrayList<GImage>(); 
		for (int x = 0; x < getHealth(); x++) { //add hearts based on player health
			bossHealth.add(new GImage("Heart.png", 725, x*50 + 50)); 
		}
		return bossHealth;
	}

	public static void main(String[] args) {

	}
}
