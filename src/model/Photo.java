package model;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class Photo implements Serializable
{
	/**
	 * file, path, caption, tags, and date of the photo object
	 * isStock is true if it is a stock photo
	 */
	private File image;
	private String imagePath;
	private String caption;
	private ArrayList<Tag> tags;
	private Date imageDate;
	private static final long serialVersionUID = 1L;
	public boolean isStock = false;
	
	/**
	 * two-arg constructor for the photo object
	 * @param f - file of a photo
	 * @param path - path of a photo
	 */
	public Photo(File f, String path) 
	{
		image = f;
		imagePath = path;
		caption = "";
		tags = new ArrayList<Tag>();
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.MILLISECOND, 0);
		imageDate = calendar.getTime();
	}
	
	/**
	 * set the path of the photo
	 * @param path - path parameter
	 */
	public void setPath(String path)
	{
		imagePath = path;
	}
	
	/**
	 * set the file of the photo
	 * @param f - file parameter
	 */
	public void setFile(File f)
	{
		image = f;
	}
	
	/**
	 * set the caption of the photo
	 * @param c - string parameter of caption
	 */
	public void setCaption(String c)
	{
		caption = c;
	}
	
	/**
	 * get the caption fo the photo
	 * @return string for caption
	 */
	public String getCaption()
	{
		return caption;
	}
	
	/**
	 * get the file for this photo
	 * @return file of photo
	 */
	public File getImage()
	{
		return image;
	}
	
	/**
	 * gets the path of this photo
	 * @return path of photo
	 */
	public String getPath()
	{
		return imagePath;
	}
	
	/**
	 * get arraylist of tags of this photo
	 * @return tags of the photo
	 */
	public ArrayList<Tag> getTags()
	{
		return tags;
	}
	
	/**
	 * get the date of the photo
	 * @return date of photo
	 */
	public Date getDate()
	{
		return imageDate;
	}
	
	/**
	 * add a tag to the photo
	 * @param tn - tag name
	 * @param tv - tag value
	 */
	public void addTag(String tn, String tv)
	{
		tags.add(new Tag(tn, tv));
	}
	
	/**
	 * remove a tag for the photo
	 * @param tn - tag name
	 * @param tv - tag value
	 */
	public void removeTag(String tn, String tv)
	{
		for(int i = 0; i < tags.size(); i++)
		{
			if(tags.get(i).getName().compareToIgnoreCase(tn) == 0 && tags.get(i).getValue().compareToIgnoreCase(tv) == 0)
			{
				tags.remove(i);
			}
		}
	}
	
	/**
	 * print out the photo file name
	 * @return the file name of photo
	 */
	public String toString()
	{
		StringTokenizer st = new StringTokenizer(imagePath, "\\");
		String result = "";
		while(st.hasMoreTokens())
		{
			result = st.nextToken();
		}
		return result;
	}
	
	/**
	 * set isStock to true
	 */
	public void setStock()
	{
		isStock = true;
	}
}