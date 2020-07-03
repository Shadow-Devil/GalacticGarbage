package controller.collision;

import model.Debris;
import model.Player;
import model.Projectile;
import model.SpaceObject;
import model.Vector;

public class CollisionProjectile_Debris implements CollisionType{

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector){
		internCollide((Projectile) one, (Debris) two);
	}
	
	private void internCollide(Projectile one, Debris two) {
		one.die();
		if(two.getSize() > 0) 
			two.die(one.getDirectionVector().copy());
			
		
	}
}
