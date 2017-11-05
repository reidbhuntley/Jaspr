package res;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Loop {

	private AudioInputStream stream;
	private Clip clip;
	private LineListener listener;
	private boolean active;
	
	protected Loop(AudioInputStream stream){
		this.stream = stream;
		active = false;
	}
	
	public void start() {
		clip = SoundManager.prepareClip(stream);
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
