package core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Routine extends ThreadSafe implements Runnable {
	
	private static List<Class<? extends Routine>> initialized = new ArrayList<>();
	
	private List<Class<? extends Dependency>> dependencies;
	
	public Routine(){
		super();
		if(dependencies() != null){
			dependencies = Arrays.asList(dependencies());
			for(Class<? extends Dependency> c : dependencies){
				if(!Dependency.class.isAssignableFrom(c))
					throw new IllegalArgumentException("Classes in this Routine's dependencies must be child classes of Dependency");
			}
		}
		
		if(!initialized.contains(getClass())){
			onInit();
			initialized.add(getClass());
		}
	}
	
	public void run(){
		es.setContext(this);
		routine();
	}
	
	@Override
	public void assertDependency(Class<? extends Dependency> type){
		if(!dependencies.contains(type))
			throw new IllegalArgumentException("Dependency " + type.getClass().getName() + " is not in this Routine's dependencies");
	}
	
	public abstract void routine();
	public abstract Class<? extends Dependency>[] dependencies();
	
}
