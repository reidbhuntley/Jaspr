package jaspr3d;

import core.Component;

public class Light extends Component {
	
	private float r,g,b;

	public Light(float r, float g, float b) {
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
