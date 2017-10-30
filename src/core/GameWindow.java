package core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import jaspr3d.Test3D;

public final class GameWindow implements GLEventListener {

	private Renderer renderer;
	private JFrame window;
	private GLCanvas canvas;
	private GLU glu;

	public GameWindow(int width, int height) {
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.setSize(width, height);
		
		window = new JFrame();
		window.setUndecorated(true);
		window.getContentPane().add(canvas);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.addKeyListener(new KeylogReciever());
		canvas.requestFocus();
	}

	public void display() {
		window.setVisible(true);
		canvas.display();
	}

	public void close() {
		window.dispose();
		canvas.destroy();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		renderer.render(drawable, glu);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		glu = new GLU(); // get GL Utilities
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL2.GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes
										// out
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1; // prevent divide by zero
		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(50, (float)Test3D.WINDOW_WIDTH/Test3D.WINDOW_HEIGHT, 0.01, 1000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	public void assignRenderer(Renderer r) {
		renderer = r;
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