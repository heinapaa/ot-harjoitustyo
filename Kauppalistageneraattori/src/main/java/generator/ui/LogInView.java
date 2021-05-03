package generator.ui;

import generator.domain.UserService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;

public class LogInView implements View {
    
    private final UserService userService;     
    private final Router router;
    
    private Label logInError;    
    
    public LogInView(Router router, UserService userService) {
        this.router = router;
        this.userService = userService;        
    }
    
    @Override
    public Scene create() {
        Label userName = new Label("Syötä käyttäjätunnus (vähintään 3 merkkiä):");
        
        TextField userNameInput = new TextField(); 
        
        this.logInError = new Label();    
        logInError.setTextFill(Color.RED);     
        
        HBox logInInput = new HBox(userName, userNameInput, logInError);
        logInInput.setAlignment(Pos.CENTER);
        logInInput.setSpacing(20);          

        Button logIn = new Button("Kirjaudu");
        logIn.setOnMouseClicked(event -> {
            if (userService.login(userNameInput.getText())) {
                router.setRecipeListView();
            } else {
                if (StringUtils.deleteWhitespace(userNameInput.getText()).isEmpty()) {
                    logInError.setText("Virhe! Syötä käyttäjätunnus.");
                } else if (StringUtils.deleteWhitespace(userNameInput.getText()).length() < 3) {
                    logInError.setText("Virhe! Käyttäjätunnuksen minimipituus on 3 merkkiä.");
                } else {
                    logInError.setText("Virhe! Käyttäjää " + StringUtils.deleteWhitespace(userNameInput.getText()) + " ei löydy.");
                } 
            }
        });
                
        Button createUser = new Button("Luo uusi käyttäjä"); 
        createUser.setOnMouseClicked(event -> {
            if (userService.addNewUser(userNameInput.getText())) {
                userService.login(StringUtils.deleteWhitespace(userNameInput.getText()));
                router.setRecipeListView();
            } else {
                if (StringUtils.deleteWhitespace(userNameInput.getText()).isEmpty()) {
                    logInError.setText("Virhe! Syötä haluttu käyttäjätunnus.");
                } else if (StringUtils.deleteWhitespace(userNameInput.getText()).length() < 3) {
                    logInError.setText("Virhe! Käyttäjätunnuksen minimipituus on 3 merkkiä.");
                } else {
                    logInError.setText("Virhe! Käyttäjää " + StringUtils.deleteWhitespace(userNameInput.getText()) + " ei voida lisätä.");
                }                
            }            
        });
        
        HBox logInButtons = new HBox(logIn, createUser);
        logInButtons.setAlignment(Pos.CENTER);
        logInButtons.setSpacing(20);              
        
        VBox logInBox = new VBox(logInInput, logInButtons);
        logInBox.setAlignment(Pos.CENTER);
        logInBox.setSpacing(20); 
        
        BorderPane pane = new BorderPane();

        userNameInput.clear();
        pane.setCenter(logInBox); 
        
        Scene scene = new Scene(pane);
        return scene;
    }
}