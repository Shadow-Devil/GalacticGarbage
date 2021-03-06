package controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.GameBoardUI;
import view.Toolbar;

public class Main extends Application {

	public GameBoardUI gameBoardUI; // the user interface object
	public Toolbar toolBar; // the tool bar object with start and stop buttons

	/**
	 * Starts the GalacticGarbage Window by setting up a new tool bar, a new user
	 * interface and adding them to the stage.
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * @param primaryStage the primary stage for this application, onto which the
	 *                     application scene can be set.
	 */
	@Override
	public void start(Stage primaryStage) {
		toolBar = new Toolbar(this);
		gameBoardUI = new GameBoardUI(this.toolBar);
		toolBar.resetToolBarButtonStatus(false); // set tool bar to default values

		// initialize GridPane and format
		// GridPanes are divided into columns and rows, like a table
		GridPane gridLayout = new GridPane();
		gridLayout.setPrefSize(505, 350);
		gridLayout.setVgap(5);
		gridLayout.setPadding(new Insets(5, 5, 5, 5));

		// add all components to the gridLayout
		// second parameter is column index, second parameter is row index of grid
		gridLayout.add(gameBoardUI, 0, 1);
		gridLayout.add(toolBar, 0, 0);

		// scene and stages
		Scene scene = new Scene(gridLayout);
		scene.setOnKeyPressed(event -> Input.handle(event, true));
		scene.setOnKeyReleased(event -> Input.handle(event, false));

		primaryStage.setTitle("Galactic Garbage");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> System.exit(0));
	}

	/**
	 * The whole game will be executed through the launch() method
	 */
	public static void startApp(String[] args) {
		launch(args);
	}
}