package core;

import java.util.HashSet;
import java.util.Set;

public class KeyManager {
	private Set<Integer> keysPressed, keysReleased, keysDown, keysPressedBuffer, keysReleasedBuffer, keysDownBuffer;
	private Set<Integer> keysListening;
	
	public KeyManager(int...keysToListen){
		keysPressed = new HashSet<>();
		keysReleased = new HashSet<>();
		keysDown = new HashSet<>();
		keysPressedBuffer = new HashSet<>();
		keysReleasedBuffer = new HashSet<>();
		keysDownBuffer = new HashSet<>();
		keysListening = new HashSet<>();
		for(int k : keysToListen){
			addKeyToListen(k);
		}
	}
	
	public void addKeyToListen(int keyEvent){
		keysListening.add(keyEvent);
	}

	protected void fetchKeys() {
		keysPressed = new HashSet<>(keysPressedBuffer);
		keysReleased = new HashSet<>(keysReleasedBuffer);
		keysDown = new HashSet<>(keysDownBuffer);
		keysPressedBuffer = new HashSet<>();
		keysReleasedBuffer = new HashSet<>();
	}

	protected void addKeyPressed(int keyEvent) {
		keysPressedBuffer.add(keyEvent);
		keysDownBuffer.add(keyEvent);
	}

	protected void addKeyReleased(int keyEvent) {
		keysReleasedBuffer.add(keyEvent);
		keysDownBuffer.remove(keyEvent);
	}

	public boolean pressed(int keyEvent) {
		return keysPressed.contains(keyEvent);
	}

	public boolean released(int keyEvent) {
		return keysReleased.contains(keyEvent);
	}
	
	public boolean down(int keyEvent){
		return keysDown.contains(keyEvent);
	}

	public Set<Integer> getPressedList() {
		return new HashSet<>(keysPressed);
	}

	public Set<Integer> getReleasedList() {
		return new HashSet<>(keysReleased);
	}
	
	public Set<Integer> getDownList() {
		return new HashSet<>(keysDown);
	}

	public Set<Integer> getKeysListening() {
		return new HashSet<>(keysListening);
	}

}
