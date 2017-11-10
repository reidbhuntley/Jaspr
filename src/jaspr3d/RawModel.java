package jaspr3d;

import java.util.HashMap;

import core.Component;

public class RawModel extends Component {
	
	private int vaoID, vertexCount, indicesCount;
	private HashMap<Integer,Integer> vbos;

	public RawModel(HashMap<Integer,Integer> vbos, int vaoID, int vertexCount, int indicesCount) {
		this.vbos = vbos;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.indicesCount = indicesCount;
	}

	public int getVaoID() {
		return vaoID;
	}
	
	public int getBufferLoc(int attr){
		return vbos.get(attr);
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getIndicesCount() {
		return indicesCount;
	}
}
