package engine;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Loop {

	private String filename;
	private Clip clip;
	private LineListener listener;
	private boolean active;
	
	protected Loop(String filename){
		this.filename = filename;
		active = false;
	}
	
	public void start() {
		clip = AssetManager.getSound(filename);
		active = true;
		clip.start();
		listener = new LineListener() {
			public void update(LineEvent event) {
				if(active && event.getType() == LineEvent.Type.STOP){
					start();
				}
			}
		};
		clip.addLineListener(listener);
	}
	
	public void stop(){
		active = false;
		clip.stop();
	}
	
}
