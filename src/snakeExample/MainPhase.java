package snakeExample;

import engine.GamePhase;
import engine.Renderer;
import engine.Routine;

public class MainPhase extends GamePhase {
	
	private Renderer renderer;
	private Routine[] routines;

	public MainPhase() {
		super();
		renderer = new RenderRoutine();
		routines = new Routine[1];
		routines[0] = new LogicRoutine();
	}

	@Override
	public Routine[] routines() {
		return routines;
	}

	@Override
	public void onQuit() {
		//System.out.println("SCORE: " + (EntitySystem.readGlobalComponent(SnakeLength.class).length - SnakeGame.START_LENGTH)*100);
	}

	@Override
	public Renderer renderer() {
		return renderer;
	}

}
