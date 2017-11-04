package jaspr3d.shaders;

import com.jogamp.opengl.GL3;

public class StaticShader extends ShadersProgram {
	
	private final static String VERTEX_FILE = "src/jaspr3d/shaders/vertexShader.txt";
	private final static String FRAGMENT_FILE = "src/jaspr3d/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public StaticShader(GL3 gl) {
		super(gl, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(float[] matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(float[] matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(float[] matrix) {
		super.loadMatrix(location_viewMatrix, matrix);
	}

}
