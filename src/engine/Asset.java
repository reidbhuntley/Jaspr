package engine;

import java.io.File;
import java.io.IOException;

public abstract class Asset<T> {
	
	private File location;
	private T asset;
	
	public Asset(File location){
		this.location = location;
	}
	
	public T get(){
		return asset;
	}
	
	public boolean isLoaded(){
		return asset != null;
	}
	
	protected void loadFromKey(){
		try {
			asset = load(location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract T load(File location) throws IOException;
	
}
