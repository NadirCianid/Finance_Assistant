package controllers;

import Staff.Assistant;
import entities.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class TransactionAddingPageController implements Initializable {
    @FXML
    private ComboBox<Account> accountCB;

    @FXML
    private TextField balanceTF;

    @FXML
    private ComboBox<Category> categoryCB;

    @FXML
    private ComboBox<Contact> contactCB;

    @FXML
    private DatePicker dateDP;

    @FXML
    private TextArea descriptionTA;

    @FXML
    private TextField sizeTF;

    @FXML
    private TextField timeTF;

    @FXML
    private ComboBox<Type> typeCB;

    @FXML
    void addTransaction(ActionEvent event) throws SQLException {
        Type type = typeCB.getValue();
        Category category = categoryCB.getValue();
        Contact contact = contactCB.getValue();
        Account account = accountCB.getValue();

        String size = sizeTF.getText();
        LocalDate date = dateDP.getValue();
        String time = timeTF.getText();
        String description = descriptionTA.getText();

        if(Assistant.validateTransactionInfo(size, date, time) && account.use(Integer.parseInt(size), type.getTypeID())) {
            try {
                Transaction newTransaction = DataController.addTransaction(type, category, size, date, time, contact, description, account);
                Assistant.homePageController.getTransactionsTableView().getItems().add(newTransaction);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            typeCB.setItems(FXCollections.observableArrayList(DataController.getTypes()));
            categoryCB.setItems(FXCollections.observableArrayList(DataController.getCategories()));
            contactCB.setItems(FXCollections.observableArrayList(DataController.getCurrentContacts()));
            accountCB.setItems(FXCollections.observableArrayList(DataController.getAccounts()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
