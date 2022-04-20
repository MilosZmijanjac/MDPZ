package app.controller;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.rpc.ServiceException;

import app.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Message;
import model.MessageType;
import model.TimetableRow;
import model.User;
import rest.TimetableService;
import soap.UserService;
import soap.UserServiceServiceLocator;
import sockets.MessageListener;
import sockets.MulticastReceiver;
import sockets.MulticastSender;

public class HomeController implements Initializable{

	@FXML
	private Button logoutBtn;
	@FXML
	private Button fileBtn;
	@FXML
	private ListView<User> activeUsersList ;
	@FXML
	private ListView<String> activeStationsList ;
	@FXML
	private Label titleLabel;
	@FXML
	private Label activeUsersListLabel;
	@FXML
	ComboBox<Integer> hourComboBox;
	@FXML
	ComboBox<Integer> minuteComboBox;
	@FXML
	ComboBox<String> notificationComboBox;
	@FXML
	Pane passagePane;
	@FXML
	TextField timetableTextField;
	@FXML
	DatePicker datePicker;
	@FXML
	TextArea messageArea;
	@FXML
	VBox chatBox;
	
	private static double xOffset = 0;
	private static double yOffset = 0;
	private boolean update;
	private boolean multicast;
	UserService service;
	

	
	public  ObservableList<String> stationsListItems = FXCollections.observableArrayList();
	public  ObservableList<User> usersListItems = FXCollections.observableArrayList();
	public static Map<String,ArrayList<Message>> inboxMap=new HashMap<>();
	private User userToChat;
	private File attachment=null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.setProperty("javax.net.ssl.keyStoreType", Main.KEYSTORE_TYPE);
		System.setProperty("javax.net.ssl.keyStore", Main.KEYSTORE);
		System.setProperty("javax.net.ssl.keyStorePassword", Main.KEYSTORE_PASSWORD);
		
		System.setProperty("javax.net.ssl.trustStoreType", Main.KEYSTORE_TYPE);
		System.setProperty("javax.net.ssl.trustStore", Main.TRUSTSTORE);
		System.setProperty("javax.net.ssl.trustStorePassword", Main.TRUSTSTORE_PASSWORD);
		
		 UserServiceServiceLocator locator=new UserServiceServiceLocator();
		 try {
			service=locator.getUserService();
		} catch (ServiceException e) {
			//e.printStackTrace();
			Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
		}
		 
		 update=true;
		 multicast=false;
		 passagePane.setVisible(false);
		 hourComboBox.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24));
		 minuteComboBox.setItems(FXCollections.observableArrayList(0,15,30,45));
		 titleLabel.setText("Station Id: "+LoginController.user.get_stationID()+" User: "+LoginController.user.get_username());
		 setCellFactoryStations();
		 setCellFactoryUsers();
		 activeStationsList.setItems(stationsListItems);
		 activeUsersList.setItems(usersListItems);
		 setStationActivityListener();
		 try {
			usersListItems.addAll(service.getUsersByStationId(LoginController.user.get_stationID()));
			startChatServer();
			inboxMap.put(Main.MULTICAST_TAG, new ArrayList<Message>());
			new MulticastReceiver(this).start();
		} catch (IOException e) {
			Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();
		}
		 
		 chatBox.setVisible(false);
		 notificationComboBox.setItems(FXCollections.observableArrayList());
		 
		
	}
	
	@FXML
    private void onExitClick() throws RemoteException {
		service.logout(LoginController.user);
		update=false;
        Platform.exit();
        System.exit(0);
    }
	 private void showLogin() throws IOException {
	        Stage stage = (Stage) logoutBtn.getScene().getWindow();
	        Parent root = FXMLLoader.load(getClass().getResource("../fxml/LoginView.fxml"));
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
	        stage.setScene(new Scene(root));
	        stage.setTitle("ZSMDP: Login");
	        stage.show();
	    }
	@FXML
	public void onLogoutClick(ActionEvent actionEvent) throws IOException {
      service.logout(LoginController.user);
      update=false;
      showLogin();
    }
	@FXML
	public void onComboClick(ActionEvent actionEvent) throws IOException {
		Platform.runLater(()->{notificationComboBox.setStyle("");
		notificationComboBox.getSelectionModel().clearSelection();
		notificationComboBox.setPromptText("No new notifications...");
		});
		
		
		
    }
	@FXML
    private void onTimetableClick() throws RemoteException {
		try {
            Parent root = FXMLLoader.load(HomeController.class.getResource("../fxml/TimetablesView.fxml"));
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
            stage.setTitle("ZSMDP: Timetables");
            stage.show();
        } catch (Exception e) {
           // e.printStackTrace();
        	Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
        }
    }
	@FXML
    private void onNoticeClick() throws RemoteException {
		multicast=!multicast;
		activeUsersList.setVisible(!activeUsersList.isVisible());
		activeUsersListLabel.setVisible(!activeUsersListLabel.isVisible());
		
		fileBtn.setVisible(!fileBtn.isVisible());
		chatBox.getChildren().clear();
		if(multicast)
		for(Message m:inboxMap.get(Main.MULTICAST_TAG))
			showMessage(m);
		chatBox.setVisible(true);
    }
	@FXML
    private void onPassageClick() throws RemoteException {
		passagePane.setVisible(!passagePane.isVisible());
    }
	@FXML
    private void onPassClick() throws RemoteException {
		if(timetableTextField.getText().isEmpty()||hourComboBox.getValue()==null||minuteComboBox.getValue()==null||datePicker.getValue()==null) {
			errorWindow("Invalid data");
			return;
		}int month=datePicker.getValue().getMonthValue();
		int year=datePicker.getValue().getYear();
		int day=datePicker.getValue().getDayOfMonth();
		TimetableRow tRow=TimetableService.getTimetableRowById(timetableTextField.getText());
		tRow.getTimeSlots().forEach(t->{
			if(t.getStation().getStation_id().equals(LoginController.user.get_stationID())){
				t.setArrivalTime(LocalDateTime.of(year,month,day,hourComboBox.getValue(),minuteComboBox.getValue()));
				t.setTrainPassed(true);
			}
					
		});
		TimetableService.update(timetableTextField.getText(), tRow);
    }
	@FXML
    private void onFileClick() throws RemoteException {
	     FileChooser fileChooser = new FileChooser();
         File selectedFile = fileChooser.showOpenDialog(null);
         if (!selectedFile.exists())
             return;
         attachment=selectedFile;
         
         fileBtn.setStyle("-fx-background-color: green");
    }
	@FXML
    private void onSendClick() throws UnknownHostException, IOException {
		Message msg=null;
		if(multicast) {
			msg=new Message(LoginController.user.get_username(),Main.MULTICAST_PORT,Main.MULTICAST_TAG,Main.MULTICAST_PORT,messageArea.getText(),MessageType.TEXT);
			new MulticastSender().multicast(msg);
			inboxMap.get(Main.MULTICAST_TAG).add(msg);
			showMessage(msg);
		}else if (userToChat!=null) {
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket socket = (SSLSocket) sf.createSocket(Main.CHAT_IP, userToChat.get_port());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		
		if(attachment==null) {
			msg =new Message(LoginController.user.get_username(),LoginController.user.get_port(),userToChat.get_username(),userToChat.get_port(),messageArea.getText(),MessageType.TEXT);
		}
		else  {
			msg =new Message(LoginController.user.get_username()
		    		 ,LoginController.user.get_port(),userToChat.get_username(),userToChat.get_port(),messageArea.getText(),MessageType.FILE,attachment.getName(),Files.readAllBytes(attachment.toPath()));
			attachment=null;
			fileBtn.setStyle("-fx-background-color: #2196f3");
		}
		
	     
		out.writeObject(msg);
		
		inboxMap.get(userToChat.get_username()).add(msg);
		showMessage(msg);
		}
		
		messageArea.clear();
    }
	@FXML
    private void onReportClick() throws RemoteException {
		try {
            Parent root = FXMLLoader.load(HomeController.class.getResource("../fxml/ReportView.fxml"));
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
            stage.setTitle("ZSMDP: Reports");
            stage.show();
        } catch (Exception e) {
            //e.printStackTrace();
        	Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
        }
    }
	
	@FXML
	 public void userChooseStation(MouseEvent mouseEvent) {
		Platform.runLater(()-> {
            usersListItems.clear();
            try {
            	usersListItems.addAll(service.getUsersByStationId(activeStationsList.getSelectionModel().getSelectedItem()));
            } catch (IOException e) {
                //e.printStackTrace();
            	Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
            }
            
            activeUsersList.refresh();
        });
		
    }
	
	
	@FXML
	 public void userChooseUser(MouseEvent mouseEvent) {
	
		userToChat=activeUsersList.getSelectionModel().getSelectedItem();
		chatBox.getChildren().clear();
	
		if(!inboxMap.containsKey(userToChat.get_username()))
		inboxMap.put(userToChat.get_username(),new ArrayList<Message>() ); 
		else {
			for(Message m:inboxMap.get(userToChat.get_username()))
			showMessage(m);
		}
		chatBox.setVisible(true);
   }
	
	private void setCellFactoryStations(){
        activeStationsList.setCellFactory(cell -> new ListCell<String>() {
            protected void updateItem(String u, boolean empty) {
                super.updateItem(u, empty);
                if (u == null || empty)
                    setText(null);
                else
                    setText(u);
            }
        });
    }
	private void setCellFactoryUsers(){
        activeUsersList.setCellFactory(cell -> new ListCell<User>() {
            protected void updateItem(User u, boolean empty) {
                super.updateItem(u, empty);
                if (u == null || empty)
                    setText(null);
                else
                    setText(u.get_username());
            }
        });
    }
	private void setStationActivityListener(){
	       new Thread(()-> {
	           try {
	               while(update) {
	                   initStationListView();
	                   Thread.sleep(5000);
	               }
	           } catch ( InterruptedException e) {
	               //e.printStackTrace();
	        	   Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
	           }
	       }).start();
	    }
	public synchronized void initStationListView(){
	        Platform.runLater(()-> {
	            stationsListItems.clear();
	            try {
	            	stationsListItems.addAll(service.getStationIds());
	            } catch (IOException e) {
	                //e.printStackTrace();
	            	Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
	            }
	            
	            activeStationsList.refresh();
	        });
	    }
	private void errorWindow(String message) {
	        Platform.runLater(()-> {
	            Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.setTitle("Warning");
	            alert.setHeaderText(null);
	            alert.setContentText(message);
	            alert.show();
	        });
	    }
	private synchronized void startChatServer() throws IOException {

			SSLServerSocketFactory ssf=(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			ServerSocket ss = ssf.createServerSocket(LoginController.user.get_port());
			new Thread(()->{
				System.out.println("Listening:"+LoginController.user.get_username()+" "+LoginController.user.get_port());
				while(true) {
					try {
						SSLSocket s = (SSLSocket) ss.accept();
						new MessageListener(s,this).start();
					} catch (IOException e) {
						Logger.getLogger(HomeController.class.getName()).log(Level.WARNING, e.toString());
						//e.printStackTrace();
					}
				}
			}).start();
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
	        if(LoginController.user.get_username().equals(msg.getSenderName()))
	            tile.setAlignment(Pos.BASELINE_RIGHT);
	        else
	            tile.setAlignment(Pos.BASELINE_LEFT);

	        label.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2196f3"), new CornerRadii(8), Insets.EMPTY)));
	        label.setTextFill(Color.WHITE);
	        tile.getChildren().add(label);
	        chatBox.getChildren().add(tile);

	    }
	public void refreshChatBox(String key) {
		chatBox.getChildren().clear();
		for(Message m:inboxMap.get(key))
			showMessage(m);
	}
	public void refreshNotice() {
		chatBox.getChildren().clear();
		for(Message m:inboxMap.get(Main.MULTICAST_TAG))
		showMessage(m);
	
}
	public void addNotification(Message msg) {
		notificationComboBox.getItems().add(msg.getTimestamp()+": "+msg.getSenderName()+" sent you a message...");
		notificationComboBox.setPromptText("New Notifications....");
		notificationComboBox.setStyle("-fx-background-color: red");
	}
}
