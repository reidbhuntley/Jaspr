package jaspr3d;

import com.jogamp.opengl.math.Matrix4;

public class Camera extends Position {

	@Override
	public Matrix4 createTransformationMatrix(float x, float y, float z, float pitch, float yaw, float roll) {
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();

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
		
		float[] translationMat = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, -x, -y, -z, 1 };
		mat.multMatrix(translationMat);
		
		return mat;
	}
	
}
