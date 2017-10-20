package snakeExample;

import java.awt.Color;

import engine.Entity;
import engine.Routine;

public class SnakeBox extends Entity {
	public SnakeBox(Routine r, double x, double y, Color color, int age){
		super();
		addComponent(r, new Position(x, y));
		addComponent(r, new BoxColor(color));
		addComponent(r, new BoxAge(age));
	}
	public SnakeBox(Routine r, double x, double y, Color color){
		this(r,x,y,color,0);
	}
}
