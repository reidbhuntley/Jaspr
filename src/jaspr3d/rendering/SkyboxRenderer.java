package jaspr3d.rendering;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import jaspr3d.Camera;
import jaspr3d.Position3;
import jaspr3d.RawPanel;
import jaspr3d.VAOLoader;
import jaspr3d.shaders.SkyboxShader;
import res.CubemapManager;

public class SkyboxRenderer {

	private static final float SIZE = 2100;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static final String[] TEXTURE_FILES = {"right.png", "left.png", "top.png", "bottom.png", "back.png", "front.png"};
	
	private RawPanel cube;
	private int texture;
	private SkyboxShader shader;
	private VAOLoader loader;
	private CubemapManager cubemaps;
	private Matrix4 transformations;
	private GL3 gl;
	
	public SkyboxRenderer(GL3 gl, Matrix4 projectionMatrix){
		this.gl = gl;
		loader = new VAOLoader();
		cube = loader.loadToVAO(gl, VERTICES, 3);
		cubemaps = new CubemapManager(gl);
		cubemaps.preload();
		texture = cubemaps.loadCubeMap(TEXTURE_FILES);
		shader = new SkyboxShader(gl);
		transformations = projectionMatrix;
	}
	
	public void render(Camera camera){
		Position3 newPos = new Position3(camera);
		newPos.set(0, 0, 0, newPos.pitch(), newPos.yaw(), newPos.roll());
		newPos.updateTransformations();
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();
		mat.multMatrix(transformations);
		mat.multMatrix(newPos.getTransformations());
		shader.start();
		shader.loadVpMatrix(mat);
		gl.glBindVertexArray(cube.getVaoID());
		gl.glActiveTexture(GL3.GL_TEXTURE0);
		gl.glBindTexture(GL3.GL_TEXTURE_CUBE_MAP, texture);
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, cube.getVertexCount());
		gl.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
		loader.cleanUp();
		cubemaps.cleanUp();
	}
	
}
