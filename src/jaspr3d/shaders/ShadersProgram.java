package jaspr3d.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jogamp.opengl.GL3;

public abstract class ShadersProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private GL3 gl;
	private HashMap<String,Integer> attributes;

	public ShadersProgram(GL3 gl, String vertexFile, String fragmentFile) {
		this.gl = gl;
		attributes = new HashMap<>();
		vertexShaderID = loadShader(vertexFile, GL3.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL3.GL_FRAGMENT_SHADER);
		programID = gl.glCreateProgram();
		gl.glAttachShader(programID, vertexShaderID);
		gl.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		gl.glLinkProgram(programID);
		gl.glValidateProgram(programID);
		initUniformLocations();
	}
	
	protected abstract void initUniformLocations();
	
	protected int initUniformLocation(String uniformName){
		int location = gl.glGetUniformLocation(programID, uniformName);
		attributes.put(uniformName, location);
		return location;
	}
	
	protected int getUniformLocation(String uniformName){
		return attributes.get(uniformName);
	}
	
	protected void loadFloat(int location, float value){
		gl.glUniform1f(location, value);
	}

	protected void loadVector(int location, float v0, float v1, float v2){
		gl.glUniform3f(location, v0, v1, v2);
	}
	
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value)
			toLoad = 1;
		else
			toLoad = 0;
		gl.glUniform1f(location, toLoad);
	}
	
	protected void loadMatrix(int location, float[] matrix){
		gl.glUniformMatrix4fv(location, 1, false, matrix, 0);
	}

	public void start() {
		gl.glUseProgram(programID);
	}

	public void stop() {
		gl.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		gl.glDetachShader(programID, vertexShaderID);
		gl.glDetachShader(programID, fragmentShaderID);
		gl.glDeleteShader(vertexShaderID);
		gl.glDeleteShader(fragmentShaderID);
		gl.glDeleteProgram(programID);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName) {
		gl.glBindAttribLocation(programID, attribute, variableName);
	}

	private int loadShader(String file, int type) {
		List<String> lines = new ArrayList<>();
		List<Integer> lengths = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if(lines.size() > 0)
					line = System.getProperty("line.separator") + line;
				lines.add(line);
				lengths.add(line.length());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		String[] shaderSource = new String[lines.size()];
		int[] lengthsArray = new int[lengths.size()];

		lines.toArray(shaderSource);
		
		for(int i = 0; i < lengths.size(); i++)
			lengthsArray[i] = lengths.get(i);
		IntBuffer shaderLengths = IntBuffer.wrap(lengthsArray);
		
		int shaderID = gl.glCreateShader(type);
		gl.glShaderSource(shaderID, lengthsArray.length, shaderSource, shaderLengths);
		gl.glCompileShader(shaderID);
		IntBuffer status = IntBuffer.allocate(1);
		gl.glGetShaderiv(shaderID, GL3.GL_COMPILE_STATUS, status);
		if (status.get(0) == GL3.GL_FALSE) {
			ByteBuffer logBytes = ByteBuffer.allocate(200);
			gl.glGetShaderInfoLog(shaderID, 200, IntBuffer.allocate(1), logBytes);
			System.err.println("Could not compile shader!");
			for(int i = 0; i < logBytes.remaining(); i++)
				System.err.print((char)logBytes.get(i));
			System.exit(-1);
		}
		return shaderID;
	}

}