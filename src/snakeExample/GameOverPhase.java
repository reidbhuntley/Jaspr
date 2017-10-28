package snakeExample;

import core.GamePhase;
import core.Renderer;
import core.Routine;

public class GameOverPhase extends GamePhase {

	public GameOverPhase() {
		super();
		SnakeGame.sounds.playSound(SnakeGame.SOUND_DEATH);
	}

	@Override
	public Routine[] routines() {
		Routine[] r = {new QuitRoutine(), new RestartRoutine()};
		return r;
	}

	@Override
	public void onQuit() { }

	@Override
	public Renderer renderer() {
		return null;
	}

}
