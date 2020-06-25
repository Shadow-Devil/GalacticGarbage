package controller.collision;

public class Policy{
	
	private Collision context;
	
	public Policy(Collision context){
		this.context = context;
	}
	
	public void selectStrategy() {
		context.selectCollisionType();
	}
}
