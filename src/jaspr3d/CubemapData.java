package jaspr3d;

import java.nio.ByteBuffer;

public class CubemapData {

	private int width, height;
	private ByteBuffer buffer;
	public CubemapData(int width, int height, ByteBuffer buffer) {
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
