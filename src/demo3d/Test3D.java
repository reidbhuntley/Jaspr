package demo3d;

import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;

import core.Entity;
import core.EntitySystem;
import core.GameManager;
import core.GameWindow;
import core.KeyManager;
import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.Position;
import jaspr3d.Renderer;
import res.ModelManager;
import res.TextureManager;

public class Test3D {

	public static final int WINDOW_WIDTH = 1400, WINDOW_HEIGHT = 1000, DESIRED_FPS = 100;
	public static final float FOV = 70, NEAR_FRAME = 0.1f, FAR_FRAME = 1000;
	public static ModelManager models;
	public static TextureManager textures;
	public static KeyManager keys;
	public static GameManager game;
	public static EntitySystem es;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		models = new ModelManager();
		textures = new TextureManager("default.png");
		es = new EntitySystem();
		GameWindow window = new GameWindow(WINDOW_WIDTH, WINDOW_HEIGHT, new Renderer(FOV, NEAR_FRAME, FAR_FRAME),
				models, textures);

		game = new GameManager(DESIRED_FPS, es, window);
		
		keys = new KeyManager(KeyEvent.VK_ESCAPE, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE);
		game.assignKeyManagers(keys);

		new Entity(new Camera());
		new Entity(new Light(0, 5, 0, 1, 1, 1), new Position(0, 5, -60));
		
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 70; j++) {
				new Entity(models.get("man.obj"), new Position((i - 35) * 60, -50, (j - 35) * 60, 0, 0, 0));
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
