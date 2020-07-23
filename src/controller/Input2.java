package controller;

public class Input2 {
	private static boolean wPressed, aPressed, sPressed, dPressed, spacePressed;
	
	public static boolean iswPressed(){
		return wPressed;
	}

	public static boolean isaPressed(){
		return aPressed;
	}

	public static boolean issPressed(){
		return sPressed;
	}

	public static boolean isdPressed(){
		return dPressed;
	}

	public static boolean isSpacePressed(){
		return spacePressed;
	}
	
	public static void resetAllPressed() {
		wPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
		spacePressed = false;
	}

	public static void setwPressed(boolean wPressed) {
		Input2.wPressed = wPressed;
	}

	public static void setaPressed(boolean aPressed) {
		Input2.aPressed = aPressed;
	}

	public static void setsPressed(boolean sPressed) {
		Input2.sPressed = sPressed;
	}

	public static void setdPressed(boolean dPressed) {
		Input2.dPressed = dPressed;
	}

	public static void setSpacePressed(boolean spacePressed) {
		Input2.spacePressed = spacePressed;
	}
}