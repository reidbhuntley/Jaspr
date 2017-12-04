package jaspr3d;

import core.Component;

public class Model implements Component {
	private Mesh model;
	private Texture texture;

	public Model(Mesh model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}

	public Mesh getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public Model getClone(){
		return new Model(model.getClone(),texture.getClone());
	}
}
