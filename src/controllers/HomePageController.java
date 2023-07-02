package controllers;

import Staff.Assistant;
import entities.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.Scene;

import javax.swing.JOptionPane;

import static javax.swing.JOptionPane.*;

public class HomePageController extends Application implements Initializable {
    private ObservableList<Type> types;
    private List<Category> categories;
    private List<String> banks;
    private static ObservableList<Transaction> transactions;
    @FXML
    private ComboBox<Account> accountsCB;

    @FXML
    private ComboBox<Type> transactionTypeCB;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label creditLabel;

    @FXML
    private Label debitLabel;

    @FXML
    private TableView<Transaction> transactionsTableView;
    @FXML
    private AnchorPane tableViewAP;
    @FXML
    private TableColumn<Transaction, Integer> balance;
    @FXML
    private TableColumn<Transaction, Category> category;
    @FXML
    private TableColumn<Transaction, Type> type;
    @FXML
    private TableColumn<Transaction, Contact> contact;
    @FXML
    private TableColumn<Transaction, LocalDate> date;
    @FXML
    private TableColumn<Transaction, Integer> size;

    @FXML
    private TableColumn<Transaction, LocalTime> time;

    @FXML
    private void accountHistory(ActionEvent event) throws SQLException {
        System.out.println(accountsCB.getValue() + " "+ accountsCB.getValue().getId());
        tableViewAP.getChildren().clear();
        transactionsTableView.setItems(DataController.getTransactionsByAccount(accountsCB.getValue()));
    }

    @FXML
    private void changeTransactionType(ActionEvent event) {

    }

    @FXML
    private void changeAccount(ActionEvent event) {

    }

    @FXML
    private void newAccount(ActionEvent event) throws SQLException {
        String accountName = JOptionPane.showInputDialog(null, "Введите название счета:", "Новый счет", PLAIN_MESSAGE);
        String balance = JOptionPane.showInputDialog(null, "Введите баланс счета:", "Новый счет", PLAIN_MESSAGE);
        String bankName = (String)JOptionPane.showInputDialog(null,
                "Выберите банк",
                "Новый счет",
                PLAIN_MESSAGE,
                null,
                banks.toArray(),
                banks.get(0));

        if(Assistant.validateAccountInfo(accountName, balance)) {
            accountsCB.getItems().add(DataController.addAccount(bankName, accountName, Integer.parseInt(balance)));
            calculateSummary();
        }
    }

    @FXML
    private void newTransaction(ActionEvent event) throws IOException {
        Stage newTransaction = new Stage();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("..//fxmls//TransactionAddingPage.fxml"))));
        newTransaction.setScene(new Scene(root));
        newTransaction.setResizable(false);

        newTransaction.show();

    }

    @FXML
    private void showCategoriesStats(ActionEvent event) throws IOException {
        final Stage statsStage = new Stage();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("..//fxmls//StatsPage.fxml"))));
        statsStage.setScene(new Scene(root));
        statsStage.setResizable(false);

        statsStage.show();
    }

    @FXML
    private void showTransactionInfo(ActionEvent event) {
        if (transactionsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        showMessageDialog(null, transactionsTableView.getSelectionModel().getSelectedItem().getDescription(), "Описание транзакции", JOptionPane.PLAIN_MESSAGE);
    }

    @FXML
    private void openContacts(ActionEvent event) throws IOException {
        final Stage contactsStage = new Stage();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("..//fxmls//ContactsPage.fxml"))));
        contactsStage.setScene(new Scene(root));
        contactsStage.setResizable(false);

        contactsStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        balance.setCellValueFactory(new PropertyValueFactory<>("currentBalance"));

        try {
            types = DataController.getTypes();
            categories = DataController.getCategories();
            banks = DataController.getBanks();

            accountsCB.setItems(DataController.getAccounts());

            transactionTypeCB.setItems(types);

            transactions = DataController.getTransactions();

            transactionsTableView.setItems(transactions);

            setMonthlySummary();
            calculateSummary();


            Assistant.homePageController = this;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DataController.initializeDataBase();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("..//fxmls//HomePage.fxml"))));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public TableView<Transaction> getTransactionsTableView() {
        return transactionsTableView;
    }

    public ComboBox<Account> getAccountsCB() {
        return accountsCB;
    }

    public void calculateSummary() {
        AtomicInteger currentTotal = new AtomicInteger();
        currentTotal.set(0);
        accountsCB.getItems().forEach(account -> {
            currentTotal.getAndAdd(account.getBalance());
        });

        balanceLabel.setText("Общий баланс: " + currentTotal + "p");
    }

    public void setMonthlySummary() throws SQLException {
        int monthlyDebit = DataController.getMonthlySummary(1);
        int monthlyCredit = DataController.getMonthlySummary(-1);

        creditLabel.setText("Потрачено за последний месяц: " + monthlyCredit + "p");
        debitLabel.setText("Заработано за последний месяц: " + monthlyDebit + "p");
    }
}
