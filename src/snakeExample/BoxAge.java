package snakeExample;

import core.Component;

public class BoxAge extends Component {
	int age;
	public BoxAge(int age){
		this.age = age;
	}
	public void increment(){
		age++;
	}
}
