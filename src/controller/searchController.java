package controller;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class searchController
{
	/**
	 * all the FXML variables in the search photo fxml file
	 */
	@FXML ListView<Photo> listview;
	@FXML Button back, display, createalbum, searchdate, singletag, andtag, ortag, next, prev;
	@FXML DatePicker from, to;
	@FXML TextField tag1, tag2;
	@FXML ImageView imageview;
	@FXML Label captionText;
	
	/**
	 * current stage s and lists to show information in the listview
	 */
	public Stage s;
	public ArrayList<Photo> photos = new ArrayList<Photo>();
	public ObservableList<Photo> oblist = null;
	
	/**
	 * start the GUI for search photos
	 * @param mStage
	 */
	public void start(Stage mStage)
	{
		s = mStage;
		listview.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {displayImage(); displayCaption(); });
	}
	
	/**
	 * goes back to the Album interface
	 * @param e - the back button
	 * @throws IOException
	 */
	public void back(ActionEvent e) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/User_Subsystem.fxml"));
		Parent root = (Parent)loader.load();
		userController user = loader.getController();
		user.start(s);
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.setTitle("Album");
		s.setResizable(false); 
		s.show();
	}
	
	/**
	 * creates a new album that gets its name from the textfield
	 * @param e - the create album button
	 */
	public void createAlbum(ActionEvent e)
	{
		if(oblist == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("the photo list is empty");
			alert.showAndWait();
			return;
		}
		int albumid = 0;
		String albumname;
		for(int i = 0; i < Admin.currentUser.getAlbum().size(); i++)
		{
			albumname = "Search Result Album " + albumid;
			if(Admin.currentUser.getAlbum().get(i).getName().equals(albumname))
			{
				albumid++;
			}
		}
		albumname = "Search Result Album " + albumid;
		Album album = new Album(albumname);
		for(int i = 0; i < photos.size(); i++)
		{
			album.getPhotos().add(photos.get(i));
		}
		album.getUpper();
		album.getLower();
		Admin.currentUser.addAlbum(album);
	}
	
	/**
	 * search photos by selected dates
	 * @param e - the search button
	 */
	public void searchDate(ActionEvent e)
	{
		LocalDate start = from.getValue();
		LocalDate end = to.getValue();
		if(start == null || end == null)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("you need to select a time");
			alert.showAndWait();
			return;
		}
		if(start.isAfter(end))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("error time input");
			alert.showAndWait();
			return;
		}
		photos = Admin.currentUser.searchByDate(start, end);
		oblist = FXCollections.observableArrayList(photos);
		listview.setItems(oblist);
		listview.getSelectionModel().select(0);
	}
	
	/**
	 * search photos that have the single tag name and tag value
	 * @param e - the single button
	 */
	public void singleTag(ActionEvent e)
	{
		String tag = tag1.getText();
		if(tag == null || tag.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("empty tag");
			alert.showAndWait();
			return;
		}
		StringTokenizer st = new StringTokenizer(tag, "=");
		String tagname = st.nextToken();
		String tagvalue = st.nextToken();
		photos = Admin.currentUser.searchSingleTag(tagname, tagvalue);
		if(photos.size() == 0)
		{
			imageview.setImage(null);
			captionText.setText("");
		}
		oblist = FXCollections.observableArrayList(photos);
		listview.setItems(oblist);
		listview.getSelectionModel().select(0);
	}
	
	/**
	 * search photos that have both tag names and tag values
	 * @param e - the and button
	 */
	public void andTag(ActionEvent e)
	{
		String tagone = tag1.getText();
		if(tagone == null || tagone.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("empty tag1");
			alert.showAndWait();
			return;
		}
		String tagtwo = tag2.getText();
		if(tagtwo == null || tagtwo.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("empty tag2");
			alert.showAndWait();
			return;
		}
		StringTokenizer st = new StringTokenizer(tagone, "=");
		StringTokenizer st2 = new StringTokenizer(tagtwo, "=");
		String tagname1 = st.nextToken();
		String tagvalue1 = st.nextToken();
		String tagname2 = st2.nextToken();
		String tagvalue2 = st2.nextToken();
		photos = Admin.currentUser.searchAndTag(tagname1, tagvalue1, tagname2, tagvalue2);
		if(photos.size() == 0)
		{
			imageview.setImage(null);
			captionText.setText("");
		}
		oblist = FXCollections.observableArrayList(photos);
		listview.setItems(oblist);
		listview.getSelectionModel().select(0);
	}
	
	/**
	 * search photso that have either the first tag name and tag value or the second tag name or tag value
	 * @param e - the or button
	 */
	public void orTag(ActionEvent e)
	{
		String tagone = tag1.getText();
		String tagtwo = tag2.getText();
		if(tagone == null || tagone.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("empty tag1");
			alert.showAndWait();
			return;
		}
		if(tagtwo == null || tagtwo.equals(""))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("empty tag2");
			alert.showAndWait();
			return;
		}
		StringTokenizer st = new StringTokenizer(tagone, "=");
		StringTokenizer st2 = new StringTokenizer(tagtwo, "=");
		String tagname1 = st.nextToken();
		String tagvalue1 = st.nextToken();
		String tagname2 = st2.nextToken();
		String tagvalue2 = st2.nextToken();
		photos = Admin.currentUser.searchOrTag(tagname1, tagvalue1, tagname2, tagvalue2);
		if(photos.size() == 0)
		{
			imageview.setImage(null);
			captionText.setText("");
		}
		oblist = FXCollections.observableArrayList(photos);
		listview.setItems(oblist);
		listview.getSelectionModel().select(0);
	}
	
	/**
	 * goes to the next photo of the current photo in the listview
	 * @param e - the next button
	 */
	public void next(ActionEvent e)
	{
		if(photos.size() == 0)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("there is no photo in the album");
			alert.showAndWait();
			return;
		}
		int index = listview.getSelectionModel().getSelectedIndex();
		if(index == photos.size()-1)
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
	 * goes to the previous photo of the current photo in the listview
	 * @param e - the prev button
	 */
	public void prev(ActionEvent e)
	{
		if(photos.size() == 0)
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
	 * display and update the imageview of the current photo in the interface
	 */
	public void displayImage()
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
			return;
		File file = p.getImage();
		Image image = new Image(file.toURI().toString());
		imageview.setImage(image);
	}
	
	/**
	 * display and update the caption information of the current photo in the interface
	 */
	public void displayCaption()
	{
		Photo p = listview.getSelectionModel().getSelectedItem();
		if(p == null)
			return;
		captionText.setText(p.getCaption());
	}
}