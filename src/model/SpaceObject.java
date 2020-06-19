package model;

import controller.GameBoard;

public abstract class SpaceObject{
	protected final int radius;
	protected final String icon;
	protected final Vector positionVector, directionVector;
	
	protected double speed;
	protected boolean alive;
	
	public SpaceObject(int radius, String icon, Vector positionVector, Vector directionVector, double speed) {
		this.radius = radius;
		this.icon = icon;
		this.positionVector = positionVector;
		this.directionVector = directionVector;
		this.speed = speed;
		this.alive = true;
	}

	public void move(int maxX, int maxY) {
		this.getPositionVector().add(directionVector.copy().multiply(speed));
		//TODO Beschleunigung
		if(this.getPositionVector().getX() >= maxX)
			this.getPositionVector().setX(0);
		if(this.getPositionVector().getY() >= maxY)
			this.getPositionVector().setY(0);
		
		if(this.getPositionVector().getX() < 0)
			this.getPositionVector().setX(maxX);
		if(this.getPositionVector().getY() > 0)
			this.getPositionVector().setY(maxY);
	}
	
	/**
	 * Adds this spaceObject to GameBoard.deadSpaceObjects. <br>
	 * Sets the boolean alive to false.
	 */
	public void die() {
		GameBoard.deadSpaceObjects.add(this);
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}

	public double getSpeed(){
		return speed;
	}

	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public int getRadius(){
		return radius;
	}

	public String getIcon(){
		return icon;
	}

	public Vector getPositionVector(){
		return positionVector;
	}

	public Vector getDirectionVector(){
		return directionVector;
	}

	public abstract void collide(SpaceObject two, Vector collisionVector);
	
	public void bounce(SpaceObject two, Vector collisionVector){
		double degree = 90 - collisionVector.getDegree();//TODO kontrollieren
		
		Vector v1 = this.directionVector.copy().turn(degree).multiply(this.speed);
		Vector v2 = two.directionVector.copy().turn(degree).multiply(two.speed);
		
		double temp = v1.getY();
		v1.setY(v2.getY());
		v2.setY(temp);
		
	}
	
	public void repel(SpaceObject two, Vector collisionVector){
		double degree = collisionVector.getDegree() - two.directionVector.getDegree();
		double diff = Math.signum(degree);
		if (diff == 0){
			two.directionVector.multiply(-1);
		} else {
			two.directionVector.turn(2*degree*diff);
		}

	}
}
