package jaspr3d;

import core.Component;

public class Position extends Component {
	float x, y, z;
	float pitch, yaw, roll;
	float pCos, pSin, yCos, ySin, rCos, rSin;
	
	public Position(float x, float y, float z, float pitch, float yaw, float roll){
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
		this.pCos = (float)Math.cos(pitch);
		this.pSin = (float)Math.sin(pitch);
		this.yCos = (float)Math.cos(yaw);
		this.ySin = (float)Math.sin(yaw);
		this.rCos = (float)Math.cos(roll);
		this.rSin = (float)Math.sin(roll);
	}
	
	public Position(float x, float y, float z){
		this(x,y,z,0,0,0);
	}
	
	public float[] getCoords(){
		float[] vec3 = {x,y,z};
		return vec3;
	}
	
	public float[] getRot(){
		float[] vec3 = {pitch,yaw,roll};
		return vec3;
	}
}
