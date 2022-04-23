package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;

public class GameEndPane extends GraphicsPane{
	private MainApplication program;
	private GButton restart;
	private GRect background;
	private GLabel message;
	
	public GameEndPane(MainApplication app) {
		super();
		program = app;
		
		background = new GRect(0, 0, program.getWidth(), program.getHeight());
		
		restart = new GButton("Restart", program.getWidth()/2 - 50, 400, 100, 100);
		restart.setFillColor(Color.GREEN);
	}

	@Override
	public void showContents() {
		if (program.isPlayerWin()) {
			message= new GLabel (" Y O U  W I N", 225, 300);
			message.setFont(new Font("Merriweather", Font.BOLD, 50));
			message.setColor(Color.GREEN);
			background.setFillColor(Color.BLUE);
			background.setFilled(true);
		}
		else {
			message= new GLabel (" G A M E  O V E R", 175, 300);
			message.setFont(new Font("Merriweather", Font.BOLD, 50));
			message.setColor(Color.RED);
			background.setFillColor(Color.BLACK);
			background.setFilled(true);
		}
		program.add(background); // add black background
		program.add(message); // add game over text
		program.add(restart);
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
