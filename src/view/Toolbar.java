package view;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.ButtonBar.ButtonData;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.Main;

public class Toolbar extends ToolBar {
	private final Main gameWindow;
    private final Button start;
    private final Button stop;
    
    private final Button scores;
//    private final Button multiplayer;

    public Toolbar(Main gameWindow) {
        this.start = new Button("Start");
        this.stop = new Button("Stop");
        
        this.scores = new Button("Scores");
//        this.multiplayer = new Button("Multiplayer"); 
        
        initActions();
        this.getItems().addAll(start, new Separator(), stop, new Separator(), scores);
        this.gameWindow = gameWindow;
    }

    /**
     * Initialises the actions
     */
    @SuppressWarnings("deprecation")
	private void initActions() {
        this.start.setOnAction(event -> {
        	gameWindow.gameBoardUI.stopGame();
        	gameWindow.gameBoardUI.multiplayer = false;
        	
        	ButtonType SINGLEPLAYER = new ButtonType("Singleplayer", ButtonBar.ButtonData.OK_DONE);
            ButtonType MULTIPLAYER = new ButtonType("Multiplayer", ButtonBar.ButtonData.OK_DONE);
            
            Alert alert4 = new Alert(AlertType.INFORMATION, "Mode?", SINGLEPLAYER, MULTIPLAYER);
            alert4.setTitle("Connection");
            alert4.setHeaderText("");
            
            Optional<ButtonType> result4 = alert4.showAndWait();
            if(result4.get() == SINGLEPLAYER) {
            	
            	ButtonType EASY = new ButtonType("Easy", ButtonBar.ButtonData.OK_DONE);
                ButtonType MEDIUM = new ButtonType("Medium", ButtonBar.ButtonData.OK_DONE);
                ButtonType HARD = new ButtonType("Hard", ButtonBar.ButtonData.OK_DONE);
                ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
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
            } else if(result4.get() == MULTIPLAYER) {
            	
            	ButtonType HOST = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
                ButtonType CLIENT = new ButtonType("Client", ButtonBar.ButtonData.OK_DONE);
                
                Alert alert5 = new Alert(AlertType.INFORMATION, "Connection?", HOST, CLIENT);
                alert5.setTitle("Connection");
                alert5.setHeaderText("");
                
                Optional<ButtonType> result5 = alert5.showAndWait();
                if(result5.get() == HOST) {
                	ButtonType CANCEL1 = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);
                	
                	Alert alert2 = new Alert(AlertType.INFORMATION, "Wait for Connection.", CANCEL1);
                    alert2.setTitle("Wait");
                    alert2.setHeaderText("");
                    
                    gameWindow.gameBoardUI.waitForCon = true;
                    
                    Runnable task = () -> {
                    	gameWindow.gameBoardUI.setConnectionInHost(); 
                    	//alert2.close();//TODO
                        Platform.runLater(() -> {
                        	alert2.close();
                            //System.out.println("I'm running later...");
                        });
                    };
                    
                    Thread thread = new Thread(task);
                    thread.start();
                    Optional<ButtonType> result2 = alert2.showAndWait();
                    if(result2.isPresent() && result2.get() == CANCEL1) {
                    	thread.stop();
                    	try {
                    		ServerSocket s = gameWindow.gameBoardUI.serverSock;
    						if (s != null && !s.isClosed()) {
    							s.close();
    						}
    					} catch (IOException e) {
    					}
                    } else if(gameWindow.gameBoardUI.multiplayer) {
//                    	 alert2.close();
                    	 
                    	ButtonType EASY = new ButtonType("Easy", ButtonBar.ButtonData.OK_DONE);
                        ButtonType MEDIUM = new ButtonType("Medium", ButtonBar.ButtonData.OK_DONE);
                        ButtonType HARD = new ButtonType("Hard", ButtonBar.ButtonData.OK_DONE);
                        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                
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
                    } else {
                    	thread.stop();
                    	try {
                    		ServerSocket s = gameWindow.gameBoardUI.serverSock;
    						if (s != null && !s.isClosed()) {
    							s.close();
    						}
    					} catch (IOException e) {
    					}
                    }
                	
                	
                } else if(result5.get() == CLIENT) {
                	//ButtonType OK = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
                	//TextField TEXT = new TextField();
                	
                	TextInputDialog txt = new TextInputDialog();
                    txt.setTitle("Adress");
                    txt.setHeaderText("IP:");
                    
                    Optional<String> result3 = txt.showAndWait();
                    if (result3.isPresent()) {
                    	boolean connected = gameWindow.gameBoardUI.setConnectionInClient(result3.get());
                    	if(connected) {
                    		gameWindow.gameBoardUI.clientLoop();
                    	}
                    }
                }
            }
        });
        
        // start without extra multiplayer button:
//        this.start.setOnAction(event -> {
//        	gameWindow.gameBoardUI.stopGame();
//        	
//            ButtonType EASY = new ButtonType("Easy", ButtonBar.ButtonData.OK_DONE);
//            ButtonType MEDIUM = new ButtonType("Medium", ButtonBar.ButtonData.OK_DONE);
//            ButtonType HARD = new ButtonType("Hard", ButtonBar.ButtonData.OK_DONE);
//            ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//    
//            Alert alert = new Alert(AlertType.INFORMATION, "Which difficulty do you want to play?", EASY, MEDIUM, HARD, CANCEL);
//            alert.setTitle("Choose Difficulty");
//            alert.setHeaderText("");
//    
//            Optional<ButtonType> result = alert.showAndWait();
//            if(result.get() == CANCEL) {
//            	gameWindow.gameBoardUI.stopGame();
//            	return;
//            }
//            if (result.get() == MEDIUM) {
//            	gameWindow.gameBoardUI.gameSetup(1);
//            } else if(result.get() == HARD){
//            	gameWindow.gameBoardUI.gameSetup(2);
//            }else{
//            	gameWindow.gameBoardUI.gameSetup(0);
//            }
//        	gameWindow.gameBoardUI.startGame();
//        });

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
        
        // old multiplayer button:
//        this.multiplayer.setOnAction(event -> {
//        	gameWindow.gameBoardUI.stopGame();
//        	gameWindow.gameBoardUI.multiplayer = false;
//        	
//        	ButtonType HOST = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
//            ButtonType CLIENT = new ButtonType("Client", ButtonBar.ButtonData.OK_DONE);
//            
//            Alert alert = new Alert(AlertType.INFORMATION, "Connection?", HOST, CLIENT);
//            alert.setTitle("Connection");
//            alert.setHeaderText("");
//            
//            Optional<ButtonType> result = alert.showAndWait();
//            if(result.get() == HOST) {
//            	ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);
//            	
//            	Alert alert2 = new Alert(AlertType.INFORMATION, "Wait for Connection.", CANCEL);
//                alert2.setTitle("Wait");
//                alert2.setHeaderText("");
//                
//                Optional<ButtonType> result2 = alert2.showAndWait();
//                gameWindow.gameBoardUI.waitForCon = true;
//                Thread thread = new Thread(() -> {gameWindow.gameBoardUI.setConnectionInHost(); alert2.close();});
//                thread.start();
//                if(result2.get() == CANCEL) {
//                	thread.stop();
//                	try {
//                		ServerSocket s = gameWindow.gameBoardUI.serverSock;
//						if (s != null && !s.isClosed()) {
//							s.close();
//						}
//					} catch (IOException e) {
//					}
//                }
//            	
//            	
//            } else if(result.get() == CLIENT) {
//            	//ButtonType OK = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
//            	//TextField TEXT = new TextField();
//            	
//            	TextInputDialog txt = new TextInputDialog();
//                txt.setTitle("Adress");
//                txt.setHeaderText("IP:");
//                
//                Optional<String> result3 = txt.showAndWait();
//                if (result3.isPresent()) {
//                	gameWindow.gameBoardUI.setConnectionInClient(result3.get());
//                }
//            }
//        });
        
        this.scores.setOnAction(event -> { 
        	Path path = Path.of("resources", "scores.txt");
        	List<String> lines = new ArrayList<String>();

    		if(!Files.isRegularFile(path)) {
    			path = Path.of("scores.txt");
    		}
    		
    		if(!Files.isRegularFile(path)) {
    			try {
					path.toFile().createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    			
    			if(!Files.isRegularFile(path)) {
    				return;
    			};
    		}
    		
        	try {
    			lines = Files.readAllLines(path);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		Alert alert = new Alert(AlertType.INFORMATION, lines.stream().collect(Collectors.joining("\n")));
    		alert.setTitle("Scores");
    		alert.setHeaderText("");
    		alert.showAndWait();
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
//        this.multiplayer.setDisable(running);
        this.scores.setDisable(running);
    }
    
}
