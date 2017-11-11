package jaspr3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Matrix4;

import core.Entity;
import core.EntitySystem;
import core.EntitySystem.EntityFetcher;
import core.EntitySystem.SingletonFetcher;
import jaspr3d.shaders.StaticShader;

public class Renderer {

	private final float FOV;
	private final float NEAR_PLANE;
	private final float FAR_PLANE;
	private final HashMap<TexturedModel,List<Position3>> worldModels;
	
	private Texture defaultTexture;
	private int WIDTH, HEIGHT;
	private EntityFetcher rawModels, texturedModels;
	private SingletonFetcher lights, cameras;
	private Matrix4 projectionMatrix;

	private static StaticShader shader;
	
	public Renderer(float fov, float nearPlane, float farPlane){
		FOV = fov;
		NEAR_PLANE = nearPlane;
		FAR_PLANE = farPlane;
		worldModels = new HashMap<>();
	}
	
	public void init(GL3 gl, EntitySystem es, int width, int height, Texture texture){		
		WIDTH = width;
		HEIGHT = height;
		defaultTexture = texture;
		
		rawModels = es.getEntityFetcher(RawModel.class);
		texturedModels = es.getEntityFetcher(TexturedModel.class);
		
		lights = es.getSingletonFetcher(Light.class);
		cameras = es.getSingletonFetcher(Camera.class);
		
		projectionMatrix = createProjectionMatrix(WIDTH, HEIGHT, FOV, NEAR_PLANE, FAR_PLANE);
		
		shader = new StaticShader(gl);
		gl.glActiveTexture(GL3.GL_TEXTURE0);
	}
	
	public void dispose(GL3 gl){
		shader.cleanUp();
	}

	public void render(GL3 gl, GLU glu, EntitySystem es) {
		gl.glEnable(GL3.GL_DEPTH_TEST);
		gl.glDepthRange(NEAR_PLANE, FAR_PLANE);
		
		gl.glEnable(GL3.GL_CULL_FACE);
		gl.glCullFace(GL3.GL_BACK);
		
		gl.glClearColor(1,1,1,0);
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		for (Entity e : rawModels.fetch()) {
			RawModel model = e.getAs(RawModel.class);
			Texture texture;
			if(e.hasComponent(Texture.class)){
				texture = e.getAs(Texture.class);
			} else {
				texture = defaultTexture;
			}
			
			Position3 pos = e.getAs(Position3.class);
			if(pos == null)
				pos = new Position3();
			
			TexturedModel tModel = new TexturedModel(model,texture);
			if(!worldModels.containsKey(tModel))
				worldModels.put(tModel, new ArrayList<>());
			worldModels.get(tModel).add(pos);
		}
		
		for (Entity e : texturedModels.fetch()) {
			TexturedModel tModel = e.getAs(TexturedModel.class);
			
			Position3 pos = e.getAs(Position3.class);
			if(pos == null)
				pos = new Position3();
			
			if(!worldModels.containsKey(tModel))
				worldModels.put(tModel, new ArrayList<>());
			worldModels.get(tModel).add(pos);
		}
		
		shader.start();
		
		Camera camera = (Camera) cameras.fetchComponent();
		if(camera != null){
			camera.updateTransformations();
			shader.loadCameraPosition(camera);
		} else {
			shader.loadCameraPosition(new Camera());
		}
		
		Light light = (Light) lights.fetchComponent();
		if(light != null){
			shader.loadLight(light);
		} else {
			shader.loadLight(new Light(0,0,0,1,1,1));
		}
		
		for(TexturedModel tModel : worldModels.keySet()){
			Texture texture = tModel.getTexture();
			RawModel model = tModel.getModel();
			List<Position3> positions = worldModels.get(tModel);
			
			gl.glBindVertexArray(model.getVaoID());
			gl.glBindTexture(GL3.GL_TEXTURE_2D, texture.getID());
			shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
			
			for(Position3 pos : positions){
				pos.updateTransformations();
				Matrix4 transformations = new Matrix4();
				transformations.loadIdentity();
				transformations.multMatrix(projectionMatrix);
				transformations.multMatrix(camera.getTransformations());
				transformations.multMatrix(pos.getTransformations());
				shader.loadMvpMatrix(transformations);
				shader.loadWorldMatrix(pos.getTransformations());
				gl.glDrawElements(GL3.GL_TRIANGLES, model.getIndicesCount(), GL3.GL_UNSIGNED_INT, 0);
			}
			gl.glBindVertexArray(0);
		}
		
		shader.stop();
		gl.glFlush();
		
		worldModels.clear();
	}

	private Matrix4 createProjectionMatrix(int width, int height, float fov, float near_plane, float far_plane) {
		final float aspectRatio = (float) width / (float) height;
		final float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		final float x_scale = y_scale / aspectRatio;
		final float frustum_length = far_plane - near_plane;
		final float[] pMat = { x_scale, 0, 0, 0, 0, y_scale, 0, 0, 0, 0, -((far_plane + near_plane) / frustum_length),
				-1, 0, 0, -((2 * near_plane * far_plane) / frustum_length), 0 };
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();
		mat.multMatrix(pMat);
		return mat;
	}
	
}
