package core;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;
import jaspr3d.Position2;

public class MouseManager extends MouseInputAdapter {
	private boolean pressed, released, down, inWindow, entered, exited;
	private boolean pressedBuffer, releasedBuffer, downBuffer, inWindowBuffer, enteredBuffer, exitedBuffer;
	private Robot robot;
	private Position2 posBuffer, pos;
	private int X, Y, MIDX, MIDY;
	
	public MouseManager(){
		X = 0;
		Y = 0;
		MIDX = 0;
		MIDY = 0;
		pressed = false;
		released = false;
		down = false;
		inWindow = true;
		entered = false;
		exited = false;
		pressedBuffer = false;
		releasedBuffer = false;
		downBuffer = false;
		inWindowBuffer = true;
		enteredBuffer = false;
		exitedBuffer = false;
		pos = new Position2();
		posBuffer = new Position2();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	protected void assignToWindow(JFrame window){
		X = window.getX();
		Y = window.getY();
		MIDX = window.getWidth() / 2;
		MIDY = window.getHeight() / 2;
	}
	
	protected void fetchEvents(){
		pressed = pressedBuffer;
		released = releasedBuffer;
		down = downBuffer;
		inWindow = inWindowBuffer;
		entered = enteredBuffer;
		exited = exitedBuffer;
		
		pos = new Position2(posBuffer);
		pressed = false;
		released = false;
		entered = false;
		exited = false;
	}
	
	public boolean pressed(){
		return pressed;
	}
	public boolean released(){
		return released;
	}
	public boolean down(){
		return down;
	}
	public boolean inWindow(){
		return inWindow;
	}
	public boolean exited(){
		return exited;
	}
	public boolean entered(){
		return entered;
	}
	public Position2 position(){
		return new Position2(pos);
	}
	public void setPosition(int x, int y){
		robot.mouseMove(x + X + MIDX, y + Y + MIDY);
		posBuffer.set(x, y);
	}
	public void movePosition(int dx, int dy){
		robot.mouseMove((int)posBuffer.x() + dx + X + MIDX, (int)posBuffer.y() + dy + Y + MIDY);
		posBuffer.move(dx, dy);
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		pressedBuffer = true;
		downBuffer = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		releasedBuffer = true;
		downBuffer = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		enteredBuffer = true;
		inWindowBuffer = true;
		setPosBuffer(e.getPoint());
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		exitedBuffer = true;
		inWindowBuffer = false;
		setPosBuffer(e.getPoint());
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		setPosBuffer(e.getPoint());
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		setPosBuffer(e.getPoint());
	}
	
	private void setPosBuffer(Point point){
		posBuffer.set((float)point.getX() - MIDX, (float)point.getY() - MIDY);
	}
	
}
