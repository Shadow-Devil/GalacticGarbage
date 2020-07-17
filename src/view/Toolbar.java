package view;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import java.util.Optional;

import controller.Main;

public class Toolbar extends ToolBar {
	private final Main gameWindow;
    private final Button start;
    private final Button stop;
    
    private final Button multiplayer;

    public Toolbar(Main gameWindow) {
        this.start = new Button("Start");
        this.stop = new Button("Stop");
        
        this.multiplayer = new Button("Multiplayer");
        
        initActions();
        this.getItems().addAll(start, new Separator(), stop, new Separator(), multiplayer);
        this.gameWindow = gameWindow;
    }

    /**
     * Initialises the actions
     */
    @SuppressWarnings("deprecation")
	private void initActions() {
        this.start.setOnAction(event -> {
        	gameWindow.gameBoardUI.stopGame();
    
            ButtonType EASY = new ButtonType("Easy", ButtonBar.ButtonData.OK_DONE);
            ButtonType MEDIUM = new ButtonType("Medium", ButtonBar.ButtonData.OK_DONE);
            ButtonType HARD = new ButtonType("Hard", ButtonBar.ButtonData.OK_DONE);
            ButtonType CANCEL = new ButtonType("Cancle", ButtonBar.ButtonData.CANCEL_CLOSE);
    
            Alert alert = new Alert(AlertType.INFORMATION, "Which difficulty do you want to play?", EASY, MEDIUM, HARD, CANCEL);
            alert.setTitle("Choose Difficulty");
            alert.setHeaderText("");
    
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == CANCEL) {
            	gameWindow.gameBoardUI.stopGame();
            	return;
            }
            if (result.get() == MEDIUM) {
            	gameWindow.gameBoardUI.gameSetup(1);
            } else if(result.get() == HARD){
            	gameWindow.gameBoardUI.gameSetup(2);
            }else{
            	gameWindow.gameBoardUI.gameSetup(0);
            }
        	gameWindow.gameBoardUI.startGame();
        });

        this.stop.setOnAction(event -> {
        	gameWindow.gameBoardUI.stopGame();

            ButtonType YES = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType NO = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game?", YES, NO);
            alert.setTitle("Stop Game Confirmation");
            alert.setHeaderText("");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == YES) {
            	gameWindow.gameBoardUI.gameSetup(0);
            } else {
            	gameWindow.gameBoardUI.startGame();
            }
        });
        
        this.multiplayer.setOnAction(event -> {
        	gameWindow.gameBoardUI.stopGame();
        	gameWindow.gameBoardUI.multiplayer = false;
        	
        	ButtonType HOST = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
            ButtonType CLIENT = new ButtonType("Client", ButtonBar.ButtonData.OK_DONE);
            
            Alert alert = new Alert(AlertType.INFORMATION, "Connection?", HOST, CLIENT);
            alert.setTitle("Connection");
            alert.setHeaderText("");
            
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == HOST) {
            	ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);
            	
            	Alert alert2 = new Alert(AlertType.INFORMATION, "Wait for Connection.", CANCEL);
                alert2.setTitle("Wait");
                alert2.setHeaderText("");
                
                Optional<ButtonType> result2 = alert2.showAndWait();
                gameWindow.gameBoardUI.waitForCon = true;
                Thread thread = new Thread(() -> {gameWindow.gameBoardUI.setConnectionInHost(); alert2.close();});
                thread.start();
                if(result.get() == CANCEL) {
                	thread.stop();
                }
            	
            	
            } else if(result.get() == CLIENT) {
            	//ButtonType OK = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
            	//TextField TEXT = new TextField();
            	
            	TextInputDialog txt = new TextInputDialog();
                txt.setTitle("Adress");
                txt.setHeaderText("IP:");
                
                gameWindow.gameBoardUI.setConnectionInClient(txt.showAndWait().get());
            }
        });
    }

    /**
     * Resets the toolbar button status
     * 
     * @param running Used to disable/enable buttons
     */
    public void resetToolBarButtonStatus(boolean running) {
        this.start.setDisable(running);
        this.stop.setDisable(!running);
        this.multiplayer.setDisable(running);
    }
    
}
