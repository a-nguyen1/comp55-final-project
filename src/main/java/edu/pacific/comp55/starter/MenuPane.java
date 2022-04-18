package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GObject;

public class MenuPane extends GraphicsPane {
	private MainApplication program; // you will use program to get access to
										// all of the GraphicsProgram calls
	private GButton play;
	private GButton music;
	private GButton howToPlay;
	private String musicButtonText;

	public MenuPane(MainApplication app) {
		super();
		program = app;
		
		play = new GButton("Play", program.getWidth()/2.5, (program.getHeight()/4)-80, 100, 100);
		play.setFillColor(Color.GREEN);
		
		howToPlay = new GButton("How to Play", program.getWidth()/2.5, program.getHeight()/3, 100, 100);
		howToPlay.setFillColor(Color.GREEN);
		
		musicButtonText = "Audio ON";
		music = new GButton(musicButtonText, program.getWidth()/2.5, ((program.getHeight()*2)/3)-70, 100, 100);
		music.setFillColor(Color.GREEN);
	}

	@Override
	public void showContents() {
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
			program.switchTo(0); // switch to playGame pane
		}
		else if (obj == howToPlay) {
			program.switchTo(1); // switch to howToPlay pane
		}
		else if (obj == music){
			if (musicButtonText == "Audio ON") { // toggle audio button from ON to OFF
				program.remove(music);
				musicButtonText = "Audio OFF";
				music = new GButton(musicButtonText, program.getWidth()/2.5, ((program.getHeight()*2)/3)-70, 100, 100);
				music.setFillColor(Color.RED);
				program.add(music);
				program.setAudioOn(false);
			}
			else { // toggle audio button from OFF to ON
				program.remove(music);
				musicButtonText = "Audio ON";
				music = new GButton(musicButtonText, program.getWidth()/2.5, ((program.getHeight()*2)/3)-70, 100, 100);
				music.setFillColor(Color.GREEN);
				program.add(music);
				program.setAudioOn(true);
			}
		}
	}
}
