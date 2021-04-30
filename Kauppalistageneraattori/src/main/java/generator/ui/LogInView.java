package generator.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LogInView {
    
    private VBox logInBox;      
    private Label logInError;
    private TextField userNameInput;   
    private String input;
    
    public BorderPane set(Button logIn, Button createUser) {
        
        Label userName = new Label("Syötä käyttäjätunnus (vähintään 3 merkkiä):");
        this.userNameInput = new TextField();        
        this.logInError = new Label();    
        logInError.setTextFill(Color.RED);
        
        HBox logInInput = new HBox(userName, userNameInput, logInError);
        logInInput.setAlignment(Pos.CENTER);
        logInInput.setSpacing(20);          
        
        HBox logInButtons = new HBox(logIn, createUser);
        logInButtons.setAlignment(Pos.CENTER);
        logInButtons.setSpacing(20);              
        
        VBox logInBox = new VBox(logInInput, logInButtons);
        logInBox.setAlignment(Pos.CENTER);
        logInBox.setSpacing(20); 
        
        userNameInput.setOnKeyReleased(event -> {
            this.input = userNameInput.getText();
        });
              
        BorderPane pane = new BorderPane();
        pane.setCenter(logInBox); 
        return pane;
    }
    
    public String getUserNameInput() {
        return this.input;
    }
    
    public void logInFailure() {
        logInError.setText("Virhe! Käyttäjää " + this.input + " ei löydy.");
    }
    
    public void createUserFailure() {
        logInError.setText("Virhe! Käyttäjää " + this.input + " ei voida rekisteröidä.");
    }    

}