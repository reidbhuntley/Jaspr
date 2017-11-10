package core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Routine implements Runnable {
	
	private static List<Class<? extends Routine>> initialized = new ArrayList<>();
	
	private List<Class<? extends Component>> dependencies;
	private GameManager gm;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Routine(Class... dependencies){
		for(Class c : dependencies){
			if(!Component.class.isAssignableFrom(c))
				throw new IllegalArgumentException("Classes in this Routine's dependencies must be child classes of Dependency");
		}
		this.dependencies = Arrays.asList(dependencies);
		
		if(!initialized.contains(getClass())){
			onInit();
			initialized.add(getClass());
		}
	}
	
	public void run(){
		EntitySystem.es.setContext(this);
		try {
			routine();
		} catch (RuntimeException e){
			e.printStackTrace();
			gm.quit();
		}
	}
	
	public List<Class<? extends Component>> dependencies(){
		return dependencies;
	}
	
	public void assertDependency(Class<? extends Component> type){
		if(dependencies == null || !dependencies.contains(type))
			throw new IllegalArgumentException("Dependency " + type.getName() + " is not in this Routine's dependencies");
	}
	
	public void setGame(GameManager gm){
		this.gm = gm;
	}
	
	public abstract void routine();
	public abstract void onInit();
	public abstract void onPhaseStart();
	public abstract void onPhaseEnd();
	
}
