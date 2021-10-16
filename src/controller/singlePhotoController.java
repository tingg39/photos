package controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
public class singlePhotoController
{
	/**
	 * all the FXML variables in the single photo fxml file
	 */
	@FXML ImageView imageview;
	@FXML Button back;
	@FXML Label captionText, dateText;
	@FXML ListView<Tag> listview;
	
	/**
	 * a photo variable to keep track of the current photo and a observable list to display the tag information of the photo
	 * current stage s
	 */
	public static Photo photo;
	public ObservableList<Tag> tags = null;
	public Stage s;
	
	/**
	 * start the single photo display interface
	 * @param mStage - the previous stage
	 */
	public void start(Stage mStage)
	{
		s = mStage;
		File file = photo.getImage();
		Image image = new Image(file.toURI().toString());
		imageview.setImage(image);
		tags = FXCollections.observableArrayList(photo.getTags());
		listview.setItems(tags);
		captionText.setText(photo.getCaption());
		SimpleDateFormat df = new SimpleDateFormat("MMM d, y, HH:mm:ss a");
		dateText.setText(df.format(photo.getDate()));
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