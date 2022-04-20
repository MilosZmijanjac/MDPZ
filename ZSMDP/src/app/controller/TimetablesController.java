package app.controller;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.TimetableRow;
import model.TimetableSlot;
import rest.TimetableService;

public class TimetablesController implements Initializable {
	@FXML
	private VBox timetableVBox;
	@FXML
	private Label titleLabel;
	
	ArrayList<TimetableRow> list;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 titleLabel.setText("Station Id: "+LoginController.user.get_stationID()+" User: "+LoginController.user.get_username());
		 try {
			list=TimetableService.getStationTimetableRows(LoginController.user.get_stationID());
			initTimetables();
		} catch (FileNotFoundException e) {
			Logger.getLogger(TimetablesController.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();
		}
		 
	}
	
	private void initTimetables() throws FileNotFoundException {
		for(TimetableRow tRow:list) {
			HBox hbox=new HBox();
			hbox.setStyle("-fx-border-color: #2196f3;-fx-border-width: 2;");
			Label temp=new Label();
				temp.setGraphic(new ImageView(new Image(new FileInputStream("./resources/view_resources/route.png"),50.0,50.0,false,true)));
				temp.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent e) {
						final Clipboard clipboard = Clipboard.getSystemClipboard();
						final ClipboardContent content = new ClipboardContent();

						content.putString(""+tRow.getTimetableRow_id());
						clipboard.setContent(content);
				      }
				});
			hbox.getChildren().add(temp);
			for(TimetableSlot tSlot:tRow.getTimeSlots()) {
				 Label lbl=new Label(""+tSlot.getExpectedTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))+"\n"+
			 tSlot.getStation().getStation_name()+"\n"+tSlot.getArrivalTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
				 if(tSlot.isTrainPassed())
					 lbl.setStyle("-fx-background-color: #66ff66;-fx-border-color: #2196f3;-fx-border-width: 2;");
				 else
				 lbl.setStyle("-fx-border-color: #2196f3;-fx-border-width: 2;");
				 hbox.getChildren().add(lbl);
				 }
			timetableVBox.getChildren().add(hbox);
		}
	}
	
	@FXML
    private void onExitClick() throws RemoteException {
		Stage stage = (Stage) timetableVBox.getScene().getWindow();
        stage.close();
    }

}
