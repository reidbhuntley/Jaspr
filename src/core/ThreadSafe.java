package core;

public abstract class ThreadSafe {
	
	public abstract void onInit();
	public abstract void onPhaseStart();
	public abstract void onPhaseEnd();
	public abstract void assertDependency(Class<? extends Dependency> type);
}
