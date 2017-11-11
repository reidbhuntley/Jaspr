package jaspr3d;

public class Vector3 {
	private float x, y, z;
	
	public Vector3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(){
		this(0,0,0);
	}
	
	public Vector3(Vector3 vec){
		this(vec.x(), vec.y(), vec.z());
	}
	
	public float x(){
		return x;
	}
	
	public float y(){
		return y;
	}
	
	public float z(){
		return z;
	}
	
	public void normalize(){
		double mag = magnitude();
		x /= mag;
		y /= mag;
		z /= mag;
	}
	
	public void scale(float scalar){
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	public void add(Vector3 vec){
		x += vec.x();
		y += vec.y();
		z += vec.z();
	}
	
	public float magnitude(){
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	
	public static Vector3 scale(Vector3 pos, float scalar){
		Vector3 posNew = new Vector3(pos);
		posNew.scale(scalar);
		return posNew;
	}
	
	public static Vector3 normalize(Vector3 pos){
		Vector3 posNew = new Vector3(pos);
		posNew.normalize();
		return posNew;
	}
	
	public static Vector3 add(Vector3 vec, Vector3...vecs){
		Vector3 vecNew = new Vector3(vec);
		for(Vector3 v : vecs)
			vecNew.add(v);
		return vecNew;
	}
	
	public static Vector3 crossProduct(Vector3 pos1, Vector3 pos2){
		final float x1 = pos1.x(), y1 = pos1.y(), z1 = pos1.z(), x2 = pos2.x(), y2 = pos2.y(), z2 = pos2.z();
		return new Vector3(
				y1*z2 - z1*y2,
				z1*x2 - x1*z2,
				x1*y2 - y1*x2
			);
	}
	
	public static float dotProduct(Vector3 pos1, Vector3 pos2){
		return pos1.x()*pos2.x() + pos1.y()*pos2.y() + pos1.z()*pos2.z();
	}
}
