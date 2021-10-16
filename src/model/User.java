package model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * 
 * @author Ting Yao
 * @author Kshitij Bafna
 *
 */
public class User implements Serializable
{
	/**
	 * name of the user and his albums
	 */
	public String name;
	private static final long serialVersionUID = 1L;
	public ArrayList<Album> albums;
	
	/**
	 * 1-arg constructor for user
	 * @param username
	 */
	public User(String username)
	{
		name = username;
		albums = new ArrayList<Album>();
	}
	
	/**
	 * add a new album to the user
	 * @param newAlbum
	 */
	public void addAlbum(Album newAlbum)
	{
		//type checking
		albums.add(newAlbum);
	}
	
	/**
	 * change the name of the user
	 * @param username
	 * @return true if successful, false otherwise
	 */
	public boolean setName(String username)
	{
		if(username.equals("admin"))
			return false;
		name = username;
		return true;
	}
	
	/**
	 * get the album of the user
	 * @return arraylist of albums
	 */
	public ArrayList<Album> getAlbum()
	{
		return albums;
	}
	
	/**
	 * get the name of the user
	 * @return the name of user
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * print out the name of the user
	 */
	public String toString()
	{
		return name;
	}
	
	/**
	 * gets all the photos between certain dates
	 * @param from
	 * @param to
	 * @return arraylist of photos
	 */
	public ArrayList<Photo> searchByDate(LocalDate from, LocalDate to)
	{
		Calendar f = Calendar.getInstance();
		Calendar t = Calendar.getInstance();
		f.set(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
		t.set(to.getYear(), to.getMonthValue(), to.getDayOfMonth());
		ArrayList<Photo> p = new ArrayList<Photo>();
		for(int i = 0; i < albums.size(); i++)
		{
			ArrayList<Photo> templist = new ArrayList<Photo>();
			templist = albums.get(i).getPhotos();
			for(int j = 0; j < templist.size(); j++)
			{
				Date d = templist.get(j).getDate();
				Calendar sc = Calendar.getInstance();
				sc.setTime(d);
				Calendar calendar = Calendar.getInstance();
				calendar.set(sc.get(Calendar.YEAR), sc.get(Calendar.MONTH)+1, sc.get(Calendar.DAY_OF_MONTH));
				if(calendar.compareTo(f) > 0 && calendar.compareTo(t) < 0 || calendar.equals(f) && calendar.equals(t))
				{
					p.add(templist.get(j));
				}
			}
		}
		return p;
	}
	
	/**
	 * get all the photos that have this specific tag
	 * @param tagname
	 * @param tagvalue
	 * @return arraylist of photos
	 */
	public ArrayList<Photo> searchSingleTag(String tagname, String tagvalue)
	{
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(int i = 0; i < albums.size(); i++)
		{
			ArrayList<Photo> templist = new ArrayList<Photo>();
			templist = albums.get(i).getPhotos();
			for(int j = 0; j < templist.size(); j++)
			{
				Photo p = templist.get(j);
				ArrayList<Tag> tag = p.getTags();
				for(int k = 0; k < tag.size(); k++)
				{
					Tag t = tag.get(k);
					if(tagname.equalsIgnoreCase(t.getName()))
					{
						String[] tagvalues = t.getValue().split(",");
						if(tagvalues.length == 1)
						{
							if(tagvalue.equalsIgnoreCase(t.getValue()))
							{
								result.add(p);
							}
						}else
						{
							for(int a = 0; a < tagvalues.length; a++)
							{
								if(tagvalue.equalsIgnoreCase(tagvalues[a]))
								{
									result.add(p);
									break;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * get all photos that have both of tags
	 * @param tagname1
	 * @param tagvalue1
	 * @param tagname2
	 * @param tagvalue2
	 * @return arraylist of photos
	 */
	public ArrayList<Photo> searchAndTag(String tagname1, String tagvalue1, String tagname2, String tagvalue2)
	{
		ArrayList<Photo> result = new ArrayList<Photo>();
		boolean fit = false;
		for(int i = 0; i < albums.size(); i++)
		{
			ArrayList<Photo> templist = new ArrayList<Photo>();
			templist = albums.get(i).getPhotos();
			for(int j = 0; j < templist.size(); j++)
			{
				fit = false;
				Photo p = templist.get(j);
				ArrayList<Tag> tag = p.getTags();
				for(int k = 0; k < tag.size(); k++)
				{
					Tag t = tag.get(k);
					if(tagname1.equalsIgnoreCase(t.getName()))
					{
						String[] tvalues = t.getValue().split(",");
						for(int a = 0; a < tvalues.length; a++)
						{
							if(tagvalue1.equalsIgnoreCase(tvalues[a]))
							{
								k++;
								while(k < tag.size())
								{
									t = tag.get(k);
									if(tagname2.equalsIgnoreCase(t.getName()))
									{
										tvalues = t.getValue().split(",");
										for(int b = 0; b < tvalues.length; b++)
										{
											if(tagvalue2.equalsIgnoreCase(tvalues[b]))
											{
												result.add(p);
												fit = true;
												break;
											}
										}
									}
									k++;
								}
							}
						}
					}
					if(fit)
					{
						break;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * get all photos have either tag1 or tag2
	 * @param tagname1
	 * @param tagvalue1
	 * @param tagname2
	 * @param tagvalue2
	 * @return arraylist of photos
	 */
	public ArrayList<Photo> searchOrTag(String tagname1, String tagvalue1, String tagname2, String tagvalue2)
	{
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(int i = 0; i < albums.size(); i++)
		{
			ArrayList<Photo> templist = new ArrayList<Photo>();
			templist = albums.get(i).getPhotos();
			for(int j = 0; j < templist.size(); j++)
			{
				Photo p = templist.get(j);
				ArrayList<Tag> tag = p.getTags();
				for(int k = 0; k < tag.size(); k++)
				{
					Tag t = tag.get(k);
					if(tagname1.equalsIgnoreCase(t.getName()))
					{
						String[] tvalue = t.getValue().split(",");
						for(int a = 0; a < tvalue.length; a++)
						{
							if(tagvalue1.equalsIgnoreCase(tvalue[a]))
							{
								result.add(p);
								break;
							}
						}
					}else if(tagname2.equalsIgnoreCase(t.getName()))
					{
						String[] tvalue = t.getValue().split(",");
						for(int a = 0; a < tvalue.length; a++)
						{
							if(tagvalue2.equalsIgnoreCase(tvalue[a]))
							{
								result.add(p);
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * copy a photo from one album to another
	 * @param aname - album name
	 * @param p - photo
	 */
	public void copyPhoto(String aname, Photo p)
	{
		for(int i = 0; i < albums.size(); i++)
		{
			if(albums.get(i).getName().equals(aname))
			{
				albums.get(i).getPhotos().add(p);
				albums.get(i).getUpper();
				albums.get(i).getLower();
				return;
			}
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("there is no album named " + aname);
		alert.showAndWait();
	}
	
	/**
	 * move a photo from one album to another, delete the photo in current album
	 * @param aname - album name
	 * @param p - photo
	 * @return true if successful, false if the album name does not exist
	 */
	public boolean movePhoto(String aname, Photo p)
	{
		for(int i = 0; i < albums.size(); i++)
		{
			if(albums.get(i).getName().equals(aname))
			{
				albums.get(i).getPhotos().add(p);
				albums.get(i).getUpper();
				albums.get(i).getLower();
				return true;
			}
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("there is no album named " + aname);
		alert.showAndWait();
		return false;
	}
}
