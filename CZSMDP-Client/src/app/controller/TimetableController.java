package app.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Station;
import model.TimetableRow;
import model.TimetableSlot;
import rest.TimetableService;

public class TimetableController implements Initializable {

	@FXML
	private VBox timetableVBox;
	ArrayList<TimetableRow> list;
	private static double xOffset = 0;
	private static double yOffset = 0;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		TimetableRow tRow=new TimetableRow();
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("KD","Kozarska Dubica"),LocalDateTime.now(),LocalDateTime.now(),true));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("BL","Banja Luka"),LocalDateTime.now(),LocalDateTime.now(),false));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("PD","Prijedor"),LocalDateTime.now(),LocalDateTime.now(),false));
		 TimetableService.add(tRow);
		 tRow=new TimetableRow();
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("KD","Kozarska Dubica"),LocalDateTime.now(),LocalDateTime.now(),true));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("BL","Banja Luka"),LocalDateTime.now(),LocalDateTime.now(),true));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("PD","Prijedor"),LocalDateTime.now(),LocalDateTime.now(),false));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("NG","NoviGrad"),LocalDateTime.now(),LocalDateTime.now(),false));
		 TimetableService.add(tRow);
		 tRow=new TimetableRow();
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("KD","Kozarska Dubica"),LocalDateTime.now(),LocalDateTime.now(),true));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("BL","Banja Luka"),LocalDateTime.now(),LocalDateTime.now(),true));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("PD","Prijedor"),LocalDateTime.now(),LocalDateTime.now(),true));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("NG","NoviGrad"),LocalDateTime.now(),LocalDateTime.now(),false));
		 tRow.getTimeSlots().add(new TimetableSlot(new Station("SA","Sarajevo"),LocalDateTime.now(),LocalDateTime.now(),false));
		 TimetableService.add(tRow);
		list=TimetableService.getTimetableRows();
		try {
			initTimetables();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableController.class.getName()).log(Level.WARNING, e.toString());
		}
		
	}
	@FXML
    private void onExitClick() throws RemoteException {
		Stage stage = (Stage) timetableVBox.getScene().getWindow();
        stage.close();
    }
	@FXML
    private void onAddClick() throws IOException {
		Stage stage = (Stage) timetableVBox.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddNewTimetableView.fxml"));
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

	private void initTimetables() throws FileNotFoundException {
		
		for(TimetableRow tRow:list) {
			HBox hbox=new HBox();
			hbox.setStyle("-fx-border-color: #2196f3;-fx-border-width: 2;");
			Label temp=new Label();
				temp.setGraphic(new ImageView(new Image(new FileInputStream("./resources/view_resources/route.png"),50.0,50.0,false,true)));
				temp.setText(""+tRow.getTimetableRow_id());
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
			Label removeLbl=new Label();
			removeLbl.setGraphic(new ImageView(new Image(new FileInputStream("./resources/view_resources/remove.png"),50.0,50.0,false,true)));
			removeLbl.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e) {
			      TimetableService.remove(temp.getText());
			      list.removeIf(t->temp.getText().equals(t.getTimetableRow_id()));
			      removeLbl.getParent().setVisible(false);
			      }
			});
		hbox.getChildren().add(removeLbl);
			timetableVBox.getChildren().add(hbox);
		}
	}
}
