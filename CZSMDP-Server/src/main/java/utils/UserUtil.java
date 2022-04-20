package utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import model.User;

public class UserUtil {
    /*public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
       createUser("Marko","123","KD","users.xml");
       createUser("Petar","123","KD","users.xml");
       createUser("Milos","123","KD","users.xml");
       createUser("Milica","123","BL","users.xml");
       createUser("Ivan","123","BL","users.xml");
       createUser("Stefan","123","BL","users.xml");
       createUser("Nikola","123","PD","users.xml");
       createUser("Ivana","123","PD","users.xml");

    }*/
    public static User createUser(String name, String password,String station_id,String xml_file) throws NoSuchAlgorithmException, IOException {
    	User user = new User();
    	
    	user.set_username(name);
    	user.set_stationID(station_id);
        user.set_salt(PasswordUtil.getSalt());
        user.set_password(PasswordUtil.hashPasswordPlusSalt(password,user.get_salt()));
        File fileName=new File(xml_file);
     
        storeUserData(user,fileName);
       return user;
    }
    @SuppressWarnings("unchecked")
	public static void storeUserData(User user,File fileName) throws IOException {
    	ArrayList<User> accounts = null;
    	
    	 if(!fileName.exists()){
    		 fileName.createNewFile();
    		 accounts=new ArrayList<User>();
    		 XMLEncoder encoder = new XMLEncoder(new FileOutputStream(fileName));
    			encoder.writeObject(accounts);
    			encoder.close();
    	 }
    	
		XMLDecoder decoder = new XMLDecoder(new FileInputStream(fileName));
		accounts = (ArrayList<User>) decoder.readObject();
		decoder.close();
		accounts.add(user);
	    XMLEncoder encoder = new XMLEncoder(new FileOutputStream(fileName));
		encoder.writeObject(accounts);
		encoder.close();
    }
    public static ArrayList<User> loadUserData(File fileName) throws IOException, ClassNotFoundException {
    	XMLDecoder decoder = new XMLDecoder(new FileInputStream(fileName));
		@SuppressWarnings("unchecked")
		ArrayList<User> accounts = (ArrayList<User>) decoder.readObject();
		decoder.close();
        return accounts;

    }
    public static void storeUsers(ArrayList<User> users,File fileName) throws IOException, ClassNotFoundException {
    	XMLEncoder encoder = new XMLEncoder(new FileOutputStream(fileName));
		encoder.writeObject(users);
		encoder.close();
       
    }

}