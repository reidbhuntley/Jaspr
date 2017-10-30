package jaspr3d;

import core.Component;

public class RawModel extends Component {
	final int vertexCount, polyCount;
	float[] vertices, normals, texture;
	int[] indices;
	
	public RawModel(int[] indices, float[] vertices, float[] normals, float[] texture){
		this.indices = indices;
		this.vertices = vertices;
		this.polyCount = indices.length / 3;
		this.vertexCount = vertices.length / 3;
		this.normals = normals;
		this.texture = texture;
	}
}
