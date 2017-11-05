package jaspr3d;

import core.Component;

public class Texture extends Component {

	private int textureID;
	private float shineDamper;
	private float reflectivity = 1;

	public Texture(int id) {
		textureID = id;
		shineDamper = 1;
		reflectivity = 0;
	}

	public Texture(int id, float shineDamper, float reflectivity) {
		textureID = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}

	public float getShineDamper() {
		return shineDamper;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}

	public int getID() {
		return textureID;
	}
	
}
