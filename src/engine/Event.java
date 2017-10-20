package engine;

public abstract class Event extends Routine {

	protected static boolean running = true;
	
	public void run(){
		if(running){
			running = event();
		}
	}
	
	public abstract boolean event();
	
}
