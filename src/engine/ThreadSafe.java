package engine;

public abstract class ThreadSafe {
	protected GameManager gm;
	protected EntitySystem es;
	
	protected void prepareForProcessing(GameManager gm, EntitySystem es){
		this.gm = gm;
		if(es == null)
			throw new IllegalStateException("There is no global EntitySystem; create a new EntitySystem before running Routines");
		this.es = es;
	}
	
	public abstract void onInit();
	public abstract void onPhaseStart();
	public abstract void onPhaseEnd();
	public abstract void assertDependency(Class<? extends Dependency> type);
}
