package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntitySystem {
	
	protected static EntitySystem defaultSys;
	private List<Class<? extends Component>> componentIndex;
	private HashMap<Class<? extends Component>, HashMap<Entity, Component>> componentTables;
	private AtomicInteger nextAvailableID;
	
	public EntitySystem(){
		componentIndex = new ArrayList<>();
		componentTables = new HashMap<>();
		nextAvailableID = new AtomicInteger(1);
	}
	
	public void setAsDefault(){
		defaultSys = this;
	}

	protected void registerEntity(Entity e) {
		e.initializeID(nextAvailableID.getAndIncrement());
	}
	
	protected <T extends Component> void createComponent(Class<T> type){
		if(!componentTables.containsKey(type)){
			componentIndex.add(type);
			componentTables.put(type, new HashMap<>());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getComponent(Entity e, Class<T> type){
	   HashMap<Entity, ? extends Component> table = componentTables.get(type);
	   T result = (T) table.get(e);
	   if(result == null)
	      throw new IllegalArgumentException("GET FAIL: "+e+" does not possess Component of class\n   missing: "+type);
	   return result;
	}
	
	protected <T extends Component> HashMap<Entity,Component> getComponentTable(Class<T> type){
		createComponent(type);
		return componentTables.get(type);
	}
	
}
