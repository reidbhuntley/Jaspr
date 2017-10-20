package engine;

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

	private static long TARGET_FPS = 80;
	private static long TARGET_NPF = (long) (Math.pow(10,9)/TARGET_FPS);
	private static long totalTimes = 0;
	private static long numOfTimes = -3;
	
	private static GamePhase currentPhase;
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
	
	public static void setPhase(GamePhase phase) {
		if(currentPhase != null)
			currentPhase.onQuit();
		currentPhase = phase;
	}
	
	public static void setTargetFps(long fps){
		TARGET_FPS = fps;
		TARGET_NPF = (long) (Math.pow(10,9)/TARGET_FPS);
	}
	
	public static void processPhase() throws InterruptedException, ExecutionException{
		long startTime = System.nanoTime();
		KeylogManager.fetchKeys();
		List<Routine> routineQueue = new ArrayList<Routine>(currentPhase.getRoutines());
		HashMap<Class<? extends Component>, Future<?>> routinesExecuting = new HashMap<>();
		while(routineQueue.size() > 0 || routinesExecuting.size() > 0){
			for(Iterator<Class<? extends Component>> iter = routinesExecuting.keySet().iterator(); iter.hasNext();){
				if(routinesExecuting.get(iter.next()).get() == null)
					iter.remove();
			}
			for(Iterator<Routine> iter = routineQueue.iterator(); iter.hasNext();){
				Routine r = iter.next();
				List<Class<? extends Component>> keySet = new ArrayList<>(routinesExecuting.keySet()), dependencies = Arrays.asList(r.dependencies());
				if(Collections.disjoint(keySet, dependencies)){
					Future<?> future = executor.submit(r);
					for(Class<? extends Component> c : dependencies)
						routinesExecuting.put(c, future);
					iter.remove();
				}
			}
		}
		long timeDiff = System.nanoTime() - startTime;
		long timeLeft = TARGET_NPF - timeDiff;
		numOfTimes++;
		if(numOfTimes > 0){
			totalTimes += timeDiff;
		}
		if(timeLeft > 0)
			Thread.sleep(timeLeft/1000000);
	}
	
	public static double averageFps(){
		return 1000000000/(double)(totalTimes/numOfTimes);
	}
	
}
