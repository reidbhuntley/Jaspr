package snakeExample;

import engine.GlobalComponent;

public class SnakeLength extends GlobalComponent {
	public int length;
	public SnakeLength(int length){
		this.length = length;
	}
	@Override
	public GlobalComponent clone() {
		return new SnakeLength(length);
	}
}
