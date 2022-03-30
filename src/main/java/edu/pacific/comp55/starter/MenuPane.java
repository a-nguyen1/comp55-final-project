package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GObject;

public class MenuPane extends GraphicsPane {
	private MainApplication program; // you will use program to get access to
										// all of the GraphicsProgram calls
	private GButton play;
	private GButton chooseWeapon;
	private GButton selectLevel;

	public MenuPane(MainApplication app) {
		super();
		program = app;
		play = new GButton("Play", program.getWidth()/2, program.getHeight()/3, 200, 200);
		play.setFillColor(Color.RED);
		chooseWeapon = new GButton("Choose your weapon", program.getWidth()/2, program.getHeight()/2, 200, 200);
		chooseWeapon.setFillColor(Color.RED);
		selectLevel = new GButton("Select Level", program.getWidth()/2, (program.getHeight()*2)/3, 200, 200);
	}

	@Override
	public void showContents() {
		program.add(play);
	}

	@Override
	public void hideContents() {
		program.remove(play);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == play) {
			program.switchToSome();
		}
	}
}
