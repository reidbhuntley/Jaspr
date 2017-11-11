package demo3d;

import core.Entity;
import core.EntitySystem.EntityFetcher;
import core.Routine;
import jaspr3d.Position3;

public class RotationRoutine extends Routine {

	private static EntityFetcher positions;
	
	public RotationRoutine(){
		super(Position3.class);
	}
	
	@Override
	public void routine() {
		for(Entity e : positions.fetch()){
			e.getAs(Position3.class).rotate(1, 1, 1);
		}
	}

	@Override
	public void onInit() {
		positions = Test3D.es.getEntityFetcher(Position3.class);
	}

	@Override
	public void onPhaseStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPhaseEnd() {
		// TODO Auto-generated method stub
		
	}

}
