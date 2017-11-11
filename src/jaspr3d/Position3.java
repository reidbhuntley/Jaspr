package jaspr3d;

import com.jogamp.opengl.math.Matrix4;

import core.Component;

public class Position3 extends Component {
	private Vector3 vec;
	private float pitch, yaw, roll;
	private Matrix4 transformationMatrix;
	private boolean needsUpdate;
	
	public Position3(Position3 pos){
		this(pos.x(),pos.y(),pos.z(),pos.pitch(),pos.yaw(),pos.roll());
	}

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

	public Vector3 getVec() {
		return new Vector3(vec);
	}

	public float[] getRots() {
		float[] vec3 = { pitch, yaw, roll };
		return vec3;
	}
	
	public float x(){
		return vec.x();
	}
	
	public float y(){
		return vec.y();
	}
	
	public float z(){
		return vec.z();
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

	public void transform(Vector3 vec, float dpitch, float dyaw, float droll) {
		this.vec.add(vec);
		this.pitch += dpitch;
		this.yaw += dyaw;
		this.roll += droll;
		needsUpdate = true;
	}
	
	public void transform(float dx, float dy, float dz, float dpitch, float dyaw, float droll) {
		this.transform(new Vector3(dx,dy,dz), dpitch, dyaw, droll);
		needsUpdate = true;
	}
	
	public void move(Vector3 vec) {
		transform(vec, 0, 0, 0);
	}

	public void move(float x, float y, float z) {
		move(new Vector3(x,y,z));
	}
	
	public Vector3 getForwardVec(){
		float[] mat = transformationMatrix.getMatrix();
		Vector3 pos = new Vector3(-mat[2],-mat[6],-mat[10]);
		pos.normalize();
		return pos;
	}
	
	public Vector3 getRightVec(){
		float[] mat = transformationMatrix.getMatrix();
		Vector3 pos = new Vector3(mat[0],mat[4],mat[8]);
		pos.normalize();
		return pos;
	}
	
	public Vector3 getUpVec(){
		float[] mat = transformationMatrix.getMatrix();
		Vector3 pos = new Vector3(mat[1],mat[5],mat[9]);
		pos.normalize();
		return pos;
	}
	
	public void moveForward(float dist){
		Vector3 pos = getForwardVec();
		pos.scale(dist);
		move(pos.x(),pos.y(),pos.z());
	}
	
	public void moveRight(float dist){
		Vector3 pos = getRightVec();
		pos.scale(dist);
		move(pos.x(),pos.y(),pos.z());
	}
	
	public void moveUp(float dist){
		Vector3 pos = getUpVec();
		pos.scale(dist);
		move(pos.x(),pos.y(),pos.z());
	}

	public void rotate(float pitch, float yaw, float roll) {
		transform(0, 0, 0, pitch, yaw, roll);
	}

	public void set(float x, float y, float z, float pitch, float yaw, float roll) {
		vec = new Vector3(x,y,z);
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
			transformationMatrix = createTransformationMatrix(x(), y(), z(), pitch, yaw, roll);
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
