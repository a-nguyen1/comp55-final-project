package edu.pacific.comp55.starter;
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

}
