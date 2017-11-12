package jaspr3d;

import java.util.HashMap;

import core.Component;

public class RawModel implements Component {
	
	private final int indicesCount;
	private final Vector3 center;
	private final float radius;
	private final int vaoID, vertexCount;
	private final HashMap<Integer,Integer> vbos;

	public RawModel(HashMap<Integer,Integer> vbos, int vaoID, int vertexCount, int indicesCount, Vector3 center, float radius) {
		this.vbos = vbos;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.indicesCount = indicesCount;
		this.center = center;
		this.radius = radius;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}
	
	public int getBufferLoc(int attr){
		return vbos.get(attr);
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
	
	@Override
	public RawModel getClone(){
		return new RawModel(new HashMap<>(vbos),vaoID,vertexCount,indicesCount,center,radius);
	}
}
