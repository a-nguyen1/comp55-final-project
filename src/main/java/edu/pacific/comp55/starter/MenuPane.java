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
	private GButton howToPlay;

	public MenuPane(MainApplication app) {
		super();
		program = app;
		play = new GButton("Play", program.getWidth()/2.5, (program.getHeight()/4)-120, 100, 100);
		play.setFillColor(Color.RED);
		chooseWeapon = new GButton("Choose your weapon", program.getWidth()/2.5, ((program.getHeight()/3))-20, 100, 100);
		chooseWeapon.setFillColor(Color.RED);
		selectLevel = new GButton("Select Level", program.getWidth()/2.5, ((program.getHeight()*2)/3)-70, 100, 100);
		selectLevel.setFillColor(Color.RED);
		howToPlay = new GButton("How to Play", program.getWidth()/2.5, (program.getHeight()*2)/3, 100, 100);
	}

	@Override
	public void showContents() {
		program.add(play);
		program.add(chooseWeapon);
		program.add(selectLevel);
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
