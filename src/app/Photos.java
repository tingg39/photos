package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import model.Admin;
import controller.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class Photos extends Application
{
	/**
	 * load everythin from user.dat and open up the login interface
	 */
	@Override
	public void start(Stage stage) throws Exception{
			Admin.load();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			TitledPane pane = (TitledPane)loader.load();
			loginController lib = loader.getController();
			lib.start(stage);
			stage.setTitle("Login");
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
	}
	
	/**
	 * save everything to user.dat when the application is closed
	 */
	@Override
	public void stop() {
		try {
			Admin.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * main method to start running the application
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}