package res;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager extends AssetType<AudioInputStream> {

	public SoundManager(){
		super("sounds", "wav");
	}
	
	public Clip getSound(String filename) {
		AudioInputStream stream = assets().get(filename);
		if(stream != null){
			return prepareClip(stream);
		} else {
			new Exception("Sound file "+filename+" missing").printStackTrace();
			return null;
		}
	}
	
	protected static Clip prepareClip(AudioInputStream stream){
		try {
			stream.reset();
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			return clip;
		} catch (IOException e) {
			new RuntimeException(e);
		} catch (LineUnavailableException e) {
			new RuntimeException(e);
		}
		return null;
	}
	
	public Loop getLoop(String filename){
		return new Loop(assets().get(filename));
	}
	
	public void playSound(String filename){
		Clip clip = getSound(filename);
		if(clip != null)
			clip.start();
	}
	
	public AudioInputStream load(InputStream in, String name) {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(in);
			byte[] buffer = new byte[1024 * 32];
			int read = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length);
			while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			AudioInputStream reusableAis = new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()),
					ais.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAis;
		} catch(IOException e){
			e.printStackTrace();
		} catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
