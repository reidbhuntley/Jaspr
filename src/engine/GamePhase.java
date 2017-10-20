package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GamePhase {
	
	protected List<Routine> getRoutines(){
		if(routines() == null)
			return new ArrayList<Routine>();
		return Arrays.asList(routines());
	}
	
	public abstract Routine[] routines();
	public abstract void onQuit();
	
}
