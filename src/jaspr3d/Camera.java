package jaspr3d;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Camera extends Position {

	@Override
	public float[] createTransformationMatrix(RealMatrix mat, float x, float y, float z, float pitch, float yaw, float roll){
		
		if (pitch != 0) { 
			pitch = (float)Math.toRadians(-pitch);
			final float pCos = (float) Math.cos(pitch), pSin = (float) Math.sin(pitch);
			double[][] pitchMat = { { 1, 0, 0, 0 }, { 0, pCos, -pSin, 0 }, { 0, pSin, pCos, 0 }, { 0, 0, 0, 1 } };
			mat = mat.multiply(new Array2DRowRealMatrix(pitchMat));
		}
		if (yaw != 0) {
			yaw = (float)Math.toRadians(-yaw);
			final float yCos = (float) Math.cos(yaw), ySin = (float) Math.sin(yaw);
			double[][] yawMat = { { yCos, 0, ySin, 0 }, { 0, 1, 0, 0 }, { -ySin, 0, yCos, 0 }, { 0, 0, 0, 1 } };
			mat = mat.multiply(new Array2DRowRealMatrix(yawMat));
		}
		if (roll != 0) {
			roll = (float)Math.toRadians(-roll);
			final float rCos = (float) Math.cos(roll), rSin = (float) Math.sin(roll);
			double[][] rollMat = { { rCos, -rSin, 0, 0 }, { rSin, rCos, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
			mat = mat.multiply(new Array2DRowRealMatrix(rollMat));
		}
		
		double[][] translationMat = { { 1, 0, 0, -x }, { 0, 1, 0, -y }, { 0, 0, 1, -z }, { 0, 0, 0, 1 } };
		mat = mat.multiply(new Array2DRowRealMatrix(translationMat));
		
		double[][] data = mat.getData();
		float[] outMatrix = new float[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				outMatrix[i * 4 + j] = (float) data[j][i];
			}
		}
		return outMatrix;
	}
	
}
