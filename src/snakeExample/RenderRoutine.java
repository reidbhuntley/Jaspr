package snakeExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import core.Entity;
import core.Renderer;

public class RenderRoutine extends Renderer {
	
	private static Image bg, apple, frame;
	
	@Override
	public void onInit() {
		bg = SnakeGame.images.readImage("res\\images\\bg.jpg");
		apple = SnakeGame.images.readImage("res\\images\\apple.png");
		frame = SnakeGame.images.readImage("res\\images\\frame.png");
	}
	
	@Override
	public void render(Graphics2D g) {
		double dDist = (double) es.readGlobalComponent(HeadDist.class).dist;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g.setBackground(new Color(51, 204, 255));
		//g.clearRect(0, 0, SnakeGame.WINDOW_WIDTH, SnakeGame.WINDOW_HEIGHT);
		g.drawImage(bg, 0, 0, SnakeGame.WINDOW_WIDTH, SnakeGame.WINDOW_HEIGHT, null);
		g.scale(boxScaleX(), boxScaleY());
		g.translate(0.5, 0.5);
		for(Entity e : es.getAllEntitiesPossessing(Position.class)){
			Position pos = e.getAs(Position.class);
			if(e.hasComponent(AppleComponent.class)){
				g.drawImage(apple, (int)pos.x, (int)pos.y, (int)pos.x+1, (int)pos.y+1, 0, 0, apple.getWidth(null), apple.getHeight(null), null);
				continue;
			}
			g.setColor(e.getAs(BoxColor.class).color);
			if(e.hasComponent(BoxAge.class)){
				if(e.getAs(BoxAge.class).age == 0){
					switch(e.getAs(SnakeDir.class).dir){
					case SnakeDir.LEFT:
						g.fill(new Arc2D.Double(pos.x-dDist, pos.y, 2, 1, 90, 180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x+1-dDist, pos.y, dDist, 1));
						break;
					case SnakeDir.RIGHT:
						g.fill(new Arc2D.Double(pos.x-1+dDist, pos.y, 2, 1, -90, 180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x, pos.y, dDist, 1));
						break;
					case SnakeDir.UP:
						g.fill(new Arc2D.Double(pos.x, pos.y-dDist, 1, 2, 0, 180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x, pos.y+1-dDist, 1, dDist));
						break;
					case SnakeDir.DOWN:
						g.fill(new Arc2D.Double(pos.x, pos.y-1+dDist, 1, 2, 180, 180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x, pos.y, 1, dDist));
						break;
					}
				} else if(e.getAs(BoxAge.class).age == es.readGlobalComponent(SnakeLength.class).length - 1){
					switch(e.getAs(SnakeDir.class).dir){
					case SnakeDir.LEFT:
						g.fill(new Arc2D.Double(pos.x-dDist, pos.y, 2, 1, 90, -180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x, pos.y, 1-dDist, 1));
						break;
					case SnakeDir.RIGHT:
						g.fill(new Arc2D.Double(pos.x-1+dDist, pos.y, 2, 1, -90, -180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x+dDist, pos.y, 1-dDist, 1));
						break;
					case SnakeDir.UP:
						g.fill(new Arc2D.Double(pos.x, pos.y-dDist, 1, 2, 0, -180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x, pos.y, 1, 1-dDist));
						break;
					case SnakeDir.DOWN:
						g.fill(new Arc2D.Double(pos.x, pos.y-1+dDist, 1, 2, 180, -180, Arc2D.PIE));
						g.fill(new Rectangle.Double(pos.x, pos.y+dDist, 1, 1-dDist));
						break;
					}
				} else {
					g.fill(new Rectangle.Double(pos.x, pos.y, 1, 1));
				}
			} else {
				g.fill(new Rectangle.Double(pos.x, pos.y, 1, 1));
			}
		}
		int score = (es.readGlobalComponent(SnakeLength.class).length - SnakeGame.START_LENGTH)*100;
		if(SnakeGame.WINDOW_WIDTH >= 200){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Calibri", Font.PLAIN, 30));
			g.scale(1/boxScaleX(), 1/boxScaleY());
			g.drawString("SCORE: " + score , 4, 24);
		}
		
		int fWidth = frame.getWidth(null), fHeight = frame.getHeight(null);
		
		AffineTransform t = new AffineTransform();
		t.scale(boxScaleX(), boxScaleY());
		t.translate(-0.5, -0.5);
		t.scale(0.5 / fWidth, SnakeGame.WINDOW_HEIGHT / (fHeight * boxScaleY()));
		g.drawImage(frame,t,null);
		
		t = new AffineTransform();
		t.scale(boxScaleX(), boxScaleY());
		t.translate(SnakeGame.GRID_WIDTH+0.5, -0.5);
		t.scale(-0.5 / fWidth, SnakeGame.WINDOW_HEIGHT / (fHeight * boxScaleY()));
		g.drawImage(frame,t,null);
		
		t = new AffineTransform();
		t.scale(boxScaleX(), boxScaleY());
		t.translate(-0.5, -0.5);
		t.rotate(Math.PI/2);
		t.scale(0.5 / fWidth, -SnakeGame.WINDOW_WIDTH/ (fHeight * boxScaleY()));
		g.drawImage(frame,t,null);
		
		t = new AffineTransform();
		t.scale(boxScaleX(), boxScaleY());
		t.translate(SnakeGame.GRID_WIDTH+0.5, SnakeGame.GRID_HEIGHT+0.5);
		t.rotate(Math.PI/2);
		t.scale(-0.5 / fWidth, SnakeGame.WINDOW_WIDTH/ (fHeight * boxScaleY()));
		g.drawImage(frame,t,null);
	}
	
	private static double boxScaleX(){
		return (SnakeGame.WINDOW_WIDTH / (SnakeGame.GRID_WIDTH+1));
	}
	
	private static double boxScaleY(){
		return (SnakeGame.WINDOW_HEIGHT / (SnakeGame.GRID_HEIGHT+1));
	}

	@Override
	public void onPhaseStart() {}

	@Override
	public void onPhaseEnd() {
		// TODO Auto-generated method stub
		
	}

}
