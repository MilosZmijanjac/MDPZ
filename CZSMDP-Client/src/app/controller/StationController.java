package app.controller;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Station;
import rest.StationService;

public class StationController implements Initializable {

	@FXML
	private TextField stationIdTextField;
	@FXML
	private TextField stationNameTextField;
	@FXML
	private ListView<Station> stationListView;
	public  ObservableList<Station> stationListItems = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		setCellFactoryStations();
		stationListView.setItems(stationListItems);
		refreshListView();
	}
	@FXML
    private void onAddStationClick() throws RemoteException {
		String id=stationIdTextField.getText();
		String station=stationNameTextField.getText();
		if(id.isEmpty()||station.isEmpty())
			{errorWindow("New Station data is invalid");
			return;}
		else {
			if(StationService.getStations().keySet().contains(id))
			{
				errorWindow("Station Id exists");
				return;
			}
			else
				StationService.add(new Station(id,station));
		}
			
		
		refreshListView();
			
    }
	@FXML
	 public void userChoose(MouseEvent mouseEvent) {
		if(mouseEvent.getClickCount()==2) {
           Station station=stationListView.getSelectionModel().getSelectedItem();
           Platform.runLater(()-> {

               Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                       "Do you want to delete station "+ station.getStation_name(),ButtonType.YES,ButtonType.NO);
               alert.setTitle("CZSDMP");
               alert.setHeaderText(null);
               alert.showAndWait()
                       .filter(response -> response == ButtonType.YES)
                       .ifPresent(response ->
                       { try {
                    	   StationService.remove(station.getStation_id());
                           	refreshListView();
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       });

           });}
		}
	private void refreshListView() {
		stationListItems.clear();
        try {
        	stationListItems.addAll(StationService.getStations().values());
        } catch (Exception e) {
            //e.printStackTrace();
        	Logger.getLogger(StationController.class.getName()).log(Level.WARNING, e.toString());
        }
        stationListView.refresh();
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
	private void setCellFactoryStations(){
	       stationListView.setCellFactory(cell -> new ListCell<Station>() {
	            protected void updateItem(Station u, boolean empty) {
	                super.updateItem(u, empty);
	                if (u == null || empty)
	                    setText(null);
	                else
	                    setText(u.getStation_id()+" "+u.getStation_name());
	            }
	        });
	    }
		@FXML
	    private void onExitClick() throws RemoteException {
			Stage stage = (Stage) stationIdTextField.getScene().getWindow();
	        stage.close();
	    }
}
