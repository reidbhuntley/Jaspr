package jaspr3d;

import core.Component;

public class TexturedPanel implements Component {
	private RawPanel panel;
	private Texture texture;
	public TexturedPanel(RawPanel panel, Texture texture) {
		this.panel = panel;
		this.texture = texture;
	}
	public RawPanel getPanel() {
		return panel;
	}
	public Texture getTexture() {
		return texture;
	}
}