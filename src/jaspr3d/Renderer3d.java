package jaspr3d;

import java.awt.event.KeyEvent;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

import core.Entity;
import core.KeylogManager;
import core.Renderer;

public class Renderer3d extends Renderer {

	// private static final int WIDTH = Test3D.WINDOW_WIDTH, HEIGHT =
	// Test3D.WINDOW_HEIGHT;
	private static float x = 0, y = 10, z = 50, angle = 0;

	public void render(GLAutoDrawable drawable, GLU glu) {
		if (KeylogManager.pressed(KeyEvent.VK_ESCAPE))
			gm.quit();
		if (KeylogManager.pressed(KeyEvent.VK_UP)){
			x += 3*Math.cos(Math.toRadians((double)angle+90));
			z -= 3*Math.sin(Math.toRadians((double)angle+90));
		}
		if (KeylogManager.pressed(KeyEvent.VK_DOWN)){
			x -= 3*Math.cos(Math.toRadians((double)angle+90));
			z += 3*Math.sin(Math.toRadians((double)angle+90));
		}
		if (KeylogManager.pressed(KeyEvent.VK_LEFT))
			angle += 3f;
		if (KeylogManager.pressed(KeyEvent.VK_RIGHT))
			angle -= 3f;

		final GL2 gl = drawable.getGL().getGL2();

		// Clear The Screen And The Depth Buffer
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity(); // Reset The View
		gl.glRotatef(-angle, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(-x, -y, -z);

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

		for (Entity e : es.getAllEntitiesPossessing(RawModel.class)) {
			gl.glPushMatrix();
			if (e.hasComponent(Position.class)) {
				Position pos = e.getAs(Position.class);
				float[] transMat = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, -pos.x, -pos.y, -pos.z, 1 };
				gl.glMultMatrixf(transMat, 0);
				float[] rotMat = { pos.yCos, 0, pos.ySin, 0, 0, 1, 0, 0, -pos.ySin, 0, pos.yCos, 0, 0, 0, 0, 1};
				gl.glMultMatrixf(rotMat, 0);
			}

			RawModel model = e.getAs(RawModel.class);
			int[] indices = model.indices;
			float[] vertices = model.vertices;
			gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
			for (int i = 0; i < indices.length; i++) {
				int index = indices[i] * 3;
				gl.glVertex3f(vertices[index], vertices[index + 1], vertices[index + 2]);
			}
			gl.glEnd();
			gl.glPopMatrix();
		}
		gl.glFlush();
		//System.out.println(gm.actualFps());
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onPhaseStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPhaseEnd() {
		// TODO Auto-generated method stub

	}

}
