package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	private HashMap<Class<? extends Component>, List<EntityFetcher>> entityFetchers;
	private HashMap<Class<? extends Component>, List<Entity>> componentsAdded, componentsRemoved;
	private AtomicInteger nextAvailableEntityID;
	ThreadLocal<Routine> context;

	public EntitySystem() {
		if (es == null)
			es = this;
		componentIndex = new ArrayList<>();
		componentTables = new HashMap<>();
		entityFetchers = new HashMap<>();
		componentsAdded = new HashMap<>();
		componentsRemoved = new HashMap<>();
		nextAvailableEntityID = new AtomicInteger(1);
		context = new ThreadLocal<Routine>() {
			protected Routine initialValue() {
				return new Routine() {
					@Override public void onInit() {}
					@Override public void onPhaseEnd() {}
					@Override public void assertDependency(Class<? extends Component> type) {}
					@Override public void onPhaseStart() {}
					@Override public void routine() {}
					@Override public Class<? extends Component>[] dependencies() { return null; }
				};
			}
		};
	}

	protected void setContext(Routine context) {
		this.context.set(context);
	}

	protected static void registerEntity(Entity e) {
		e.initializeID(es.nextAvailableEntityID.getAndIncrement());
	}

	protected void registerComponent(Class<? extends Component> type) {
		if (!componentTables.containsKey(type)) {
			componentIndex.add(type);
			componentTables.put(type, new HashMap<>());
			entityFetchers.put(type, new ArrayList<EntityFetcher>());
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T readComponent(Entity e, Class<T> type) {
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		Component result = table.get(e).clone();
		assertClone(result, type);
		return (T) result;
	}

	public <T extends Component> HashMap<Entity, Component> readComponentTable(Class<T> type) {
		HashMap<Entity, Component> old = componentTables.get(type), out = new HashMap<>();
		for (Entity e : old.keySet()) {
			Component c = old.get(e).clone();
			assertClone(c, type);
			out.put(e, (Component) c);
		}
		return out;
	}

	public boolean entityHasComponent(Entity e, Class<? extends Component> c) {
		registerComponent(c);
		return componentTables.get(c).containsKey(e);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Entity> getAllEntitiesPossessing(Class... classes) {
		List<Entity> out = new ArrayList<>();
		List<Class> classesList = new ArrayList<Class>(Arrays.asList(classes));
		for (Iterator<Class> iter = classesList.iterator(); iter.hasNext();) {
			if (!Component.class.isAssignableFrom(iter.next()))
				iter.remove();
		}
		for (Class c : classesList) {
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
		for (Class c : classesList) {
			if (out.size() == 0) {
				for (Entity e : componentTables.get(c).keySet())
					out.add(e);
			} else
				out.retainAll(componentTables.get(c).keySet());
			if (out.size() == 0)
				return out;
		}
		return out;
	}

	public List<Entity> getAllEntitiesPossessing() {
		if (Routine.class.isAssignableFrom(context.get().getClass()))
			return getAllEntitiesPossessing(((Routine) context.get()).dependencies());
		else
			return getAllEntitiesPossessing(componentTables.keySet().toArray(new Class[0]));
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Component>[] getComponentTypes() {
		return componentIndex.toArray(new Class[0]);
	}

	public void addComponentToEntity(Entity e, Component component) {
		context.get().assertDependency(component.getClass());
		Class<? extends Component> componentClass = component.getClass();

		registerComponent(componentClass);
		componentTables.get(componentClass).put(e, component);

		if (!componentsAdded.containsKey(componentClass))
			componentsAdded.put(componentClass, new ArrayList<Entity>());
		componentsAdded.get(componentClass).add(e);
	}

	public void removeComponentFromEntity(Entity e, Class<? extends Component> c) {
		context.get().assertDependency(c);
		componentTables.get(c).remove(e);

		if (!componentsRemoved.containsKey(c))
			componentsRemoved.put(c, new ArrayList<Entity>());
		componentsRemoved.get(c).add(e);
	}

	public void removeComponentsFromEntity(Entity e) {
		if (Routine.class.isAssignableFrom(context.get().getClass())) {
			for (Class<? extends Component> c : ((Routine) context.get()).dependencies()) {
				if (Component.class.isAssignableFrom(c))
					removeComponentFromEntity(e, (Class<? extends Component>) c);
			}
		} else {
			for (Class<? extends Component> c : componentTables.keySet())
				removeComponentFromEntity(e, (Class<? extends Component>) c);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Entity e, Class<T> type) {
		context.get().assertDependency(type);
		registerComponent(type);
		HashMap<Entity, ? extends Component> table = componentTables.get(type);
		return (T) table.get(e);
	}

	protected static void assertClone(Component dependency, Class<? extends Component> type) {
		if (dependency == null)
			throw new IllegalArgumentException(
					"Class " + type + " must override the clone() method in order to be read-only");
		if (dependency.getClass() != type)
			throw new IllegalArgumentException(
					"The clone method of " + type + " does not return an object of its own type");
	}

	public EntityFetcher genEntityFetcher(Class<? extends Component> component) {
		List<Entity> entities = new ArrayList<>();
		registerComponent(component);
		entities.addAll(componentTables.get(component).keySet());
		EntityFetcher fetcher = new EntityFetcher();
		registerComponent(component);
		entityFetchers.get(component).add(fetcher);
		return fetcher;
	}
	
	public SingletonFetcher genSingletonFetcher(Class<? extends Component> component){
		return new SingletonFetcher(this, component);
	}

	protected void updateFetchers() {
		for (Class<? extends Component> c : entityFetchers.keySet()) {
			List<Entity> entitiesAdded = componentsAdded.get(c);
			List<Entity> entitiesRemoved = componentsRemoved.get(c);
			for (EntityFetcher fetcher : entityFetchers.get(c)) {
				if (fetcher.isNew()) {
					fetcher.addEnts(componentTables.get(c).keySet());
				} else {
					if (entitiesAdded != null)
						fetcher.addEnts(entitiesAdded);
					if (entitiesRemoved != null)
						fetcher.removeEnts(entitiesRemoved);
				}
			}
		}
		componentsAdded.clear();
		componentsRemoved.clear();
	}

	public class EntityFetcher {
		private List<Entity> entities;
		private boolean isNew;

		private EntityFetcher() {
			entities = new ArrayList<>();
			isNew = true;
		}
		
		public List<Entity> fetch(){
			return entities;
		}

		private boolean isNew() {
			if (isNew) {
				isNew = false;
				return true;
			}
			return false;
		}

		private void addEnts(Collection<Entity> entities) {
			this.entities.addAll(entities);
		}

		private void removeEnts(Collection<Entity> entities) {
			this.entities.removeAll(entities);
		}
		
	}
	
	public class SingletonFetcher {
		private EntityFetcher fetcher;
		private Entity entity;
		private Class<? extends Component> c;
		private SingletonFetcher(EntitySystem es, Class<? extends Component> type){
			c = type;
			fetcher = es.genEntityFetcher(c);
			entity = null;
		}
		public Entity fetch(){
			if(entity == null && fetcher.fetch().size() > 0){
				entity = fetcher.fetch().get(0);
				fetcher = null;
			}
			return entity;
		}
		public Component fetchComponent(){
			fetch();
			if(entity == null)
				return null;
			return entity.getAs(c);
		}
	}

}
