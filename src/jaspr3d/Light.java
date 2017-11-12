package jaspr3d;

import core.Component;

public class Light extends Vector3 implements Component {
	
	private Vector3 color, attenuation; 

	public Light(Vector3 position, Vector3 color, Vector3 attenuation) {
		super(position);
		this.color = color;
		this.attenuation = attenuation;
	}
	
	public Light(Vector3 position, Vector3 color){
		this(position, color, new Vector3(1,0,0));
	}
	
	public Light(Vector3 position) {
		this(position, new Vector3(1,1,1));
	}
	
	public Light(){
		this(new Vector3());
	}
	
	public void setColor(Vector3 color){
		this.color = color;
	}
	
	public Vector3 getColor(){
		return color;
	}

	public void setAttenuation(Vector3 attenuation) {
		this.attenuation = attenuation;
	}
	
	public Vector3 getAttenuation() {
		return attenuation;
	}
	
}
