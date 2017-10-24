package snakeExample;

import engine.GamePhase;
import engine.Renderer;
import engine.Routine;

public class GameOverPhase extends GamePhase {

	public GameOverPhase() {
		super();
	}

	@Override
	public Routine[] routines() {
		Routine[] r = {new RestartRoutine()};
		return r;
	}

	@Override
	public void onQuit() { }

	@Override
	public Renderer renderer() {
		return null;
	}

}
