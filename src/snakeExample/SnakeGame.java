package snakeExample;

import java.util.concurrent.ExecutionException;

import engine.EntitySystem;
import engine.GameManager;

public class SnakeGame {
	
	public static final int GRID_WIDTH = 9, GRID_HEIGHT = 9;
	public static final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;
	public static final int START_X = 2, START_Y = 7, START_LENGTH = 3;
	public static final int TARGET_FPS = 8;
	public static final int MAX_R = 50, MAX_G = 190, MAX_B = 100;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InterruptedException, ExecutionException {
		EntitySystem testSys = new EntitySystem();
		testSys.setAsDefault();
		GameManager.setTargetFps(TARGET_FPS);
		GameManager.setPhase(new MainPhase());
		while(true)
			GameManager.processPhase();
	}
	
}
