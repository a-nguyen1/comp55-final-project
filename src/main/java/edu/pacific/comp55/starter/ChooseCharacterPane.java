package edu.pacific.comp55.starter;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;

public class ChooseCharacterPane extends GraphicsPane {
	private MainApplication program; // you will use program to get access to
										// all of the GraphicsProgram calls
	private GImage longRangeCharacter;
	private GImage closeRangeCharacter;
	private GLabel chooseCharacterLabel;
	
	public ChooseCharacterPane(MainApplication app) {
		this.program = app;
		longRangeCharacter = new GImage(ImageFolder.get() + "PlayerWizardSprite.png", program.getWidth() * 2 / 3, 200);
		closeRangeCharacter = new GImage(ImageFolder.get() + "PlayerKnightSprite.png", program.getWidth() / 3, 200);
		chooseCharacterLabel = new GLabel("Choose Your Character", program.getWidth() / 3, 100);
		chooseCharacterLabel.setFont("Arial-24");
	}

	@Override
	public void showContents() {
		program.add(longRangeCharacter);
		program.add(closeRangeCharacter);
		program.add(chooseCharacterLabel);
	}

	@Override
	public void hideContents() {
		program.remove(longRangeCharacter);
		program.remove(closeRangeCharacter);
		program.remove(chooseCharacterLabel);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (obj == longRangeCharacter) {
			program.setCloseRangeCharacter(false);
			startGame();
		} else if (obj == closeRangeCharacter){
			program.setCloseRangeCharacter(true);
			startGame();
		}
	}
	public void startGame() {
		program.remove(chooseCharacterLabel);
		program.switchTo(2); //Switch to DisplayPane.
	}
 }
