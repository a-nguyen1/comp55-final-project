package edu.pacific.comp55.starter;

import acm.graphics.GImage;

public class Enemy extends Character {
	private Weapon weapon;
	private boolean attackAvailable;
	private String enemyType;
	private int detectionRange;
	private boolean bulletTraveling;
	private int bulletDistance;
	private GImage bulletSprite;
	
	public Enemy(GImage image, int health, String enemyName) {
		super(image, health);
		setEnemyType(enemyName);
		setBulletTraveling(false);
		setBulletDistance(0);
		bulletSprite = new GImage("poisonBallSprite.png", getSprite().getX() + getSprite().getWidth() / 2, getSprite().getY() + getSprite().getHeight() / 2);
		bulletSprite.setVisible(false);
		attackAvailable = false;
		detectionRange = 100; // detection range is 100 by default
		super.setSpeed(5); // speed is 5 by default
	}
	
	public boolean canInteract(double x, double y) {
		double xDiff = Math.abs(x - super.getSprite().getX()); // find difference in x coordinates
		double yDiff = Math.abs(y - super.getSprite().getY()); // find difference in y coordinates
		return xDiff <= detectionRange && yDiff <= detectionRange; //returns true if x,y coordinates are within 100 in x direction and y direction
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public boolean isAttackAvailable() {
		return attackAvailable;
	}

	public void setAttackAvailable(boolean attackAvailable) {
		this.attackAvailable = attackAvailable;
	}
	
	public String getEnemyType() {
		return enemyType;
	}

	public void setEnemyType(String enemyType) {
		this.enemyType = enemyType;
	}

	public int getDetectionRange() {
		return detectionRange;
	}

	public void setDetectionRange(int detectionRange) {
		this.detectionRange = detectionRange;
	}
	
	public GImage moveBullet(GImage bulletSprite) {
		setBulletDistance(getBulletDistance() + 1);
		bulletSprite.movePolar(1, getWeapon().getAngle()); // move towards mouse click
		return bulletSprite;
	}
	
	public void enemyInvincibility() {
			setInvincibilityCounter(getInvincibilityCounter() + 1); //enemy is invincible for a time.
			if (getInvincibilityCounter() > 100) { //enemy is not invincible.
				setDamaged(false);
				setInvincibilityCounter(0); 
			}
			System.out.println("invincibility counter: " + getInvincibilityCounter());
	}

	@Override
	public void move(double x, double y) {
		getSprite().move(x, y);
		weapon.getSprite().move(x,y);
	}
	
	@Override
	public void movePolar(double r, double theta) {
		getSprite().movePolar(r, theta);
		weapon.getSprite().movePolar(r, theta);
	}

	public static void main(String[] args) {

	}

	public boolean isBulletTraveling() {
		return bulletTraveling;
	}

	public void setBulletTraveling(boolean bulletTraveling) {
		this.bulletTraveling = bulletTraveling;
	}

	public int getBulletDistance() {
		return bulletDistance;
	}

	public void setBulletDistance(int bulletDistance) {
		this.bulletDistance = bulletDistance;
	}
	
	public void setBulletSprite(GImage bullet) {
		bulletSprite = bullet;
	}
	
	public GImage getBulletSprite() {
		return bulletSprite;
	}
}
