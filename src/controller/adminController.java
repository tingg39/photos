package controller;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 */
public class adminController {
	
	/**
	 * all the FXML elements in the GUI
	 */
	@FXML ListView<User> listview;
	@FXML Button createuser, delete, logout;
	@FXML TextField username;
	
	/**
	 * current stage and an observable list for the listview
	 */
	public Stage s;
	public ObservableList<User> oblist = null;
	
	/**
	 * start the Admin_Subsystem interface
	 * @param mStage - previous stage
	 */
	public void start(Stage mStage)
	{
		s = mStage;
		oblist = FXCollections.observableArrayList(Admin.users);
		listview.setItems(oblist);
	}
	
	/**
	 * Create a new user that has the name in the textfield
	 * @param e - create user button
	 */
	public void createUser(ActionEvent e)
	{
		String u = username.getText();
		if(u.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("invalid input");
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Create User");
		alert.setHeaderText("Do you want to create this user?");
		Optional<ButtonType> res = alert.showAndWait();
		if(res.get() == ButtonType.OK)
		{
			Admin.users.add(new User(u));
			oblist = FXCollections.observableArrayList(Admin.users);
			listview.setItems(oblist);
		}
	}
	
	/**
	 * delete the currently selected user
	 * @param e - the delete button
	 */
	public void delete(ActionEvent e)
	{
		if(Admin.users.size() == 0)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("there is no user to be deleted");
			alert.showAndWait();
			return;
		}
		int index = listview.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete User");
		alert.setHeaderText("Do you want to delete this user?");
		Optional<ButtonType> res = alert.showAndWait();
		if(res.get() == ButtonType.OK)
		{
			Admin.users.remove(index);
			oblist = FXCollections.observableArrayList(Admin.users);
			listview.setItems(oblist);
		}
	}
	
	/**
	 * log you out of the application, goes back to the login interface
	 * @param e - the log out button
	 * @throws Exception
	 */
	public void logout(ActionEvent e) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.fxml"));
		Parent root = (Parent)loader.load();
		loginController lib = loader.getController();
		lib.start(s);
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("Login");
		s.setResizable(false); 
		s.show();
	}
}
