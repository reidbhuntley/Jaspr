package demo3d;

import java.util.concurrent.ExecutionException;

import assets.TextureManager;
import core.Entity;
import core.EntitySystem;
import core.GameManager;
import core.GameWindow;
import jaspr3d.Camera;
import jaspr3d.ModelManager;
import jaspr3d.Position;
import jaspr3d.Renderer;

public class Test3D {
	
	public static final int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;
	public static final float FOV = 70, NEAR_FRAME = 0.1f, FAR_FRAME = 1000;
	public static ModelManager models;
	public static TextureManager textures;
	public static GameManager game;
	public static EntitySystem es;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		models = new ModelManager();
		textures = new TextureManager();
		es = new EntitySystem();
		GameWindow window = new GameWindow(WINDOW_WIDTH, WINDOW_HEIGHT, new Renderer(FOV, NEAR_FRAME, FAR_FRAME), models, textures);
		
		game = new GameManager(100, es, window);
		
		new Entity(new Camera());
		new Entity(models.get("res\\models\\boots.obj"), textures.get("res\\textures\\uvgrid2.png"), new Position(10.0f,-30.0f,-10.0f));
		new Entity(models.get("res\\models\\boots.obj"), textures.get("res\\textures\\uvgrid2.png"), new Position(-20.0f,0.0f,-40.0f));
		new Entity(models.get("res\\models\\capricorn.obj"), textures.get("res\\textures\\uvgrid2.png"), new Position(20.0f,-10.0f,-100f, 0,20.0f,0));
		
		game.setPhase(new MainPhase());
		game.start();
		
		Thread.activeCount();
	}
	
}
