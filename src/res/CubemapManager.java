package res;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import jaspr3d.CubemapData;

public class CubemapManager extends AssetType<CubemapData> {

	private List<Integer> textureIDs;

	public CubemapManager() {
		super("cubemaps", "png");
		textureIDs = new ArrayList<>();
	}

	@Override
	public CubemapData loadAssetFromFile(InputStream in, String name) throws IOException {
		PNGDecoder decoder = new PNGDecoder(in);
		int width = decoder.getWidth();
		int height = decoder.getHeight();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
		decoder.decode(buffer, width * 4, Format.RGBA);
		buffer.flip();
		in.close();
		return new CubemapData(width,height,buffer);
	}

	public int loadCubeMap(GL3 gl, String[] textureFiles) {
		IntBuffer ids = IntBuffer.allocate(1);
		gl.glGenTextures(1, ids);
		int texID = ids.get(0);
		gl.glActiveTexture(texID);
		gl.glBindTexture(GL3.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			CubemapData data = getRawAsset(textureFiles[i]);
			gl.glTexImage2D(GL3.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL3.GL_RGBA, data.getWidth(), data.getHeight(),
					0, GL3.GL_RGBA, GL3.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);

		textureIDs.add(texID);
		return texID;
	}

	public void cleanUp(GL3 gl) {
		IntBuffer ids = IntBuffer.allocate(textureIDs.size());
		for (int id : textureIDs)
			ids.put(id);
		ids.flip();
		gl.glDeleteTextures(ids.remaining(), ids);
	}

}
