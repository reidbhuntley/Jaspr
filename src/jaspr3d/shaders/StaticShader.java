package jaspr3d.shaders;

import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.VAOLoader;
import jaspr3d.Vector3;

public class StaticShader extends ShadersProgram {
	
	private final static String VERTEX_FILE = "jaspr3d/shaders/vertexShader.txt";
	private final static String FRAGMENT_FILE = "jaspr3d/shaders/fragmentShader.txt";
	public final static int MAX_LIGHTS = 4;

	public StaticShader(GL3 gl) {
		super(gl, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes(GL3 gl) {
		super.bindAttribute(gl, VAOLoader.ATTR_VERTICES, "position");
		super.bindAttribute(gl, VAOLoader.ATTR_TEXCOORDS, "textureCoords");
		super.bindAttribute(gl, VAOLoader.ATTR_NORMALS, "normal");
	}

	@Override
	protected void initUniformLocations(GL3 gl) {
		super.initUniformLocation(gl, "modelMatrix");
		super.initUniformLocation(gl, "mvpMatrix");
		super.initUniformLocation(gl, "cameraPosition");
		super.initUniformLocation(gl, "shineDamper");
		super.initUniformLocation(gl, "reflectivity");
		for(int i = 0; i < MAX_LIGHTS; i++){
			super.initUniformLocation(gl, "lightPosition["+i+"]");
			super.initUniformLocation(gl, "lightColor["+i+"]");
			super.initUniformLocation(gl, "attenuation["+i+"]");
		}
	}
	
	public void loadShineVariables(GL3 gl, float shineDamper, float reflectivity){
		super.loadFloat(gl, getUniformLocation("shineDamper"), shineDamper);
		super.loadFloat(gl, getUniformLocation("reflectivity"), reflectivity);
	}
	
	public void loadCameraPosition(GL3 gl, Camera pos){
		super.loadVector(gl, getUniformLocation("cameraPosition"),pos.x(),pos.y(),pos.z());
	}
	
	public void loadWorldMatrix(GL3 gl, Matrix4 matrix){
		super.loadMatrix(gl, getUniformLocation("modelMatrix"), matrix);
	}
	
	public void loadMvpMatrix(GL3 gl, Matrix4 matrix){
		super.loadMatrix(gl, getUniformLocation("mvpMatrix"), matrix);
	}
	
	public void loadLights(GL3 gl, List<Light> lights){
		for(int i = 0; i < MAX_LIGHTS; i++){
			Light light;
			Vector3 color, attenuation;
			if(i < lights.size()){
				light = lights.get(i);
				color = light.getColor();
				attenuation = light.getAttenuation();
			} else {
				light = new Light();
				color = new Vector3();
				attenuation = new Vector3(1,0,0);
			}
			super.loadVector(gl, getUniformLocation("lightPosition["+i+"]"),light.x(),light.y(),light.z());
			super.loadVector(gl, getUniformLocation("lightColor["+i+"]"),color.x(),color.y(),color.z());
			super.loadVector(gl, getUniformLocation("attenuation["+i+"]"),attenuation.x(),attenuation.y(),attenuation.z());
		}
	}

}
