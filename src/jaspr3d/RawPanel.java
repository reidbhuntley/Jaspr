package jaspr3d;

import java.util.HashMap;

import core.Component;

public class RawPanel implements Component {
	
	private final int vaoID, vertexCount;
	private final HashMap<Integer,Integer> vbos;

	public RawPanel(HashMap<Integer,Integer> vbos, int vaoID, int vertexCount) {
		this.vbos = vbos;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
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
}
