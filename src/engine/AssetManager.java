package engine;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AssetManager {
	
	private static HashMap<String,BufferedImage> images = new HashMap<>();
	private static HashMap<String,AudioInputStream> sounds = new HashMap<>();
	
	public static void preload() {
		File imagesDir = new File("res/images/");
		for(File f : imagesDir.listFiles()){
			try {
				BufferedImage image = ImageIO.read(f);
				images.put(f.getName(), image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File soundsDir = new File("res/sounds/");
		for(File f : soundsDir.listFiles()){
			try {
				AudioInputStream audioInputStream = createReusableAudioInputStream(f);
				sounds.put(f.getName(), audioInputStream);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static BufferedImage readImage(String filename) {
		BufferedImage bi = images.get(filename);
		if(bi != null){
			ColorModel cm = bi.getColorModel();
			return new BufferedImage(cm, bi.copyData(null), cm.isAlphaPremultiplied(), null);
		} else {
			new Exception("Image file "+filename+" missing").printStackTrace();
			return null;
		}
	}
	
	public static Clip getSound(String filename) {
		try {
			AudioInputStream stream = sounds.get(filename);
			if(stream != null){
				stream.reset();
				Clip clip = AudioSystem.getClip();
				clip.open(stream);
				return clip;
			} else {
				new Exception("Sound file "+filename+" missing").printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Loop getLoop(String filename){
		return new Loop(filename);
	}
	
	public static void playSound(String filename){
		Clip clip = getSound(filename);
		if(clip != null)
			clip.start();
	}
	
	private static AudioInputStream createReusableAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(file);
			byte[] buffer = new byte[1024 * 32];
			int read = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length);
			while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			AudioInputStream reusableAis = new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()),
					ais.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAis;
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
