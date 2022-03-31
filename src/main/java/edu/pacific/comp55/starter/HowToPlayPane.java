package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GObject;

public class HowToPlayPane extends GraphicsPane {
	private MainApplication program; // you will use program to get access to
									// all of the GraphicsProgram calls
	private GParagraph para;
	private GButton menu;
	private GButton play;
	
	public HowToPlayPane (MainApplication app) {
		this.program = app;
		para = new GParagraph("How To Play", 200, 200); //TODO change to show how to play
		menu = new GButton("Back to Menu", program.getWidth()/2.5, program.getHeight()/3, 100, 100);
		menu.setFillColor(Color.RED);
		play = new GButton("Play", program.getWidth()/2.5, (program.getHeight()/4)-80, 100, 100);
		play.setFillColor(Color.RED);
		
	}
	
	@Override
	public void showContents() {
		program.add(para);
		program.add(menu);
		program.add(play);
	}

	@Override
	public void hideContents() {
		program.remove(para);
		program.remove(menu);
		program.remove(play);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == play) {
			program.switchTo(0); // switch to playGame pane
		}
		else if (obj == menu) {
			program.switchToMenu(); // switch to menu pane
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
