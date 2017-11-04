package core;

public class Entity {

	private int id;
	
	public Entity(int i){
		if(EntitySystem.es == null)
			throw new IllegalStateException("There is no global EntitySystem; create a new EntitySystem before creating Entities");
		id = i;
	}
	
	public Entity(){
		if(EntitySystem.es == null)
			throw new IllegalStateException("There is no global EntitySystem; create a new EntitySystem before creating Entities");
		id = 0;
		EntitySystem.registerEntity(this);
	}
	
	public Entity(Component... components){
		this();
		addComponents(components);
	}
	
	public <T extends Component> T getAs(Class<T> type){
		return EntitySystem.es.getComponent(this, type);
	}
	
	public void addComponent(Component c){
		EntitySystem.es.addComponentToEntity(this, c);
	}
	
	public void addComponents(Component... components){
		for(Component c : components)
			addComponent(c);
	}
	
	public boolean hasComponent(Class<? extends Component> c){
		return EntitySystem.es.entityHasComponent(this, c);
	}
	
	public void removeComponent(Class<? extends Component> c){
		EntitySystem.es.removeComponentFromEntity(this, c);
	}
	
	public void removeAllPossibleComponents(){
		EntitySystem.es.removeComponentsFromEntity(this);
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
