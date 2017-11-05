package core;

public abstract class Component {
	public Component clone() {
		throw new IllegalStateException(
				"Component " + getClass().getName() + " can't be read as it doesn't override the clone() method");
	}
}
