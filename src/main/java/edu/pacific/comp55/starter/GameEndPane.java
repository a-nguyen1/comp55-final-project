package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

public class GameEndPane extends GraphicsPane{
	private static final int BUTTON_SIZE = 100;
	private static final int FONT_SIZE = 50;
	private MainApplication program;
	private GButton restart;
	private GRect background;
	private GLabel message;
	
	public GameEndPane(MainApplication app) {
		super();
		program = app;
		
		background = new GRect(0, 0, program.getWidth(), program.getHeight());
		
		restart = new GButton("Restart", program.getWidth()/2 - 50, program.getHeight() * 2 / 3, BUTTON_SIZE, BUTTON_SIZE);
		restart.setFillColor(Color.GREEN);
	}

	@Override
	public void showContents() {
		if (program.isPlayerWin()) {
			message = new GLabel ("Congratulations! You escaped!", 25, program.getHeight() / 3);
			message.setColor(Color.GREEN);
			background.setFillColor(Color.BLUE);
			background.setFilled(true);
		}
		else {
			message= new GLabel (" G A M E  O V E R", 175, program.getHeight() / 3);
			message.setColor(Color.RED);
			background.setFillColor(Color.BLACK);
			background.setFilled(true);
		}
		message.setFont(new Font("Merriweather", Font.BOLD, FONT_SIZE));
		program.add(background); // add black background
		program.add(message); // add text
		program.add(restart); // add restart button
	}

	@Override
	public void hideContents() {
		program.remove(background);
		program.remove(message);
		program.remove(restart);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == restart) {
			program.switchToMenu();
		}
	}

}
