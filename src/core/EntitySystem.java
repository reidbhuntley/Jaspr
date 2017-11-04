package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntitySystem {
	
	protected static EntitySystem es;
	private List<Class<? extends Component>> componentIndex;
	private HashMap<Class<? extends Component>, HashMap<Entity, Component>> componentTables;
	private AtomicInteger nextAvailableID;
	ThreadLocal<ThreadSafe> context;
	
	public EntitySystem(){
		if(es == null)
			es = this;
		componentIndex = new ArrayList<>();
		componentTables = new HashMap<>();
		nextAvailableID = new AtomicInteger(1);
		context = new ThreadLocal<ThreadSafe>(){
			protected ThreadSafe initialValue(){
				return new ThreadSafe(){
					@Override public void onInit() {}
					@Override public void onPhaseEnd() {}
					@Override public void assertDependency(Class<? extends Component> type) {}
					@Override public void onPhaseStart() {}
				};
			}
		};
	}

	protected void setContext(ThreadSafe context){
		this.context.set(context);
	}

	protected static void registerEntity(Entity e) {
		e.initializeID(es.nextAvailableID.getAndIncrement());
	}
	
	protected void registerComponent(Class<? extends Component> type){
		if(!es.componentTables.containsKey(type)){
			es.componentIndex.add(type);
			es.componentTables.put(type, new HashMap<>());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T readComponent(Entity e, Class<T> type){
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		Component result = table.get(e).clone();
		assertClone(result, type);
		return (T) result;
	}
	
	public <T extends Component> HashMap<Entity, Component> readComponentTable(Class<T> type) {
		HashMap<Entity, Component> old = componentTables.get(type), out = new HashMap<>();
		for(Entity e : old.keySet()){
			Component c = old.get(e).clone();
			assertClone(c, type);
			out.put(e, (Component) c);
		}
		return out;
	}
	
	public boolean entityHasComponent(Entity e, Class<? extends Component> c){
		return componentTables.get(c).containsKey(e);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Entity> getAllEntitiesPossessing(Class... classes){
		List<Entity> out = new ArrayList<>();
		List<Class> classesList = new ArrayList<Class>(Arrays.asList(classes));
		for(Iterator<Class> iter = classesList.iterator(); iter.hasNext();){
			if(!Component.class.isAssignableFrom(iter.next()))
				iter.remove();
		}
		for(Class c : classesList){
			c = (Class<? extends Component>) c;
			context.get().assertDependency(c);
			registerComponent(c);
		}
		Collections.sort(classesList, new Comparator<Class>() {
			@Override
			public int compare(Class c0, Class c1) {
				return componentTables.get(c0).size() - componentTables.get(c1).size();
			}
		});
		for(Class c : classesList){
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
		if(Routine.class.isAssignableFrom(context.get().getClass()))
			return getAllEntitiesPossessing(((Routine)context.get()).dependencies());
		else
			return getAllEntitiesPossessing(componentTables.keySet().toArray(new Class[0]));
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Component>[] componentTypes(){
		return componentIndex.toArray(new Class[0]);
	}
	
	public void addComponentToEntity(Entity e, Component component){
		context.get().assertDependency(component.getClass());
		Class<? extends Component> componentClass = component.getClass();
		registerComponent(componentClass);
		componentTables.get(componentClass).put(e, component);
	}
	
	public void removeComponentFromEntity(Entity e, Class<? extends Component> c){
		context.get().assertDependency(c);
		componentTables.get(c).remove(e);
	}
	
	@SuppressWarnings("unchecked")
	public void removeComponentsFromEntity(Entity e){
		if(Routine.class.isAssignableFrom(context.get().getClass())){
			for(Class<? extends Component> c : ((Routine) context.get()).dependencies()){
				if(Component.class.isAssignableFrom(c))
					removeComponentFromEntity(e, (Class<? extends Component>) c);
			}
		} else {
			for(Class<? extends Component> c : componentTables.keySet())
				removeComponentFromEntity(e, (Class<? extends Component>) c);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Entity e, Class<T> type){
		context.get().assertDependency(type);
		registerComponent(type);
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		return  (T) table.get(e);
	}
	
	protected static void assertClone(Component dependency, Class<? extends Component> type){
		if(dependency == null)
			throw new IllegalArgumentException("Class "+type+" must override the clone() method in order to be read-only");
		if(dependency.getClass() != type)
			throw new IllegalArgumentException("The clone method of " + type + " does not return an object of its own type");
	}
	
}
