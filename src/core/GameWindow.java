package core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import assets.TextureManager;
import jaspr3d.ModelManager;
import jaspr3d.Renderer;

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
		this.models  = models;
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
		canvas.addKeyListener(new KeylogReciever());
		canvas.requestFocus();

		display();
	}

	protected void display() {
		window.setVisible(true);
		canvas.display();
	}
	
	protected void assignEntitySystem(EntitySystem es){
		this.es = es;
	}

	public void close() {
		window.dispose();
		canvas.destroy();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if(renderer != null && es != null)
			renderer.render(gl, glu, es);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if(renderer != null)
			renderer.dispose(gl);
		if(models != null)
			models.cleanUp();
		if(textures != null)
			textures.cleanUp();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		glu = new GLU();
		
		if(models != null)
			models.preload(gl);
		if(textures != null)
			textures.preload(gl);
		if(renderer != null)
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

	private class KeylogReciever extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			KeylogManager.addKeyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			KeylogManager.addKeyReleased(e);
		}

	}

}