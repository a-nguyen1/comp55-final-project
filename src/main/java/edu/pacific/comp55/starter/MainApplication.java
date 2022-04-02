package edu.pacific.comp55.starter;
public class MainApplication extends GraphicsApplication {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final String MUSIC_FOLDER = "sounds";
	private static final String[] SOUND_FILES = { "r2d2.mp3", "somethinlikethis.mp3" };

	private HowToPlayPane howToPlay;
	private PlayGamePane playGame;
	private MenuPane menu;
	private DisplayPane display;
	private int count;

	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void run() {
		howToPlay = new HowToPlayPane(this);
		playGame = new PlayGamePane(this);
		menu = new MenuPane(this);
		display = new DisplayPane(this);
		setupInteractions();
		switchToMenu();
	}

	public void switchToMenu() {
		count++;
		switchToScreen(menu);
	}

	public void switchTo(int n) {
		if (n == 0) {
			switchToScreen(playGame);
		}
		else if (n == 1){ // n == 1
			switchToScreen(howToPlay);
		}
		else if (n == 2) {
			switchToScreen(display);
		}
	}

	private void playRandomSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, SOUND_FILES[count % SOUND_FILES.length]);
	}
	
	public static void main(String[] args) {
		new MainApplication().start();
	}
}
