package jaspr3d;

import core.GamePhase;
import core.Renderer;
import core.Routine;

public class MainPhase extends GamePhase{
	
	@Override
	public Routine[] routines() {
		return null;
	}

	@Override
	public Renderer renderer() {
		return new Renderer3d();
	}

	@Override
	public void onQuit() {
		// TODO Auto-generated method stub
		
	}
	
}
