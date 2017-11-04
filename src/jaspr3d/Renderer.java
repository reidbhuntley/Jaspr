package jaspr3d;

import java.awt.event.KeyEvent;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.glu.GLU;

import core.Entity;
import core.KeylogManager;
import jaspr3d.shaders.StaticShader;

public class Renderer {

	// private static final int WIDTH = Test3D.WINDOW_WIDTH, HEIGHT =
	// Test3D.WINDOW_HEIGHT;
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	private static StaticShader shader;
	
	public void init(GL3 gl){
		
	}
	
	public void dispose(GL3 gl){
		shader.cleanUp();
	}

	public void render(GL3 gl, GLU glu) {
		if (KeylogManager.pressed(KeyEvent.VK_ESCAPE))
			Test3D.game.quit();
		/*
		 * if (KeylogManager.pressed(KeyEvent.VK_UP)){ x +=
		 * 3*Math.cos(Math.toRadians((double)angle+90)); z -=
		 * 3*Math.sin(Math.toRadians((double)angle+90)); } if
		 * (KeylogManager.pressed(KeyEvent.VK_DOWN)){ x -=
		 * 3*Math.cos(Math.toRadians((double)angle+90)); z +=
		 * 3*Math.sin(Math.toRadians((double)angle+90)); } if
		 * (KeylogManager.pressed(KeyEvent.VK_LEFT)) angle += 3f; if
		 * (KeylogManager.pressed(KeyEvent.VK_RIGHT)) angle -= 3f;
		 */

		if (shader == null) {
			shader = new StaticShader(gl);
			shader.start();
			shader.loadProjectionMatrix(
					createProjectionMatrix(Test3D.WINDOW_WIDTH, Test3D.WINDOW_HEIGHT, FOV, NEAR_PLANE, FAR_PLANE));
			shader.stop();
		}
		
		shader.start();
		
		Camera camera = (Camera) Test3D.es.getFirstComponent(Camera.class);
		if(camera != null){
			shader.loadViewMatrix(camera.getTransformations());
			camera.transform(0, 0, -0.05f, 0, 0, 0.5f);
		}
		
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_LINE);

		for (Entity e : Test3D.es.getAllEntitiesPossessing(RawModel.class)) {
			RawModel model = e.getAs(RawModel.class);
			if (e.hasComponent(Position.class)) {
				//e.getAs(Position.class).rotate(0, 0.5f, 0);
				shader.loadTransformationMatrix(e.getAs(Position.class).getTransformations());
			}
			gl.glBindVertexArray(model.getVaoID());
			gl.glDrawElements(GL3.GL_TRIANGLES, model.getIndicesCount(), GL3.GL_UNSIGNED_INT, 0);
			gl.glBindVertexArray(0);
		}
		shader.stop();
		gl.glFlush();
		System.out.println(Test3D.game.actualFps());
	}

	private float[] createProjectionMatrix(int width, int height, float fov, float near_plane, float far_plane) {
		final float aspectRatio = (float) width / (float) height;
		final float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		final float x_scale = y_scale / aspectRatio;
		final float frustum_length = far_plane - near_plane;
		final float[] pMat = { x_scale, 0, 0, 0, 0, y_scale, 0, 0, 0, 0, -((far_plane + near_plane) / frustum_length),
				-1, 0, 0, -((2 * near_plane * far_plane) / frustum_length), 0 };
		return pMat;
	}
	
}