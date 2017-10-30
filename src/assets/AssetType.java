package assets;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public abstract class AssetType<T> {
	
	private HashMap<String,T> assets;
	
	public AssetType(){
		assets = new HashMap<>();
	}
	
	public void preload(){
		File dir = directory();
		if(dir == null)
			throw new IllegalStateException("directory() method in class "+getClass().getName()+" must return a directory File");
		if(!dir.isDirectory()){
			throw new IllegalStateException("directory() method in class "+getClass().getName()+" must return a directory File");
		}
		loadFromDir(dir);
	}
	
	private void loadFromDir(File dir){
		for(File f : dir.listFiles()){
			if(f.isFile()){
				try {
					assets.put(f.getPath(), load(f));
				} catch(IOException e){
					e.printStackTrace();
				}
			} else if(dir.isDirectory()) {
				loadFromDir(f);
			}
		}
	}
	
	protected HashMap<String,T> assets(){
		return assets;
	}
	
	public abstract File directory();
	public abstract T load(File f) throws IOException;
}
