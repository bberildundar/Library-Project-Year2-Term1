module com.example.libraryprojectv2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.libraryprojectv2 to javafx.fxml;
    exports com.example.libraryprojectv2;
}