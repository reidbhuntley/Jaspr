package jaspr3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jogamp.opengl.GL3;

public class VAOLoader {

	private GL3 gl;
	private HashMap<Integer,Integer> currentVbos;
	private List<Integer> vaos, vbos;
	private final static int SIZE_FLOAT = 4, SIZE_INT = 4;
	public final static int ATTR_INDICES = -1, ATTR_VERTICES = 0, ATTR_TEXCOORDS = 1, ATTR_NORMALS = 2;
	
	public VAOLoader(){
		vaos = new ArrayList<>();
		vbos = new ArrayList<>();
	}

	public RawModel loadToVAO(GL3 gl, int[] indices, float[] vertices, float[] normals, float[] texCoords, Vector3 center, float radius) {
		this.gl = gl;
		currentVbos = new HashMap<>();
		int vaoID = createVAO();
		bindIndices(indices);
		storeAttribute(ATTR_VERTICES, vertices, 3);
		storeAttribute(ATTR_TEXCOORDS, texCoords, 2);
		storeAttribute(ATTR_NORMALS, normals, 3);
		unbindVAO();
		return new RawModel(currentVbos, vaoID, vertices.length / 3, indices.length, center, radius);
	}
	
	public RawPanel loadToVAO(GL3 gl, float[] positions, int dimensions){
		this.gl = gl;
		currentVbos = new HashMap<>();
		int vaoID = createVAO();
		storeAttribute(ATTR_VERTICES, positions, dimensions);
		unbindVAO();
		return new RawPanel(currentVbos, vaoID, positions.length/2);
	}

	public void cleanUp() {
		IntBuffer arrays = IntBuffer.allocate(vaos.size());
		IntBuffer buffers = IntBuffer.allocate(vbos.size());

		for (int vao : vaos)
			arrays.put(vao);
		arrays.position(0);
		gl.glDeleteVertexArrays(vaos.size(), arrays);

		for (int vbo : vbos)
			buffers.put(vbo);
		buffers.position(0);
		gl.glDeleteBuffers(vbos.size(), buffers);
	}

	private int createVAO() {
		IntBuffer array = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, array);
		int vaoID = array.get(0);
		vaos.add(vaoID);
		gl.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void unbindVAO() {
		gl.glBindVertexArray(0);
	}

	private void storeAttribute(int attributeIndex, float[] data, int componentsPerVertex) {
		IntBuffer buffers = IntBuffer.allocate(1);
		gl.glGenBuffers(1, buffers);
		int vboID = buffers.get(0);
		addVBO(attributeIndex, vboID);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeToFloatBuffer(data);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, SIZE_FLOAT*data.length, buffer, GL3.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(attributeIndex);
		gl.glVertexAttribPointer(attributeIndex, componentsPerVertex, GL3.GL_FLOAT, false, 0, 0);
		// gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
	}

	private void bindIndices(int[] indices) {
		IntBuffer buffers = IntBuffer.allocate(1);
		gl.glGenBuffers(1, buffers);
		int vboID = buffers.get(0);
		addVBO(ATTR_INDICES, vboID);
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer indicesBuffer = storeToIntBuffer(indices);
		gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, SIZE_INT*indices.length, indicesBuffer, GL3.GL_STATIC_DRAW);
	}
	
	private void addVBO(int attr, int loc){
		vbos.add(loc);
		currentVbos.put(attr, loc);
	}

	private IntBuffer storeToIntBuffer(int[] data) {
		IntBuffer buffer = IntBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private FloatBuffer storeToFloatBuffer(float[] data) {
		FloatBuffer buffer = FloatBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public int getBufferLoc(int attr) {
		return vbos.get(attr);
	}

}
