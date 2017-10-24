package snakeExample;

import java.util.concurrent.ExecutionException;

import engine.EntitySystem;
import engine.GameManager;

public class SnakeGame {
	
	public static final int GRID_WIDTH = 7, GRID_HEIGHT = 7;
	public static final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;
	public static final int START_LENGTH = 2;
	public static final int TICKS_PER_SECOND = 8, FRAMES_PER_TICK = 50;
	public static final int MAX_R = 50, MAX_G = 200, MAX_B = 150;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InterruptedException, ExecutionException {
		EntitySystem testSys = new EntitySystem();
		testSys.setAsDefault();
		testSys.registerGlobalComponent(new SnakeLength(START_LENGTH));
		testSys.registerGlobalComponent(new HeadDist(0.0));
		GameManager game = new GameManager(TICKS_PER_SECOND * FRAMES_PER_TICK, new MainPhase());
		game.addEntitySystem(testSys);
		game.start();
	}
	
}
