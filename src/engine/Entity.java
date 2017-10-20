package engine;

public class Entity {

	private int id;
	
	public Entity(int i){
		if(EntitySystem.defaultSys == null)
			throw new IllegalStateException("There is no global EntitySystem; create a new EntitySystem before creating Entities");
		id = i;
	}
	
	public Entity(){
		if(EntitySystem.defaultSys == null)
			throw new IllegalStateException("There is no global EntitySystem; create a new EntitySystem before creating Entities");
		id = 0;
		EntitySystem.defaultSys.registerEntity(this);
	}
	
	public Entity(Routine routine, Component... components){
		this();
		addComponents(routine, components);
	}
	
	public <T extends Component> T getAs(Routine routine, Class<T> type){
		return routine.getComponent(this, type);
	}
	
	public void addComponent(Routine routine, Component c){
		routine.addComponentToEntity(this, c);
	}
	
	public void addComponents(Routine routine, Component... components){
		for(Component c : components)
			addComponent(routine, c);
	}
	
	public void removeComponent(Routine routine, Class<? extends Component> c){
		routine.removeComponentFromEntity(this, c);
	}
	
	public void removeAllPossibleComponents(Routine routine){
		routine.removeComponentsFromEntity(this);
	}
	
	protected void initializeID(int i){
		if(id != 0)
			throw new IllegalStateException("This entity already has an ID");
		id = i;
	}
	
	public int hashCode(){
		return id;
	}
	
}
