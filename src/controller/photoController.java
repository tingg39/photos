package controller;

import java.util.*;
import java.io.*;
import javafx.event.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class photoController
{
	
	/**
	 * all the FXML variables
	 */
	@FXML ListView<Photo> listview;
	@FXML Button addphoto;
	@FXML Button deletePhoto;
	@FXML Button edittag;
	@FXML Button caption;
	@FXML Button back;
	@FXML Button copy;
	@FXML Button move;
	@FXML Button next;
	@FXML Button prev;
	@FXML Button display;
	@FXML ImageView thumbnail;
	@FXML Label captionText;
	
	/**
	 * a list to show information in the listview, an album to keep track of the current album and a stage for current stage
	 */
	public static Album album;
	public ObservableList<Photo> oblist = null;
	public Stage s;
	
	/**
	 * start the GUI for list of photos in an album
	 * @param mStage - the previous stage
	 */
	public void start(Stage mStage)
	{
		s = mStage;
		oblist = FXCollections.observableArrayList(album.getPhotos());
		listview.setItems(oblist);
		listview.getSelectionModel().select(0);
		displayImage();
		displayCaption();
		listview.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {displayImage(); displayCaption(); });
	}
	
	/**
	 * add a photo to the current album
	 * @param e - the add photo button
	 */
	public void addPhoto(ActionEvent e)
	{
		FileChooser filechooser = new FileChooser();
		filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
		File iFile = filechooser.showOpenDialog(null);
		if(iFile == null)
			return;
		Photo p = new Photo(iFile, iFile.getAbsolutePath());
		if(Admin.currentUser.getName().equals("stock"))
		{
			if(iFile.getAbsolutePath().contains("stock"))
			{
				int index = iFile.getAbsolutePath().indexOf("stock");
				String path = iFile.getAbsolutePath().substring(index,iFile.getAbsolutePath().length());
				Photo photo = new Photo(iFile, path);
				photo.setStock();
				album.getPhotos().add(photo);
				album.getUpper();
				album.getLower();
				oblist = FXCollections.observableArrayList(album.getPhotos());
				listview.setItems(oblist);
				listview.getSelectionModel().select(album.getPhotos().size()-1);
				return;
			}
		}
		album.getPhotos().add(p);
		album.getUpper();
		album.getLower();
		oblist = FXCollections.observableArrayList(album.getPhotos());
		listview.setItems(oblist);
		listview.getSelectionModel().select(album.getPhotos().size()-1);
	}
	
	/**
	 * delete the currently selected photo from the album
	 * @param e - the delete photo button
	 */
	public void deletePhoto(ActionEvent e)
	{
		if(album.getPhotos().size() == 0)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("there is no photo in the album");
			alert.showAndWait();
			return;
		}
		int index = listview.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Photo");
		alert.setHeaderText("Do you want to delete this photo?");
		Optional<ButtonType> res = alert.showAndWait();
		if(res.get() == ButtonType.OK)
		{
			album.getPhotos().remove(index);
			album.getUpper();
			album.getLower();
			oblist = FXCollections.observableArrayList(album.getPhotos());
			listview.setItems(oblist);
			if(album.getPhotos().size() > 0)
			{
				if(index == 0)
				{
					listview.getSelectionModel().select(0);
				}else
				{
					listview.getSelectionModel().select(index-1);
				}
			}else
			{
				thumbnail.setImage(null);
				captionText.setText("");
			}
		}
	}
	
	/**
	 * goes back to the Album GUI for the current user
	 * @param e - the back button
	 * @throws IOException
	 */
	public void goBack(ActionEvent e) throws IOException
	{
		album = null;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/User_Subsystem.fxml"));
		Parent root = (Parent)loader.load();
		userController user = loader.getController();
		user.start(s);
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("User Subsystem");
		s.setResizable(false); 
		s.show();
	}
	
	/**
	 * caption or recaption the currently selected photo
	 * @param e - the edit caption button
	 */
	public void editCaption(ActionEvent e)
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("photo is not selected");
			alert.showAndWait();
			return;
		}
		TextInputDialog tid = new TextInputDialog();
		tid.setTitle("Caption");
		tid.setHeaderText("Edit your photo's caption");
		Optional<String> cap = tid.showAndWait();
		p.setCaption(cap.get());
		captionText.setText(p.getCaption());
	}
	
	/**
	 * select the next photo of the currently selected photo in the listview
	 * @param e - the next button
	 */
	public void nextPhoto(ActionEvent e)
	{
		if(album.getPhotos().size() == 0)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("there is no photo in the album");
			alert.showAndWait();
			return;
		}
		int index = listview.getSelectionModel().getSelectedIndex();
		if(index == album.getPhotos().size()-1)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("last photo of the album");
			alert.showAndWait();
			return;
		}else
		{
			listview.getSelectionModel().select(index+1);
		}
	}
	
	/**
	 * select the previous photo of the currently selected photo in the listview
	 * @param e
	 */
	public void prevPhoto(ActionEvent e)
	{
		if(album.getPhotos().size() == 0)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("there is no photo in the album");
			alert.showAndWait();
			return;
		}
		int index = listview.getSelectionModel().getSelectedIndex();
		if(index == 0)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("first photo of the album");
			alert.showAndWait();
			return;
		}else
		{
			listview.getSelectionModel().select(index-1);
		}
	}
	
	/**
	 * goes to the edit tag interface to modify tags for the current photo
	 * @param e - the edit tag button
	 * @throws IOException
	 */
	public void editTag(ActionEvent e) throws IOException
	{
		editTagController.photo = listview.getSelectionModel().getSelectedItem();
		if(editTagController.photo == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("no photo selected");
			alert.showAndWait();
			return;
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/editTag.fxml"));
		Parent root = (Parent)loader.load();
		editTagController edit = loader.getController();
		edit.start(s);
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("Edit Tags");
		s.setResizable(false); 
		s.show();
	}
	
	/**
	 * goes to the single photo interface and display the current photo in the list
	 * @param e - the display button
	 * @throws IOException
	 */
	public void display(ActionEvent e) throws IOException
	{
		singlePhotoController.photo = listview.getSelectionModel().getSelectedItem();
		if(singlePhotoController.photo == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("no photo selected");
			alert.showAndWait();
			return;
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/singlePhoto.fxml"));
		Parent root = (Parent)loader.load();
		singlePhotoController single = loader.getController();
		single.start(s);
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("Photo Display");
		s.setResizable(false); 
		s.show();
	}
	
	/**
	 * display and update the imageview in the interface
	 */
	public void displayImage()
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
			return;
		File file = p.getImage();
		if(Admin.currentUser.getName().equals("stock") && p.isStock == true)
		{
			String path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("stock"), file.getAbsolutePath().length());
			file  = new File(path);
			Image image = new Image(file.toURI().toString());
			thumbnail.setImage(image);
		}else
		{
			Image image = new Image(file.toURI().toString());
			thumbnail.setImage(image);
		}
	}
	
	/**
	 * display and update the caption information about the current photo
	 */
	public void displayCaption()
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
			return;
		captionText.setText(p.getCaption());
	}
	
	/**
	 * copy the current photo to another album
	 * @param e - the copy to button
	 */
	public void copyTo(ActionEvent e)
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("photo is not selected");
			alert.showAndWait();
			return;
		}
		TextInputDialog tid = new TextInputDialog();
		tid.setTitle("Copy a photo");
		tid.setHeaderText("enter the album name you want the photo to copy to");
		Optional<String> cap = tid.showAndWait();
		if(!cap.isPresent())
			return;
		if(cap.get() == null || cap.get().equalsIgnoreCase(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("input error for album name");
			alert.showAndWait();
			return;
		}
		String aname = cap.get();
		Admin.currentUser.copyPhoto(aname, p);
	}
	
	/**
	 * move the current photo to another album, delete the current photo in current album
	 * @param e - the move button
	 */
	public void move(ActionEvent e)
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("photo is not selected");
			alert.showAndWait();
			return;
		}
		TextInputDialog tid = new TextInputDialog();
		tid.setTitle("Move a photo");
		tid.setHeaderText("enter the album name you want the photo to move to");
		Optional<String> cap = tid.showAndWait();
		if(!cap.isPresent())
			return;
		if(cap.get() == null || cap.get().equalsIgnoreCase(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("input error for album name");
			alert.showAndWait();
			return;
		}
		String aname = cap.get();
		if(Admin.currentUser.movePhoto(aname, p))
		{
			int index = listview.getSelectionModel().getSelectedIndex();
			album.getPhotos().remove(index);
			if(album.getPhotos().size() == 0)
			{
				thumbnail.setImage(null);
				captionText.setText("");
			}
			album.getUpper();
			album.getLower();
			oblist = FXCollections.observableArrayList(album.getPhotos());
			listview.setItems(oblist);
			listview.getSelectionModel().select(0);
		}
	}
	
}