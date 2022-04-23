package edu.pacific.comp55.starter;

import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GLabel;

public class Boss extends Enemy {
	private static final int HEART_SIZE = 50;
	private Weapon weaponUpgrade;
	
	public Boss(GImage image, int hp, String bossName) {
		super(image, hp, bossName);
	}

	public Weapon death() {
		return weaponUpgrade;
	}
	
	public ArrayList<GImage> displayHealth(int yOffset) {
		ArrayList<GImage> bossHealth = new ArrayList<GImage>(); 
		for (int x = 0; x < getHealth(); x++) { //add hearts based on boss health
			bossHealth.add(new GImage("Heart.png", 725, x * HEART_SIZE + yOffset)); 
		}
		return bossHealth;
	}

	public static void main(String[] args) {

	}
}
