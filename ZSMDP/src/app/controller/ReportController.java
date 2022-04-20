package app.controller;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.rmi.server.AZSInterface;

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReportController implements Initializable {

	@FXML
	private Label titleLabel;
	@FXML
	private TextField txtArea;
	@FXML
	private TextField txtField;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 titleLabel.setText("Station Id: "+LoginController.user.get_stationID()+" User: "+LoginController.user.get_username());
	}
	
	@FXML
    private void onExitClick() throws RemoteException {
		Stage stage = (Stage) txtArea.getScene().getWindow();
        stage.close();
    }
	@FXML
    private void onSendClick() throws RemoteException {
		String filename=txtField.getText();
		if(!filename.endsWith(".pdf"))
			errorWindow("File name not valid");
		else {
			System.setProperty("java.security.policy", Main.POLICY_FILE);
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			try{
				String name = "AZS";
				Registry registry = LocateRegistry.getRegistry(1099);
				AZSInterface azs = (AZSInterface) registry.lookup(name);
				
					try {
						azs.upload(txtArea.getText().getBytes(), filename.replaceAll(".pdf", ""),LoginController.user.get_username(),LoginController.user.get_stationID());
					} catch (RemoteException e) {
						Logger.getLogger(ReportController.class.getName()).log(Level.WARNING, e.toString());
						//e.printStackTrace();
					}
					succesWindow("Report filed");
				
			}
			catch(Exception ex) {
				//ex.printStackTrace();
				Logger.getLogger(ReportController.class.getName()).log(Level.WARNING, ex.toString());
			}
		}
		
			
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
	private void succesWindow(String message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

}
