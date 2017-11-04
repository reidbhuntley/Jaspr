package jaspr3d;

import core.Component;

public class RawModel extends Component {
	
	private int vaoID, vertexCount, indicesCount;

	public RawModel(int vaoID, int vertexCount, int indicesCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.indicesCount = indicesCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getIndicesCount() {
		return indicesCount;
	}
}
