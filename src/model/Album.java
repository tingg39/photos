package model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class Album implements Serializable
{
	/**
	 * the album object contains the name of the album, an arraylist of photos, and the range of date for the album
	 */
	private String name;
	private ArrayList<Photo> photos;
	private Date lower = null;
	private Date upper = null;
	private static final long serialVersionUID = 1L;
	
	/**
	 * constructor for album to set its name and create an arraylist for photos
	 * @param albumName - name of the album
	 */
	public Album(String albumName)
	{
		name = albumName;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * changes the name of the album
	 * @param albumName - name of the album
	 */
	public void setName(String albumName)
	{
		name = albumName;
	}
	
	/**
	 * get the name of the album
	 * @return the name of the album
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * get the arraylist of photos in this album
	 * @return arraylist of photos
	 */
	public ArrayList<Photo> getPhotos()
	{
		return photos;
	}
	
	/**
	 * sets the latest date in the list of photos
	 */
	public void getUpper() {
		if(photos.size() == 0)
		{
			return;
		}
		Date highest = photos.get(0).getDate();
		for(Photo p: photos)
		{
			if(p.getDate().after(highest))
				highest = p.getDate();
		}
		upper = highest;
	}

	/**
	 * sets the earliest date in the list of photos
	 */
	public void getLower()
	{
		if(photos.size() == 0)
		{
			return;
		}
		Date lowest = photos.get(0).getDate();
		for(Photo p: photos)
		{
			if(p.getDate().before(lowest))
				lowest = p.getDate();
		}
		lower = lowest;
	}
	
	/**
	 * display the date range for the album
	 * @return the date range to show in the album interface
	 */
	public String getRange()
	{
		String result = "";
		if(lower == null || upper == null)
		{
			return result;
		}
		SimpleDateFormat df = new SimpleDateFormat("MMM d, y, HH:mm:ss a");
		result = df.format(lower) + " to\n" + df.format(upper);
		return result;
	}
	
	/**
	 * gets the size of this album
	 * @return size of album
	 */
	public int size()
	{
		return photos.size();
	}
	
	/**
	 * print out the name of the album
	 */
	public String toString()
	{
		return name;
	}
}