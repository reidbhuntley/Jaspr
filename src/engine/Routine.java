package engine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public abstract class Routine implements Runnable {
	
	private static List<Class<? extends Routine>> initialized = new ArrayList<>();
	
	private HashMap<Class<? extends Component>, HashMap<Entity, Component>> componentTables;
	private List<Class<? extends Component>> dependencies;
	
	public Routine(){
		super();
		dependencies = Arrays.asList(dependencies());
		componentTables = new HashMap<>();
		
		if(dependencies() != null){
			for(Class<? extends Component> c : dependencies){
				if(!Component.class.isAssignableFrom(c))
					throw new IllegalArgumentException("Classes in this Routine's dependencies must be child classes of Component");
				componentTables.put(c, EntitySystem.defaultSys.getComponentTable(c));
			}
		}
		
		if(!initialized.contains(getClass())){
			onInit();
			initialized.add(getClass());
		}
	}
	
	public void run(){
		routine();
	}
	
	public void addComponentToEntity(Entity e, Component component){
		Class<? extends Component> componentClass = component.getClass();
		componentTables.get(componentClass).put(e, component);
	}
	
	public void removeComponentFromEntity(Entity e, Class<? extends Component> c){
		componentTables.get(c).remove(e);
	}
	
	public void removeComponentsFromEntity(Entity e){
		for(Class<? extends Component> c : dependencies())
			removeComponentFromEntity(e, c);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Entity e, Class<T> type){
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		T result = (T) table.get(e);
		if(result == null)
		   throw new IllegalArgumentException("Routine "+e+" does not possess Component of class\n   missing: "+type);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Entity> getAllEntitiesPossessing(Class... classes){
		List<Entity> out = new ArrayList<>();
		Arrays.sort(classes, new Comparator<Class>() {
			@Override
			public int compare(Class c0, Class c1) {
				if(!Component.class.isAssignableFrom(c0) || !Component.class.isAssignableFrom(c1))
					throw new IllegalArgumentException("Class arguments must be child classes of Component");
				return componentTables.get(c0).size() - componentTables.get(c1).size();
			}
		});
		for(Class c : classes){
			if(out.size() == 0){
				for(Entity e : componentTables.get(c).keySet())
					out.add(e);
			} else
				out.retainAll(componentTables.get(c).keySet());
			if(out.size() == 0)
				return out;
		}
		return out;
	}
	
	public List<Entity> getAllEntitiesPossessing(){
		return getAllEntitiesPossessing(dependencies());
	}
	
	public abstract void onInit();
	public abstract void routine();
	public abstract Class<? extends Component>[] dependencies();
	
}
