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
		String key = event.getCode().getChar();
		//System.out.println("Handle");
		if (key.equals("E") && !spaceAlreadyPressed)
			spacePressed = pressed;
		spaceAlreadyPressed = pressed;
	    //System.out.println(key);
		
		if (key.equals("W"))
			wPressed = pressed;
		
		if (key.equals("A"))
			aPressed = pressed;
		
		if (key.equals("S"))
			sPressed = pressed;
		
		if (key.equals("D"))
			dPressed = pressed;
		
		
		
		if (event.getCode().isFunctionKey())
			escapePressed = pressed;
   
    }
    


	public static boolean isSpaceAlreadyPressed(){
		return spaceAlreadyPressed;
	}
	
	public static boolean iswPressed(){
		//System.out.println(wPressed);
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
