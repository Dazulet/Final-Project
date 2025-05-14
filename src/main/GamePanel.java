package src.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

import src.entity.Player;
import src.screens.EndScreen;
import src.screens.LevelScreen;
import src.screens.StartScreen;
import src.tile.TileManager;
import src.main.ItemManager;
import src.main.MonstersManager;
import src.main.MinesManager;

public class GamePanel extends JPanel implements Runnable{

	final int originalTileSize = 16;
	final int scale = 3;
	public final int tileSize = originalTileSize * scale;

	public final int maxScreenCol = 19;
	public final int maxScreenRow = 16;
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;

	int screenWidthActual;
	int screenHeightActual;

	public int panelState;
	public final int START = 0;
	public final int GAME = 1;
	public final int LEVEL = 2;
	public final int END = 3;
	public final int FPS = 60;
	private boolean sound_off = false;

	public KeyHandler keyH = new KeyHandler(this);
	public UI ui = new UI(this);
	public StartScreen ss = new StartScreen(this);
	public EndScreen es = new EndScreen(this);
	public LevelScreen ls = new LevelScreen(this);
	public TileManager tileM;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public BattleManager battleM = new BattleManager(this);
	public MonstersManager monstersM;
	public MinesManager minesM;
	public ItemManager itemM;
	public Sound sound = new Sound();
	Thread gameThread;

	public Player player = new Player(this);
	public int currentLevel = 1;
	public final int MAX_LEVELS = 3;
	private String transitionMessage = null;
	private int transitionMessageTimer = 0;
	private List<String> mapFiles;
	private Random randomMapSelector;
	private String currentMapPath;

	public GamePanel() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidthActual = screenSize.width;
		screenHeightActual = screenSize.height;

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);

		mapFiles = new ArrayList<>();
		mapFiles.add("/res/maps/map.txt");
		mapFiles.add("/res/maps/map2.txt");
		mapFiles.add("/res/maps/map3.txt");

		randomMapSelector = new Random();

		if (!mapFiles.isEmpty()) {
			setupNewGameComponents(mapFiles.get(0));
		} else {
			System.err.println("CRITICAL ERROR: Map files list is empty at GamePanel construction!");

		}
	}

	private void setupNewGameComponents(String mapPath) {
		this.currentMapPath = mapPath;
		this.tileM = new TileManager(this, currentMapPath);
		this.monstersM = new MonstersManager(this);
		this.minesM = new MinesManager(this);
		this.itemM = new ItemManager(this);

		if (this.player != null) {

		} else {

			this.player = new Player(this);
			System.err.println("Warning: Player was null during setupNewGameComponents. Recreated.");
		}
	}

	public List<String> getMapFiles() {
		return mapFiles;
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		currentLevel = 1;

		if (!mapFiles.isEmpty()) {
			setupNewGameComponents(mapFiles.get(0));
		} else {
			System.err.println("CRITICAL ERROR: Map files list is empty! Cannot start game.");
			return;
		}

		if (player != null) {
			player.setDefaultValues();
		} else {
			player = new Player(this);
			player.setDefaultValues();
		}

		panelState = START;
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1_000_000_000.0 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
			if (timer >= 1_000_000_000) {
				timer = 0;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		System.exit(0);
	}

	public void update() {
		if (transitionMessageTimer > 0) {
			transitionMessageTimer--;
			if (transitionMessageTimer == 0) {
				transitionMessage = null;
			}
		}
		if(panelState == GAME) {
			if (player != null) player.update();
			if (battleM != null) battleM.update();
			if (itemM != null) itemM.update();
			if (minesM != null) {  }
			if (monstersM != null) {  }
		}
		else if (panelState == START) {

		}
		else if (panelState == LEVEL) {

		}
		else if (panelState == END) {
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int currentWidth = getWidth();
		int currentHeight = getHeight();
		if (currentWidth <= 0 || currentHeight <= 0) {
			currentWidth = screenWidthActual;
			currentHeight = screenHeightActual;
		}
		Graphics2D g2 = (Graphics2D)g.create();
		if (currentWidth > 0 && currentHeight > 0 && screenWidth > 0 && screenHeight > 0) {
			double scaleX = (double) currentWidth / screenWidth;
			double scaleY = (double) currentHeight / screenHeight;

			g2.scale(scaleX, scaleY);
		} else {
			System.err.println("Warning: Invalid dimensions for scaling in paintComponent.");
		}
		if(panelState == START) {
			if (ss != null) ss.draw(g2);
		}
		else if(panelState == GAME) {

			if (tileM != null) tileM.draw(g2);
			if (itemM != null) itemM.draw(g2);
			if (monstersM != null) monstersM.draw(g2);
			if (player != null) player.draw(g2);
			if (minesM != null) minesM.explode(g2);
			if (ui != null) ui.draw(g2);
		}
		else if(panelState == LEVEL) {
			if (ls != null) ls.draw(g2);
		}
		else if(panelState == END) {
			if (es != null) es.draw(g2);
		}

		if (transitionMessage != null && ui != null) {
			ui.drawTransitionMessage(g2, transitionMessage);
		}

		g2.dispose();
	}

	public void playMusic(int i) { if(sound != null) {sound.setFile(i); sound.play(); sound.loop();} }
	public void stopMusic() { if(sound != null) sound.stop(); }
	public void playSE(int i) { if(sound_off || sound == null) return; sound.setFile(i); sound.play(); }
	public void toggle_sound() { sound_off = !sound_off; }

	public void prepareNextLevel() {
		currentLevel++;
		if (currentLevel > MAX_LEVELS) {

			panelState = END;
			if (es != null && player != null) es.setTitleTexts("CONGRATULATIONS!\nYou Beat All Levels!\nTotal Steps: " + player.steps);
		} else {

			showTransitionMessage("Level " + currentLevel, 120);

			String nextMapPath = currentMapPath;
			List<String> availableMaps = getMapFiles();
			if (availableMaps != null && !availableMaps.isEmpty()) {
				if (currentLevel -1 < availableMaps.size()) {
					nextMapPath = availableMaps.get(currentLevel - 1);
				} else {

					nextMapPath = availableMaps.get(availableMaps.size() - 1);
					System.out.println("Warning: currentLevel (" + currentLevel + ") exceeds map list size ("+ availableMaps.size() +"). Using last map: " + nextMapPath);
				}
			} else { System.err.println("Map files list is null or empty in prepareNextLevel!"); }

			setupNewGameComponents(nextMapPath);

			if (player != null) {
				player.resetForNewLevel();
			}

			if (battleM != null && player != null) {
				player.gameState = player.MOVE;
				player.movingState = 0;
				battleM.rolling = false;
				battleM.diceCounter = 0;
				battleM.battleState = 0;
				battleM.monsterIndex = -1;
				battleM.clearMessages();
			}

			panelState = GAME;
		}
	}

	public void handleZorkDefeat(int defeatedZorkIndex) {
		if (player == null || monstersM == null || battleM == null) return;

		if (defeatedZorkIndex >= 0 && defeatedZorkIndex < monstersM.monsters.length && monstersM.monsters[defeatedZorkIndex] != null) {
			player.gainExperience(monstersM.monsters[defeatedZorkIndex].experienceDropped);
			monstersM.monsters[defeatedZorkIndex] = null;

		}

		player.gameState = player.MOVE;
		player.movingState = 0;

		battleM.rolling = false;
		battleM.diceCounter = 0;
		battleM.battleState = 0;
		battleM.monsterIndex = -1;
		battleM.clearMessages();

		prepareNextLevel();
	}

	public void showTransitionMessage(String message, int durationFrames) {
		this.transitionMessage = message;
		this.transitionMessageTimer = durationFrames;

	}

	void restart() {
		currentLevel = 1;
		List<String> availableMaps = getMapFiles();
		if (availableMaps != null && !availableMaps.isEmpty()) {

			setupNewGameComponents(availableMaps.get(0));
		} else {
			System.err.println("CRITICAL ERROR: Map files list is null or empty! Cannot restart properly.");
			return;
		}

		if (player == null) player = new Player(this);
		player.setDefaultValues();

		battleM = new BattleManager(this);
		cChecker = new CollisionChecker(this);

		player.gameState = player.MOVE;
		player.movingState = 0;

		showTransitionMessage("Level " + currentLevel, 120);
		panelState = GAME;
	}

	public String getCurrentMapPath() { return currentMapPath; }
}