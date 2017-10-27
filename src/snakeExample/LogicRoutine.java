package snakeExample;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import engine.AssetManager;
import engine.Dependency;
import engine.Entity;
import engine.KeylogManager;
import engine.Routine;

public class LogicRoutine extends Routine {
	private static final int[] KEYS = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};
	private static int[] KEYS_TO_CHECK = KEYS.clone();
	private static int lastKeyPressed;
	private static Random rng = new Random();
	private static int ticks = -1;
	
	@Override
	public void onInit() {
		ticks = -1;
	}
	
	@Override
	public void onPhaseStart() {
		lastKeyPressed = KeyEvent.VK_RIGHT;
		es.registerGlobalComponent(new SnakeLength(SnakeGame.START_LENGTH));
		for(Entity e : es.getAllEntitiesPossessing(Position.class, BoxColor.class))
			e.removeAllPossibleComponents();
		int startX = rng.nextInt(SnakeGame.GRID_WIDTH), startY = rng.nextInt(SnakeGame.GRID_HEIGHT);
		new SnakeBox(startX, startY, new Color(SnakeGame.MAX_R, SnakeGame.MAX_G, SnakeGame.MAX_B), 0, SnakeDir.RIGHT);
		spawnApple(Arrays.asList(new Position(startX, startY)));
	}

	@Override
	public void routine() {
		for(int keyCode : KEYS_TO_CHECK){
			if(KeylogManager.pressed(keyCode))
				lastKeyPressed = keyCode;
		}
		es.getGlobalComponent(HeadDist.class).increment(((double)gm.dt()/1000000000)*(SnakeGame.TICKS_PER_SECOND));
		
		ticks++;
		if(ticks % SnakeGame.FRAMES_PER_TICK != 0)
			return;

		Entity head = null;
		List<Entity> entList = es.getAllEntitiesPossessing();
		List<Position> posList = new ArrayList<>();
		
		es.getGlobalComponent(HeadDist.class).reset();
		
		SnakeLength snakeLength = es.getGlobalComponent(SnakeLength.class);
		int length = snakeLength.length;
		for(Entity e : entList){
			BoxAge boxAge = e.getAs(BoxAge.class);
			int age = boxAge.age;
			if(entList.size() >= length - 1 && age >= length - 1){
				e.removeAllPossibleComponents();
				continue;
			}
			
			if(age == 0){
				head = e;
			}
			
			boxAge.increment();
			int red = (int)(SnakeGame.MAX_R-((SnakeGame.MAX_R/length)*(double)(age+1)));
			int green = (int)(SnakeGame.MAX_G-((SnakeGame.MAX_G/length)*(double)(age+1)));
			int blue = (int)(SnakeGame.MAX_B-((SnakeGame.MAX_B/length)*(double)(age+1)));
			e.getAs(BoxColor.class).color = new Color(red,green,blue);
			posList.add(e.getAs(Position.class));
		}
		
		if(head != null){
			Position headPos = head.getAs(Position.class);
			int dir = SnakeDir.RIGHT;
			double x = headPos.x, y = headPos.y;
			KEYS_TO_CHECK = KEYS.clone();
			switch(lastKeyPressed){
			case KeyEvent.VK_LEFT:
				x--; dir = SnakeDir.LEFT; KEYS_TO_CHECK[1] = -1; break;
			case KeyEvent.VK_RIGHT:
				x++; dir = SnakeDir.RIGHT; KEYS_TO_CHECK[0] = -1; break;
			case KeyEvent.VK_UP:
				y--; dir = SnakeDir.UP; KEYS_TO_CHECK[3] = -1; break;
			case KeyEvent.VK_DOWN:
				y++; dir = SnakeDir.DOWN; KEYS_TO_CHECK[2] = -1; break;
			}
			x %= SnakeGame.GRID_WIDTH;
			y %= SnakeGame.GRID_HEIGHT;
			if(x < 0) x += SnakeGame.GRID_WIDTH;
			if(y < 0) y += SnakeGame.GRID_HEIGHT;
			Position newPos = new Position(x,y);
			
			if(/*x < 0 || y < 0 || x >= SnakeGame.GRID_WIDTH || y >= SnakeGame.GRID_HEIGHT ||*/ posList.contains(newPos)){
				gm.setPhase(new GameOverPhase());
				return;
			}
			
			new SnakeBox(x, y, new Color(SnakeGame.MAX_R,SnakeGame.MAX_G,SnakeGame.MAX_B),0,dir);
			posList.add(newPos);
			
			for(Entity e : es.getAllEntitiesPossessing(Position.class, BoxColor.class)){
				if(e.getAs(BoxColor.class).color.getRed() == 255 && e.getAs(Position.class).equals(newPos)){
					snakeLength.length++;
					e.removeAllPossibleComponents();
					spawnApple(posList);
					AssetManager.playSound(SnakeGame.SOUND_APPLE);
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
		new Entity(new BoxColor(Color.RED), emptyPos.get(rng.nextInt(emptyPos.size())));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<? extends Dependency>[] dependencies() {
		Class[] d = {Position.class, BoxAge.class, BoxColor.class, SnakeLength.class, SnakeDir.class, HeadDist.class};
		return d;
	}

}
