package model;

import java.io.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 *
 */
public class Tag implements Serializable
{
	/**
	 * string variables of tag name and tag value
	 */
	private String name;
	private String value;
	private static final long serialVersionUID = 1L;
	
	/**
	 * two-arg constructor of tag
	 * @param tagName
	 * @param tagValue - if want multiple values, seperate them by commas ","
	 */
	public Tag(String tagName, String tagValue)
	{
		name = tagName;
		value = tagValue;
	}
	
	/**
	 * get the tagname
	 * @return tag name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * get the tag value
	 * @return tag value
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * print out the tag as "tagname, tagvalue"
	 */
	public String toString()
	{
		return name + ", " + value;
	}
}