package jaspr3d.shaders;

import com.jogamp.opengl.GL3;

import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.Position;

public class StaticShader extends ShadersProgram {
	
	private final static String VERTEX_FILE = "jaspr3d/shaders/vertexShader.txt";
	private final static String FRAGMENT_FILE = "jaspr3d/shaders/fragmentShader.txt";

	public StaticShader(GL3 gl) {
		super(gl, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void initUniformLocations() {
		super.initUniformLocation("transformationMatrix");
		super.initUniformLocation("projectionMatrix");
		super.initUniformLocation("viewMatrix");
		super.initUniformLocation("cameraPosition");
		super.initUniformLocation("lightPosition");
		super.initUniformLocation("lightColor");
		super.initUniformLocation("shineDamper");
		super.initUniformLocation("reflectivity");
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity){
		super.loadFloat(getUniformLocation("shineDamper"), shineDamper);
		super.loadFloat(getUniformLocation("reflectivity"), reflectivity);
	}
	
	public void loadCameraPosition(Camera pos){
		float[] coords = pos.getCoords();
		super.loadVector(getUniformLocation("cameraPosition"),coords[0],coords[1],coords[2]);
	}
	
	public void loadTransformationMatrix(float[] matrix){
		super.loadMatrix(getUniformLocation("transformationMatrix"), matrix);
	}
	
	public void loadProjectionMatrix(float[] matrix){
		super.loadMatrix(getUniformLocation("projectionMatrix"), matrix);
	}

	public void loadViewMatrix(float[] matrix) {
		super.loadMatrix(getUniformLocation("viewMatrix"), matrix);
	}
	
	public void loadLight(Light light, Position pos){
		float[] coords = pos.getCoords(), color = light.getColor();
		super.loadVector(getUniformLocation("lightPosition"),coords[0],coords[1],coords[2]);
		super.loadVector(getUniformLocation("lightColor"),color[0],color[1],color[2]);
	}

}
