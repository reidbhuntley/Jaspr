package jaspr3d;

import java.io.InputStream;

import core.Component;

public class Texture implements Component {

	private int textureID;
	private float shineDamper;
	private float reflectivity = 1;
	private InputStream data;

	public Texture(int id, float shineDamper, float reflectivity) {
		this.textureID = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.data = null;
	}
	
	public Texture(int id) {
		this(id,1,0);
	}
	
	public Texture(InputStream data, float shineDamper, float reflectivity) {
		this.data = data;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	
	public Texture(InputStream data){
		this(data,1,0);
	}
	
	public void load(int textureID){
		if(!isLoaded()){
			this.textureID = textureID;
			this.data = null;
		}
	}
	
	public InputStream getData(){
		return data;
	}

	public float getShineDamper() {
		return shineDamper;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}

	public int getID() {
		return textureID;
	}
	
	public boolean isLoaded(){
		return data == null;
	}
	
	@Override
	public Texture getClone(){
		return new Texture(textureID,shineDamper,reflectivity);
	}
	
}
