package com.example.libraryprojectv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddOrEditItemController implements Initializable {

    private Database db;
    private Item item;

    @FXML
    private TextField txtItemTitleInAddItem;
    @FXML
    private TextField txtItemAuthorInAddItem;
    @FXML
    private Label lblInformationInAddItem;
    @FXML
    private Label lblAddItemHeader;
    @FXML
    private Button btnConfirmInAddItem;
    @FXML
    private Button btnCancelInAddItem;

    public AddOrEditItemController(Database db){
        this.db = db;
    }

    public AddOrEditItemController(Database db, Item item){
        this.db = db;
        this.item = item;
    }

    public Item getItem(){
        return item;
    }

    @FXML
    public void onClickConfirmInAddItemButton(ActionEvent actionEvent) {
        if ((txtItemAuthorInAddItem.getText().isEmpty()) || txtItemTitleInAddItem.getText().isEmpty()) {
            lblInformationInAddItem.setTextFill(Color.RED);
            lblInformationInAddItem.setText("Please fill in all the fields.");
            return;
        }
        item = new Item(txtItemTitleInAddItem.getText(), txtItemAuthorInAddItem.getText());
        Stage currentStage = (Stage)txtItemAuthorInAddItem.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onClickCloseInAddItemButton() {
        Stage currentStage = (Stage)txtItemAuthorInAddItem.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblInformationInAddItem.setText("");
        // this is for editing the item. if item isn't null, the form is setting itself to edit form.
        if(item != null){
            lblAddItemHeader.setText("Edit Item");
            txtItemAuthorInAddItem.setText(item.getAuthor());
            txtItemTitleInAddItem.setText(item.getTitle());
        }
    }
}
