package edu.pacific.comp55.starter;
import acm.graphics.GImage;

public class Character {
	private GImage sprite;
	private int health;
	
	public Character(GImage image, int hp) {
		sprite = image;
		health = hp;
	}

	public void healthChanged(int h) { //TODO implement health changed
		
	}
	
	public boolean healthIsZero() { //TODO implement health is zero
		return true;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int h) {
		health = h;
	}
	
	public void setSprite(GImage image) {
		sprite = image;
	}
	
	public GImage getSprite() {
		return sprite;
	}
	
	public boolean isHit() { //TODO implement is hit
		return true;
	}
	
	public static void main(String[] args) {

	}

}
