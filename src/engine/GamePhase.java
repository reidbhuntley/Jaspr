package engine;

import java.util.ArrayList;
import java.util.List;

public abstract class GamePhase {
	
	protected List<Routine> getRoutines(){
		if(routines() == null)
			return new ArrayList<Routine>();
		List<Routine> routines = new ArrayList<>();
		for(Routine r : routines()){
			//if(!Event.class.isAssignableFrom(r.getClass()))
				routines.add(r);
		}
		return routines;
	}
	
	/*protected List<Event> getEvents(){
		if(routines() == null)
			return new ArrayList<Event>();
		List<Event> events = new ArrayList<>();
		for(Routine r : routines()){
			if(Event.class.isAssignableFrom(r.getClass()))
				events.add((Event) r);
		}
		return events;
	}*/
	
	public abstract Routine[] routines();
	public abstract Renderer renderer();
	public abstract void onQuit();
	
}
