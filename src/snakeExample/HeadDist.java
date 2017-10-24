package snakeExample;

import engine.GlobalComponent;

public class HeadDist extends GlobalComponent {
	public static final int DIGITS = 7;
	public static final int ROUNDING = (int)Math.pow(10, DIGITS);
	public double dist = 0;
	public HeadDist(double d){
		dist = d;
	}
	public void increment(double amt){
		dist += amt;
		dist = (double)Math.round(dist*ROUNDING)/ROUNDING;
	}
	public void reset(){
		dist = 0;
	}
	public HeadDist clone(){
		return new HeadDist(dist);
	}
}
