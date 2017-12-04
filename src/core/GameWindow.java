package core;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

import jaspr3d.rendering.Renderer;
import res.MeshManager;
import res.TextureManager;

public final class GameWindow implements GLEventListener {

	private Renderer renderer;
	private MeshManager meshes;
	private TextureManager textures;
	private JFrame window;
	private GLCanvas canvas;
	private GLU glu;
	private List<GLEvent> eventQueue;
	private Iterator<GLEvent> eventIter;
	// private boolean initialized;
	public final int WIDTH, HEIGHT;

	private GameWindow(int width, int height, Renderer renderer) {
		WIDTH = width;
		HEIGHT = height;
		eventQueue = Collections.synchronizedList(new ArrayList<>());
		this.renderer = renderer;
		this.meshes = new MeshManager(this);
		this.textures = new TextureManager(this);

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
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		window.getContentPane().setCursor(blankCursor);
	}

	public static GameWindow create(int width, int height, Renderer renderer) {
		GameWindow window = new GameWindow(width, height, renderer);
		window.display();
		return window;
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

	public void display() {
		window.setVisible(true);
		canvas.display();
	}

	public void close() {
		window.dispose();
		canvas.destroy();
	}

	public MeshManager getMeshManager() {
		return meshes;
	}

	public TextureManager getTextureManager() {
		return textures;
	}
	
	public void addGLEvent(GLEvent event){
		eventQueue.add(event);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();

		synchronized (eventQueue) {
			eventIter = eventQueue.iterator(); // Must be in synchronized block
			while (eventIter.hasNext()){
				GLEvent e = eventIter.next();
				e.run(gl);
				eventIter.remove();
			}
		}
		if (renderer != null)
			renderer.render(gl, glu);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if (renderer != null)
			renderer.dispose(gl);
		if (meshes != null)
			meshes.cleanUp(gl);
		if (textures != null)
			textures.cleanUp(gl);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		glu = new GLU();
		renderer.init(gl, WIDTH, HEIGHT, textures.defaultTexture());
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