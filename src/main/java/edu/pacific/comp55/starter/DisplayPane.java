package edu.pacific.comp55.starter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // for ArrayList
import java.util.HashMap; // for HashMap

import acm.graphics.GImage; // for GImage
import acm.graphics.GPoint; // for GPoint

public class DisplayPane extends GraphicsPane {
	private MainApplication program;
	
	private GImage background;
	private ArrayList<GImage> playerHealth;
	private ArrayList<GImage> bossHealth;
	private String displayType; // to display current game state (lose/win/playing)
	private ArrayList<Level> levels;
	private int currentLevel;
	private HashMap<Integer, GPoint> map;
	
	private Player player;
	
	public DisplayPane(MainApplication app) {
		super();
		program = app;
		//background = new GImage("bow.png", program.getWidth() * 2 / 3, 200);
		GImage playerSprite = new GImage ("Player-Sprite.png", program.getWidth()/2, program.getHeight()/2);
		player = new Player(playerSprite, 5);
		map = new HashMap<Integer, GPoint>();
		map.put(87, new GPoint(0, -5)); //w
		map.put(65, new GPoint(-5, 0)); //a
		map.put(83, new GPoint(0, 5)); //s
		map.put(68, new GPoint(5, 0)); //d
	}

	public void setBackground(String b) { //TODO set background
		//background = newGImage(b, program.getWidth() / 2, program.getHeight() / 2);
	}
	
	public void createLevel(int l) { //TODO create level
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showContents() {
		//program.add(background);
		program.add(player.getSprite());
	}

	@Override
	public void hideContents() {
		program.remove(player.getSprite());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { //TODO implement attack

	}	
	
	@Override
	public void keyPressed(KeyEvent e) {
		GImage playerSprite = player.getSprite();
		GPoint p = map.get(e.getKeyCode());
		if (p != null) {
			playerSprite.move(p.getX(), p.getY());
		} else if (e.getKeyCode() == 'e') { //TODO implement item interaction
			
		}
		
		// setting bounds for player
		if (playerSprite.getLocation().getX() < 0) {
			playerSprite.setLocation(0,playerSprite.getY());
		} else if (playerSprite.getLocation().getY() < 0) {
			playerSprite.setLocation(playerSprite.getX(),0);
		} else if (playerSprite.getLocation().getX()+ playerSprite.getWidth() > program.getWidth()) {
			playerSprite.setLocation(program.getWidth()-playerSprite.getWidth(),playerSprite.getY());
		} else if (playerSprite.getLocation().getY()+ playerSprite.getHeight() * 2 > program.getHeight()) {
			playerSprite.setLocation(playerSprite.getX(), program.getHeight()-playerSprite.getHeight() * 2);
		} 
		

		}
	}
