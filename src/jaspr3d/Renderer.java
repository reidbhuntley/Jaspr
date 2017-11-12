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

	private final float NEAR_PLANE, FAR_PLANE, FOV;
	private final HashMap<TexturedModel,List<Position3>> worldModels;
	
	private Texture defaultTexture;
	private int WIDTH, HEIGHT;
	private EntityFetcher rawModels, texturedModels, lights;
	private SingletonFetcher cameras;
	private Matrix4 projectionMatrix;
	private ViewFrustum frustum;

	private static StaticShader shader;
	
	public Renderer(float fov, float nearPlane, float farPlane){
		NEAR_PLANE = nearPlane;
		FAR_PLANE = farPlane;
		FOV = fov;
		worldModels = new HashMap<>();
	}
	
	public void init(GL3 gl, EntitySystem es, int width, int height, Texture texture){		
		WIDTH = width;
		HEIGHT = height;
		defaultTexture = texture;
		
		rawModels = es.getEntityFetcher(RawModel.class);
		texturedModels = es.getEntityFetcher(TexturedModel.class);
		lights = es.getEntityFetcher(Light.class);
		
		cameras = es.getSingletonFetcher(Camera.class);
		
		frustum = new ViewFrustum(WIDTH, HEIGHT, FOV, NEAR_PLANE, FAR_PLANE);
		projectionMatrix = frustum.getProjectionMatrix();
		
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
		if(camera == null)
			camera = new Camera();
		camera.updateTransformations();
		frustum.genPlanes(camera);
		shader.loadCameraPosition(camera);
		
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
				
				if(!frustum.sphereIntersects(Vector3.add(pos.getVec(), model.getCenter()), model.getRadius()))
					continue;
				
				List<Float> closestDistances = new ArrayList<>();
				List<Light> closestLights = new ArrayList<>();
				for(Entity e : lights.fetch()){
					Light light = e.getAs(Light.class);
					float distance = Vector3.add(pos.getVec(), Vector3.scale(light, -1)).magnitude();
					for(int i = 0; i < StaticShader.MAX_LIGHTS; i++){
						if(i >= closestDistances.size()){
							closestDistances.add(i, distance);
							closestLights.add(i, light);
							break;
						}
						if(distance < closestDistances.get(i)){
							closestDistances.add(i, distance);
							closestLights.add(i, light);
							if(closestDistances.size() > StaticShader.MAX_LIGHTS){
								closestDistances.remove(StaticShader.MAX_LIGHTS);
								closestLights.remove(StaticShader.MAX_LIGHTS);
							}
							break;
						}
					}
				}
				shader.loadLights(closestLights);
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
	
}
