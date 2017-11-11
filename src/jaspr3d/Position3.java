package jaspr3d;

import com.jogamp.opengl.math.Matrix4;

import core.Component;

public class Position3 extends Component {
	private float x, y, z;
	private float pitch, yaw, roll;
	private Matrix4 transformationMatrix;
	private boolean needsUpdate;

	public Position3(float x, float y, float z, float pitch, float yaw, float roll) {
		set(x, y, z, pitch, yaw, roll);
		updateTransformations();
	}

	public Position3(float x, float y, float z) {
		this(x, y, z, 0, 0, 0);
	}

	public Position3() {
		this(0, 0, 0, 0, 0, 0);
	}

	public float[] coords() {
		float[] vec3 = { x, y, z };
		return vec3;
	}

	public float[] rots() {
		float[] vec3 = { pitch, yaw, roll };
		return vec3;
	}
	
	public float x(){
		return x;
	}
	
	public float y(){
		return y;
	}
	
	public float z(){
		return z;
	}
	
	public float pitch(){
		return pitch;
	}
	
	public float yaw(){
		return yaw;
	}
	
	public float roll(){
		return roll;
	}

	public void transform(float dx, float dy, float dz, float dpitch, float dyaw, float droll) {
		this.x += dx;
		this.y += dy;
		this.z += dz;
		this.pitch += dpitch;
		this.yaw += dyaw;
		this.roll += droll;
		needsUpdate = true;
	}

	public void move(float x, float y, float z) {
		transform(x, y, z, 0, 0, 0);
	}
	
	public void moveForward(float dist){
		float[] mat = transformationMatrix.getMatrix();
		dist *= -1;
		transform(mat[2]*dist,mat[6]*dist,mat[10]*dist,0,0,0);
	}
	
	public void moveRight(float dist){
		float[] mat = transformationMatrix.getMatrix();
		transform(mat[0]*dist,mat[4]*dist,mat[8]*dist,0,0,0);
	}
	
	public void moveUp(float dist){
		float[] mat = transformationMatrix.getMatrix();
		transform(mat[1]*dist,mat[5]*dist,mat[9]*dist,0,0,0);
	}

	public void rotate(float pitch, float yaw, float roll) {
		transform(0, 0, 0, pitch, yaw, roll);
	}

	public void set(float x, float y, float z, float pitch, float yaw, float roll) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
		needsUpdate = true;
	}

	public Matrix4 createTransformationMatrix(float x, float y, float z, float pitch, float yaw, float roll) {
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();

		float[] translationMat = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, x, y, z, 1 };
		mat.multMatrix(translationMat);

		if (pitch != 0) {
			pitch = (float) Math.toRadians(pitch);
			final float pCos = (float) Math.cos(pitch), pSin = (float) Math.sin(pitch);
			float[] pitchMat = { 1, 0, 0, 0, 0, pCos, pSin, 0, 0, -pSin, pCos, 0, 0, 0, 0, 1 };
			mat.multMatrix(pitchMat);
		}
		if (yaw != 0) {
			yaw = (float) Math.toRadians(yaw);
			final float yCos = (float) Math.cos(yaw), ySin = (float) Math.sin(yaw);
			float[] yawMat = {yCos, 0, -ySin, 0, 0, 1, 0, 0, ySin, 0, yCos, 0, 0, 0, 0, 1 };
			mat.multMatrix(yawMat);
		}
		if (roll != 0) {
			roll = (float) Math.toRadians(roll);
			final float rCos = (float) Math.cos(roll), rSin = (float) Math.sin(roll);
			float[] rollMat = {rCos, rSin, 0, 0, -rSin, rCos, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
			mat.multMatrix(rollMat);
		}
		
		return mat;
	}
	
	public void updateTransformations(){
		if(needsUpdate){
			transformationMatrix = createTransformationMatrix(x, y, z, pitch, yaw, roll);
			needsUpdate = false;
		}
	}

	public Matrix4 getTransformations() {
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();
		mat.multMatrix(transformationMatrix);
		return mat;
	}
}
