package core;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public abstract class Renderer extends ThreadSafe {
	
	@Override
	public void assertDependency(Class<? extends Dependency> type){
		return;
	}
	
	public abstract void render(GLAutoDrawable drawable, GLU glu);
	
}
