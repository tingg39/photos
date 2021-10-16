package controller;

import java.io.*;
import java.util.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class userController {

	/**
	 * all the FXML variables in the user subsystem fxml file
	 */
	@FXML Button add; 
	@FXML Button change_name;
	@FXML Button delete;
	@FXML Button open_album;
	@FXML Button logout;
	@FXML ListView<Album> album_list;
	@FXML TextField editAlbum;
	@FXML Label name;
	@FXML Label numPhotos;
	@FXML Label range;
	
	/**
	 * current stage s and an observablelist of albums to show all the album names
	 */
	public Stage s;
	public ObservableList<Album> oblist = null;
	
	/**
	 * start the user subsystem interface
	 * @param mStage - the previous stage
	 */
	public void start(Stage mStage)
	{
		s = mStage;
		oblist = FXCollections.observableArrayList(Admin.currentUser.getAlbum());
		album_list.setItems(oblist);
		album_list.getSelectionModel().select(0);
		Album a = album_list.getSelectionModel().getSelectedItem();
		if(a != null)
		{
			setDetails(a);
		}
		album_list.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {setDetails(newValue); });
	}
	
	
	/**
	 * add a new album to the current user
	 * @param e - the create album button
	 */
	public void addAlbum(ActionEvent e)
	{
		String album_name = editAlbum.getText();
		if(album_name == null || album_name.equals(""))
		{
			Alert serr = new Alert(AlertType.INFORMATION);
			serr.setTitle("Error");
			serr.setHeaderText("need a album name");
			serr.showAndWait();
			return;
		}
		
		for(int i = 0; i < oblist.size(); i++)
		{
			Album temp = oblist.get(i);
			if(album_name.equals(temp.getName()))
			{
				Alert alerror = new Alert(AlertType.INFORMATION);
				alerror.setTitle("Error");
				alerror.setHeaderText("album already exists in the library");
				alerror.showAndWait();
					return;
			}
		}	
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setTitle("Alert");
		al.setHeaderText("you are going to add a new album");
		Optional<ButtonType> res = al.showAndWait();
		if(res.get() == ButtonType.OK)
		{
			Album newAlbum = new Album(album_name);
			Admin.currentUser.addAlbum(newAlbum);
			oblist = FXCollections.observableArrayList(Admin.currentUser.getAlbum());
			//FXCollections.sort(albums, new compareSong());
			album_list.setItems(oblist);
			album_list.getSelectionModel().select(findAlbum(album_name));
		}
	}
	
	/**
	 * changes the name of the selected album
	 * @param e - the edit name button
	 */
	public void changeName(ActionEvent e)
	{
		Album currentAlbum = album_list.getSelectionModel().getSelectedItem();
		int size = oblist.size();
		if(size == 0)
		{
			Alert err = new Alert(AlertType.INFORMATION);
			err.setTitle("Error");
			err.setHeaderText("no album exists");
			err.showAndWait();
			return;
		}
		String album_name = editAlbum.getText();
		for(int i = 0; i < size; i++)
		{
			Album temp = oblist.get(i);
			if(album_name.equals(temp.getName()))
			{
				Alert alerror = new Alert(AlertType.INFORMATION);
				alerror.setTitle("Error");
				alerror.setHeaderText("Album "+album_name+" already exists in the library");
				alerror.showAndWait();
					return;
			}
		}
		if(album_name == null || album_name.equals(""))
		{
			Alert alerror = new Alert(AlertType.INFORMATION);
			alerror.setTitle("Error");
			alerror.setHeaderText("invalid name change requested");
			alerror.showAndWait();
				return;
		}
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setTitle("Alert");
		al.setHeaderText("you are going to edit the current album");
		Optional<ButtonType> res = al.showAndWait();
		if(res.get() == ButtonType.OK)
		{
			currentAlbum.setName(album_name);
			oblist = FXCollections.observableArrayList(Admin.currentUser.getAlbum());
			album_list.setItems(oblist);
			album_list.getSelectionModel().select(findAlbum(currentAlbum.getName()));
			setDetails(currentAlbum);
		}
	}

	/**
	 * open the selected album and goes to the photos interface
	 * @param e - the open button
	 * @throws IOException
	 */
	public void openAlbum(ActionEvent e) throws IOException
	{
		Album currentAlbum = album_list.getSelectionModel().getSelectedItem();
		int size = oblist.size();
		if(size == 0)
		{
			Alert err = new Alert(AlertType.INFORMATION);
			err.setTitle("Error");
			err.setHeaderText("no album exists");
			err.showAndWait();
			return;
		}
		photoController.album = currentAlbum;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/photo.fxml"));
		Parent root = (Parent)loader.load();
		photoController photosController = loader.getController();
		photosController.start(s);	
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("Photos");
		s.setResizable(false); 
		s.show();
	}
	
	/**
	 * delete the selected album for current user
	 * @param e - the delete button
	 */
	public void deleteAlbum(ActionEvent e)
	{
		if(Admin.currentUser.getAlbum().size() == 0)
		{
			Alert err = new Alert(AlertType.INFORMATION);
			err.setTitle("Error");
			err.setHeaderText("no Album exists");
			err.showAndWait();
			return;
		}
		int index = album_list.getSelectionModel().getSelectedIndex();
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setTitle("Alert");
		al.setHeaderText("you are going to delete the current album in the list");
		Optional<ButtonType> res = al.showAndWait();
		
		if(res.get() == ButtonType.OK)
		{
			Admin.currentUser.getAlbum().remove(index);
			oblist = FXCollections.observableArrayList(Admin.currentUser.getAlbum());
			album_list.setItems(oblist);
			if(Admin.currentUser.getAlbum().size() == 0)
			{
				name.setText("");
				numPhotos.setText("");
				range.setText("");
			}else
			{
				album_list.getSelectionModel().select(index-1);
			}
		}
	}
	
	/**
	 * log the current user out and goes back to the login interface
	 * @param e - the logout button
	 * @throws Exception
	 */
	public void logout(ActionEvent e) throws Exception
	{
		Admin.currentUser = null;
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
		return;
	}
	
	/**
	 * goes to the serach photo interface
	 * @param e - the search photos button
	 * @throws Exception
	 */
	public void search(ActionEvent e) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/search photo.fxml"));
		Parent root = (Parent)loader.load();
		searchController search = loader.getController();
		search.start(s);
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("Search Photos");
		s.setResizable(false); 
		s.show();
		return;
	}
	
	/**
	 * display and update the album details in the interfaces
	 * @param alb - selected album
	 */
	private void setDetails(Album alb)
	{
		if(alb == null)
			return;
		name.setText(alb.getName());
		numPhotos.setText(""+alb.size());
		range.setText(alb.getRange());
	}


	/**
	 * find the index of album with this name
	 * @param name - name of the album
	 * @return the index of the album users want to find
	 */
	private int findAlbum(String name)
	{
		int index;
		for(index = 0; index < oblist.size(); index++)
		{
			if(name.equalsIgnoreCase(oblist.get(index).getName()))
			{
				break;
			}
		}
		return index;
	}
}
