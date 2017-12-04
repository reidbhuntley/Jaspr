package res;

import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.GL3;

import jaspr3d.GeometryModel;
import jaspr3d.Level;
import jaspr3d.VAOLoader;

public class LevelManager extends AssetType<Level> {

	private VAOLoader loader;
	//private GL3 gl;

	public LevelManager() {
		super("levels", "jpr");
		loader = new VAOLoader();
	}

	public Level get(String filename) {
		return getRawAsset(filename);
	}

	public void preload(GL3 gl) {
		//this.gl = gl;
		loadFromDir(folderName);	
	}

	@Override
	public void preload() {

	}

	@Override
	public Level loadAssetFromFile(InputStream in, String name) throws IOException {
		/*
		 * Obj obj = ObjReader.read(in);
		 * 
		 * List<Integer> indices = new ArrayList<>(); List<FloatTuple> textures
		 * = new ArrayList<>(); List<FloatTuple> normals = new ArrayList<>();
		 * 
		 * float[] verticesArray = new float[obj.getNumVertices() * 3]; float[]
		 * normalsArray = new float[obj.getNumVertices() * 3]; float[]
		 * textureArray = new float[obj.getNumVertices() * 2]; int[]
		 * indicesArray = null;
		 * 
		 * float xTotal = 0, yTotal = 0, zTotal = 0; float magMax = 0;
		 * 
		 * for (int i = 0; i < obj.getNumVertices(); i++) { FloatTuple vertex =
		 * obj.getVertex(i); float x = vertex.getX(), y = vertex.getY(), z =
		 * vertex.getZ(); verticesArray[i * 3] = x; verticesArray[i * 3 + 1] =
		 * y; verticesArray[i * 3 + 2] = z; xTotal += x; yTotal += y; zTotal +=
		 * z; float mag = (float)Math.sqrt(x*x+y*y+z*z); if(mag > magMax){
		 * magMax = mag; } } for (int i = 0; i < obj.getNumNormals(); i++) {
		 * normals.add(obj.getNormal(i)); } for (int i = 0; i <
		 * obj.getNumTexCoords(); i++) { textures.add(obj.getTexCoord(i)); }
		 * 
		 * for (int i = 0; i < obj.getNumFaces(); i++) { ObjFace face =
		 * obj.getFace(i); int numVerts = face.getNumVertices(); List<Integer>
		 * points = new ArrayList<>(); if (numVerts == 3) { points.add(0);
		 * points.add(1); points.add(2); } else if (numVerts == 4) {
		 * points.add(0); points.add(1); points.add(2); points.add(0);
		 * points.add(2); points.add(3); } else { throw new
		 * IllegalArgumentException("Polys in .obj file " + name +
		 * " must be triangles or quads"); } for (int j = 0; j < points.size();
		 * j++) { int point = points.get(j); int currentVertexPointer =
		 * face.getVertexIndex(point); indices.add(currentVertexPointer); if
		 * (textures.size() > 0) { FloatTuple currentTex =
		 * textures.get(face.getTexCoordIndex(point));
		 * textureArray[currentVertexPointer * 2] = currentTex.getX();
		 * textureArray[currentVertexPointer * 2 + 1] = currentTex.getY(); } if
		 * (normals.size() > 0) { FloatTuple currentNorm =
		 * normals.get(face.getNormalIndex(point));
		 * normalsArray[currentVertexPointer * 3] = currentNorm.getX();
		 * normalsArray[currentVertexPointer * 3 + 1] = currentNorm.getY();
		 * normalsArray[currentVertexPointer * 3 + 2] = currentNorm.getZ(); } }
		 * }
		 * 
		 * indicesArray = new int[indices.size()]; for (int i = 0; i <
		 * indicesArray.length; i++) { indicesArray[i] = indices.get(i); }
		 * 
		 * float invNumVertices = 3/verticesArray.length; Vector3 center = new
		 * Vector3(xTotal,yTotal,zTotal); center.scale(invNumVertices); Mesh
		 * model = loader.loadToVAO(gl, 3, indicesArray, verticesArray,
		 * normalsArray, textureArray, center, magMax); return model;
		 */
		return new Level(new GeometryModel[0]);
	}

	public void cleanUp(GL3 gl) {
		loader.cleanUp(gl);
	}

	public Level load(String filename) {
		return getRawAsset(filename,true);
	}

}
