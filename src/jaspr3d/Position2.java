package jaspr3d;

import java.awt.Point;

import core.Component;

public class Position2 implements Component {
	private float x, y;
	private float rotation;

	public Position2(float x, float y, float rotation) {
		set(x, y, rotation);
	}

	public Position2(float x, float y) {
		this(x, y, 0);
	}
	
	public Position2(Point point){
		this((float)point.getX(), (float)point.getY(), 0);
	}
	
	public Position2(Position2 pos){
		this(pos.x,pos.y,pos.rotation);
	}

	public Position2() {
		this(0, 0, 0);
	}

	public float[] coords() {
		float[] vec3 = { x, y };
		return vec3;
	}
	
	public float x(){
		return x;
	}
	
	public float y(){
		return y;
	}
	
	public float rot(){
		return rotation;
	}

	public void transform(float dx, float dy, float drotation) {
		this.x += dx;
		this.y += dy;
		this.rotation += drotation;
	}

	public void move(float x, float y) {
		transform(x, y, 0);
	}

	public void rotate(float rotation) {
		transform(0, 0, rotation);
	}

	public void set(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}
	
	public void set(float x, float y){
		set(x,y,this.rotation);
	}
	
	public void set(Point point){
		set((int)point.getX(), (int)point.getY(), this.rotation);
	}
	
	public void set(float rotation){
		set(this.x,this.y,rotation);
	}
	
}
