package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("BallTracking.fxml"));			
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
	        primaryStage.show();
	        
	        TrackingController controller = loader.getController();
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        	public void handle(WindowEvent we) {
	        		controller.setClosed();
	        	}
	        });
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
