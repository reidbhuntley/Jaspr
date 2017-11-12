package jaspr3d;

import core.Component;

public class TexturedModel implements Component {
	private RawModel model;
	private Texture texture;

	public TexturedModel(RawModel model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}

	public RawModel getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public TexturedModel getClone(){
		return new TexturedModel(model.getClone(),texture.getClone());
	}
}
