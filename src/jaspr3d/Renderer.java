package jaspr3d;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.glu.GLU;

import core.Entity;
import core.EntitySystem;
import jaspr3d.shaders.StaticShader;

public class Renderer {

	private final float FOV;
	private final float NEAR_PLANE;
	private final float FAR_PLANE;
	private int WIDTH, HEIGHT;

	private static StaticShader shader;
	
	public Renderer(float fov, float nearPlane, float farPlane){
		FOV = fov;
		NEAR_PLANE = nearPlane;
		FAR_PLANE = farPlane;
	}
	
	public void init(GL3 gl, int width, int height){
		WIDTH = width;
		HEIGHT = height;
		shader = new StaticShader(gl);
		shader.start();
		shader.loadProjectionMatrix(
				createProjectionMatrix(WIDTH, HEIGHT, FOV, NEAR_PLANE, FAR_PLANE));
		shader.stop();
	}
	
	public void dispose(GL3 gl){
		shader.cleanUp();
	}

	public void render(GL3 gl, GLU glu, EntitySystem es) {
		shader.start();
		
		Camera camera = (Camera) es.getFirstComponent(Camera.class);
		if(camera != null)
			shader.loadViewMatrix(camera.getTransformations());
		
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		//gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_LINE);

		for (Entity e : es.getAllEntitiesPossessing(RawModel.class)) {
			RawModel model = e.getAs(RawModel.class);
			if (e.hasComponent(Position.class)) {
				shader.loadTransformationMatrix(e.getAs(Position.class).getTransformations());
			}
			gl.glBindVertexArray(model.getVaoID());
			if(e.hasComponent(Texture.class)){
				gl.glActiveTexture(GL3.GL_TEXTURE0);
				gl.glBindTexture(GL3.GL_TEXTURE_2D, e.getAs(Texture.class).getID());
			}
			gl.glDrawElements(GL3.GL_TRIANGLES, model.getIndicesCount(), GL3.GL_UNSIGNED_INT, 0);
			gl.glBindVertexArray(0);
		}
		shader.stop();
		gl.glFlush();
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
