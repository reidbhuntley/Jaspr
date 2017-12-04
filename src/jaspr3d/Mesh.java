package jaspr3d;

import java.util.HashMap;

import core.Component;

public class Mesh implements Component {
	
	private final int indicesCount, vertexCount;
	private final Vector3 center;
	private final float radius;
	private int vaoID;
	private HashMap<Integer,Integer> vbos;
	private MeshData data;

	public Mesh(HashMap<Integer,Integer> vbos, int vaoID, int vertexCount, int indicesCount, Vector3 center, float radius) {
		this.vbos = vbos;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.indicesCount = indicesCount;
		this.center = center;
		this.radius = radius;
		this.data = null;
	}
	
	public Mesh(MeshData data, Vector3 center, float radius){
		this.vertexCount = data.getVertices().length / data.getDimensions();
		this.indicesCount = data.getIndices().length;
		this.center = center;
		this.radius = radius;
		this.data = data;
	}
	
	public Mesh(MeshData data){
		this(data,new Vector3(),0);
	}
	
	public void load(HashMap<Integer,Integer> vbos, int vaoID){
		if(!isLoaded()){
			this.vbos = vbos;
			this.vaoID = vaoID;
			this.data = null;
		}
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
	
	public boolean isLoaded(){
		return data == null;
	}
	
	public MeshData getData(){
		return data;
	}
	
	@Override
	public Mesh getClone(){
		return new Mesh(new HashMap<>(vbos),vaoID,vertexCount,indicesCount,center,radius);
	}
}
