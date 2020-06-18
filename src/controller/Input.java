package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener{

	private boolean wPressed, aPressed, sPressed, dPressed, spacePressed, spaceAlreadyPressed, escapePressed;
	
	public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        handle(e, true);
    }

    public void keyReleased(KeyEvent e) {
        handle(e, false);
    }    
    
    /**
     * Wird bei Tastendruck aufgerufen & aktualisiert entsprechende booleans
     * 
     * @param e
     * @param pressed
     */
    private void handle(KeyEvent e, boolean pressed) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_SPACE && spaceAlreadyPressed != true)
            spacePressed = pressed;            
        spaceAlreadyPressed = pressed;
           
        
        if (keyCode == KeyEvent.VK_W)
            wPressed = pressed;
        
        if (keyCode == KeyEvent.VK_A)
            aPressed = pressed;
        
        if (keyCode == KeyEvent.VK_S)
            sPressed = pressed;
        
        if (keyCode == KeyEvent.VK_D)
            dPressed = pressed;
            
             
    
        if (keyCode == KeyEvent.VK_ESCAPE)
            escapePressed = pressed;
    }
    
	public boolean iswPressed(){
		return wPressed;
	}

	public boolean isaPressed(){
		return aPressed;
	}

	public boolean issPressed(){
		return sPressed;
	}

	public boolean isdPressed(){
		return dPressed;
	}

	public boolean isSpacePressed(){
		return spacePressed;
	}

	public boolean isEscapePressed(){
		return escapePressed;
	}

    public void updateLoop() {
    	spaceAlreadyPressed = false;
    }
	
	
}
