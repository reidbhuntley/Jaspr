package engine;

public abstract class GlobalComponent extends Dependency{
	public static void register(GlobalComponent c){
		EntitySystem.es.registerGlobalComponent(c);
	}
}
