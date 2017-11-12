package core;

import java.util.ArrayList;
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
	private boolean newPhase, quit;
	// private List<Event> events;
	private long t;
	private final long dt;
	private double actualFps;
	private GameWindow window;
	private List<KeyManager> keyManagers;
	private List<MouseManager> mouseManagers;

	public GameManager(long targetFps, EntitySystem entitySystem, GameWindow window) {
		es = entitySystem;
		this.window = window;
		window.assignEntitySystem(es);
		window.run();
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
		newPhase = false;
		quit = true;
		t = 0;
		dt = (long) (Math.pow(10, 9) / targetFps);
		// events = new ArrayList<>();
		keyManagers = new ArrayList<>();
		mouseManagers = new ArrayList<>();
	}

	public void start() throws InterruptedException, ExecutionException {
		if (currentPhase == null)
			throw new IllegalStateException("The GameManager can't start without being set to a GamePhase");

		if (!quit)
			return;
		quit = false;

		if (es == null)
			throw new IllegalStateException(
					"This GameManager must have an EntitySystem assigned to it before starting");

		long currentTime = System.nanoTime(), accumulator = 0;
		while (!quit) {
			long newTime = System.nanoTime();
			long frameTime = newTime - currentTime;
			actualFps = Math.pow(10, 9) / ((double) frameTime);
			if (frameTime < dt)
				Thread.sleep((dt - frameTime) / 1000000);
			currentTime = System.nanoTime();
			
			Future<?> renderFuture = null;
			if(window != null){
	        	renderFuture = executor.submit(window);
	        }
			
			accumulator += frameTime;
			while (accumulator >= dt) {
				processPhase();
				accumulator -= dt;
				t += dt;
			}
			
			while(renderFuture != null && renderFuture.get() != null){
				Thread.sleep(1);
			}

		}
		executor.shutdown();
		window.close();
	}

	private void processPhase() throws InterruptedException, ExecutionException {
		for (KeyManager km : keyManagers) {
			km.fetchKeys();
		}
		for (MouseManager mm : mouseManagers) {
			mm.fetchEvents();
		}

		es.updateFetchers();

		List<Routine> routineQueue = new ArrayList<Routine>(currentPhase.getRoutines());
		// routineQueue.addAll(events);
		for (Routine r : routineQueue) {
			if (newPhase)
				r.onPhaseStart();
		}
		newPhase = false;

		HashMap<Class<? extends Component>, Future<?>> routinesExecuting = new HashMap<>();
		while (routineQueue.size() > 0 || routinesExecuting.size() > 0) {
			for (Iterator<Class<? extends Component>> iter = routinesExecuting.keySet().iterator(); iter.hasNext();) {
				if (routinesExecuting.get(iter.next()).get() == null)
					iter.remove();
			}
			for (Iterator<Routine> iter = routineQueue.iterator(); iter.hasNext();) {
				Routine r = iter.next();
				r.setGame(this);
				List<Class<? extends Component>> keySet = new ArrayList<>(routinesExecuting.keySet());
				if (r.dependencies() != null) {
					List<Class<? extends Component>> dependencies = r.dependencies();
					if (Collections.disjoint(keySet, dependencies)) {
						Future<?> future = executor.submit(r);
						for (Class<? extends Component> c : dependencies)
							routinesExecuting.put(c, future);
						iter.remove();
					}
				} else {
					executor.submit(r);
					iter.remove();
				}
			}

		}
		if (newPhase) {
			for (Routine r : oldPhase.getRoutines()) {
				r.onPhaseEnd();
			}
		}
	}

	public void setPhase(GamePhase phase) {
		if (currentPhase != null)
			currentPhase.onQuit();
		oldPhase = currentPhase;
		currentPhase = phase;
		// events.addAll(phase.getEvents());
		newPhase = true;
	}

	public void assignKeyManagers(KeyManager... keyManagers) {
		for (KeyManager km : keyManagers) {
			this.keyManagers.add(km);
			window.assignKeyManager(km);
		}
	}

	public void assignMouseManagers(MouseManager... mouseManagers) {
		for (MouseManager mm : mouseManagers) {
			this.mouseManagers.add(mm);
			window.assignMouseManager(mm);
		}
	}

	/*
	 * public void queueEvent(Event e){ events.add(e); }
	 * 
	 * public void killEvent(Event e){ events.remove(e); }
	 */

	public void quit() {
		quit = true;
	}

	public long dt() {
		return dt;
	}

	public long t() {
		return t;
	}

	public double actualFps() {
		return actualFps;
	}

}
