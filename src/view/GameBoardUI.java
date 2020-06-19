package view;

import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.Player;
import model.SpaceObject;
import model.Vector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import controller.GameBoard;
import controller.Input;

public class GameBoardUI extends Canvas implements Runnable{

	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 300;

	private static final Color backgroundColor = Color.WHITE;
	private static final int SLEEP_TIME = 1000 / 25; // this gives us 25fps
	// attribute inherited by the JavaFX Canvas class
	private GraphicsContext graphicsContext = this.getGraphicsContext2D();

	// thread responsible for starting game
	private Thread theThread;

	// user interface objects
	private GameBoard gameBoard;
	private int width, height;
	private Toolbar toolBar;

	// user control objects
	//private Input input;

	private HashMap<SpaceObject, Image> spaceImages;

	/**
	 * Sets up all attributes, starts the mouse steering and sets up all graphics
	 * 
	 * @param toolBar used to start and stop the game
	 */
	public GameBoardUI(Toolbar toolBar){
		this.toolBar = toolBar;
		this.width = getDEFAULT_WIDTH();
		this.height = getDEFAULT_HEIGHT();
		gameSetup(0);
	}

	/**
	 * Called after starting the game thread Constantly updates the game board and renders
	 * graphics
	 * 
	 * @see Runnable#run()
	 */
	@Override
	public void run(){
		while (this.gameBoard.isRunning()){
			// updates car positions and re-renders graphics
			this.gameBoard.updateSpaceObjects();
			// when this.gameBoard.hasWon() is null, do nothing
			if (this.gameBoard.hasEnded() == true){
				showAsyncAlert("Oh.. you lost.");
				this.stopGame();
			}
			paint(this.graphicsContext);
			try{
				Thread.sleep(SLEEP_TIME); // milliseconds to sleep
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @return current gameBoard
	 */
	public GameBoard getGameBoard(){
		return this.gameBoard;
	}

//	/**
//	 * @return mouse steering control object
//	 */
//	public Input getInput(){
//		return this.input;
//	}

	public static int getDEFAULT_WIDTH(){
		return DEFAULT_WIDTH;
	}
	
	public static int getDEFAULT_HEIGHT(){
		return DEFAULT_HEIGHT;
	}

	/**
	 * Removes all existing spaceObjects from the game board and re-adds them. Status bar is set to
	 * default value. Player spaceship is reset to default starting position. Renders graphics.
	 */
	public void gameSetup(int difficulty){
		//this.input = new Input();
		
		this.gameBoard = new GameBoard(width, height, difficulty);
		this.widthProperty().set(this.width);
		this.heightProperty().set(this.height);
		this.width = (int) getWidth();
		this.height = (int) getHeight();
		this.spaceImages = new HashMap<>();
		
		this.gameBoard.resetSpaceObjects();
		this.gameBoard.getspaceObjects().forEach((so -> this.spaceImages.put(so, getImage(so.getIcon()))));
		this.spaceImages.put(this.gameBoard.getPlayer(),
			this.getImage(this.gameBoard.getPlayer().getIcon()));
		paint(this.graphicsContext);
		this.toolBar.resetToolBarButtonStatus(false);
	}

	/**
	 * Sets the car's image
	 *
	 * @param carImageFilePath: an image file path that needs to be available in the resources
	 *                          folder of the project
	 */
	private Image getImage(String carImageFilePath){	//TODO GameBoardUI - getImage()
		try{
			URL carImageUrl = getClass().getClassLoader().getResource(carImageFilePath);
			if (carImageUrl == null){
				throw new RuntimeException(
					"Please ensure that your resources folder contains the appropriate files for this exercise.");
			}
			InputStream inputStream = carImageUrl.openStream();
			return new Image(inputStream);
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board, which
	 * causes the spaceObjects to change their positions (i.e. move). Renders graphics and updates
	 * tool bar status.
	 */
	public void startGame(){
		if (!this.gameBoard.isRunning()){
			this.gameBoard.startGame();
			this.theThread = new Thread(this);
			this.theThread.start();
			paint(this.graphicsContext);
			this.toolBar.resetToolBarButtonStatus(true);
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

		for (SpaceObject so: this.gameBoard.getspaceObjects()){
			paintSpaceObject(so, graphics);
		}
		// render player spaceShip
		paintSpaceObject(this.gameBoard.getPlayer(), graphics);
	}

	/**
	 * Show image of a spaceObject at the current position of the spaceObject.
	 * 
	 * @param so SpaceObject to be drawn
	 * @param graphics used to draw changes
	 */
	private void paintSpaceObject(SpaceObject so, GraphicsContext graphics){
		Vector spaceObjectPositionVector = so.getPositionVector();
		Vector canvasPosition = convertPosition(spaceObjectPositionVector);

		//TODO richtige drehung, vll. invertieren
		graphics.save(); // saves the current state on stack, including the current transform
        graphics.rotate((double) ((so instanceof Player ? ((Player)so).getFacingVector() : so.getDirectionVector()).getDegree()));
        graphics.drawImage(this.spaceImages.get(
			so), canvasPosition.getX() - so.getRadius(), canvasPosition.getY() - so.getRadius(), so.getRadius()*2, so.getRadius()*2);
        graphics.restore(); // back to original state (before rotation)
	}

	/**
	 * Converts position of spaceObject to position on the canvas
	 * 
	 * @param toConvert the point to be converted
	 */
	public Vector convertPosition(Vector toConvert){
		return new Vector(toConvert.getX(), (int) (getHeight() - toConvert.getY()));
	}

	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame(){
		if (this.gameBoard.isRunning()){
			this.gameBoard.stopGame();
			this.toolBar.resetToolBarButtonStatus(false);
		}
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
			this.gameSetup(0);
		});
	}
}
