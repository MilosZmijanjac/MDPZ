package app.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Message;
import model.MessageType;
import sockets.MulticastReceiver;
import sockets.MulticastSender;

public class HomeController implements Initializable{
		
	private static double xOffset = 0;
	private static double yOffset = 0;
	@FXML
	private VBox chatBox;
	@FXML
	private TextArea messageArea;


	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		 
		new MulticastReceiver(this).start();
		
	}
	
	@FXML
    private void onExitClick() throws RemoteException {
        Platform.exit();
        System.exit(0);
    }
	@FXML
    private void onUsersClick() throws IOException {
	     Parent root = FXMLLoader.load(HomeController.class.getResource("../fxml/UsersView.fxml"));
         Stage stage =new Stage();
         root.setOnMousePressed(event ->{
             xOffset = event.getSceneX();
             yOffset = event.getSceneY();
         });
         root.setOnMouseDragged(event ->{
                     stage.setX(event.getScreenX() - xOffset);
                     stage.setY(event.getScreenY() - yOffset);
                 }
         );
         stage.getIcons().clear();
         stage.initStyle(StageStyle.TRANSPARENT);
         stage.setScene(new Scene(root));
         stage.setTitle("CZSMDP: Users");
         stage.show();
    }
	@FXML
    private void onReportsClick() throws IOException {
	     Parent root = FXMLLoader.load(HomeController.class.getResource("../fxml/ReportsView.fxml"));
         Stage stage =new Stage();
         root.setOnMousePressed(event ->{
             xOffset = event.getSceneX();
             yOffset = event.getSceneY();
         });
         root.setOnMouseDragged(event ->{
                     stage.setX(event.getScreenX() - xOffset);
                     stage.setY(event.getScreenY() - yOffset);
                 }
         );
         stage.getIcons().clear();
         stage.initStyle(StageStyle.TRANSPARENT);
         stage.setScene(new Scene(root));
         stage.setTitle("CZSMDP: Reports");
         stage.show();
    }
	@FXML
    private void onTimetablesClick() throws IOException {
		Parent root = FXMLLoader.load(HomeController.class.getResource("../fxml/TimetableView.fxml"));
        Stage stage =new Stage();
        root.setOnMousePressed(event ->{
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event ->{
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
        );
        stage.getIcons().clear();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root));
        stage.setTitle("CZSMDP: Timetables");
        stage.show();
    }
	@FXML
    private void onSendClick() throws RemoteException {
		Message msg=new Message("ADMIN",Main.MULTICAST_PORT,Main.MULTICAST_TAG,Main.MULTICAST_PORT,messageArea.getText(),MessageType.TEXT);
		new MulticastSender().multicast(msg);
		
		showMessage(msg);
		messageArea.clear();
    }
	@FXML
    private void onStationsClick() throws IOException {
		Parent root = FXMLLoader.load(HomeController.class.getResource("../fxml/StationView.fxml"));
        Stage stage =new Stage();
        root.setOnMousePressed(event ->{
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event ->{
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
        );
        stage.getIcons().clear();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root));
        stage.setTitle("CZSMDP: Stations");
        stage.show();
    }
	public  synchronized void showMessage(Message msg) {
		
        Label label= new Label("");
        Tooltip tooltip = new Tooltip();
        tooltip.setText(msg.getTimestamp());

        
            label.setText(msg.getSenderName()+": " + msg.getText() + " ");
            label.setTooltip(tooltip);
        
        VBox tile=new VBox();
        tile.setPrefHeight(30);
        tile.setPrefWidth(470);

        label.setPrefHeight(30);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setWrapText(true);
        label.setAlignment(Pos.BASELINE_CENTER);
        if("ADMIN".equals(msg.getSenderName()))
            tile.setAlignment(Pos.BASELINE_RIGHT);
        else
            tile.setAlignment(Pos.BASELINE_LEFT);

        label.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2196f3"), new CornerRadii(8), Insets.EMPTY)));
        label.setTextFill(Color.WHITE);
        tile.getChildren().add(label);
        chatBox.getChildren().add(tile);

    }
	 
}
