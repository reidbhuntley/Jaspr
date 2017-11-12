package jaspr3d.rendering;

import jaspr3d.Vector3;

public final class Plane {

	private final Vector3 point, normal;
	
	public Plane(Vector3 point, Vector3 normal){
		this.point = point;
		this.normal = normal;
	}
	
	public float distanceToPoint(Vector3 point){
		return Vector3.dotProduct(normal, Vector3.add(point, Vector3.scale(this.point, -1)));
	}
	
}
