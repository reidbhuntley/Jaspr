package jaspr3d;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import core.Component;

public class Position extends Component {
	private float x, y, z;
	private float pitch, yaw, roll;
	private float[] transformationMatrix;
	private boolean needsUpdate;

	public Position(float x, float y, float z, float pitch, float yaw, float roll) {
		set(x, y, z, pitch, yaw, roll);
		updateTransformations();
	}

	public Position(float x, float y, float z) {
		this(x, y, z, 0, 0, 0);
	}

	public Position() {
		this(0, 0, 0, 0, 0, 0);
	}

	public float[] getCoords() {
		float[] vec3 = { x, y, z };
		return vec3;
	}

	public float[] getRot() {
		float[] vec3 = { pitch, yaw, roll };
		return vec3;
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

	public RealMatrix createTransformationMatrix(float x, float y, float z, float pitch, float yaw, float roll) {
		double[][] identityMat = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		RealMatrix mat = new Array2DRowRealMatrix(identityMat);

		double[][] translationMat = { { 1, 0, 0, x }, { 0, 1, 0, y }, { 0, 0, 1, z }, { 0, 0, 0, 1 } };
		mat = mat.multiply(new Array2DRowRealMatrix(translationMat));

		if (pitch != 0) {
			pitch = (float) Math.toRadians(pitch);
			final float pCos = (float) Math.cos(pitch), pSin = (float) Math.sin(pitch);
			double[][] pitchMat = { { 1, 0, 0, 0 }, { 0, pCos, -pSin, 0 }, { 0, pSin, pCos, 0 }, { 0, 0, 0, 1 } };
			mat = mat.multiply(new Array2DRowRealMatrix(pitchMat));
		}
		if (yaw != 0) {
			yaw = (float) Math.toRadians(yaw);
			final float yCos = (float) Math.cos(yaw), ySin = (float) Math.sin(yaw);
			double[][] yawMat = { { yCos, 0, ySin, 0 }, { 0, 1, 0, 0 }, { -ySin, 0, yCos, 0 }, { 0, 0, 0, 1 } };
			mat = mat.multiply(new Array2DRowRealMatrix(yawMat));
		}
		if (roll != 0) {
			roll = (float) Math.toRadians(roll);
			final float rCos = (float) Math.cos(roll), rSin = (float) Math.sin(roll);
			double[][] rollMat = { { rCos, -rSin, 0, 0 }, { rSin, rCos, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
			mat = mat.multiply(new Array2DRowRealMatrix(rollMat));
		}
		
		return mat;
	}
	
	public void updateTransformations(){
		if(needsUpdate){
			transformationMatrix = unwrapMatrix(createTransformationMatrix(x, y, z, pitch, yaw, roll));
			needsUpdate = false;
		}
	}
	
	public float[] unwrapMatrix(RealMatrix mat){
		double[][] data = mat.getData();
		float[] outMatrix = new float[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				outMatrix[i * 4 + j] = (float) data[j][i];
			}
		}
		return outMatrix;
	}

	public float[] getTransformations() {
		return transformationMatrix;
	}
}
