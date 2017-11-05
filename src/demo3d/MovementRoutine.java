package demo3d;

import java.awt.event.KeyEvent;

import core.Component;
import core.Entity;
import core.EntitySystem.EntityFetcher;
import core.EntitySystem.SingletonFetcher;
import core.KeylogManager;
import core.Routine;
import jaspr3d.Camera;
import jaspr3d.Position;

public class MovementRoutine extends Routine {

	private static SingletonFetcher cameras;
	private static EntityFetcher positions;
	
	@Override
	public void routine() {
		if (KeylogManager.pressed(KeyEvent.VK_ESCAPE))
			Test3D.game.quit();

		Camera camera = (Camera) cameras.fetchComponent();
		
		float z = 0, yaw = 0;
		if (KeylogManager.pressed(KeyEvent.VK_LEFT))
			yaw -= 5;
		if (KeylogManager.pressed(KeyEvent.VK_RIGHT))
			yaw += 5;
		if (KeylogManager.pressed(KeyEvent.VK_UP))
			z -= 5;
		if (KeylogManager.pressed(KeyEvent.VK_DOWN))
			z += 5;
		
		if(camera != null){
			camera.rotate(0, yaw, 0);
			yaw = (float)Math.toRadians(camera.getRot()[1]);
			camera.move((float)(-z*Math.sin(yaw)), 0, (float)(z*Math.cos(yaw)));
		}
		
		for(Entity e : positions.fetch()){
			e.getAs(Position.class).rotate(0, 1, 0);
		}
		
		System.out.println(Test3D.game.actualFps());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<? extends Component>[] dependencies() {
		Class[] dependencies = { Camera.class, Position.class };
		return dependencies;
	}

	@Override
	public void onInit() {
		cameras = Test3D.es.genSingletonFetcher(Camera.class);
		positions = Test3D.es.genEntityFetcher(Position.class);
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
