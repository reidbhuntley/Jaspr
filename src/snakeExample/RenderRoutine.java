package snakeExample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;

import engine.Entity;
import engine.GameWindow;
import engine.Renderer;

public class RenderRoutine extends Renderer {

	private static GameWindow window;
	
	@Override
	public void render() {
		double dDist = (double) es.readGlobalComponent(HeadDist.class).dist;
		Graphics2D g = window.getStrategyGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0, 0, window.getWidth(), window.getHeight());
		g.scale(boxScaleX(), boxScaleY());
		for(Entity e : es.getAllEntitiesPossessing(Position.class, BoxColor.class)){
			Position pos = e.getAs(Position.class);
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
		g.setColor(Color.BLACK);
		g.setFont(new Font("Calibri", Font.PLAIN, 30));
		g.scale(1/boxScaleX(), 1/boxScaleY());
		g.drawString("SCORE: " + score , 4, 34);
		g.dispose();
		window.showStrategy();
	}
	
	private static double boxScaleX(){
		return (window.getParent().getWidth() / SnakeGame.GRID_WIDTH);
	}
	
	private static double boxScaleY(){
		return (window.getParent().getHeight() / SnakeGame.GRID_HEIGHT);
	}

	@Override
	public void onInit() {
		window = new GameWindow(SnakeGame.WINDOW_WIDTH,SnakeGame.WINDOW_HEIGHT); 
	}

	@Override
	public void onPhaseStart() {}

}
