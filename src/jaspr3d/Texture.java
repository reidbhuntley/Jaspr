package jaspr3d;

import core.Component;

public class Texture extends Component {

	private int textureID;

	public Texture(int id) {
		textureID = id;
	}

	public int getID() {
		return textureID;
	}
	
}
