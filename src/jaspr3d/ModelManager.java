package jaspr3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import assets.AssetType;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjReader;

public class ModelManager extends AssetType<RawModel> {
	
	private VAOLoader loader;
	private GL3 gl;
	
	public ModelManager(){
		loader = new VAOLoader();
	}

	public RawModel get(String filename) {
		return assets().get(filename);
	}

	@Override
	public File directory() {
		return new File("res/models/");
	}
	
	public void preload(GL3 gl){
		File dir = directory();
		if(dir == null)
			throw new IllegalStateException("directory() method in class "+getClass().getName()+" must return a directory File");
		if(!dir.isDirectory()){
			throw new IllegalStateException("directory() method in class "+getClass().getName()+" must return a directory File");
		}
		this.gl = gl;
		loadFromDir(dir);
	}
	
	@Override
	public void preload(){
		
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
			FloatTuple vertex = obj.getVertex(i);
			verticesArray[i * 3] = vertex.getX();
			verticesArray[i * 3 + 1] = vertex.getY();
			verticesArray[i * 3 + 2] = vertex.getZ();
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
				int point = points.get(j);
				int currentVertexPointer = face.getVertexIndex(point);
				indices.add(currentVertexPointer);
				if(textures.size() > 0){
					FloatTuple currentTex = textures.get(face.getTexCoordIndex(point));
					textureArray[currentVertexPointer * 2] = currentTex.getX();
					textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.getY();
				}
				if(normals.size() > 0){
					FloatTuple currentNorm = normals.get(face.getNormalIndex(point));
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
		
		RawModel model = loader.loadToVAO(gl, indicesArray, verticesArray, normalsArray, textureArray);
		return model;

	}
	
	public void cleanUp(){
		loader.cleanUp();
	}

}
