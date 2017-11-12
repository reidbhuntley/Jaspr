package jaspr3d;

import java.nio.ByteBuffer;

public class TextureData {

	private int width, height;
	private ByteBuffer buffer;
	public TextureData(int width, int height, ByteBuffer buffer) {
		super();
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
}
