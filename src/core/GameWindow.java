package core;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public final class GameWindow extends JFrame {

    private BufferStrategy bs;
    private Renderer renderer;

    public GameWindow(int width, int height) {
    	setBounds(0, 0, width, height);
    	setIgnoreRepaint(true);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	setUndecorated(true);
		setResizable(true);
		setVisible(true);
    	
		addKeyListener(new KeylogReciever());
		requestFocus();
		
		createBufferStrategy(3);
		bs = getBufferStrategy();
    }
    
    protected void render(){
    	if(renderer != null){
	    	Graphics2D g = (Graphics2D) bs.getDrawGraphics();
	    	renderer.render(g);
	    	g.dispose();
	    	bs.show();
    	}
    }
    
    protected void assignRenderer(Renderer r){
    	renderer = r;
    }
    
	public void addNotify() {
		super.addNotify();
		createBufferStrategy(2);
		bs = getBufferStrategy();
	}
    
	private class KeylogReciever extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			KeylogManager.addKeyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			KeylogManager.addKeyReleased(e);
		}
	}
    
}