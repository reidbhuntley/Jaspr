package core;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameManager {
	
	private GamePhase currentPhase, oldPhase;
	private ExecutorService executor;
	private EntitySystem es;
	private GameWindow window;
	private boolean newPhase, quit;
	//private List<Event> events;
	private long t;
	private final long dt;
	
	public GameManager(long targetFps, GamePhase startPhase){
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
		newPhase = false;
		quit = true;
		t = 0;
		dt = (long) (Math.pow(10,9)/targetFps);
		//events = new ArrayList<>();
		setPhase(startPhase);
	}
	
	public void start() throws InterruptedException, ExecutionException{
		if(!quit)
			return;
		quit = false;
		
		if(es == null)
			throw new IllegalStateException("This GameManager must have an EntitySystem assigned to it before starting");
		
	    long currentTime = System.nanoTime(), accumulator = 0;
	    currentPhase.renderer().onInit();
	    while (!quit){
	       	long newTime = System.nanoTime();
	        long frameTime = newTime - currentTime;
	        if(frameTime < dt)
	        	Thread.sleep((dt - frameTime)/1000000);
	        currentTime = newTime;

	        accumulator += frameTime;
	        while (accumulator >= dt){
	            processPhase();
	            accumulator -= dt;
	            t += dt;
	        }
	        
	        Renderer r = currentPhase.renderer();
	        if(window != null && r != null){
	        	r.prepareForProcessing(this, es);
	        	window.assignRenderer(r);
	        	window.render();
	        }
	    }
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
	
	private void processPhase() throws InterruptedException, ExecutionException{
		KeylogManager.fetchKeys();
		List<Routine> routineQueue = new ArrayList<Routine>(currentPhase.getRoutines());
		//routineQueue.addAll(events);
		for(Routine r : routineQueue){
			r.prepareForProcessing(this, es);
			if(newPhase)
				r.onPhaseStart();
		}
		if(newPhase && currentPhase.renderer() != null)
			currentPhase.renderer().onPhaseStart();
		newPhase = false;
		
		HashMap<Class<? extends Dependency>, Future<?>> routinesExecuting = new HashMap<>();
		while(routineQueue.size() > 0 || routinesExecuting.size() > 0){
			for(Iterator<Class<? extends Dependency>> iter = routinesExecuting.keySet().iterator(); iter.hasNext();){
				if(routinesExecuting.get(iter.next()).get() == null)
					iter.remove();
			}
			for(Iterator<Routine> iter = routineQueue.iterator(); iter.hasNext();){
				Routine r = iter.next();
				List<Class<? extends Dependency>> keySet = new ArrayList<>(routinesExecuting.keySet());
				if(r.dependencies() != null){
					List<Class<? extends Dependency>> dependencies = Arrays.asList(r.dependencies());
					if(Collections.disjoint(keySet, dependencies)){
						r.prepareForProcessing(this, es);
						Future<?> future = executor.submit(r);
						for(Class<? extends Dependency> c : dependencies)
							routinesExecuting.put(c, future);
						iter.remove();
					}
				} else {
					executor.submit(r);
					iter.remove();
				}
			}
			
		}
		if(newPhase){
			for(Routine r : oldPhase.getRoutines()){
				r.onPhaseEnd();
			}
			oldPhase.renderer().onPhaseEnd();
		}
	}
	
	public void assignEntitySystem(EntitySystem entitySystem){
		es = entitySystem;
	}
	
	public void assignGameWindow(GameWindow window){
		this.window = window;
	}
	
	public void setPhase(GamePhase phase) {
		if(currentPhase != null)
			currentPhase.onQuit();
		oldPhase = currentPhase;
		currentPhase = phase;
		//events.addAll(phase.getEvents());
		newPhase = true;
	}
	
	/*
	public void queueEvent(Event e){
		events.add(e);
	}
	
	public void killEvent(Event e){
		events.remove(e);
	}
	*/
	
	public void quit(){
		quit = true;
	}
	
	public long dt(){
		return dt;
	}
	
	public long t(){
		return t;
	}
	
}
