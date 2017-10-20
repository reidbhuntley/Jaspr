package snakeExample;

import engine.GamePhase;
import engine.Routine;

public class GameOverPhase extends GamePhase {

	public GameOverPhase() {
		super();
	}

	@Override
	public Routine[] routines() {
		Routine[] r = {new RenderRoutine()};
		return r;
	}

	@Override
	public void onQuit() { }

}
