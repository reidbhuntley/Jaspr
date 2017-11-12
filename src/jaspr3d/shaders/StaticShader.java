package jaspr3d.shaders;

import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.VAOLoader;

public class StaticShader extends ShadersProgram {
	
	private final static String VERTEX_FILE = "jaspr3d/shaders/vertexShader.txt";
	private final static String FRAGMENT_FILE = "jaspr3d/shaders/fragmentShader.txt";
	public final static int MAX_LIGHTS = 4;

	public StaticShader(GL3 gl) {
		super(gl, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(VAOLoader.ATTR_VERTICES, "position");
		super.bindAttribute(VAOLoader.ATTR_TEXCOORDS, "textureCoords");
		super.bindAttribute(VAOLoader.ATTR_NORMALS, "normal");
	}

	@Override
	protected void initUniformLocations() {
		super.initUniformLocation("modelMatrix");
		super.initUniformLocation("mvpMatrix");
		super.initUniformLocation("cameraPosition");
		super.initUniformLocation("shineDamper");
		super.initUniformLocation("reflectivity");
		for(int i = 0; i < MAX_LIGHTS; i++){
			super.initUniformLocation("lightPosition["+i+"]");
			super.initUniformLocation("lightColor["+i+"]");
		}
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity){
		super.loadFloat(getUniformLocation("shineDamper"), shineDamper);
		super.loadFloat(getUniformLocation("reflectivity"), reflectivity);
	}
	
	public void loadCameraPosition(Camera pos){
		super.loadVector(getUniformLocation("cameraPosition"),pos.x(),pos.y(),pos.z());
	}
	
	public void loadWorldMatrix(Matrix4 matrix){
		super.loadMatrix(getUniformLocation("modelMatrix"), matrix);
	}
	
	public void loadMvpMatrix(Matrix4 matrix){
		super.loadMatrix(getUniformLocation("mvpMatrix"), matrix);
	}
	
	public void loadLights(List<Light> lights){
		for(int i = 0; i < MAX_LIGHTS; i++){
			Light light;
			float[] color;
			if(i < lights.size()){
				light = lights.get(i);
				color = light.getColor();
			} else {
				light = new Light(0,0,0,0,0,0);
				color = new float[3];
			}
			super.loadVector(getUniformLocation("lightPosition["+i+"]"),light.x(),light.y(),light.z());
			super.loadVector(getUniformLocation("lightColor["+i+"]"),color[0],color[1],color[2]);
		}
	}

}
