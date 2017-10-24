package snakeExample;

import java.awt.Color;

import engine.Entity;

public class SnakeBox extends Entity {
	public SnakeBox(double x, double y, Color color, int age, int dir){
		super();
		addComponent(new Position(x, y));
		addComponent(new BoxColor(color));
		addComponent(new BoxAge(age));
		addComponent(new SnakeDir(dir));
	}
}
