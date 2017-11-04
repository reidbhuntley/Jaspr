package demo3d;

import java.awt.event.KeyEvent;

import core.Component;
import core.KeylogManager;
import core.Routine;
import jaspr3d.Camera;

public class MovementRoutine extends Routine {

	@Override
	public void routine() {
		/*
		 * if (KeylogManager.pressed(KeyEvent.VK_UP)){ x +=
		 * 3*Math.cos(Math.toRadians((double)angle+90)); z -=
		 * 3*Math.sin(Math.toRadians((double)angle+90)); } if
		 * (KeylogManager.pressed(KeyEvent.VK_DOWN)){ x -=
		 * 3*Math.cos(Math.toRadians((double)angle+90)); z +=
		 * 3*Math.sin(Math.toRadians((double)angle+90)); } if
		 * (KeylogManager.pressed(KeyEvent.VK_LEFT)) angle += 3f; if
		 * (KeylogManager.pressed(KeyEvent.VK_RIGHT)) angle -= 3f;
		 */
		if (KeylogManager.pressed(KeyEvent.VK_ESCAPE))
			Test3D.game.quit();

		Camera camera = (Camera) Test3D.es.getFirstComponent(Camera.class);
		camera.transform(0, 0, -0.05f, 0, 0, 0.5f);
		
		System.out.println(Test3D.game.actualFps());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<? extends Component>[] dependencies() {
		Class[] dependencies = { Camera.class };
		return dependencies;
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPhaseStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPhaseEnd() {
		// TODO Auto-generated method stub

	}

}
