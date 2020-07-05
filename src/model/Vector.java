package model;

public class Vector{
	
	private double x, y;
	
	//1,0 - rechts - deg=0
	//0,1 - oben - deg=90
	//-1,0 - links - deg=180
	//0,-1 - unten - deg=270
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector add(Vector v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector sub(Vector v){
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public Vector multiply(double scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector toUnit() {
		double length = getLength();
		x /= length;
		y /= length;
		return this;
	}
	
	public Vector turn(double degree) {
		double length = getLength();
		turnUnitVector(degree);
		multiply(length);
		return this;
	}
	
	public Vector turnUnitVector(double degree) {
		double oldDegree = this.getDegree();
		this.x = Math.cos(Math.toRadians(degree + oldDegree));
		this.y = Math.sin(Math.toRadians(degree + oldDegree));
		return this;
	}
	
	public double getDegree() {
		double degree = Math.atan2(y, x)*180/Math.PI;
		return degree >= 0 ? degree : degree + 360;
	}
	
//	public static Vector vectorAfterDegree(int degree){
//		return new Vector((float)Math.cos(Math.toRadians(degree)), (float)Math.sin(Math.toRadians(degree)));
//	}
	
	public double getLength() {
		return Math.sqrt(x*x + y*y);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setXY(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return new vector with same coordinates
	 */
	public Vector copy() {
		return new Vector(x, y);
	}

	@Override
	public String toString(){
		return "Vector [x=" + x + ", y=" + y + "]";
	}
}
