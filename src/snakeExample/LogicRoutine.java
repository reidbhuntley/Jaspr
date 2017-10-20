package snakeExample;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import engine.Component;
import engine.Entity;
import engine.GameManager;
import engine.KeylogManager;
import engine.Routine;

public class LogicRoutine extends Routine {

	private static final int[] KEYS_TO_CHECK = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};
	private static int lastKeyPressed = KeyEvent.VK_RIGHT, snakeLength = SnakeGame.START_LENGTH;
	private static Random rng = new Random();
	
	@Override
	public void onInit() {
		new SnakeBox(this,SnakeGame.START_X,SnakeGame.START_Y,Color.CYAN);
		spawnApple(Arrays.asList(new Position(SnakeGame.START_X, SnakeGame.START_Y)));
	}

	@Override
	public void routine() {
		for(int keyCode : KEYS_TO_CHECK){
			if(KeylogManager.pressed(keyCode))
				lastKeyPressed = keyCode;
		}
		Entity head = null;
		List<Entity> entList = getAllEntitiesPossessing();
		List<Position> posList = new ArrayList<>();
		
		for(Entity e : entList){
			BoxAge boxAge = e.getAs(this, BoxAge.class);
			int age = boxAge.age;
			if(entList.size() >= snakeLength - 1 && age >= snakeLength - 1){
				e.removeAllPossibleComponents(this);
				continue;
			}
			
			if(age == 0){
				head = e;
			}
			
			boxAge.increment();
			int red = (int)(SnakeGame.MAX_R-((SnakeGame.MAX_R/snakeLength)*(double)(age+1)));
			int green = (int)(SnakeGame.MAX_G-((SnakeGame.MAX_G/snakeLength)*(double)(age+1)));
			int blue = (int)(SnakeGame.MAX_B-((SnakeGame.MAX_B/snakeLength)*(double)(age+1)));
			e.getAs(this, BoxColor.class).color = new Color(red,green,blue);
			posList.add(e.getAs(this, Position.class));
		}
		
		if(head != null){
			Position headPos = head.getAs(this, Position.class);
			double x = headPos.x, y = headPos.y;
			switch(lastKeyPressed){
			case KeyEvent.VK_LEFT:
				x--; break;
			case KeyEvent.VK_RIGHT:
				x++; break;
			case KeyEvent.VK_UP:
				y--; break;
			case KeyEvent.VK_DOWN:
				y++; break;
			}
			Position newPos = new Position(x,y);
			
			if(x < 0 || y < 0 || x >= SnakeGame.GRID_WIDTH || y >= SnakeGame.GRID_HEIGHT || posList.contains(newPos)){
				GameManager.setPhase(new GameOverPhase());
				return;
			}
			
			new SnakeBox(this, x, y, new Color(SnakeGame.MAX_R,SnakeGame.MAX_G,SnakeGame.MAX_B));
			posList.add(newPos);
			
			for(Entity e : getAllEntitiesPossessing(Position.class, BoxColor.class)){
				if(e.getAs(this, BoxColor.class).color.getRed() == 255 && e.getAs(this, Position.class).equals(newPos)){
					snakeLength++;
					e.removeAllPossibleComponents(this);
					spawnApple(posList);
					break;
				}
			}
			
		}
		
	}
	
	private void spawnApple(List<Position> filledPos){
		List<Position> emptyPos = new ArrayList<>();
		for(int x = 0; x < SnakeGame.GRID_WIDTH; x++){
			for(int y = 0; y < SnakeGame.GRID_HEIGHT; y++){
				Position pos = new Position(x,y);
				if(!filledPos.contains(pos)){
					emptyPos.add(new Position(x,y));
				}
			}
		}
		new Entity(this, new BoxColor(Color.RED), emptyPos.get(rng.nextInt(emptyPos.size())));
	}
	
	public static int getSnakeLength(){
		return snakeLength;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<? extends Component>[] dependencies() {
		Class[] d = {Position.class, BoxAge.class, BoxColor.class};
		return d;
	}

}
