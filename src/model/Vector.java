package model;

public class Vector{
	
	private double x, y;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector v) {
		x += v.x;
		y += v.y;
	}
	
	public void multiply(int scalar) {
		x *= scalar;
		y *= scalar;
	}
	
	public void turnUnitVector(int degree) {
		this.x = Math.cos(Math.toRadians(degree + this.getDegree()));
		this.y = Math.sin(Math.toRadians(degree + this.getDegree()));
	}
	
	public int getDegree() {
		return (int) (Math.atan2(0.5, 0.5)*180/Math.PI);
	}
	
//	public static Vector vectorAfterDegree(int degree){
//		return new Vector((float)Math.cos(Math.toRadians(degree)), (float)Math.sin(Math.toRadians(degree)));
//	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public Vector copy() {
		return new Vector(x, y);
	}
}
