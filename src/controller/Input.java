package controller;

import javafx.scene.input.KeyEvent;


public class Input{

	private static boolean wPressed, aPressed, sPressed, dPressed, spacePressed, spaceAlreadyPressed, escapePressed;
 
    
    /**
     * Wird bei Tastendruck aufgerufen & aktualisiert entsprechende booleans
     * 
     * @param e
     * @param pressed
     */
    public static void handle(KeyEvent event, boolean pressed) {
		String key = event.getCharacter();
		System.out.println("Handle");
		if (key.equalsIgnoreCase(" ") && spaceAlreadyPressed != true)
			spacePressed = pressed;
		spaceAlreadyPressed = pressed;
		
		
		if (key.equalsIgnoreCase("w"))
			wPressed = pressed;
		
		if (key.equalsIgnoreCase("a"))
			aPressed = pressed;
		
		if (key.equalsIgnoreCase("s"))
			sPressed = pressed;
		
		if (key.equalsIgnoreCase("d"))
			dPressed = pressed;
		
		
		
		if (event.getCode().isFunctionKey())
			escapePressed = pressed;
   
    }
    


	public static boolean isSpaceAlreadyPressed(){
		return spaceAlreadyPressed;
	}
	
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

	public static boolean isEscapePressed(){
		return escapePressed;
	}

    public static void updateLoop() {
    	spaceAlreadyPressed = false;
    }

}
