package app.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Station;
import model.TimetableRow;
import model.TimetableSlot;
import rest.StationService;
import rest.TimetableService;

public class AddNewTimetableController implements Initializable {

	@FXML
	ComboBox<Integer> hourComboBox;
	@FXML
	ComboBox<Integer> minuteComboBox;
	@FXML
	ComboBox<Station> stationComboBox;
	@FXML
	VBox timetableVBox;
	@FXML
	DatePicker datePicker;
	HBox hbox=new HBox();
	private static double xOffset = 0;
	private static double yOffset = 0;
	TimetableRow tRow;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	   timetableVBox.getChildren().add(hbox);
	   hourComboBox.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24));
	   minuteComboBox.setItems(FXCollections.observableArrayList(0,15,30,45));
	   stationComboBox.setItems(FXCollections.observableArrayList(StationService.getStations().values()));
	   tRow=new TimetableRow();
	}

	@FXML
    private void onExitClick() throws RemoteException {
		Stage stage = (Stage) hourComboBox.getScene().getWindow();
        stage.close();
    }
	@FXML
    private void onAddClick() throws RemoteException {
		if(hourComboBox.getValue()==null||minuteComboBox.getValue()==null||stationComboBox.getValue()==null||datePicker.getValue()==null)
			{errorWindow("Invalid data"); return;}
		int month=datePicker.getValue().getMonthValue();
		int year=datePicker.getValue().getYear();
		int day=datePicker.getValue().getDayOfMonth();
		TimetableSlot tSlot=new TimetableSlot(stationComboBox.getValue(),LocalDateTime.of(year,month,day,hourComboBox.getValue(),minuteComboBox.getValue()),LocalDateTime.of(year,month,day,hourComboBox.getValue(),minuteComboBox.getValue()),false);
		tRow.getTimeSlots().add(tSlot);
		
		hbox.setStyle("-fx-border-color: #2196f3;-fx-border-width: 2;");
		
	     Label lbl=new Label(""+tSlot.getExpectedTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))+"\n"+
		 tSlot.getStation().getStation_name()+"\n"+tSlot.getArrivalTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
			 if(tSlot.isTrainPassed())
				 lbl.setStyle("-fx-background-color: #66ff66;-fx-border-color: #2196f3;-fx-border-width: 2;");
			 else
			 lbl.setStyle("-fx-border-color: #2196f3;-fx-border-width: 2;");
			 hbox.getChildren().add(lbl);
		
    }
	@FXML
    private void onSaveClick() throws IOException {
		TimetableService.add(tRow);
		Stage stage = (Stage) datePicker.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/TimetableView.fxml"));
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

        stage.show();
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
