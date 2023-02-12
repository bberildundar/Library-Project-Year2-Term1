package com.example.libraryprojectv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

import java.io.IOException;

//Betül Beril Dündar (691136)

public class LibraryProject extends Application {

    @Override
    public void start(Stage window) throws IOException {
        // the program is loading the login form first. You can find the login information in the README file.
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryProject.class.getResource("login-view.fxml"));
        LoginController controller = new LoginController();
        fxmlLoader.setController(controller);

        Scene scene = new Scene(fxmlLoader.load(), 800, 450);
        // the program uses one css file for all forms:
        scene.getStylesheets().add(Objects.requireNonNull(LibraryProject.class.getResource("style.css")).toExternalForm());
        window.setTitle("Login");
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


