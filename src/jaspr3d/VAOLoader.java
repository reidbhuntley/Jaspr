package jaspr3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jogamp.opengl.GL3;

public class VAOLoader {

	private HashMap<Integer,Integer> currentVbos;
	private List<Integer> vaos, vbos;
	private final static int SIZE_FLOAT = 4, SIZE_INT = 4;
	public final static int ATTR_INDICES = -1, ATTR_VERTICES = 0, ATTR_TEXCOORDS = 1, ATTR_NORMALS = 2;
	
	public VAOLoader(){
		vaos = new ArrayList<>();
		vbos = new ArrayList<>();
	}

	public void loadToVAO(GL3 gl, Mesh mesh) {
		MeshData data = mesh.getData();
		int dimensions = data.getDimensions();
		int[] indices = data.getIndices();
		float[] vertices = data.getVertices();
		float[] normals = data.getNormals();
		float[] texCoords = data.getTexCoords();
		
		currentVbos = new HashMap<>();
		int vaoID = createVAO(gl);
		
		if(indices.length > 0)
			bindIndices(gl, indices);
		storeAttribute(gl, ATTR_VERTICES, vertices, dimensions);
		if(texCoords.length > 0)
			storeAttribute(gl, ATTR_TEXCOORDS, texCoords, 2);
		if(normals.length > 0)
			storeAttribute(gl, ATTR_NORMALS, normals, dimensions);
		unbindVAO(gl);
		mesh.load(currentVbos, vaoID);
	}

	public void cleanUp(GL3 gl) {
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

	private int createVAO(GL3 gl) {
		IntBuffer array = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, array);
		int vaoID = array.get(0);
		vaos.add(vaoID);
		gl.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void unbindVAO(GL3 gl) {
		gl.glBindVertexArray(0);
	}

	private void storeAttribute(GL3 gl, int attributeIndex, float[] data, int componentsPerVertex) {
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

	private void bindIndices(GL3 gl, int[] indices) {
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
