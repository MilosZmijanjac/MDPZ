package app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sockets.MulticastReceiver;

public class Main extends Application {
	
	public static  String POLICY_FILE ;
	public static  String TRUSTSTORE ;
	public static  String KEYSTORE ;
	public static  String KEYSTORE_TYPE ;
	public static  String TRUSTSTORE_PASSWORD ;
	public static  String KEYSTORE_PASSWORD;
	public static  String MULTICAST_IP ;
	public static  String CHAT_IP ;
	public static  String MULTICAST_TAG ;
	public static  String TIMETABLES_URL ;
	public static  String TIMETABLES_STATION_URL ;
	public static  Integer MULTICAST_PORT ;
	
	
     private double xOffset = 0;
     private double yOffset = 0;
     
     static{
 		try (InputStream inputStream = new FileInputStream("./config.properties")) {
 			
 		   Properties serverprop = new Properties();
 		   serverprop.load(inputStream);
 		   KEYSTORE = serverprop.getProperty("server_keystore");
 		   KEYSTORE_TYPE = serverprop.getProperty("keystore_type");
 	   	   TRUSTSTORE = serverprop.getProperty("client_truststore");
 	       KEYSTORE_PASSWORD = serverprop.getProperty("server_keystore_password");
	   	   TRUSTSTORE_PASSWORD = serverprop.getProperty("client_truststore_password");
 		   TIMETABLES_URL = serverprop.getProperty("timetables_url");
 		   TIMETABLES_STATION_URL = serverprop.getProperty("timetables_station_url");
 		   MULTICAST_IP = serverprop.getProperty("multicast_ip");
 		   CHAT_IP = serverprop.getProperty("chat_ip");
 		   MULTICAST_TAG = serverprop.getProperty("multicast_tag");
 		   MULTICAST_PORT = Integer.parseInt(serverprop.getProperty("multicast_port"));
 		   POLICY_FILE=serverprop.getProperty("client_policy");
 		
 		   }catch(Exception ex){
 			   //ex.printStackTrace();
 			  Logger.getLogger(Main.class.getName()).log(Level.WARNING, ex.toString());
 		   }
 	}
     

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("fxml/LoginView.fxml"));
        Scene scene = new Scene(root);

        root.setOnMousePressed(event ->{
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event ->{
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        );

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("ZSMDP: Login");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
