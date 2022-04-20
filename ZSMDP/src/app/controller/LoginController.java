package app.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import soap.UserService;
import soap.UserServiceServiceLocator;

public class LoginController {

	@FXML
    private TextField userField ;

    @FXML
    private PasswordField passField ;
    
    @ FXML 
    private Button loginBtn;
    
    double xOffset;
    double yOffset;
    public static User user=new User();

    @FXML
    private void onLoginClick(ActionEvent actionEvent) {

        UserServiceServiceLocator locator=new UserServiceServiceLocator();
        try {
			UserService service=locator.getUserService();
			user=service.login(userField.getText(),  passField.getText());
			if(user!=null)
				showHome();
			else
				errorWindow("Invalid username or password");
			
		} catch (IOException | ServiceException  e) {
			//e.printStackTrace();
			Logger.getLogger(LoginController.class.getName()).log(Level.WARNING, e.toString());
		} 

    }
    
    private void showHome() throws IOException {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/HomeView.fxml"));
        
        root.setOnMousePressed(event ->{
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        );
        root.setOnMouseDragged(event ->{
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        );
        
        stage.getIcons().clear();
        stage.setScene(new Scene(root));
        stage.setTitle("ZSMDP: "+user.get_username());
        stage.show();
}
    @FXML
    private void onExitClick() {
        Platform.exit();
        System.exit(0);
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
