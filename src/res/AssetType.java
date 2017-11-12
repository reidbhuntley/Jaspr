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

	public AssetType(String folderName, String fileExtension) {
		this.folderName = folderName;
		this.fileExtension = fileExtension;
		assets = new HashMap<>();
		loaded = false;
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
			final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			if (jarFile.isFile()) { // Run with JAR file
				final JarFile jar = new JarFile(jarFile);
				final Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					final JarEntry entry = entries.nextElement();
					final String name = entry.getName();
					if(name.equals("com/"))
						break;
					if (name.startsWith(path) && fileExtension.equals(getExtension(name))) { // filter according to the path
						File file = new File(name);
						assets.put(file.getName(), load(jar.getInputStream(entry), file.getName()));
					}
				}
				jar.close();
			} else { // Run with IDE
				final File url = new File("src/"+path);
				if (url != null) {
					final File apps = new File(url.toURI());
					for (File app : apps.listFiles()) {
						if(fileExtension.equals(getExtension(app.getName())))
							assets.put(app.getName(), load(new FileInputStream(app), app.getName()));
					}
				}
			}
		} catch (IOException e) {
			System.err.println("There was an error loading files from AssetType " + getClass().getName());
			System.exit(-1);
		}
	}

	protected T getAsset(String file){
		T asset = assets.get(file);
		if(asset == null){
			System.err.println("Missing file " + file);
			System.exit(-1);
		}
		return asset;
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

	public abstract T load(InputStream in, String filename) throws IOException;
}
