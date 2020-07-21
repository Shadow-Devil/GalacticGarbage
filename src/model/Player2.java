package model;

import controller.GameBoard;
import controller.Input2;

public class Player2 extends Player {

	public Player2(double x, double y) {
		super(x, y);
	}
	
	@Override
	public void move() {
		super.move();

		if (Input2.isaPressed() && !Input2.isdPressed())
			facingVector.turn(DEGREE_ON_TURN).toUnit();
		if (Input2.isdPressed() && !Input2.isaPressed())
			facingVector.turn(-DEGREE_ON_TURN).toUnit();
		


		if (Input2.iswPressed() && !Input2.issPressed()) {
			Vector n = facingVector.copy().multiply(maxSpeed);
			accelerationVector.add(n);
			GameBoard.keepInFrame(positionVector);
		}
		
		if (Input2.issPressed() && !Input2.iswPressed()) {
			Vector n = facingVector.copy().multiply(-1*maxSpeed);
			accelerationVector.add(n);
			GameBoard.keepInFrame(positionVector);
		}
		
		if (Input2.isSpacePressed())
			shoot();
	}

}
