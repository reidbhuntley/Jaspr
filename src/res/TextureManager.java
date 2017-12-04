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

import core.GLEvent;
import core.GameWindow;
import jaspr3d.Texture;

public class TextureManager extends AssetType<Texture> {

	private List<Integer> textureIDs;
	private GameWindow window;
	private final String defaultTexturePath = "default.png";

	public TextureManager(GameWindow window) {
		super("textures", "png");
		this.window = window;
		this.textureIDs = new ArrayList<>();
	}
	
	@Override
	public Texture loadAssetFromFile(InputStream in, String name) throws IOException {
		
		Texture texture = new Texture(in);
		
		window.addGLEvent(new GLEvent(){
			public void run(GL3 gl){
				TextureData data;
				try {
					data = TextureIO.newTextureData(gl.getGLProfile(), texture.getData(), false, TextureIO.PNG);
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
					
					texture.load(textureID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		return texture;
	}

	public void cleanUp(GL3 gl) {
		IntBuffer ids = IntBuffer.allocate(textureIDs.size());
		for (int id : textureIDs)
			ids.put(id);
		ids.flip();
		gl.glDeleteTextures(ids.remaining(), ids);
	}
	
	public Texture get(String filename, float shineDamper, float reflectivity) {
		Texture texture = getRawAsset(filename);
		if(texture == null){
			throw new IllegalArgumentException("Missing texture file '"+filename+"'");
		}
		return new Texture(getRawAsset(filename).getID(), shineDamper, reflectivity);
	}
	
	public Texture get(String filename) {
		return getRawAsset(filename);
	}

	public Texture defaultTexture() {
		return getRawAsset(defaultTexturePath,true);
	}

}
