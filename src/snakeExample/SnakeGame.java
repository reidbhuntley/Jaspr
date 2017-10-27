package snakeExample;

import java.util.concurrent.ExecutionException;

import engine.AssetManager;
import engine.EntitySystem;
import engine.GameManager;
import engine.GameWindow;

public class SnakeGame {
	
	public static final int GRID_WIDTH = 7, GRID_HEIGHT = 7;
	public static final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;
	public static final int START_LENGTH = 2;
	public static final int TICKS_PER_SECOND = 8, FRAMES_PER_TICK = 50;
	public static final int MAX_R = 102, MAX_G = 255, MAX_B = 51;
	public static final String SOUND_START = "start.wav", SOUND_APPLE = "correct.wav", SOUND_DEATH = "death.wav";
	
	public static void main(String[] args) throws IllegalAccessException, InterruptedException, ExecutionException {
		AssetManager.preload();
		EntitySystem testSys = new EntitySystem();
		testSys.setAsDefault();
		testSys.registerGlobalComponent(new SnakeLength(START_LENGTH));
		testSys.registerGlobalComponent(new HeadDist(0.0));
		GameManager game = new GameManager(TICKS_PER_SECOND * FRAMES_PER_TICK, new MainPhase());
		game.assignEntitySystem(testSys);
		game.assignGameWindow(new GameWindow(WINDOW_WIDTH/GRID_WIDTH*GRID_WIDTH, WINDOW_HEIGHT/GRID_HEIGHT*GRID_HEIGHT));
		game.start();
	}
	
}
