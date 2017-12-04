package res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AssetType<T> {

	private HashMap<String, T> assets;
	protected final String folderName, fileExtension;
	private boolean loaded;
	private boolean isInJar;
	private JarFile jar;

	public AssetType(String folderName, String fileExtension) {
		this.folderName = folderName;
		this.fileExtension = fileExtension;
		assets = new HashMap<>();
		loaded = false;
		final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		isInJar = jarFile.isFile();
		if(isInJar){
			try {
				jar = new JarFile(jarFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void preload() {
		if(!loaded){
			loadFromDir(folderName);
			loaded = true;
		}
	}

	public void loadFromDir(String path) {
		path = "res/"+path+"/";
		try {
			if (isInJar) { // Run with JAR file
				final Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					final JarEntry entry = entries.nextElement();
					final String name = entry.getName();
					if(name.equals("com/"))
						break;
					if (name.startsWith(path) && fileExtension.equals(getExtension(name))) { // filter according to the path
						File file = new File(name);
						assets.put(file.getName(), loadAssetFromFile(jar.getInputStream(entry), file.getName()));
					}
				}
				jar.close();
			} else { // Run with IDE
				
				final File url = new File("src/"+path);
				if (url != null) {
					final File apps = new File(url.toURI());
					for (File app : apps.listFiles()) {
						if(fileExtension.equals(getExtension(app.getName()))){
							assets.put(app.getName(), loadAssetFromFile(new FileInputStream(app), app.getName()));
						}
					}
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("There was an error loading files from AssetType " + getClass().getName());
			//System.exit(-1);
		}
	}

	protected T getRawAsset(String file, boolean loadIfMissing){
		T asset = assets.get(file);
		if(asset == null){
			if(loadIfMissing){
				String fullFile = "res/"+folderName+"/"+file+"/";
				try {
					if(isInJar){
						asset = loadAssetFromFile(jar.getInputStream(jar.getEntry(fullFile)), file);
					} else {
						asset = loadAssetFromFile(new FileInputStream("src/"+fullFile), file);
					}
					assets.put(file, asset);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				throw new IllegalArgumentException("Missing file " + file);
				//System.exit(-1);
			}
		}
		return asset;
	}
	
	protected T getRawAsset(String file){
		return getRawAsset(file,false);
	}

	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int extensionPos = filename.lastIndexOf('.');
		int lastUnixPos = filename.lastIndexOf('/');
		int lastWindowsPos = filename.lastIndexOf('\\');
		int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

		int index = lastSeparator > extensionPos ? -1 : extensionPos;
		if (index == -1) {
			return null;
		} else {
			return filename.substring(index + 1);
		}
	}

	protected abstract T loadAssetFromFile(InputStream in, String filename) throws IOException;
}
