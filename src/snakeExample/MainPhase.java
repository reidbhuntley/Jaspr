package snakeExample;

import engine.AssetManager;
import engine.GamePhase;
import engine.Loop;
import engine.Renderer;
import engine.Routine;

public class MainPhase extends GamePhase {
	
	private Renderer renderer;
	private Routine[] routines;
	private Loop music;

	public MainPhase() {
		super();
		renderer = new RenderRoutine();
		routines = new Routine[2];
		routines[0] = new LogicRoutine();
		routines[1] = new QuitRoutine();
		AssetManager.playSound(SnakeGame.SOUND_START);
		music = AssetManager.getLoop(SnakeGame.SOUND_MUSIC);
		music.start();
	}

	@Override
	public Routine[] routines() {
		return routines;
	}

	@Override
	public void onQuit() {
		music.stop();
	}

	@Override
	public Renderer renderer() {
		return renderer;
	}

}
