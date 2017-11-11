package jaspr3d;

import com.jogamp.opengl.math.Matrix4;

public class ViewFrustum {
	
	private final Matrix4 projectionMatrix;
	private final float PLANE_NEAR, PLANE_FAR;
	private final float HNEAR, WNEAR;
	private Plane[] PLANES;

	public ViewFrustum(int width, int height, float fov, float nearPlane, float farPlane){
		PLANE_NEAR = nearPlane;
		PLANE_FAR = farPlane;
		
		final float aspectRatio = (float) width / (float) height;
		final float tanAmt = (float) Math.tan(Math.toRadians(fov / 2f));
		HNEAR = 2 *tanAmt * nearPlane;
		WNEAR = HNEAR * aspectRatio;
		
		final float y_scale = 1/tanAmt * aspectRatio;
		final float x_scale = 1/tanAmt;
		final float frustumLength = farPlane - nearPlane;
		final float[] pMat = { x_scale, 0, 0, 0, 0, y_scale, 0, 0, 0, 0, -((farPlane + nearPlane) / frustumLength),
				-1, 0, 0, -((2 * nearPlane * farPlane) / frustumLength), 0 };
		projectionMatrix = new Matrix4();
		projectionMatrix.loadIdentity();
		projectionMatrix.multMatrix(pMat);
		
		PLANES = new Plane[6];
	}
	
	public void genPlanes(Camera cam){
		final Vector3 camVec = cam.getVec();
		final Vector3 fwd = cam.getForwardVec();
		final Vector3 up = cam.getUpVec();
		final Vector3 right = cam.getRightVec();
		
		
		final Vector3 fwdFar = Vector3.scale(fwd, PLANE_FAR);
		final Vector3 centerFar = Vector3.add(camVec, fwdFar);
		
		
		final Vector3 fwdNear = Vector3.scale(fwd, PLANE_NEAR);
		
		final Vector3 upNear = Vector3.scale(up, HNEAR/2);
		final Vector3 pointUp = Vector3.add(upNear, fwdNear);
		final Vector3 pointDown = Vector3.add(Vector3.scale(upNear, -1), fwdNear);
		
		final Vector3 rightNear = Vector3.scale(right, WNEAR/2);
		final Vector3 pointRight = Vector3.add(rightNear, fwdNear);
		final Vector3 pointLeft = Vector3.add(Vector3.scale(rightNear, -1), fwdNear);
		
		final Vector3 centerNear = Vector3.add(camVec, fwdNear);
		
		
		final Vector3 normalRight = Vector3.crossProduct(up, Vector3.normalize(pointRight));
		final Vector3 normalLeft = Vector3.crossProduct(Vector3.normalize(pointLeft), up);
		final Vector3 normalUp = Vector3.crossProduct(Vector3.normalize(pointUp), right);
		final Vector3 normalDown = Vector3.crossProduct(right, Vector3.normalize(pointDown));
		
		PLANES[0] = new Plane(centerNear, fwd);
		PLANES[1] = new Plane(centerFar, Vector3.scale(fwd, -1));
		PLANES[2] = new Plane(Vector3.add(camVec, pointRight), normalRight);
		PLANES[3] = new Plane(Vector3.add(camVec, pointLeft), normalLeft);
		PLANES[4] = new Plane(Vector3.add(camVec, pointUp), normalUp);
		PLANES[5] = new Plane(Vector3.add(camVec, pointDown), normalDown);
	}
	
	public boolean sphereIntersects(Vector3 center, float radius) {
		float distance;
		for(Plane pl : PLANES) {
			distance = pl.distanceToPoint(center);
			//System.out.println(distance + ", " + -radius);
			if (distance < -radius)
				return false;
		}
		return true;
	}
	
	public Matrix4 getProjectionMatrix(){
		Matrix4 mat = new Matrix4();
		mat.loadIdentity();
		mat.multMatrix(projectionMatrix);
		return mat;
	}
	
}
