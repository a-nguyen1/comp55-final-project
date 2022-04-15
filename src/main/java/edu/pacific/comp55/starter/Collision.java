package edu.pacific.comp55.starter;

import acm.graphics.GRectangle;

public class Collision {

	public Collision() {
	}
	
	public static boolean check(GRectangle obj, GRectangle obj2) { //TODO return true if GRects are colliding, false otherwise
		double xDiff = obj.getX() - obj2.getX();
		double yDiff = obj.getY() - obj2.getY();
		if (xDiff > 0) {
			if (obj2.getWidth() >= xDiff) {
				if (yDiff > 0) {
					if (obj2.getHeight() >= yDiff) {
						return true;
					}
				}
				else { // yDiff <= 0
					if (obj.getHeight() >= Math.abs(yDiff)) {
						return true;
					}
				}
			}
		}
		else { // xDiff <= 0
			if (obj.getWidth() >= Math.abs(xDiff)) {
				if (yDiff > 0) {
					if (obj2.getHeight() >= yDiff) {
						return true;
					}
				}
				else { // yDiff <= 0
					if (obj.getHeight() >= Math.abs(yDiff)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
	}

}
