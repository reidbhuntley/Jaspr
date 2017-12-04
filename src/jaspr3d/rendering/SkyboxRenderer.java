package jaspr3d.rendering;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import jaspr3d.Camera;
import jaspr3d.Position3;
import jaspr3d.Mesh;
import jaspr3d.MeshData;
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
	
	private Mesh cube;
	private int texture;
	private SkyboxShader shader;
	private VAOLoader loader;
	private CubemapManager cubemaps;
	private Matrix4 transformations;
	
	public SkyboxRenderer(GL3 gl, Matrix4 projectionMatrix){
		loader = new VAOLoader();
		cube = new Mesh(new MeshData(3, VERTICES));
		loader.loadToVAO(gl, cube);
		cubemaps = new CubemapManager();
		cubemaps.preload();
		texture = cubemaps.loadCubeMap(gl, TEXTURE_FILES);
		shader = new SkyboxShader(gl);
		transformations = projectionMatrix;
	}
	
	public void render(GL3 gl, Camera camera){
		Position3 newPos = new Position3(camera);
		newPos.set(0, 0, 0, newPos.pitch(), newPos.yaw(), newPos.roll());
		newPos.updateTransformations();
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();
		mat.multMatrix(transformations);
		mat.multMatrix(newPos.getTransformations());
		shader.start(gl);
		shader.loadVpMatrix(gl, mat);
		gl.glBindVertexArray(cube.getVaoID());
		gl.glActiveTexture(GL3.GL_TEXTURE0);
		gl.glBindTexture(GL3.GL_TEXTURE_CUBE_MAP, texture);
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, cube.getVertexCount());
		gl.glBindVertexArray(0);
		shader.stop(gl);
	}
	
	public void cleanUp(GL3 gl){
		shader.cleanUp(gl);
		loader.cleanUp(gl);
		cubemaps.cleanUp(gl);
	}
	
}
