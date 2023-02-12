package com.example.libraryprojectv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Database db;

    @FXML
    public TextField txtBoxUsername;
    @FXML
    public PasswordField txtBoxPassword;
    @FXML
    public Button btnLogin;
    @FXML
    public Label lblFailedToLoginMessage;

    @FXML
    public void onLoginButtonClick(ActionEvent event) throws IOException {
        for (User u : db.users) {
            if (txtBoxUsername.getText().equals(u.getUsername())
                    && txtBoxPassword.getText().equals(u.getPassword())) {
                MainController mainController = new MainController(u, db);
                loadScene(mainController, "main-view.fxml");
            } else if (txtBoxUsername.getText().isEmpty() || txtBoxPassword.getText().isEmpty()) {
                lblFailedToLoginMessage.setText("Please enter a username and a password.");
            } else {
                lblFailedToLoginMessage.setText("Invalid username/password combination. Please try again.");
            }
        }
    }

    private void loadScene(Object controller, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryProject.class.getResource(fxmlFile));
            fxmlLoader.setController(controller);
            Scene mainScene = new Scene(fxmlLoader.load(), 650, 450);
            mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            Stage window = (Stage) btnLogin.getScene().getWindow();
            window.setTitle("Main window");
            window.setScene(mainScene);
        }catch(IOException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            db = new Database();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


