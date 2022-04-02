package edu.pacific.comp55.starter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for arraylist
import javax.swing.*; // for timer
import acm.graphics.GImage; // for GImage

public class DisplayPane extends GraphicsPane {
	private MainApplication program;
	
	private GImage background;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> bossHealth;
	private String displayType; // to display current game state (lose/win/playing)
	private ArrayList<Level> levels;
	private int currentLevel;
	
	private Player player;
	private GImage playerSprite;
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		//background = new GImage("bow.png", program.getWidth() * 2 / 3, 200);
		playerSprite = new GImage ("Player-Sprite.png", program.getWidth()/2, program.getHeight()/2);
		
		player = new Player(playerSprite, 5);
		
	}

	public void setBackground(String b) { //TODO set background
		
	}
	
	public void createLevel(int l) { //TODO create level
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showContents() {
		//program.add(background);
		//program.add(player.getSprite());
		program.add(playerSprite);
	}

	@Override
	public void hideContents() {
		
	}
	@Override

	public void mouseClicked(MouseEvent e) {
		System.out.println("mouse clicked");

		}	
	
	public void keyPressed(KeyEvent e) {
		char C = e.getKeyChar();
		if (C == 'w') {
			playerSprite.move(0, -5);

		} else if (C == 'a') {
			playerSprite.move(-5,0);
		} else if (C== 's') {
			playerSprite.move(0, 5);
		} else if (C=='d' ) {
			playerSprite.move(5,0);
		}
		
		// setting bounds for player
		
		if (playerSprite.getLocation().getX() < 0) {
			playerSprite.setLocation(0,playerSprite.getY());
		} else if (playerSprite.getLocation().getY() < 0) {
			playerSprite.setLocation(playerSprite.getX(),0);
		} else if (playerSprite.getLocation().getX()+ playerSprite.getWidth() > program.getWidth()) {
			playerSprite.setLocation(program.getWidth()-playerSprite.getWidth(),playerSprite.getY());
		} else if (playerSprite.getLocation().getY()+ playerSprite.getHeight() * 2 > program.getHeight()) {
			playerSprite.setLocation(playerSprite.getX() ,program.getHeight()-playerSprite.getHeight() * 2);
		} 
		

		}
	}
