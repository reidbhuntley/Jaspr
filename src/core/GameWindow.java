package core;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import jaspr3d.Renderer;
import res.ModelManager;
import res.TextureManager;

public final class GameWindow implements GLEventListener {

	private Renderer renderer;
	private ModelManager models;
	private TextureManager textures;
	private EntitySystem es;
	private JFrame window;
	private GLCanvas canvas;
	private GLU glu;
	public final int WIDTH, HEIGHT;

	public GameWindow(int width, int height, Renderer renderer, ModelManager models, TextureManager textures) {
		WIDTH = width;
		HEIGHT = height;
		this.renderer = renderer;
		this.models = models;
		this.textures = textures;

		final GLProfile profile = GLProfile.get(GLProfile.GL3);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.setSize(WIDTH, HEIGHT);

		window = new JFrame();
		window.setUndecorated(true);
		window.getContentPane().add(canvas);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.requestFocus();
		
		
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		window.getContentPane().setCursor(blankCursor);
		
	}
	
	public void assignMouseManager(MouseManager mouseManager) {
		mouseManager.assignToWindow(window);
		canvas.addMouseListener(mouseManager);
		canvas.addMouseMotionListener(mouseManager);
	}

	protected void assignKeyManager(KeyManager keyManager) {
		ActionMap actionMap = window.getRootPane().getActionMap();
		InputMap inputMap = window.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		for (int k : keyManager.getKeysListening()) {
			KeyStroke stroke = KeyStroke.getKeyStroke(k, 0, false);
			String str = stroke.toString();
			inputMap.put(stroke, str);
			actionMap.put(str, new KeyBinding(str, keyManager, k, false));
			
			stroke = KeyStroke.getKeyStroke(k, 0, true);
			str = stroke.toString();
			inputMap.put(stroke, str);
			actionMap.put(str, new KeyBinding(str, keyManager, k, true));
		}
	}

	private class KeyBinding extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private KeyManager km;
		private boolean isRelease;
		private int event;

		public KeyBinding(String text, KeyManager km, int event, boolean isRelease) {
			super(text);
			putValue(ACTION_COMMAND_KEY, text);
			this.event = event;
			this.km = km;
			this.isRelease = isRelease;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isRelease) {
				km.addKeyPressed(event);
			} else {
				km.addKeyReleased(event);
			}
		}

	}

	protected void display() {
		window.setVisible(true);
		canvas.display();
	}

	protected void assignEntitySystem(EntitySystem es) {
		this.es = es;
	}

	public void close() {
		window.dispose();
		canvas.destroy();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if (renderer != null && es != null)
			renderer.render(gl, glu, es);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if (renderer != null)
			renderer.dispose(gl);
		if (models != null)
			models.cleanUp();
		if (textures != null)
			textures.cleanUp();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		glu = new GLU();

		models.preload(gl);
		textures.preload(gl);
		renderer.init(gl, es, WIDTH, HEIGHT, textures.defaultTexture());
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL3 gl = drawable.getGL().getGL3(); // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1; // prevent divide by zero
		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);
	}

}