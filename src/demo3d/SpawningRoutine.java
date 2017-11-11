package demo3d;

import java.awt.event.KeyEvent;

import core.Entity;
import core.EntitySystem.SingletonFetcher;
import core.Routine;
import jaspr3d.Camera;
import jaspr3d.Position3;
import jaspr3d.RawModel;
import jaspr3d.Texture;

public class SpawningRoutine extends Routine {

	private static SingletonFetcher cameras;
	
	public SpawningRoutine(){
		super(Camera.class,Position3.class,Texture.class,RawModel.class);
	}
	
	@Override
	public void routine() {
		Camera camera = (Camera) cameras.fetchComponent();
		if (Test3D.keys.down(KeyEvent.VK_SPACE)){
			new Entity(Test3D.models.get("man.obj"), Test3D.textures.get("pepe.png"), new Position3(camera.x(),camera.y() - 25,camera.z()));
		}
	}

	@Override
	public void onInit() {
		cameras = Test3D.es.getSingletonFetcher(Camera.class);
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
