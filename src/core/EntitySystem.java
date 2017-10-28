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
	private HashMap<Class<? extends GlobalComponent>, GlobalComponent> globalsTable;
	private AtomicInteger nextAvailableID;
	private ThreadLocal<ThreadSafe> context;
	
	public EntitySystem(){
		componentIndex = new ArrayList<>();
		componentTables = new HashMap<>();
		globalsTable = new HashMap<>();
		nextAvailableID = new AtomicInteger(1);
		context = new ThreadLocal<ThreadSafe>(){
			protected ThreadSafe initialValue(){
				return new ThreadSafe(){
					@Override public void onInit() {}
					@Override public void onPhaseStart() {}
					@Override public void onPhaseEnd() {}
					@Override public void assertDependency(Class<? extends Dependency> type) {}
				};
			}
		};
	}
	
	public void setAsDefault(){
		es = this;
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
	protected <T extends GlobalComponent> T getGlobalComponentUnsafe(Class<T> type) {
		if(!globalsTable.containsKey(type))
			throw new IllegalArgumentException("The GlobalComponent of type " + type + " has not yet been registered");
		return (T) globalsTable.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T readComponent(Entity e, Class<T> type){
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		Dependency result = table.get(e).clone();
		assertClone(result, type);
		return (T) result;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GlobalComponent> T readGlobalComponent(Class<T> type) {
		Dependency result = getGlobalComponentUnsafe(type).clone();
		assertClone(result, type);
		return (T) result;
	}
	
	public <T extends Component> HashMap<Entity, Component> readComponentTable(Class<T> type) {
		HashMap<Entity, Component> old = componentTables.get(type), out = new HashMap<>();
		for(Entity e : old.keySet()){
			Dependency c = old.get(e).clone();
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
	
	@SuppressWarnings("unchecked")
	public Class<? extends GlobalComponent>[] globalComponentTypes(){
		return globalsTable.keySet().toArray(new Class[0]);
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
			for(Class<? extends Dependency> c : ((Routine) context.get()).dependencies()){
				if(Component.class.isAssignableFrom(c))
					removeComponentFromEntity(e, (Class<? extends Component>) c);
			}
		} else {
			for(Class<? extends Dependency> c : componentTables.keySet())
				removeComponentFromEntity(e, (Class<? extends Component>) c);
		}
	}
	
	
	public void registerGlobalComponent(GlobalComponent c){
		context.get().assertDependency(c.getClass());
		globalsTable.put(c.getClass(), c);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Entity e, Class<T> type){
		context.get().assertDependency(type);
		registerComponent(type);
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		return  (T) table.get(e);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GlobalComponent> T getGlobalComponent(Class<T> type){
		context.get().assertDependency(type);
		return (T) globalsTable.get(type);
	}
	
	protected static void assertClone(Dependency dependency, Class<? extends Dependency> type){
		if(dependency == null)
			throw new IllegalArgumentException("Class "+type+" must override the clone() method in order to be read-only");
		if(dependency.getClass() != type)
			throw new IllegalArgumentException("The clone method of " + type + " does not return an object of its own type");
	}
	
}
