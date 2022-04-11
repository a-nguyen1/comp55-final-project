package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class Weapon extends Item {
	private int range;
	
	public Weapon(GImage image, String name) {
		super(image, name);
	}
	
	public void setRange(int r) {
		range = r;
		
	}
	
	public int getRange() {
		return range;
	}

	public static void main(String[] args) {

	}

}
