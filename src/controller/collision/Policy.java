package controller.collision;

public class Policy {

	private CollisionInterface context;

	public Policy(CollisionInterface collision) {
		this.context = collision;
	}

	/**
	 * Calls selectCollisionType in context
	 */
	public void selectStrategy() {
		context.selectCollisionType();
	}
}