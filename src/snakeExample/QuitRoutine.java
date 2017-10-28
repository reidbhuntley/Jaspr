package snakeExample;

import java.awt.event.KeyEvent;

import core.Dependency;
import core.KeylogManager;
import core.Routine;

public class QuitRoutine extends Routine {

	@Override
	public void routine() {
		if(KeylogManager.pressed(KeyEvent.VK_ESCAPE))
			gm.quit();
	}

	@Override
	public Class<? extends Dependency>[] dependencies() {
		return null;
	}

	@Override
	public void onInit() {}

	@Override
	public void onPhaseStart() {}

	@Override
	public void onPhaseEnd() {
		// TODO Auto-generated method stub
		
	}

}
