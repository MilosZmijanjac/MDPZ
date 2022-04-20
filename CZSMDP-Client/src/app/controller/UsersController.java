package app.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Station;
import model.User;
import rest.StationService;
import soap.UserService;
import soap.UserServiceServiceLocator;

public class UsersController implements Initializable {

	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private ComboBox<Station> stationsComboBox;
	@FXML
	private ListView<User> usersListView;
	public  ObservableList<User> usersListItems = FXCollections.observableArrayList();
	UserService service;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 UserServiceServiceLocator locator=new UserServiceServiceLocator();
		 try {
		      service=locator.getUserService();
	          setCellFactoryUsers();
		       usersListView.setItems(usersListItems);
		
				usersListItems.addAll(service.getUsers());
				stationsComboBox.setItems(FXCollections.observableArrayList(StationService.getStations().values()));
			} catch (RemoteException | ServiceException e) {
				//e.printStackTrace();
				Logger.getLogger(UsersController.class.getName()).log(Level.WARNING, e.toString());
			}
	}
	@FXML
	 public void userChoose(MouseEvent mouseEvent) {
		if(mouseEvent.getClickCount()==2) {
            User user=usersListView.getSelectionModel().getSelectedItem();
            Platform.runLater(()-> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Do you want to delete a user "+ user.get_username(),ButtonType.YES,ButtonType.NO);
                alert.setTitle("CZSDMP");
                alert.setHeaderText(null);
                alert.showAndWait()
                        .filter(response -> response == ButtonType.YES)
                        .ifPresent(response ->
                        {
                            try {
                            	try {
                        			service.removeUser(usersListView.getSelectionModel().getSelectedItem());
                        		} catch (RemoteException e) {
                        		 errorWindow("Error deleting user");
                        			//e.printStackTrace();
                        		 Logger.getLogger(UsersController.class.getName()).log(Level.WARNING, e.toString());
                        		}
                        		refreshListView();
                            } catch (Exception e) {
                               // e.printStackTrace();
                            	Logger.getLogger(UsersController.class.getName()).log(Level.WARNING, e.toString());
                            }
                        });

            });}
   }
	@FXML
    private void onExitClick() throws RemoteException {
		Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.close();
    }
	
	@FXML
    private void onAddUserClick() throws RemoteException {
		String username=usernameTextField.getText();
		String password=passwordTextField.getText();
		String station=stationsComboBox.getSelectionModel().getSelectedItem().getStation_id();
		if(username.isEmpty()||password.isEmpty()||station.isEmpty())
			{errorWindow("New User data is invalid");
			return;}
		else
			service.addNewUser(username, password, station, "");
		
		refreshListView();
			
    }
	private void refreshListView() {
		usersListItems.clear();
        try {
        	usersListItems.addAll(service.getUsers());
        } catch (IOException e) {
            //e.printStackTrace();
        	Logger.getLogger(UsersController.class.getName()).log(Level.WARNING, e.toString());
        }
        usersListView.refresh();
	}
	private void setCellFactoryUsers(){
       usersListView.setCellFactory(cell -> new ListCell<User>() {
            protected void updateItem(User u, boolean empty) {
                super.updateItem(u, empty);
                if (u == null || empty)
                    setText(null);
                else
                    setText(u.get_username()+" "+u.get_stationID());
            }
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
}
