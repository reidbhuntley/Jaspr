package jaspr3d;

import core.Component;

public class GeometryModel implements Component {
	private Model[] faces;
	private int facesCount;
	
	public GeometryModel(Model[] faces){
		this.faces = faces;
		this.facesCount = faces.length;
	}
	
	@Override
	public GeometryModel getClone(){
		return new GeometryModel(faces);
	}
	
	public Model[] getFaces(){
		return faces;
	}
	
	public int getFacesCount(){
		return facesCount;
	}
}
