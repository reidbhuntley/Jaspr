package snakeExample;

import java.util.concurrent.ExecutionException;

import engine.AssetManager;
import engine.EntitySystem;
import engine.GameManager;
import engine.GameWindow;

public class SnakeGame {
	
	public static final int GRID_WIDTH = 6, GRID_HEIGHT = 6;
	public static final int WINDOW_WIDTH_TEMP = 1000, WINDOW_HEIGHT_TEMP = 1000;
	public static final int WINDOW_WIDTH = WINDOW_WIDTH_TEMP/GRID_WIDTH*GRID_WIDTH, WINDOW_HEIGHT = WINDOW_HEIGHT_TEMP/GRID_HEIGHT*GRID_HEIGHT;
	public static final int START_LENGTH = 2;
	public static final int TICKS_PER_SECOND = 7, FRAMES_PER_TICK = 50;
	public static final int MAX_R = 66, MAX_G = 155, MAX_B = 244;
	public static final String SOUND_START = "start.wav", SOUND_APPLE = "correct.wav", SOUND_DEATH = "death.wav", SOUND_MUSIC = "music.wav";
	
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
