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

import jaspr3d.ModelManager;

public final class GameWindow implements GLEventListener {

	private Renderer renderer;
	private ModelManager models;
	private JFrame window;
	private GLCanvas canvas;
	private GLU glu;
	private boolean open;
	public final int width, height;

	public GameWindow(int width, int height) {
		this.width = width;
		this.height = height;
		open = false;
		
		final GLProfile profile = GLProfile.get(GLProfile.GL3);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		window = new JFrame();
	}
	
	public void open(){
		if(!open){
			open = true;
			canvas.addGLEventListener(this);
			canvas.setSize(width, height);
			window.setUndecorated(true);
			window.getContentPane().add(canvas);
			window.pack();
			window.setLocationRelativeTo(null);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			canvas.addKeyListener(new KeylogReciever());
			canvas.requestFocus();
			display();
		}
	}

	protected void display() {
		window.setVisible(true);
		canvas.display();
	}

	public void close() {
		window.dispose();
		canvas.destroy();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if(renderer != null)
			renderer.render(drawable, glu);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		if(models != null)
			models.cleanUp();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		glu = new GLU();
		
		gl.glEnable(GL3.GL_DEPTH_TEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		if(models != null)
			models.preload(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL3 gl = drawable.getGL().getGL3(); // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1; // prevent divide by zero
		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);
	}

	public void assignRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public void assignModelManager(ModelManager models) {
		this.models = models;
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