package res;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import jaspr3d.Texture;

public class TextureManager extends AssetType<Texture> {

	private GL3 gl;
	private List<Integer> textureIDs;
	private final String defaultTexturePath;
	private Texture defaultTexture;

	public TextureManager(String defaultTexturePath) {
		textureIDs = new ArrayList<>();
		this.defaultTexturePath = defaultTexturePath;
	}

	public void preload(GL3 gl) {
		this.gl = gl;
		loadFromDir(folderName());
		defaultTexture = assets().get(defaultTexturePath);
	}

	@Override
	public void preload() {

	}

	@Override
	public String folderName() {
		return "textures";
	}

	@Override
	public Texture load(InputStream in, String name) throws IOException {
		TextureData data = TextureIO.newTextureData(gl.getGLProfile(), in, false, TextureIO.PNG);

		int level = 0;

		IntBuffer buffer = IntBuffer.allocate(1);
		gl.glGenTextures(1, buffer);
		int textureID = buffer.get(0);
		textureIDs.add(textureID);
		gl.glBindTexture(GL3.GL_TEXTURE_2D, textureID);
		gl.glTexImage2D(GL3.GL_TEXTURE_2D, level, data.getInternalFormat(), data.getWidth(), data.getHeight(),
				data.getBorder(), data.getPixelFormat(), data.getPixelType(), data.getBuffer());
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_BASE_LEVEL, 0);
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAX_LEVEL, level);
		
		IntBuffer swizzle = GLBuffers
				.newDirectIntBuffer(new int[] { GL3.GL_RED, GL3.GL_GREEN, GL3.GL_BLUE, GL3.GL_ONE });
		gl.glTexParameterIiv(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_SWIZZLE_RGBA, swizzle);
		
		gl.glGenerateMipmap(GL3.GL_TEXTURE_2D);
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR_MIPMAP_LINEAR);
		
		gl.glBindTexture(GL3.GL_TEXTURE_2D, 0);
		return new Texture(textureID);
	}

	public void cleanUp() {
		IntBuffer ids = IntBuffer.allocate(textureIDs.size());
		for (int id : textureIDs)
			ids.put(id);
		ids.flip();
		gl.glDeleteTextures(ids.remaining(), ids);
	}

	public Texture get(String filename, float shineDamper, float reflectivity) {
		return new Texture(assets().get(filename).getID(), shineDamper, reflectivity);
	}
	
	public Texture get(String filename) {
		return assets().get(filename);
	}

	public Texture defaultTexture() {
		return defaultTexture;
	}

	@Override
	public String extension() {
		return "png";
	}

}