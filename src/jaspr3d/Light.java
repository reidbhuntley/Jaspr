package jaspr3d;

public class Light extends Position3 {
	
	private float r,g,b;

	public Light(float x, float y, float z, float r, float g, float b) {
		super(x,y,z);
		setColor(r,g,b);
	}
	
	public void setColor(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public float[] getColor(){
		float[] color = {r,g,b};
		return color;
	}
	
}
