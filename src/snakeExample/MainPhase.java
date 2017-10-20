package snakeExample;

import engine.GameManager;
import engine.GamePhase;
import engine.Routine;

public class MainPhase extends GamePhase {

	public MainPhase() throws InstantiationException, IllegalAccessException {
		super();
	}

	@Override
	public Routine[] routines() {
		Routine[] r = {new LogicRoutine(), new RenderRoutine()};
		return r;
	}

	@Override
	public void onQuit() {
		System.out.println("avg. FPS: " + GameManager.averageFps());
		System.out.println("YOUR SCORE WAS: " + (LogicRoutine.getSnakeLength() - SnakeGame.START_LENGTH)*100);
	}

}
