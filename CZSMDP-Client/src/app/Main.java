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
import soap.UserService;


public class Main extends Application {
	
	public static  String MULTICAST_IP ;
	public static  String MULTICAST_TAG ;
	public static  String TIMETABLES_URL ;
	public static  String STATIONS_URL ;
	public static  String TIMETABLES_STATION_URL ;
	public static  Integer MULTICAST_PORT ;
	public static  Integer RMI_PORT ;
	
     private double xOffset = 0;
     private double yOffset = 0;
    
     static{
  		try (InputStream inputStream = new FileInputStream("./config.properties")) {
  			
  		   Properties serverprop = new Properties();
  		   serverprop.load(inputStream);
  		   TIMETABLES_URL = serverprop.getProperty("timetables_url");
  		   STATIONS_URL = serverprop.getProperty("stations_url");
  		   TIMETABLES_STATION_URL = serverprop.getProperty("timetables_station_url");
  		   MULTICAST_IP = serverprop.getProperty("multicast_ip");
  		   MULTICAST_TAG = serverprop.getProperty("multicast_tag");
  		   MULTICAST_PORT = Integer.parseInt(serverprop.getProperty("multicast_port"));
  	       RMI_PORT = Integer.parseInt(serverprop.getProperty("rmi_port"));
  		
  		   }catch(Exception ex){
  			   //ex.printStackTrace();
  			 Logger.getLogger(Main.class.getName()).log(Level.WARNING, ex.toString());
  		   }
  	}

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("fxml/HomeView.fxml"));
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
        primaryStage.setTitle("CZSMDP: ADMIN");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);    	
    }
}
