package core;

public interface Component {
	public default Component getClone(){
		return null;
	};
}
