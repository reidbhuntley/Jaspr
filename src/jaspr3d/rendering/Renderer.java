package jaspr3d.rendering;

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
import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.Position3;
import jaspr3d.Mesh;
import jaspr3d.Texture;
import jaspr3d.Model;
import jaspr3d.Vector3;
import jaspr3d.shaders.StaticShader;

public class Renderer {

	private final float NEAR_PLANE, FAR_PLANE, FOV;
	private final HashMap<Model,List<Position3>> worldModels;
	private Texture defaultTexture;
	private ViewFrustum frustum;
	private Matrix4 projectionMatrix;
	private int WIDTH, HEIGHT;
	
	private EntityFetcher rawModels, texturedModels, lights;
	private SingletonFetcher cameras;

	private StaticShader shader;
	private SkyboxRenderer skybox;
	
	private EntitySystem es;
	
	public Renderer(EntitySystem es, float fov, float nearPlane, float farPlane){
		this.es = es;
		NEAR_PLANE = nearPlane;
		FAR_PLANE = farPlane;
		FOV = fov;
		worldModels = new HashMap<>();
	}
	
	public void init(GL3 gl, int width, int height, Texture texture){		
		WIDTH = width;
		HEIGHT = height;
		defaultTexture = texture;
		rawModels = es.getEntityFetcher(Mesh.class);
		texturedModels = es.getEntityFetcher(Model.class);
		lights = es.getEntityFetcher(Light.class);
		
		cameras = es.getSingletonFetcher(Camera.class);
		
		frustum = new ViewFrustum(WIDTH, HEIGHT, FOV, NEAR_PLANE, FAR_PLANE);
		projectionMatrix = frustum.getProjectionMatrix();
		
		
		shader = new StaticShader(gl);
		skybox = new SkyboxRenderer(gl, projectionMatrix);
		
		gl.glActiveTexture(GL3.GL_TEXTURE0);
	}
	
	public void dispose(GL3 gl){
		shader.cleanUp(gl);
		skybox.cleanUp(gl);
	}

	public void render(GL3 gl, GLU glu) {
		
		Camera camera = prepareCamera();
		
		gl.glEnable(GL3.GL_CULL_FACE);
		gl.glCullFace(GL3.GL_BACK);
		
		gl.glClearColor(1,1,1,0);
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		gl.glDisable(GL3.GL_DEPTH_TEST);
		
		skybox.render(gl, camera);
		
		gl.glEnable(GL3.GL_DEPTH_TEST);
		gl.glDepthRange(NEAR_PLANE, FAR_PLANE);
		
		
		fetchTexturedModels();
		shader.start(gl);		
		shader.loadCameraPosition(gl, camera);
		
		for(Model tModel : worldModels.keySet()){
			Texture texture = tModel.getTexture();
			Mesh model = tModel.getModel();
			List<Position3> positions = worldModels.get(tModel);
			
			gl.glBindVertexArray(model.getVaoID());
			gl.glBindTexture(GL3.GL_TEXTURE_2D, texture.getID());
			shader.loadShineVariables(gl, texture.getShineDamper(), texture.getReflectivity());
			
			for(Position3 pos : positions){
				if(!frustum.sphereIntersects(Vector3.add(pos.getVec(), model.getCenter()), model.getRadius()))
					continue;
				
				pos.updateTransformations();
				Matrix4 transformations = new Matrix4();
				transformations.loadIdentity();
				transformations.multMatrix(projectionMatrix);
				transformations.multMatrix(camera.getTransformations());
				transformations.multMatrix(pos.getTransformations());
				
				shader.loadMvpMatrix(gl, transformations);
				shader.loadWorldMatrix(gl, pos.getTransformations());
				shader.loadLights(gl, getClosestLights(pos, StaticShader.MAX_LIGHTS));
				gl.glDrawElements(GL3.GL_TRIANGLES, model.getIndicesCount(), GL3.GL_UNSIGNED_INT, 0);
			}
			gl.glBindVertexArray(0);
		}
		
		shader.stop(gl);
		worldModels.clear();
		
		gl.glFlush();
	}
	
	
	private void fetchTexturedModels(){
		for (Entity e : rawModels.fetch()) {
			Mesh model = e.readAs(Mesh.class);
			Texture texture;
			if(e.hasComponent(Texture.class)){
				texture = e.readAs(Texture.class);
			} else {
				texture = defaultTexture;
			}
			
			Position3 pos = e.readAs(Position3.class);
			if(pos == null)
				pos = new Position3();
			
			Model tModel = new Model(model,texture);
			if(!worldModels.containsKey(tModel))
				worldModels.put(tModel, new ArrayList<>());
			worldModels.get(tModel).add(pos);
		}
		
		for (Entity e : texturedModels.fetch()) {
			Model tModel = e.readAs(Model.class);
			
			Position3 pos = e.readAs(Position3.class);
			if(pos == null)
				pos = new Position3();
			
			if(!worldModels.containsKey(tModel))
				worldModels.put(tModel, new ArrayList<>());
			worldModels.get(tModel).add(pos);
		}
	}
	
	private Camera prepareCamera(){
		Camera camera = (Camera) cameras.readComponent();
		if(camera == null)
			camera = new Camera();
		frustum.genPlanes(camera);
		return camera;
	}
	
	private List<Light> getClosestLights(Position3 pos, int amt){
		List<Float> closestDistances = new ArrayList<>();
		List<Light> closestLights = new ArrayList<>();
		for(Entity e : lights.fetch()){
			Light light = e.readAs(Light.class);
			float distance = Vector3.add(pos.getVec(), Vector3.scale(light, -1)).magnitude();
			for(int i = 0; i < amt; i++){
				if(i >= closestDistances.size()){
					closestDistances.add(i, distance);
					closestLights.add(i, light);
					break;
				}
				if(distance < closestDistances.get(i)){
					closestDistances.add(i, distance);
					closestLights.add(i, light);
					if(closestDistances.size() > amt){
						closestDistances.remove(amt);
						closestLights.remove(amt);
					}
					break;
				}
			}
		}
		return closestLights;
	}
	
}
