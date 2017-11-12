package demo3d;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import core.EntitySystem.SingletonFetcher;
import core.Routine;
import jaspr3d.Camera;
import jaspr3d.Position2;
import jaspr3d.Position3;
import jaspr3d.RawModel;
import jaspr3d.Texture;

public class CameraRoutine extends Routine {

	private static SingletonFetcher cameras;
	
	private static int lastX = 0, lastY = 0;
	private static List<Position2> mouseList = new ArrayList<>();
	private static final int MOUSE_LIST_MAX = 5;
	
	public CameraRoutine(){
		super(Camera.class, Position3.class, RawModel.class, Texture.class);
	}
	
	@Override
	public void routine() {
		if (Test3D.keys.down(KeyEvent.VK_ESCAPE))
			Test3D.game.quit();
		
		Camera camera = (Camera) cameras.fetchComponent();
		
		float fwd = 0, right = 0;
		if (Test3D.keys.down(KeyEvent.VK_W))
			fwd += 3;
		if (Test3D.keys.down(KeyEvent.VK_S))
			fwd += -3;
		if (Test3D.keys.down(KeyEvent.VK_A))
			right += -3;
		if (Test3D.keys.down(KeyEvent.VK_D))
			right += 3;
		
		Position2 mouse = Test3D.mouse.position();
		int x = (int)mouse.x(), y = (int)mouse.y();
		int diffX = x - lastX, diffY = y - lastY;
		lastX = x;
		lastY = y;
		
		mouseList.add(new Position2(diffX, diffY));
		if(mouseList.size() > MOUSE_LIST_MAX)
			mouseList.remove(0);
		double avgX = 0, avgY = 0;
		for(Position2 pos : mouseList){
			avgX += pos.x();
			avgY += pos.y();
		}
		avgX /= mouseList.size();
		avgY /= mouseList.size();
		
		camera.rotate((float)avgY/15, (float)avgX/15, 0.0f);
		
		if(Math.hypot(x, y) > 10){
			lastX = 0;
			lastY = 0;
			Test3D.mouse.setPosition(0, 0);
		}
		
		camera.moveForward(fwd);
		camera.moveRight(right);
		camera.updateTransformations();
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
