package com.example.libraryprojectv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private User loggedUser;
    private Database db;
    private ObservableList<Member> members;
    private ObservableList<Item> items;

    final ToggleGroup group = new ToggleGroup();

    @FXML
    private Label lblWelcomeText;
    @FXML
    private Label lblInformation;
    @FXML
    private Label lblReceivedDaysInformation;
    @FXML
    private Button btnLendItem;
    @FXML
    private Button btnReceiveItem;
    @FXML
    private TextField txtItemCodeLendItem;
    @FXML
    private TextField txtMemberIdLendItem;
    @FXML
    private TextField txtItemCodeReceiveItem;
    @FXML
    private TextField txtSearchItem, txtSearchMember;
    @FXML
    private Label lblLendItemInformation;
    @FXML
    private Label lblReceiveItemInformation;
    @FXML
    private TableView<Item> tableViewItems;
    @FXML
    private TableColumn<Item, Integer> colItemCode;
    @FXML
    private TableColumn<Item, Availability> colAvailable;
    @FXML
    private TableColumn<Item, String> colTitle;
    @FXML
    private TableColumn<Item, String> colAuthor;
    @FXML
    private TableColumn<Item, String> colLender;
    @FXML
    private TableView<Member> tableViewMembers;
    @FXML
    private TableColumn<Member, Integer> colMemberId;
    @FXML
    private TableColumn<Member, String> colFirstName;
    @FXML
    private TableColumn<Member, String> colLastName;
    @FXML
    private TableColumn<Member, LocalDate> colBirthDate;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnEditItem;
    @FXML
    private Button btnDeleteItem;
    @FXML
    private Button btnAddMember;
    @FXML
    private Button btnEditMember;
    @FXML
    private Button btnDeleteMember;
    @FXML
    private Button btnFilter;
    @FXML
    private RadioButton rbAllItems, rbUnavailableItems;


    public MainController(User user, Database db){
        members = FXCollections.observableList(db.getMembers());
        items = FXCollections.observableList(db.getItems());
        this.loggedUser = user;
        this.db = db;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblWelcomeText.setText("Welcome " + loggedUser.getFullName());
        lblInformation.setText("");
        loadAllItemTable();
        loadMemberTable();

        //for searching items and members:
        txtSearchItem.textProperty().addListener((observable, oldValue, newValue) -> tableViewItems.setItems(filterListForSearchItem(items, newValue)));
        txtSearchMember.textProperty().addListener((observable, oldValue, newValue) -> tableViewMembers.setItems(filterListForSearchMember(members, newValue)));

        //setting radioButtons
        rbAllItems.setToggleGroup(group);
        rbUnavailableItems.setToggleGroup(group);
        rbAllItems.setSelected(true); //all items radio button is auto-selected.

    }

    private void loadAllItemTable(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colLender.setCellValueFactory(new PropertyValueFactory<>("lentMemberName"));
        tableViewItems.setItems(items);
    }

    private void loadMemberTable(){
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        tableViewMembers.setItems(members);
    }

    @FXML
    public void onClickLendItemButton() throws IOException{
        int itemId;
        int memberId;
        if(txtMemberIdLendItem.getText().isEmpty() || txtItemCodeLendItem.getText().isEmpty()){
            lblLendItemInformation.setTextFill(Color.RED);
            lblLendItemInformation.setText("Please fill in all the fields.");
            return;
        }
        try {
            itemId = Integer.parseInt(txtItemCodeLendItem.getText());
            memberId = Integer.parseInt(txtMemberIdLendItem.getText());
        } catch (NumberFormatException nfe) {
            itemId = 0;
            memberId = 0;
        }
        lendItem(itemId, memberId);
    }

    public void lendItem(int itemId, int memberId) throws IOException {
        for (Item i : items) {
            if (i.getItemId() == itemId) {
                if (i.getAvailability() == Availability.No) {
                    lblLendItemInformation.setTextFill(Color.RED);
                    lblLendItemInformation.setText("Item isn't available.");
                    break;
                }
                Member m = db.getMemberById(memberId);
                if (m != null) {
                    lendItemToMember(i, m); //this method (lendItem) was longer than 30lines so i created lendItemToMember method to make it shorter
                    return;
                } else {
                    lblLendItemInformation.setTextFill(Color.RED);
                    lblLendItemInformation.setText("This member doesn't exist.");
                    break;
                }
            }
            lblLendItemInformation.setTextFill(Color.RED);
            lblLendItemInformation.setText("This item doesn't exist");
        }
    }

    private void lendItemToMember(Item i, Member m) throws IOException {
        i.setLendingDate(LocalDate.now());
        i.setLentMember(m.getFullName());
        i.setAvailability(Availability.No);
        lblLendItemInformation.setTextFill(Color.GREEN);
        lblLendItemInformation.setText("Item successfully lent.");
        txtItemCodeLendItem.setText("");
        txtMemberIdLendItem.setText("");
        db.writeItems();
        tableViewItems.refresh();
        loadAllItemTable();
    }

    @FXML
    public void onClickReceiveItemButton() throws IOException {
        int itemId;
        if (txtItemCodeReceiveItem.getText().equals("")) {
            lblReceiveItemInformation.setTextFill(Color.RED);
            lblReceiveItemInformation.setText("Please enter an item ID.");
            return;
        }
        try {
            itemId = Integer.parseInt(txtItemCodeReceiveItem.getText());
        } catch (NumberFormatException pe) {
            //if the input isn't in the required format, it is throwing an exception
            // and the itemid is set to 0 since there isn't any item with the id = 0.
            itemId = 0;
        }
        receiveItem(itemId);
    }

    private void receiveItem(int itemCode) throws IOException {
        for (Item i : db.items) {
            if (i.getItemId() == itemCode) {
                if (i.getAvailability() == Availability.No) {
                    i.setAvailability(Availability.Yes);
                    displayDaysText(i);
                    lblReceiveItemInformation.setTextFill(Color.GREEN);
                    lblReceiveItemInformation.setText("Item received successfully.");
                    txtItemCodeReceiveItem.setText("");
                    i.setLendingDate(null);
                    i.setLentMember(null);
                    db.writeItems();
                    tableViewItems.refresh();
                } else {
                    lblReceiveItemInformation.setTextFill(Color.RED);
                    lblReceiveItemInformation.setText("This item is already available.");
                }
                return;
            }
        }
        lblReceiveItemInformation.setTextFill(Color.RED);
        lblReceiveItemInformation.setText("Item doesnt exist.");
    }

    public void displayDaysText(Item item){
        Period daysBetween = Period.between(item.getLendingDate(), LocalDate.now());
        if (daysBetween.getDays() > 21){
            lblReceivedDaysInformation.setTextFill(Color.RED);
            lblReceivedDaysInformation.setText("This item is late for " + (daysBetween.getDays()-21)+ " days.");
        }
    }

    @FXML
    public void onClickAddItemButton(ActionEvent event) throws IOException {
        try {
            AddOrEditItemController addItemController = new AddOrEditItemController(db);
            loadScene(addItemController, "add-or-edit-item-view.fxml", "Add Item");

            Item i =(addItemController.getItem());
            if (i != null) {
                i.setAvailability(Availability.Yes);
                i.setItemId(db.getNewItemID());
                items.add(i);
            }
            db.writeItems();
            loadAllItemTable();
            tableViewItems.refresh();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @FXML
    public void onClickEditItemButton(ActionEvent event) throws IOException {
        try {
            Item inputItem = tableViewItems.getSelectionModel().getSelectedItem();
            AddOrEditItemController editItemController = new AddOrEditItemController(db, inputItem);
            loadScene(editItemController, "add-or-edit-item-view.fxml", "Edit Item");
            tableViewItems.getSelectionModel().clearSelection();

            Item i = (editItemController.getItem());
            if (i != null) {
                i.setItemId(inputItem.getItemId());
                i.setAvailability(inputItem.getAvailability());
                i.setLentMember(inputItem.getLentMemberName());
                db.getItems().set(db.getItems().indexOf(inputItem), i);
            }
            db.writeItems();
            loadAllItemTable();
            tableViewItems.refresh();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @FXML
    private void onClickDeleteItemButton(ActionEvent event) throws IOException {
        Item i = tableViewItems.getSelectionModel().getSelectedItem();
        items.remove(i);
        db.writeItems();
        loadAllItemTable();
        tableViewItems.refresh();
    }

    @FXML
    private void onClickFilterButton(ActionEvent event) throws IOException {
        loadAllItemTable();
        if(rbUnavailableItems.isSelected()){
            tableViewItems.setItems(filterList(items));
        }
        else{
            loadAllItemTable();
            tableViewItems.refresh();
        }
    }

    //assignment 2 in exam:
    private ObservableList<Item> filterList(List<Item> list){ //code based on: https://edencoding.com/search-bar-dynamic-filtering/
        List<Item> filteredList = new ArrayList<>();

        for (Item item : list){
            if(searchNotAvailableBooks(item)) filteredList.add(item);
        }
        return FXCollections.observableList(filteredList);
    }

    private boolean searchNotAvailableBooks(Item book){
        return (book.getAvailability() == Availability.No);
    }

    //search functionality in Items tab for extra grade:
    private ObservableList<Item> filterListForSearchItem(List<Item> list, String searchText){ //code based on: https://edencoding.com/search-bar-dynamic-filtering/
        List<Item> filteredList = new ArrayList<>();
        for (Item book : list){
            if(searchItemsFindsOrder(book, searchText)) filteredList.add(book);
        }
        return FXCollections.observableList(filteredList);
    }

    private boolean searchItemsFindsOrder(Item book, String searchText){
        return (book.getAuthor().toLowerCase().contains(searchText.toLowerCase())) || (book.getTitle().toLowerCase().contains(searchText.toLowerCase()));
    }

    @FXML
    public void onClickAddMemberButton(ActionEvent event) throws IOException {
        try {
            AddOrEditMemberController addOrEditMemberController = new AddOrEditMemberController(db);
            loadScene(addOrEditMemberController, "add-or-edit-member-view.fxml", "Add Member");

            Member m =(addOrEditMemberController.getMember());
            if (m != null) {
                m.setId(db.getNewMemberID());
                members.add(m);
            }
            db.writeMembers();
            loadMemberTable();
            tableViewMembers.refresh();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @FXML
    public void onClickEditMemberButton(ActionEvent event) throws IOException {
        try {
            Member inputMember = tableViewMembers.getSelectionModel().getSelectedItem();
            AddOrEditMemberController addOrEditMemberController = new AddOrEditMemberController(db, inputMember);
            loadScene(addOrEditMemberController, "add-or-edit-member-view.fxml", "Edit Member");
            tableViewMembers.getSelectionModel().clearSelection();

            Member m = (addOrEditMemberController.getMember());
            if (m != null) {
                m.setId(inputMember.getId());
                db.getMembers().set(db.getMembers().indexOf(inputMember), m);
            }
            db.writeMembers();
            loadMemberTable();
            tableViewMembers.refresh();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @FXML
    private void onClickDeleteMemberButton(ActionEvent event) throws IOException {
        Member m = tableViewMembers.getSelectionModel().getSelectedItem();
        members.remove(m);
        db.writeMembers();
        loadMemberTable();
        tableViewMembers.refresh();
    }

    //search functionality in Members tab for extra grade:
    private ObservableList<Member> filterListForSearchMember(List<Member> list, String searchText){ //Code based on: https://edencoding.com/search-bar-dynamic-filtering/
        List<Member> filteredList = new ArrayList<>();
        for (Member m : list){
            if(searchMemberFindsOrder(m, searchText)) filteredList.add(m);
        }
        return FXCollections.observableList(filteredList);
    }
    private boolean searchMemberFindsOrder(Member member, String searchText){
        return (member.getFirstName().toLowerCase().contains(searchText.toLowerCase())) || (member.getLastName().toLowerCase().contains(searchText.toLowerCase()));
    }

    private void loadScene(Object controller, String fxmlFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryProject.class.getResource(fxmlFile));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 650, 350);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.setScene(scene);
            dialog.showAndWait();
        }catch(IOException e){
            throw new RuntimeException();
        }
    }
}
