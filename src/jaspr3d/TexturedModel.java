package jaspr3d;

public class TexturedModel extends TexturedPanel {
	public TexturedModel(RawModel model, Texture texture) {
		super(model, texture);
	}
	public RawModel getModel() {
		return (RawModel) this.getPanel();
	}
}
