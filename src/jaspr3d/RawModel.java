package jaspr3d;

import java.util.HashMap;

public class RawModel extends RawPanel {
	
	private final int indicesCount;
	private final Vector3 center;
	private final float radius;

	public RawModel(HashMap<Integer,Integer> vbos, int vaoID, int vertexCount, int indicesCount, Vector3 center, float radius) {
		super(vbos, vaoID, vertexCount);
		this.indicesCount = indicesCount;
		this.center = center;
		this.radius = radius;
	}
	
	public int getIndicesCount() {
		return indicesCount;
	}
	
	public Vector3 getCenter(){
		return new Vector3(center);
	}
	
	public float getRadius(){
		return radius;
	}
}
