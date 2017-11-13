package demo3d;

import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;

import core.Entity;
import core.EntitySystem;
import core.GameManager;
import core.GameWindow;
import core.KeyManager;
import core.MouseManager;
import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.Position3;
import jaspr3d.Vector3;
import jaspr3d.rendering.Renderer;
import res.ModelManager;
import res.TextureManager;

public class Test3D {

	public static final int WINDOW_WIDTH = 1400, WINDOW_HEIGHT = 1000, DESIRED_FPS = 100;
	public static final float FOV = 70, NEAR_FRAME = 0.1f, FAR_FRAME = 6000;
	public static ModelManager models;
	public static TextureManager textures;
	public static KeyManager keys;
	public static MouseManager mouse;
	public static GameManager game;
	public static EntitySystem es;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		models = new ModelManager();
		textures = new TextureManager("default.png");
		es = new EntitySystem();
		GameWindow window = new GameWindow(WINDOW_WIDTH, WINDOW_HEIGHT, new Renderer(FOV, NEAR_FRAME, FAR_FRAME),
				models, textures);

		game = new GameManager(DESIRED_FPS, es, window);
		
		keys = new KeyManager(KeyEvent.VK_ESCAPE, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
		game.assignKeyManagers(keys);
		
		mouse = new MouseManager();
		game.assignMouseManagers(mouse);

		new Entity(new Camera());
		//new Entity(new Light(new Vector3(0,-50,0), new Vector3(0.7f,0.7f,0.7f)));
		new Entity(new Light(new Vector3(-300, 20, -400), new Vector3(0.2f, 0.2f, 1), new Vector3(0,0.001f,0.0001f)));
		new Entity(new Light(new Vector3(-300, 20, 400), new Vector3(1, 0.2f, 0.2f), new Vector3(0,0.001f,0.0001f)));
		new Entity(new Light(new Vector3(350, 20, 0), new Vector3(0.2f, 1, 0.2f), new Vector3(0,0.001f,0.0001f)));
		
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 70; j++) {
				new Entity(models.get("capricorn.obj"), new Position3((i - 35) * 60, -50, (j - 35) * 60, 0, 0, 0));
			}
		}
		
		// new Entity(models.get("res\\models\\boots.obj"), new
		// Position(10.0f,-30.0f,-10.0f));
		// new Entity(models.get("res\\models\\capricorn.obj"),
		// textures.get("res\\textures\\2537.png", 20, 1), new
		// Position(-20.0f,0.0f,-40.0f,0.0f,180.0f,0.0f));

		game.setPhase(new MainPhase());
		game.start();

	}

}
