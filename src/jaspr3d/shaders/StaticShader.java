package jaspr3d.shaders;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import jaspr3d.Camera;
import jaspr3d.Light;
import jaspr3d.VAOLoader;

public class StaticShader extends ShadersProgram {
	
	private final static String VERTEX_FILE = "jaspr3d/shaders/vertexShader.txt";
	private final static String FRAGMENT_FILE = "jaspr3d/shaders/fragmentShader.txt";

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
		super.loadVector(getUniformLocation("cameraPosition"),pos.x(),pos.y(),pos.z());
	}
	
	public void loadWorldMatrix(Matrix4 matrix){
		super.loadMatrix(getUniformLocation("modelMatrix"), matrix);
	}
	
	public void loadMvpMatrix(Matrix4 matrix){
		super.loadMatrix(getUniformLocation("mvpMatrix"), matrix);
	}
	
	public void loadLight(Light light){
		float[] color = light.getColor();
		super.loadVector(getUniformLocation("lightPosition"),light.x(),light.y(),light.z());
		super.loadVector(getUniformLocation("lightColor"),color[0],color[1],color[2]);
	}

}
