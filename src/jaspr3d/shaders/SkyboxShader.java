package jaspr3d.shaders;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import jaspr3d.VAOLoader;

public class SkyboxShader extends ShadersProgram {

	private static final String VERTEX_FILE = "jaspr3d/shaders/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "jaspr3d/shaders/skyboxFragmentShader.txt";

	public SkyboxShader(GL3 gl) {
		super(gl, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void initUniformLocations(GL3 gl) {
		super.initUniformLocation(gl, "vpMatrix");
	}

	@Override
	protected void bindAttributes(GL3 gl) {
		super.bindAttribute(gl, VAOLoader.ATTR_VERTICES, "position");
	}

	public void loadVpMatrix(GL3 gl, Matrix4 matrix) {
		super.loadMatrix(gl, super.getUniformLocation("vpMatrix"), matrix);
	}

}
