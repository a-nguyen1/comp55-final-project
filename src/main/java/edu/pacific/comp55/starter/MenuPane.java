package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import acm.graphics.GLabel;
import acm.graphics.GObject;

public class MenuPane extends GraphicsPane {
	
	private static final int BUTTON_SIZE = 100;
	private static final int FONT_SIZE = 50;
	
	private MainApplication program; // you will use program to get access to
										// all of the GraphicsProgram calls
	private GLabel game;
	private GButton play;
	private GButton music;
	private GButton howToPlay;
	private String musicButtonText;

	public MenuPane(MainApplication app) {
		super();
		program = app;
		
		game = new GLabel("Escape the Dungeon");
		game.setFont(new Font("Merriweather", Font.BOLD, FONT_SIZE));
		game.setColor(Color.RED);
		game.setLocation(program.getWidth() / 2 - game.getWidth() / 2, program.getHeight() / 5);
		
		double xCenter = program.getWidth() / 2 - BUTTON_SIZE / 2;
		
		play = new GButton("Play", xCenter, program.getHeight() * 2 / 5 - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE);
		play.setFillColor(Color.GREEN);
		
		howToPlay = new GButton("How to Play", xCenter, program.getHeight() * 3 / 5 - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE);
		howToPlay.setFillColor(Color.GREEN);
		
		musicButtonText = "Audio ON";
		music = new GButton(musicButtonText, xCenter, program.getHeight() * 4 / 5 - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE);
		music.setFillColor(Color.GREEN);
	}

	@Override
	public void showContents() {
		program.add(game);
		program.add(play);
		program.add(howToPlay);
		program.add(music);
	}

	@Override
	public void hideContents() {
		program.remove(play);
		program.remove(howToPlay);
		program.remove(music);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == play) {
			program.remove(game);
			program.switchTo(0); // switch to playGame pane
		}
		else if (obj == howToPlay) {
			program.remove(game);
			program.switchTo(1); // switch to howToPlay pane
		}
		else if (obj == music){
			program.remove(music);
			if (program.isAudioOn()) { // toggle audio button from ON to OFF
				music = new GButton("Audio OFF", music.getX(), music.getY(), music.getWidth(), music.getHeight());
				music.setFillColor(Color.RED);
			}
			else { // toggle audio button from OFF to ON
				music = new GButton("Audio ON", music.getX(), music.getY(), music.getWidth(), music.getHeight());
				music.setFillColor(Color.GREEN);
			}
			program.setAudioOn(!program.isAudioOn()); // toggle audio
			program.add(music);
		}
	}
}
