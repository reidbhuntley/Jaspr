package core;

import java.awt.Graphics2D;

public abstract class Renderer extends ThreadSafe {
	
	@Override
	public void assertDependency(Class<? extends Dependency> type){
		return;
	}
	
	public abstract void render(Graphics2D g);
	
}
