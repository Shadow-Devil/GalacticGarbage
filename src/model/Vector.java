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
	
	public void turn(int degree) {
		//TODO
	}
	
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
