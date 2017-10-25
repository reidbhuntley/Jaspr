package engine;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class AssetManager {
	private static HashMap<String,Image> images = new HashMap<>();
	//private static HashMap<String,Image> sounds = new HashMap<>();
	public static void load() throws IOException{
		File imagesDir = new File("res/images/");
		for(File f : imagesDir.listFiles()){
			images.put(f.getName(), ImageIO.read(f));
		}
		
		//File sounds = new File("assets/sounds/");
	}
	
	public static Image readImage(String filename){
		return images.get(filename).getScaledInstance(1, 1, 0);
	}
	
}
