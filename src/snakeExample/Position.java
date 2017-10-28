package snakeExample;

import core.Component;

public class Position extends Component {
	double x, y;
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	public boolean equals(Object object){
		if(object != null && object instanceof Position){
			object = ((Position) object);
			return this.x == ((Position) object).x && this.y == ((Position) object).y;
		}
		return false;
	}
	public boolean overlaps (Position r) {
	    return x < r.x + 1 && x + 1 > r.x && y < r.y + 1 && y + 1 > r.y;
	}
}
