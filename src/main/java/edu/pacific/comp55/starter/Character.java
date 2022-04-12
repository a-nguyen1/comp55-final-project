package edu.pacific.comp55.starter;
import acm.graphics.GImage;

public class Character {
	private GImage sprite;
	private int health;
	private double moveX;
	private double moveY;
	private double speed;
	private boolean isDamaged;
	private int invincibilityCounter;
	
	public Character(GImage image, int hp) {
		sprite = image;
		health = hp;
		isDamaged = false;
	}

	public void changeHealth(int h) { 
		health = health + h; //Either add health or remove it depending if h is positive or negative.
	}
	
	public boolean isDead() { 
		return health <= 0;
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

	public boolean isDamaged() {
		return isDamaged;
	}

	public void setDamaged(boolean isDamaged) {
		this.isDamaged = isDamaged;
	}

	public int getInvincibilityCounter() {
		return invincibilityCounter;
	}

	public void setInvincibilityCounter(int invincibilityCounter) {
		this.invincibilityCounter = invincibilityCounter;
	}
}
