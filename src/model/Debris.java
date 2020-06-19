package model;

public class Debris extends SpaceObject{
	
	private static final String ICONNAME = "debrisIcon";
	private final int size;//0, 1, 2
	public static final int damagePerSize = 10;
	private static final int radiusPerSize = 10;
	
	public Debris(int size, Vector positionVector, Vector directionVector, double speed){
		super((size+1) * radiusPerSize, ICONNAME, positionVector, directionVector, speed);
		this.size = size;
	}

	/**
	 * Is invoked when debris is split into smaller parts.
	 */
	public void split() {
		//TODO Debris - split()
	}
	
	/**
	 * @return size
	 */
	public int getSize(){
		return size;
	}

	@Override
	public void collide(SpaceObject two, Vector collisionVector){
		if(two instanceof Player) {
			if(size > 0)
				((Player) two).loseHealth(size * damagePerSize);
			
			bounce(two, collisionVector);
		}else if(two instanceof Projectile) {
			if(size > 0)
				split();
			
			two.die();
		}else if(two instanceof Moon || two instanceof Planet){
			if(size == 0)
				die();
			else {
				//TODO eventuell GameLost
				two.repel(this, collisionVector);
			}
		}else {
			bounce(two, collisionVector);
		}
		
		
	}


}
