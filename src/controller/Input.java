package controller;

import javafx.scene.input.KeyEvent;

public class Input {

	private static boolean wPressed, aPressed, sPressed, dPressed, spacePressed, resetet;

	/**
	 * Is called on keystrokes and updates corresponding booleans
	 * 
	 * @param event
	 * @param pressed
	 */
	public static void handle(KeyEvent event, boolean pressed) {
		String key = event.getCode().getChar();
		if (key.equals("E")) {
			if (pressed && !resetet)
				spacePressed = pressed;

			if (!pressed) {
				spacePressed = false;
				resetet = false;
			}
		}

		if (key.equals("W"))
			wPressed = pressed;
		if (key.equals("A"))
			aPressed = pressed;
		if (key.equals("S"))
			sPressed = pressed;
		if (key.equals("D"))
			dPressed = pressed;
	}

	public static void resetSpacePressed() {
		spacePressed = false;
		resetet = true;
	}

	public static boolean iswPressed() {
		return wPressed;
	}

	public static boolean isaPressed() {
		return aPressed;
	}

	public static boolean issPressed() {
		return sPressed;
	}

	public static boolean isdPressed() {
		return dPressed;
	}

	public static boolean isSpacePressed() {
		return spacePressed;
	}

	public static void resetAllPressed() {
		wPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
		spacePressed = false;
		resetet = false;
	}
}