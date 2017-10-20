package snakeExample;

import java.awt.Graphics2D;

import engine.Component;
import engine.Entity;
import engine.GameWindow;
import engine.Routine;

public class RenderRoutine extends Routine {

	private static GameWindow window;
	
	@Override
	public void routine() {
		Graphics2D g = window.getStrategyGraphics();
		g.clearRect(0, 0, window.getWidth(), window.getHeight());
		for(Entity e : getAllEntitiesPossessing()){
			Position pos = e.getAs(this, Position.class);
			int x = (int) (pos.x * boxScaleX()), y = (int) (pos.y * boxScaleY());
			g.setColor(e.getAs(this, BoxColor.class).color);
			g.fillRect(x, y, boxScaleX(), boxScaleY());
		}
		g.dispose();
		window.showStrategy();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<? extends Component>[] dependencies() {
		Class[] d = {Position.class, BoxColor.class};
		return d;
	}
	
	private static int boxScaleX(){
		return (window.getParent().getWidth() / SnakeGame.GRID_WIDTH);
	}
	
	private static int boxScaleY(){
		return (window.getParent().getHeight() / SnakeGame.GRID_HEIGHT);
	}

	@Override
	public void onInit() {
		window = new GameWindow(SnakeGame.WINDOW_WIDTH,SnakeGame.WINDOW_HEIGHT);
	}

}
