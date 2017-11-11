package demo3d;

import core.GamePhase;
import core.Routine;

public class MainPhase extends GamePhase{
	
	@Override
	public Routine[] routines() {
		Routine[] routines = {new CameraRoutine(), new RotationRoutine(), new SpawningRoutine()};
		return routines;
	}

	@Override
	public void onQuit() {
		// TODO Auto-generated method stub
		
	}
	
}
