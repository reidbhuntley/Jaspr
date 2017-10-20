package engine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public final class GameWindow extends Canvas {

    private BufferStrategy bs;

    public GameWindow(int width, int height) {
    	JFrame frame = new JFrame();
    	JPanel panel = (JPanel) frame.getContentPane();
    	panel.setPreferredSize(new Dimension(width,height));
    	panel.setLayout(null);
    	
    	setBounds(0,0,width,height);
    	panel.add(this);
    	
    	setIgnoreRepaint(true);
    	
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
    	
		addKeyListener(new KeylogReciever());
		requestFocus();
		
		createBufferStrategy(2);
		bs = getBufferStrategy();
    }
    
	public void addNotify() {
		super.addNotify();
		createBufferStrategy(2);
		bs = getBufferStrategy();
	}

    public Graphics2D getStrategyGraphics(){
    	return (Graphics2D) bs.getDrawGraphics();
    }
    
    public void showStrategy(){
    	bs.show();
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