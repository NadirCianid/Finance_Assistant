package controllers;

import Staff.Assistant;
import entities.Contact;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public class ContactsPageController extends Application implements Initializable {
    @FXML
    private TableView<Contact> contactsTableView;

    @FXML
    private TableColumn<Contact, String> firstName;

    @FXML
    private TableColumn<Contact, String> lastName;

    @FXML
    private TableColumn<Contact, String> phoneNumber;

    @FXML
    private TableColumn<Contact, String> role;
    private List<String> roles;

    @FXML
    void addContact(ActionEvent event) throws SQLException {
        String firstName = JOptionPane.showInputDialog(null, "Введите имя:", "Новый контакт", PLAIN_MESSAGE);
        String lastName = JOptionPane.showInputDialog(null, "Введите фамилию:", "Новый контакт", PLAIN_MESSAGE);
        String roleName = (String)JOptionPane.showInputDialog(null,
                "Выберите роль",
                "Новый контакт",
                PLAIN_MESSAGE,
                null,
                roles.toArray(),
                roles.get(0));
        String phoneNumber = JOptionPane.showInputDialog(null, "Введите номер:", "Новый контакт", PLAIN_MESSAGE);

        if(Assistant.validatePersonalInfo(firstName, lastName, phoneNumber)) {

            contactsTableView.getItems().add(DataController.addContact(firstName, lastName, roleName, phoneNumber));
        }


    }

    @FXML
    void toHomePage(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        try {
            contactsTableView.setItems(DataController.getCurrentContacts());
            roles = DataController.getRoles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("fxmls/ContactsPage.fxml"))));
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        stage.show();
    }
}
