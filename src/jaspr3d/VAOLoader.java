package jaspr3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

public class VAOLoader {
	
	private GL3 gl;
	private List<Integer> vaos = new ArrayList<>(), vbos = new ArrayList<>();
	
	public RawModel loadToVAO(GL3 gl, int[] indices, float[] vertices, float[] normals, float[] texCoords){
		this.gl = gl;
		int vaoID = createVAO();
		bindIndices(indices);
		storeAttribute(0, vertices, 3);
		storeAttribute(1, texCoords, 2);
		//storeAttribute(2, normals, 3);
		unbindVAO();
		return new RawModel(vaoID, vertices.length / 3, indices.length);
	}
	
	public void cleanUp(){
		IntBuffer arrays = IntBuffer.allocate(vaos.size());
		IntBuffer buffers = IntBuffer.allocate(vbos.size());
		
		for(int vao : vaos)
			arrays.put(vao);
		arrays.position(0);
		gl.glDeleteVertexArrays(vaos.size(), arrays);
		
		for(int vbo : vbos)
			buffers.put(vbo);
		buffers.position(0);
		gl.glDeleteBuffers(vbos.size(), buffers);
	}
	
	private int createVAO(){
		IntBuffer array = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, array);
		int vaoID = array.get(0);
		vaos.add(vaoID);
		gl.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void unbindVAO(){
		gl.glBindVertexArray(0);
	}
	
	private void storeAttribute(int attributeIndex, float[] data, int componentsPerVertex){
		IntBuffer buffers = IntBuffer.allocate(1); 
		gl.glGenBuffers(1, buffers);
		int vboID = buffers.get(0);
		vbos.add(vboID);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeToFloatBuffer(data);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4*data.length, buffer, GL3.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(attributeIndex);
		gl.glVertexAttribPointer(attributeIndex, componentsPerVertex, GL3.GL_FLOAT, false, 4*componentsPerVertex, 0);
		//gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
	}
	
	private void bindIndices(int[] indices){
		IntBuffer buffers = IntBuffer.allocate(1); 
		gl.glGenBuffers(1, buffers);
		int vboID = buffers.get(0);
		vbos.add(vboID);
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer indicesBuffer = storeToIntBuffer(indices);
		gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, 4*indices.length, indicesBuffer, GL3.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeToIntBuffer(int[] data){
		IntBuffer buffer = IntBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeToFloatBuffer(float[] data){
		FloatBuffer buffer = FloatBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
}
