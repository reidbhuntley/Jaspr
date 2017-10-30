package jaspr3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import assets.AssetType;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjReader;

public class ModelManager extends AssetType<RawModel> {

	public RawModel get(String filename) {
		return assets().get(filename);
	}

	@Override
	public File directory() {
		return new File("res/models/");
	}

	@Override
	public RawModel load(File f) throws IOException {
		InputStream objInputStream = new FileInputStream(f);
		Obj obj = ObjReader.read(objInputStream);

		List<Integer> indices = new ArrayList<>();
		List<FloatTuple> textures = new ArrayList<>();
		List<FloatTuple> normals = new ArrayList<>();

		float[] verticesArray = new float[obj.getNumVertices() * 3];
		float[] normalsArray = new float[obj.getNumVertices() * 3];
		float[] textureArray = new float[obj.getNumVertices() * 2];
		int[] indicesArray = null;

		for (int i = 0; i < obj.getNumVertices(); i++) {
			FloatTuple coords = obj.getVertex(i);
			verticesArray[i * 3] = coords.getX();
			verticesArray[i * 3 + 1] = coords.getY();
			verticesArray[i * 3 + 2] = coords.getZ();
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
			if(numVerts == 3){
				points.add(0);
				points.add(1);
				points.add(2);
			} else if(numVerts == 4){
				points.add(0);
				points.add(1);
				points.add(2);
				points.add(0);
				points.add(2);
				points.add(3);
			} else {
				throw new IllegalArgumentException("Polys in .obj file "+f.getName()+" must be triangles or quads");
			}
			for(int j = 0; j < points.size(); j++){
				int currentVertexPointer = face.getVertexIndex(points.get(j));
				indices.add(currentVertexPointer);
				if(textures.size() > 0){
					FloatTuple currentTex = textures.get(j);
					textureArray[currentVertexPointer * 2] = currentTex.getX();
					textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.getY();
				}
				if(normals.size() > 0){
					FloatTuple currentNorm = normals.get(j);
					normalsArray[currentVertexPointer * 3] = currentNorm.getX();
					normalsArray[currentVertexPointer * 3 + 1] = currentNorm.getY();
					normalsArray[currentVertexPointer * 3 + 2] = currentNorm.getZ();
				}
			}
		}
		
		indicesArray = new int[indices.size()];
		for(int i = 0; i < indicesArray.length; i++){
			indicesArray[i] = indices.get(i);
		}
		return new RawModel(indicesArray, verticesArray, normalsArray, textureArray);

	}

}
