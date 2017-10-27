package snakeExample;

import engine.AssetManager;
import engine.GamePhase;
import engine.Renderer;
import engine.Routine;

public class GameOverPhase extends GamePhase {

	public GameOverPhase() {
		super();
		AssetManager.playSound(SnakeGame.SOUND_DEATH);
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
