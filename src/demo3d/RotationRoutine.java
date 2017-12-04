package demo3d;

import core.Component;
import core.Routine;
import jaspr3d.Position3;

public class RotationRoutine extends Routine {
	
	public RotationRoutine(){
		super(Position3.class);
	}
	
	@Override
	public void routine() {
		for(Component c : Test3D.es.getComponentTable(Position3.class).values()){
			((Position3) c).rotate(1, 1, 1);;
		}
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
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
