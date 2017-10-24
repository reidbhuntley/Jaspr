package snakeExample;

import engine.Component;

public class SnakeDir extends Component {

	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
	public int dir;
	
	public SnakeDir(int direction){
		dir = direction;
	}

}
