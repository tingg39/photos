package model;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Kshitij Bafna
 * @author Ting Yao
 */
public class Admin{
	
	/**
	 * an arraylist of users and the file name and directory for user.dat to store all data
	 * an user variable to keep track of the current user
	 */
	public static Admin instance = null;
	public static ArrayList<User> users = new ArrayList<User>(Arrays.asList(new User("stock")));
	public static final String storeDir = "dat";
	public static final String storeFile = "user.dat";
	public static User currentUser;
	
	/**
	 * save all the data into user.dat
	 * @throws IOException
	 */
	public static void save() throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(users);
	}

	/**
	 * load all the data into the application
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void load() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		users = (ArrayList<User>) ois.readObject();
	}
}