package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
public class editTagController
{
		/**
		 * all the FXML variables in the editTagController
		 */
		@FXML ListView<Tag> listview;
		@FXML TextField tagname;
		@FXML TextField tagvalue;
		@FXML Button addtag, removetag, back;
		@FXML Label title;
		/**
		 * lists to display information in the listview and a static photo variable to keep track of the current photo
		 * a Stage variable to keep track of the current stage
		 */
		public ArrayList<Tag> tags;
		public ObservableList<Tag> oblist = null;
		public static Photo photo;
		public Stage s;
		
		/**
		 * starts the GUI for tag editting
		 * @param mStage - the previous stage
		 */
		public void start(Stage mStage)
		{
			s = mStage;
			tags = photo.getTags();
			title.setText("Tags of " + photo.toString());
			oblist = FXCollections.observableArrayList(tags);
			listview.setItems(oblist);
			listview.getSelectionModel().select(0);
		}
		
		/**
		 * add a tag to the selected photo
		 * @param e - the add tag button
		 */
		public void addTag(ActionEvent e)
		{
			String tn = tagname.getText();
			String tv = tagvalue.getText();
			if(tn == null || tn.equals(""))
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error");
				alert.setHeaderText("need a tag name");
				alert.showAndWait();
				return;
			}
					
			if(tv == null || tv.equals(""))
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error");
				alert.setHeaderText("need a tag value");
				alert.showAndWait();
				return;
			}
			
			for(int i = 0; i < tags.size(); i++)
			{
				if(tn.equalsIgnoreCase(tags.get(i).getName()) && tv.equalsIgnoreCase(tags.get(i).getValue()))
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Error");
					alert.setHeaderText("tag already exists");
					alert.showAndWait();
					return;
				}
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Add Tag");
			alert.setHeaderText("Do you want to add this tag?");
			Optional<ButtonType> res = alert.showAndWait();
			if(res.get() == ButtonType.OK)
			{
				photo.addTag(tn, tv);
				oblist = FXCollections.observableArrayList(tags);
				listview.setItems(oblist);
				listview.getSelectionModel().select(photo.getTags().size()-1);
			}
		}
		
		/**
		 * remove the selected tag from the photo
		 * @param e - the remove tag button
		 */
		public void removeTag(ActionEvent e)
		{
			Tag t = listview.getSelectionModel().getSelectedItem();
			int index = listview.getSelectionModel().getSelectedIndex();
			if(t == null)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error");
				alert.setHeaderText("no tag is selected");
				alert.showAndWait();
				return;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Tag");
			alert.setHeaderText("Do you want to delete this tag?");
			Optional<ButtonType> res = alert.showAndWait();
			if(res.get() == ButtonType.OK)
			{
				photo.removeTag(t.getName(), t.getValue());
				oblist = FXCollections.observableArrayList(tags);
				listview.setItems(oblist);
				if(index > 0)
					listview.getSelectionModel().select(index-1);
				else
				{
					if(photo.getTags().size() != 0)
						listview.getSelectionModel().select(0);
				}
			}
		}
		
		/**
		 * goes back to the photo interface
		 * @param e - the back button
		 * @throws IOException
		 */
		public void back(ActionEvent e) throws IOException
		{
			photo = null;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/photo.fxml"));
			Parent root = (Parent)loader.load();
			photoController photosController = loader.getController();
			photosController.start(s);
			Scene scene = new Scene(root);
			s.setScene(scene);
			s.setTitle("Album");
			s.setResizable(false); 
			s.show();
		}
}