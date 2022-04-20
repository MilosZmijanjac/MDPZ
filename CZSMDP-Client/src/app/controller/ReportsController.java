package app.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.unibl.etf.mdp.rmi.server.AZSInterface;

import app.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ReportsController implements Initializable {
	@FXML
	private ListView<String> reportsListView;
	
	public  ObservableList<String> reportsListItems = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String name = "AZS";
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(Main.RMI_PORT);
			AZSInterface azs = (AZSInterface) registry.lookup(name);
			setCellFactoryReports();
			reportsListView.setItems(reportsListItems);
			
				reportsListItems.addAll(azs.list());
		} catch (NotBoundException | IOException e) {
			Logger.getLogger(ReportsController.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();
		}
		
	}
	@FXML
    private void onExitClick() throws RemoteException {
		Stage stage = (Stage) reportsListView.getScene().getWindow();
        stage.close();
    }
	@FXML
	 public void userChoose(MouseEvent mouseEvent) {
		if(mouseEvent.getClickCount()==2) {
          String path=reportsListView.getSelectionModel().getSelectedItem();
          Platform.runLater(()-> {

              Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                      "Do you want to download  report "+ path,ButtonType.YES,ButtonType.NO);
              alert.setTitle("CZSDMP");
              alert.setHeaderText(null);
              alert.showAndWait()
                      .filter(response -> response == ButtonType.YES)
                      .ifPresent(response ->
                      {
                    	  PDDocument document = new PDDocument();
                          try {
                        	String name = "AZS";
                      		Registry registry;
                      		registry = LocateRegistry.getRegistry(Main.RMI_PORT);
                			AZSInterface azs = (AZSInterface) registry.lookup(name);
                			
                			PDPage page = new PDPage();
                			document.addPage(page);
                			PDPageContentStream contentStream = new PDPageContentStream(document, page);
                			contentStream.beginText(); 
                		    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                		    contentStream.newLineAtOffset(50F, 750F);
                		    contentStream.showText(new String(azs.download(path)).replace("\n", "").replace("\r", ""));
                		    contentStream.endText();
                		    contentStream.close();
                		    document.save(path);
                          	
                          } catch (Exception e) {
                             // e.printStackTrace();
                        	  Logger.getLogger(ReportsController.class.getName()).log(Level.WARNING, e.toString());
                          }finally {try {
							document.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							Logger.getLogger(ReportsController.class.getName()).log(Level.WARNING, e.toString());
						}}
                          
                      });
              succesWindow("File downloaded");
          });}
		}
	private void setCellFactoryReports(){
	       reportsListView.setCellFactory(cell -> new ListCell<String>() {
	            protected void updateItem(String u, boolean empty) {
	                super.updateItem(u, empty);
	                if (u == null || empty)
	                    setText(null);
					else
						setText(u);
	            }
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
