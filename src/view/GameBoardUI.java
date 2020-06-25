package view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import model.Player;
import model.SpaceObject;
import model.Vector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import controller.GameBoard;

public class GameBoardUI extends Canvas implements Runnable{

	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 600;

	private static final Color backgroundColor = Color.BLACK;
	private static final int SLEEP_TIME = 1000 / 25; // this gives us 25fps
	
	// attribute inherited by the JavaFX Canvas class
	private final GraphicsContext graphicsContext = this.getGraphicsContext2D();

	// thread responsible for starting game
	private Thread theThread;

	// user interface objects
	private GameBoard gameBoard;
	private int width, height;
	private Toolbar toolBar;

	// user control objects
	//private Input input;

	private static HashMap<SpaceObject, Image> spaceImages;

	public static GameBoardUI gui;

	/**
	 * Sets up all attributes, starts the mouse steering and sets up all graphics
	 * 
	 * @param toolBar used to start and stop the game
	 */
	public GameBoardUI(Toolbar toolBar){
		this.toolBar = toolBar;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		gameSetup(0);
		gui = this;
	}

	/**
	 * Called after starting the game thread Constantly updates the game board and renders
	 * graphics
	 * 
	 * @see Runnable#run()
	 */
	@Override
	public void run(){
		while (gameBoard.isRunning()){
			// updates Spaceobjects positions and re-renders graphics
			gameBoard.updateSpaceObjects();
			// when this.gameBoard.hasWon() is null, do nothing
			if (gameBoard.hasEnded()){
				showAsyncAlert("Oh.. you lost.\n" +
						"Your Score: " + gameBoard.getScore());
				stopGame();
			}
			paint(graphicsContext);
			try{
				Thread.sleep(SLEEP_TIME); // milliseconds to sleep
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}


	/**
	 * Removes all existing spaceObjects from the game board and re-adds them. Status bar is set to
	 * default value. Player spaceship is reset to default starting position. Renders graphics.
	 */
	public void gameSetup(int difficulty){
		
		gameBoard = new GameBoard(width, height, difficulty);
		widthProperty().set(width);
		heightProperty().set(height);
		width = (int) getWidth();
		height = (int) getHeight();
		spaceImages = new HashMap<>();
		
		gameBoard.resetSpaceObjects();
		gameBoard.getspaceObjects().forEach((so -> spaceImages.put(so, getImage(so.getIcon()))));
		paint(graphicsContext);
		toolBar.resetToolBarButtonStatus(false);
	}

	//public static HashMap<SpaceObject, Image> getSpaceImages(){
	//	return spaceImages;
	//}

	/**
	 * Sets the Spaceobjects image
	 *
	 * @param soImageFilePath: an image file path that needs to be available in the resources
	 *                          folder of the project
	 */
	private Image getImage(String soImageFilePath){	//TODO GameBoardUI - getImage()
		try{
			URL soImageUrl = getClass().getClassLoader().getResource(soImageFilePath);
			if (soImageUrl == null)
				throw new RuntimeException("Resource not found: " + soImageFilePath);
			
			InputStream inputStream = soImageUrl.openStream();
			return new Image(inputStream);
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

//	public static void addNew(SpaceObject so){
//		spaceImages.put(so, gui.getImage(so.getIcon()));
//	}

	/**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board, which
	 * causes the spaceObjects to change their positions (i.e. move). Renders graphics and updates
	 * tool bar status.
	 */
	public void startGame(){
		if (!gameBoard.isRunning()){
			gameBoard.startGame();
			theThread = new Thread(this);
			theThread.start();
			paint(graphicsContext);
			toolBar.resetToolBarButtonStatus(true);
		}
	}
	
	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame(){
		if (gameBoard.isRunning()){
			gameBoard.stopGame();
			toolBar.resetToolBarButtonStatus(false);
		}
	}

	/**
	 * Render the graphics of the whole game by iterating through the spaceObjects 
	 * of the game board at render each of them individually.
	 * 
	 * @param graphics used to draw changes
	 */
	private void paint(GraphicsContext graphics){ 
		graphics.setFill(backgroundColor);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		for (SpaceObject so: gameBoard.getspaceObjects()){
			paintSpaceObject(so, graphics);
		}
		// render player spaceShip
		//paintSpaceObject(gameBoard.getPlayer(), graphics); //Unnötig, da player auch in der Liste drin ist
	}

	/**
	 * Show image of a spaceObject at the current position of the spaceObject.
	 * 
	 * @param so SpaceObject to be drawn
	 * @param graphics used to draw changes
	 */
	private void paintSpaceObject(SpaceObject so, GraphicsContext graphics){
		Vector canvasPosition = convertPosition(so.getPositionVector());
		//System.out.println(so);
		//TODO richtige drehung, vll. invertieren
		
		graphics.save(); // saves the current state on stack, including the current transform
		//graphics.rotate((double) (so.getDirectionVector().getDegree()));
        rotate(graphics, -((so instanceof Player ? ((Player)so).getFacingVector() : so.getDirectionVector()).getDegree()),
		 canvasPosition.getX(), canvasPosition.getY());
   
		//TODO FELIX: hier überprüfen, ob das bild geladen ist und wenn nein neu laden??
		if(spaceImages.get(so) == null)
			spaceImages.put(so, gui.getImage(so.getIcon()));
		
        graphics.drawImage(spaceImages.get(so),
		        canvasPosition.getX() - so.getRadius(), canvasPosition.getY() - so.getRadius(), so.getRadius()*2, so.getRadius()*2);
        graphics.restore(); // back to original state (before rotation)
	}

	private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

	/**
	 * Converts position of spaceObject to position on the canvas
	 * 
	 * @param toConvert the point to be converted
	 */
	private Vector convertPosition(Vector toConvert){
		return new Vector(toConvert.getX(), (int) (getHeight() - toConvert.getY()));
	}

	//TODO "in updateSpaceObjects()" ?
	/**
	 * Method used to display alerts in moveCars() Java 8 Lambda Functions: java 8 lambda
	 * function without arguments Platform.runLater Function:
	 * https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html
	 *
	 * @param message you want to display as a String
	 */
	public void showAsyncAlert(String message){
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(message);
			alert.showAndWait();
			gameSetup(0);
		});
	}
	
//	/**
//	 * @return current gameBoard
//	 */
//	public GameBoard getGameBoard(){
//		return this.gameBoard;
//	}
}
