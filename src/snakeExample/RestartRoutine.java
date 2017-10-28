package snakeExample;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.Dependency;
import core.KeylogManager;
import core.Routine;

public class RestartRoutine extends Routine {

	private static List<Integer> KEYS_TO_EXCLUDE = new ArrayList<Integer>();
	
	@Override
	public Class<? extends Dependency>[] dependencies() {
		return null;
	}

	@Override
	public void onInit() {
		 KEYS_TO_EXCLUDE.add(KeyEvent.VK_LEFT);
		 KEYS_TO_EXCLUDE.add(KeyEvent.VK_RIGHT);
		 KEYS_TO_EXCLUDE.add(KeyEvent.VK_UP);
		 KEYS_TO_EXCLUDE.add(KeyEvent.VK_DOWN);
		 KEYS_TO_EXCLUDE.add(KeyEvent.VK_ESCAPE);
	}

	@Override
	public void routine() {
		if(KeylogManager.getPressedList().size() > 0 && Collections.disjoint(KeylogManager.getPressedList(), KEYS_TO_EXCLUDE)){
			gm.setPhase(new MainPhase());
		}
	}

	@Override
	public void onPhaseStart() {
	}

	@Override
	public void onPhaseEnd() {
		// TODO Auto-generated method stub
		
	}

}
