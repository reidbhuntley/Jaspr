package jaspr3d;

public class MeshData {
	private final int dimensions;
	private final int[] indices;
	private final float[] vertices, normals, texCoords;
	public MeshData(int dimensions, int[] indices, float[] vertices, float[] normals, float[] texCoords) {
		this.dimensions = dimensions;
		this.indices = indices;
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
	}
	public MeshData(int dimensions, float[] vertices){
		this(dimensions,new int[0],vertices,new float[0],new float[0]);
	}
	public int getDimensions() {
		return dimensions;
	}
	public int[] getIndices() {
		return indices;
	}
	public float[] getVertices() {
		return vertices;
	}
	public float[] getNormals() {
		return normals;
	}
	public float[] getTexCoords() {
		return texCoords;
	}
}
