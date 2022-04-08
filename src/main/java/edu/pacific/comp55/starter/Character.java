package edu.pacific.comp55.starter;
import acm.graphics.GImage;

public class Character {
	private GImage sprite;
	private int health;
	private double moveX;
	private double moveY;
	private double speed;
	
	public Character(GImage image, int hp) {
		sprite = image;
		health = hp;
	}

	public void healthChanged(int h) { //TODO implement health changed
		health = health + h; //Either add health or remove it depending if h is pos or neg.
	}
	
	public boolean healthIsZero() { //TODO implement health is zero
		if (health == 0) {
			return true;
		}
		else {
			return false;
		}
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

	public double getMoveX() {
		return moveX;
	}

	public void setMoveX(double moveX) {
		this.moveX = moveX;
	}

	public double getMoveY() {
		return moveY;
	}

	public void setMoveY(double moveY) {
		this.moveY = moveY;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public static void main(String[] args) {

	}
}
