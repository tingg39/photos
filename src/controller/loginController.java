package controller;

import java.util.*;
import java.io.*;
import javafx.event.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 */
public class loginController
{
	/**
	 * all the FXML variables
	 */
	@FXML Button login;
	@FXML Button quit;
	@FXML Label username;
	@FXML TextField user;
	
	/**
	 * current stage s and the user list to check if the user exists
	 */
	public Stage s;
	public ArrayList<User> userlist;
	
	/**
	 * start the GUI for login
	 * @param mStage - the main stage
	 * @throws Exception
	 */
	public void start(Stage mStage) throws Exception
	{
		s = mStage;
		userlist = Admin.users;
	}

	/**
	 * gets the username in the textfield then log you into either Admin interface or User interface
	 * @param e - the login button
	 * @throws IOException
	 */
	public void login(ActionEvent e) throws IOException
	{
		String user_name = user.getText();
		if(user_name == null || user_name.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("need a user login name");
			alert.showAndWait();
			return;
		}
		
		if(user_name.equals("admin"))
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Admin_Subsystem.fxml"));
			Parent root = (Parent)loader.load();
			adminController photosController = loader.getController();
			photosController.start(s);
			Scene scene = new Scene(root);
			s.setScene(scene);
			s.setTitle("Admin Subsystem");
			s.setResizable(false); 
			s.show();
		}else
		{
			for(int i = 0; i < userlist.size(); i++)
			{
				String username = userlist.get(i).getName();
				if(user_name.equals(username))
				{
					Admin.currentUser = userlist.get(i);
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/User_Subsystem.fxml"));
					Parent root = (Parent)loader.load();
					userController photosController = loader.getController();
					photosController.start(s);
					
					Scene scene = new Scene(root);
					s.setScene(scene);
					s.setTitle("User Subsystem");
					s.setResizable(false); 
					s.show();
					return;
				}
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("username does not exist");
			alert.showAndWait();
			return;
		}
	}

	/**
	 * exit the application
	 * @param e - the logout button
	 */
	public void quit(ActionEvent e)
	{
		Platform.exit();
	}
}

