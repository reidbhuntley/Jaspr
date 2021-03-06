package jaspr3d.shaders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

public abstract class ShadersProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private HashMap<String, Integer> attributes;

	public ShadersProgram(GL3 gl, String vertexFile, String fragmentFile) {
		attributes = new HashMap<>();
		vertexShaderID = loadShader(gl, vertexFile, GL3.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(gl, fragmentFile, GL3.GL_FRAGMENT_SHADER);
		programID = gl.glCreateProgram();
		gl.glAttachShader(programID, vertexShaderID);
		gl.glAttachShader(programID, fragmentShaderID);
		bindAttributes(gl);
		gl.glLinkProgram(programID);
		gl.glValidateProgram(programID);
		initUniformLocations(gl);
	}

	protected abstract void initUniformLocations(GL3 gl);

	protected int initUniformLocation(GL3 gl, String uniformName) {
		int location = gl.glGetUniformLocation(programID, uniformName);
		attributes.put(uniformName, location);
		return location;
	}

	protected int getUniformLocation(String uniformName) {
		return attributes.get(uniformName);
	}

	protected void loadFloat(GL3 gl, int location, float value) {
		gl.glUniform1f(location, value);
	}

	protected void loadVector(GL3 gl, int location, float v0, float v1, float v2) {
		gl.glUniform3f(location, v0, v1, v2);
	}

	protected void loadBoolean(GL3 gl, int location, boolean value) {
		float toLoad = 0;
		if (value)
			toLoad = 1;
		else
			toLoad = 0;
		gl.glUniform1f(location, toLoad);
	}

	protected void loadMatrix(GL3 gl, int location, Matrix4 matrix) {
		gl.glUniformMatrix4fv(location, 1, false, matrix.getMatrix(), 0);
	}

	public void start(GL3 gl) {
		gl.glUseProgram(programID);
	}

	public void stop(GL3 gl) {
		gl.glUseProgram(0);
	}

	public void cleanUp(GL3 gl) {
		stop(gl);
		gl.glDetachShader(programID, vertexShaderID);
		gl.glDetachShader(programID, fragmentShaderID);
		gl.glDeleteShader(vertexShaderID);
		gl.glDeleteShader(fragmentShaderID);
		gl.glDeleteProgram(programID);
	}

	protected abstract void bindAttributes(GL3 gl);

	protected void bindAttribute(GL3 gl, int attribute, String variableName) {
		gl.glBindAttribLocation(programID, attribute, variableName);
	}

	private int loadShader(GL3 gl, String file, int type) {
		List<String> lines = new ArrayList<>();
		List<Integer> lengths = new ArrayList<>();
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
			String line;
			byte[] lineBytes;
			while (true) {
				lineBytes = new byte[in.available()];
				if (in.read(lineBytes) == 0)
					break;
				line = new String(lineBytes);
				if (lines.size() > 0)
					line = System.getProperty("line.separator") + line;
				lines.add(line);
				lengths.add(line.length());
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		String[] shaderSource = new String[lines.size()];
		int[] lengthsArray = new int[lengths.size()];

		lines.toArray(shaderSource);

		for (int i = 0; i < lengths.size(); i++)
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
			for (int i = 0; i < logBytes.remaining(); i++)
				System.err.print((char) logBytes.get(i));
			System.exit(-1);
		}
		return shaderID;
	}

}