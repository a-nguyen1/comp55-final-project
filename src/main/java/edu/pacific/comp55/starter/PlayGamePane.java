package edu.pacific.comp55.starter;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;

public class PlayGamePane extends GraphicsPane {
	private MainApplication program; // you will use program to get access to
										// all of the GraphicsProgram calls
	private GImage longRangeWeapon;
	private GParagraph para;
	private GImage closeRangeWeapon;
	
	public PlayGamePane(MainApplication app) {
		this.program = app;
		longRangeWeapon = new GImage("bow.png", program.getWidth() * 2 / 3, 200);
		closeRangeWeapon = new GImage("sword.png", program.getWidth() / 3, 200);
		para = new GParagraph("Choose Your Weapon", program.getWidth() / 3, 100);
		para.setFont("Arial-24");
	}

	@Override
	public void showContents() {
		program.add(longRangeWeapon);
		program.add(closeRangeWeapon);
		program.add(para);
	}

	@Override
	public void hideContents() {
		program.remove(longRangeWeapon);
		program.remove(closeRangeWeapon);
		program.remove(para);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == longRangeWeapon) {
			chooseLevel();
		} else if (obj == closeRangeWeapon){
			chooseLevel();
		}
	}
	public void chooseLevel() {
		program.remove(longRangeWeapon);
		program.remove(closeRangeWeapon);
		program.remove(para);
		//TODO initialize character and level
	}
 }