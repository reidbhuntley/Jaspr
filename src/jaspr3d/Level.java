package jaspr3d;

public class Level {
	
	private GeometryModel[] geometry;
	
	public Level(GeometryModel[] geometry){
		this.geometry = geometry;
	}
	
	public GeometryModel[] getGeometry(){
		return geometry;
	}
	
}
