package jaspr3d;

import java.util.concurrent.ExecutionException;

import core.Entity;
import core.EntitySystem;
import core.GameManager;
import core.GameWindow;

public class Test3D {
	
	public static final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;
	public static ModelManager models;
	public static GameManager game;
	public static EntitySystem es;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		game = new GameManager(100);
		
		es = new EntitySystem();
		game.assignEntitySystem(es);
		
		models = new ModelManager();
		GameWindow window = new GameWindow(WINDOW_WIDTH, WINDOW_HEIGHT, new Renderer(), models);
		game.assignGameWindow(window);
		
		new Entity(new Camera());
		new Entity(models.get("res\\models\\boots.obj"), new Position(10.0f,-30.0f,-10.0f));
		new Entity(models.get("res\\models\\boots.obj"), new Position(0.0f,0.0f,-40.0f));
		new Entity(models.get("res\\models\\capricorn.obj"), new Position(20.0f,0,-100f, 0,180.0f,0));
		
		game.setPhase(new MainPhase());
		game.start();
		
		Thread.activeCount();
	}
	
}
