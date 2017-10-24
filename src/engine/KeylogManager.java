package engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KeylogManager {
	private static List<Integer> keysPressed = new ArrayList<>(), keysReleased = new ArrayList<>(), 
			keysPressedBuffer = new ArrayList<>(), keysReleasedBuffer = new ArrayList<>();
	protected static void fetchKeys(){
		keysPressed = new ArrayList<>(keysPressedBuffer);
		keysReleased = new ArrayList<>(keysReleasedBuffer);
		keysPressedBuffer = new ArrayList<>();
		keysReleasedBuffer = new ArrayList<>();
	}
	protected static void addKeyPressed(KeyEvent e){
		keysPressedBuffer.add(e.getKeyCode());
	}
	protected static void addKeyReleased(KeyEvent e){
		keysReleasedBuffer.add(e.getKeyCode());
	}
	public static boolean pressed(int keyCode){
		return keysPressed.contains(keyCode);
	}
	public static boolean released(int keyCode){
		return keysReleased.contains(keyCode);
	}
	public static List<Integer> getPressedList(){
		return new ArrayList<>(keysPressed);
	}
	public static List<Integer> getReleasedList(){
		return new ArrayList<>(keysReleased);
	}
}
