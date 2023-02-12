package com.example.libraryprojectv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddOrEditMemberController implements Initializable {

    public static final String DATE_FORMAT = "dd-MM-yyyy";

    private Database db;
    private Member member;
    DateTimeFormatter dateFormatter;

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label lblInformationAddOrEditMember;
    @FXML
    private Label lblAddOrEditMemberHeader;
    @FXML
    private Button btnConfirmAddOrEditMember;
    @FXML
    private Button btnCloseAddOrEditMember;

    public AddOrEditMemberController(Database database) {
        this.db = database;
    }

    public AddOrEditMemberController(Database database, Member member) {
        this.db = database;
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @FXML
    public void onClickCloseButton() {
        Stage currentStage = (Stage)txtFirstName.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onClickConfirmButton(ActionEvent actionEvent) {
        try {
            if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || datePicker.getEditor().getText().isEmpty()){
                lblInformationAddOrEditMember.setTextFill(Color.RED);
                lblInformationAddOrEditMember.setText("Please fill in all the fields.");
                return;
            }
            LocalDate dateOfBirth = datePicker.getConverter().fromString(datePicker.getEditor().getText());
            member = new Member(txtFirstName.getText(), txtLastName.getText(), dateOfBirth);
            Stage currentStage = (Stage)txtFirstName.getScene().getWindow();
            currentStage.close();
        }
        catch (DateTimeParseException e){
            lblInformationAddOrEditMember.setTextFill(Color.RED);
            lblInformationAddOrEditMember.setText("The date of birth should be written as day-month-year, using two digits for each field (e.g. 31-01-2023).");
        }
        catch (Exception e){
            lblInformationAddOrEditMember.setTextFill(Color.RED);
            lblInformationAddOrEditMember.setText(e.getMessage());
        }
    }

    // For datePicker format and configuring the datePicker, code based on answer from Stack Overflow: https://stackoverflow.com/a/51697739
    private void configureDatePicker(){
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate localDate) {

                return localDate.format(dateFormatter);
            }

            @Override
            public LocalDate fromString(String s) {
                return LocalDate.parse(s, dateFormatter);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblInformationAddOrEditMember.setText("");

        // Again, code based on answer from Stack Overflow: https://stackoverflow.com/a/51697739
        dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        datePicker.setValue(LocalDate.now());
        configureDatePicker();

        if(member != null){
            lblAddOrEditMemberHeader.setText("Edit Member");
            btnConfirmAddOrEditMember.setText("UPDATE");
            txtFirstName.setText(member.getFirstName());
            txtLastName.setText(member.getLastName());
            datePicker.setValue(member.getDateOfBirth());
        }
    }
}
