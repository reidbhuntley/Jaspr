package res;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjReader;
import jaspr3d.RawModel;
import jaspr3d.VAOLoader;
import jaspr3d.Vector3;

public class ModelManager extends AssetType<RawModel> {

	private VAOLoader loader;
	private GL3 gl;

	public ModelManager() {
		super("models", "obj");
		loader = new VAOLoader();
	}

	public RawModel get(String filename) {
		return assets().get(filename);
	}

	public void preload(GL3 gl) {
		this.gl = gl;
		loadFromDir(folderName);
	}

	@Override
	public void preload() {

	}

	@Override
	public RawModel load(InputStream in, String name) throws IOException {
		Obj obj = ObjReader.read(in);

		List<Integer> indices = new ArrayList<>();
		List<FloatTuple> textures = new ArrayList<>();
		List<FloatTuple> normals = new ArrayList<>();

		float[] verticesArray = new float[obj.getNumVertices() * 3];
		float[] normalsArray = new float[obj.getNumVertices() * 3];
		float[] textureArray = new float[obj.getNumVertices() * 2];
		int[] indicesArray = null;
		
		float xTotal = 0, yTotal = 0, zTotal = 0;
		float magMax = 0;

		for (int i = 0; i < obj.getNumVertices(); i++) {
			FloatTuple vertex = obj.getVertex(i);
			float x = vertex.getX(), y = vertex.getY(), z = vertex.getZ();
			verticesArray[i * 3] = x;
			verticesArray[i * 3 + 1] = y;
			verticesArray[i * 3 + 2] = z;
			xTotal += x;
			yTotal += y;
			zTotal += z;
			float mag = (float)Math.sqrt(x*x+y*y+z*z);
			if(mag > magMax){
				magMax = mag;
			}
		}
		for (int i = 0; i < obj.getNumNormals(); i++) {
			normals.add(obj.getNormal(i));
		}
		for (int i = 0; i < obj.getNumTexCoords(); i++) {
			textures.add(obj.getTexCoord(i));
		}

		for (int i = 0; i < obj.getNumFaces(); i++) {
			ObjFace face = obj.getFace(i);
			int numVerts = face.getNumVertices();
			List<Integer> points = new ArrayList<>();
			if (numVerts == 3) {
				points.add(0);
				points.add(1);
				points.add(2);
			} else if (numVerts == 4) {
				points.add(0);
				points.add(1);
				points.add(2);
				points.add(0);
				points.add(2);
				points.add(3);
			} else {
				throw new IllegalArgumentException("Polys in .obj file " + name + " must be triangles or quads");
			}
			for (int j = 0; j < points.size(); j++) {
				int point = points.get(j);
				int currentVertexPointer = face.getVertexIndex(point);
				indices.add(currentVertexPointer);
				if (textures.size() > 0) {
					FloatTuple currentTex = textures.get(face.getTexCoordIndex(point));
					textureArray[currentVertexPointer * 2] = currentTex.getX();
					textureArray[currentVertexPointer * 2 + 1] = currentTex.getY();
				}
				if (normals.size() > 0) {
					FloatTuple currentNorm = normals.get(face.getNormalIndex(point));
					normalsArray[currentVertexPointer * 3] = currentNorm.getX();
					normalsArray[currentVertexPointer * 3 + 1] = currentNorm.getY();
					normalsArray[currentVertexPointer * 3 + 2] = currentNorm.getZ();
				}
			}
		}

		indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		
		float invNumVertices = 3/verticesArray.length;
		Vector3 center = new Vector3(xTotal,yTotal,zTotal);
		center.scale(invNumVertices);
		RawModel model = loader.loadToVAO(center, magMax, gl, indicesArray, verticesArray, normalsArray, textureArray);
		return model;
	}

	public void cleanUp() {
		loader.cleanUp();
	}

}
