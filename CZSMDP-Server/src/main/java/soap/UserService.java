package soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;
import rest.TimetableService;
import utils.PasswordUtil;
import utils.UserUtil;

public class UserService  {
	
	private static Integer PORT;
	private static String USERS_XML;
	
	static{
		try (InputStream inputStream = new FileInputStream("config.properties")) {
		   Properties serverprop = new Properties();
		   serverprop.load(inputStream);
		   
		   USERS_XML = serverprop.getProperty("path_to_users_xml");
		   PORT = Integer.parseInt(serverprop.getProperty("port"));
		   }catch(Exception ex){
			   //ex.printStackTrace();
			   Logger.getLogger(UserService.class.getName()).log(Level.WARNING, ex.toString());
		   }
	}
	// key=stationId, value=User
	private static HashMap<String, List<User>> activeUsers = new HashMap<>();
	
	public User login(String username,String password) throws IOException, NoSuchAlgorithmException
	{ 
		try {
			ArrayList<User> users=UserUtil.loadUserData(new File(USERS_XML));
			for(User user : users)
				 if(username.equals(user.get_username())&& PasswordUtil.compareSaltedHashWithUserEnteredPwd(user.get_salt(),password,user.get_password())){
					 if(activeUsers.containsKey(user.get_stationID())&&activeUsers.get(user.get_stationID()).contains(user))
						 break;
					 
					 user.set_port(++PORT);
					 if(!activeUsers.containsKey(user.get_stationID()))
						 activeUsers.put(user.get_stationID(), new ArrayList<User>());
					 
				     activeUsers.get(user.get_stationID()).add(user);
				     System.out.println("Login: "+user.get_username());
					 return user;
				 }
		} catch (ClassNotFoundException e) {
			
			//e.printStackTrace();
			Logger.getLogger(UserService.class.getName()).log(Level.WARNING, e.toString());
		}
		return null;
	}
	
	public User addNewUser(String name, String password,String station_id,String xml_file){
		try{
			return UserUtil.createUser(name, password,station_id,USERS_XML);
			
		}catch(Exception ex){
			//ex.printStackTrace();
			Logger.getLogger(UserService.class.getName()).log(Level.WARNING, ex.toString());
		}
		return null;
	}
	
	public boolean removeUser(User user){
		try{
			ArrayList<User> users=UserUtil.loadUserData(new File(USERS_XML));
			users.remove(user);
			UserUtil.storeUsers(users, new File(USERS_XML));
			return true;
		}catch(Exception ex){
			// ex.printStackTrace();
			Logger.getLogger(UserService.class.getName()).log(Level.WARNING, ex.toString());
		}
		return false;
	}
	
    public User[] getUsers() throws ClassNotFoundException, IOException{
    	List<User> list=UserUtil.loadUserData(new File(USERS_XML));
    	return  list.toArray(new User[list.size()]);
    }	
    
	public void logout(User user){
		activeUsers.get(user.get_stationID()).remove(user);
		if(activeUsers.get(user.get_stationID()).isEmpty())
			activeUsers.keySet().remove(user.get_stationID());
		System.out.println("Logout: "+user.get_username());
	}
	
	public String[] getStationIds(){
		Set<String> set=activeUsers.keySet();
		return set.toArray(new String[set.size()]);
	}
	
	public User[] getUsersByStationId(String station_id){
		List<User> list=activeUsers.get(station_id);
		return list.toArray(new User[list.size()]);
	}
}

