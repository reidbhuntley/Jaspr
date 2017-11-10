package demo3d;

import java.awt.event.KeyEvent;

import core.Entity;
import core.EntitySystem.EntityFetcher;
import core.EntitySystem.SingletonFetcher;
import core.Routine;
import jaspr3d.Camera;
import jaspr3d.Position;
import jaspr3d.RawModel;
import jaspr3d.Texture;

public class MovementRoutine extends Routine {

	private static SingletonFetcher cameras;
	private static EntityFetcher positions;
	
	public MovementRoutine(){
		super(Camera.class, Position.class, RawModel.class, Texture.class);
	}
	
	@Override
	public void routine() {
		if (Test3D.keys.down(KeyEvent.VK_ESCAPE))
			Test3D.game.quit();

		Camera camera = (Camera) cameras.fetchComponent();
		
		float z = 0, yaw = 0;
		if (Test3D.keys.down(KeyEvent.VK_LEFT))
			yaw -= 1;
		if (Test3D.keys.down(KeyEvent.VK_RIGHT))
			yaw += 1;
		if (Test3D.keys.down(KeyEvent.VK_UP))
			z -= 5;
		if (Test3D.keys.down(KeyEvent.VK_DOWN))
			z += 5;
		if (Test3D.keys.down(KeyEvent.VK_SPACE)){
			new Entity(Test3D.models.get("man.obj"), Test3D.textures.get("pepe.png"), new Position(camera.x(),camera.y() - 25,camera.z()));
		}
		
		if(camera != null){
			camera.rotate(0, yaw, 0);
			yaw = (float)Math.toRadians(camera.yaw());
			camera.move((float)(-z*Math.sin(yaw)), 0, (float)(z*Math.cos(yaw)));
		}
		
		for(Entity e : positions.fetch()){
			e.getAs(Position.class).rotate(1, 1, 1);
		}
		
		System.out.println(Test3D.game.actualFps());
	}

	@Override
	public void onInit() {
		cameras = Test3D.es.getSingletonFetcher(Camera.class);
		positions = Test3D.es.getEntityFetcher(Position.class);
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
