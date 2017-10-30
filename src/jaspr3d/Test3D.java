package jaspr3d;

import java.util.concurrent.ExecutionException;

import core.Entity;
import core.EntitySystem;
import core.GameManager;
import core.GameWindow;

public class Test3D {
	
	public static final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;
	public static ModelManager models;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		EntitySystem entSys = new EntitySystem();
		entSys.setAsDefault();
		
		models = new ModelManager();
		models.preload();
		
		new Entity(models.get("res\\models\\boots.obj"));
		new Entity(models.get("res\\models\\boots.obj"), new Position(20.0f,-10.0f,10.0f, 0,-80.0f,0));
		new Entity(models.get("res\\models\\capricorn.obj"), new Position(20.0f,0,-100f, 0,180.0f,0));
		
		GameManager game = new GameManager(100);
		game.assignEntitySystem(entSys);
		game.assignGameWindow(new GameWindow(WINDOW_WIDTH, WINDOW_HEIGHT));
		game.setPhase(new MainPhase());
		game.start();
		
	}
	
}
