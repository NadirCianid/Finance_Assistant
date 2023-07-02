package controllers;

import Staff.Assistant;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataController {
    private static ResultSet resultSet;
    private static PreparedStatement addContactPS;
    private static PreparedStatement getContactsPS;
    private static PreparedStatement getContactByID_PS;
    private static PreparedStatement getContactPS;

    private static PreparedStatement addAccountPS;
    private static PreparedStatement getAccountsPS;
    private static PreparedStatement getAccountByID_PS;
    private static PreparedStatement getAccountPS;
    private static PreparedStatement updateAccountBalancePS;

    private static PreparedStatement getTypePS;
    private static PreparedStatement getTypesPS;

    private static PreparedStatement getCategoryPS;
    private static PreparedStatement getCategoriesPS;
    private static PreparedStatement getCategoriesStatsPS;

    private static PreparedStatement getTransactionsPS;
    private static PreparedStatement getTransactionsByAccountPS;
    private static PreparedStatement addTransactionPS;
    private static PreparedStatement getTransactionPS;
    private static PreparedStatement getTransactionModifierPS;

    private static PreparedStatement getRolesPS;
    private static PreparedStatement getBankNamesPS;

    private static PreparedStatement getMonthlySummaryPS;

    public static void initializeDataBase() throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/buhgalter?&serverTimezone=UTC";
        String user = "root";
        String password = "root";
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);

            addContactPS = conn.prepareStatement("insert into contacts  (first_name, last_name, role_id, phone_number)\n" +
                    "values (?,?, (select role_id from roles where name = ?), ?)");
            getContactsPS = conn.prepareStatement("select contact_id, first_name, last_name, roles.name, phone_number from contacts natural join roles");
            getContactPS = conn.prepareStatement("select  contact_id from contacts\n" +
                    "where first_name = ? and last_name = ? and role_id = (select role_id from roles where name = ? ) and phone_number = ?");
            getContactByID_PS = conn.prepareStatement("select contact_id, first_name, last_name, roles.name, phone_number from contacts natural join roles where contact_id = ?");

            addAccountPS = conn.prepareStatement("insert into accounts (bank_id, account_name, balance)\n" +
                    "values ((select bank_id from banks where name = ?), ?, ?)");
            getAccountsPS = conn.prepareStatement("SELECT account_id, banks.name, account_name, balance FROM accounts natural join banks");
            getAccountByID_PS = conn.prepareStatement("SELECT account_id, banks.name, account_name, balance FROM accounts natural join banks where account_id = ?");
            getAccountPS = conn.prepareStatement("select account_id from accounts \n" +
                    "where bank_id = (select bank_id from banks where name = ?) and account_name = ?");
            updateAccountBalancePS = conn.prepareStatement("update accounts set balance = ? where account_id = ?");

            getTypePS = conn.prepareStatement("select * from transaction_types where type_id = ?");
            getTypesPS = conn.prepareStatement("select * from transaction_types");

            getCategoryPS = conn.prepareStatement("select * from operation_categories where category_id = ?");
            getCategoriesPS = conn.prepareStatement("select * from operation_categories");
            getCategoriesStatsPS = conn.prepareStatement("select sum(size), category_name from transactions  join operation_categories on transactions.category_id = operation_categories.category_id\n" +
                    "where TIMESTAMPDIFF(MONTH, date, sysdate()) <= 1 group by category_name");

            getTransactionsPS = conn.prepareStatement("select transaction_id,\n" +
                    "\t   type_id,\n" +
                    "       category_id,\n" +
                    "       size,\n" +
                    "       date,\n" +
                    "       contact_id,\n" +
                    "       description,\n" +
                    "       account_id,\n" +
                    "       current_balance\n" +
                    "from transactions");
            getTransactionsByAccountPS = conn.prepareStatement("select * from transactions \n" +
                    "where account_id = ?");
            addTransactionPS = conn.prepareStatement("insert into transactions (type_id,\n" +
                    "       category_id,\n" +
                    "       size,\n" +
                    "       date,\n" +
                    "       contact_id,\n" +
                    "       description,\n" +
                    "       account_id,\n" +
                    "       current_balance) \n" +
                    "values (?, ?, ?,? ,? ,? , ?, ?)");
            getTransactionPS = conn.prepareStatement("select transaction_id from transactions\n" +
                    "where type_id = ? and category_id = ? and size = ? and date = ? and contact_id = ? and account_id = ?");
            getTransactionModifierPS = conn.prepareStatement("select transaction_modifier from transaction_types\n" +
                    "where type_id = ?");

            getRolesPS = conn.prepareStatement("select name from roles");
            getBankNamesPS = conn.prepareStatement("select name from banks");

            getMonthlySummaryPS = conn.prepareStatement("select sum(size),  transaction_types.transaction_modifier from transactions join transaction_types on transactions.type_id = transaction_types.type_id\n" +
                    "where timestampdiff(day, date, sysdate()) <=31 and  transaction_types.transaction_modifier = ?\n" +
                    "group by transaction_types.transaction_modifier ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Contact addContact(String firstName, String lastName, String roleName, String phoneNumber) throws SQLException {
        addContactPS.setString(1, firstName);
        addContactPS.setString(2, lastName);
        addContactPS.setString(3, roleName);
        addContactPS.setString(4,phoneNumber);

        addContactPS.execute();


        getContactPS.setString(1, firstName);
        getContactPS.setString(2, lastName);
        getContactPS.setString(3, roleName);
        getContactPS.setString(4, phoneNumber);

        resultSet = getContactPS.executeQuery();
        resultSet.next();

        return new Contact(resultSet.getInt(1),
                firstName,
                lastName,
                roleName,
                phoneNumber);
    }

    public static Contact getContactByID(int contactID) throws SQLException {
        getContactByID_PS.setInt(1, contactID);
        resultSet = getContactByID_PS.executeQuery();
        resultSet.next();

        return new Contact(resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5));
    }

    public static ObservableList<Contact> getCurrentContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        resultSet = getContactsPS.executeQuery();

        while (resultSet.next()) {
            contacts.add(new Contact(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)));
        }
        return contacts;
    }

    public static List<String> getRoles() throws SQLException {
        resultSet = getRolesPS.executeQuery();
        List<String> roles = new ArrayList<>();

        while (resultSet.next()) {
            roles.add(resultSet.getString(1));
        }

        return roles;
    }

    public static ObservableList<Account> getAccounts() throws SQLException {
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        resultSet = getAccountsPS.executeQuery();

        while (resultSet.next()) {
            accounts.add(new Account(resultSet.getInt(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getInt(4)));
        }

        return accounts;
    }

    public static Account getAccount(int accountID) throws SQLException {
        getAccountByID_PS.setInt(1, accountID);
        resultSet = getAccountByID_PS.executeQuery();
        resultSet.next();

        return new Account(resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getInt(4));
    }

    public static Account addAccount(String bankName, String accountName, int balance) throws SQLException {
        addAccountPS.setString(1, bankName);
        addAccountPS.setString(2, accountName);
        addAccountPS.setInt(3, balance);

        addAccountPS.execute();

        getAccountPS.setString(1, bankName);
        getAccountPS.setString(2, accountName);

        resultSet = getAccountsPS.executeQuery();
        resultSet.next();

        return new Account(resultSet.getInt(1), bankName, accountName, balance);
    }

    public static void updateBalance(int accountID, int balance) throws SQLException {
        updateAccountBalancePS.setInt(1, balance);
        updateAccountBalancePS.setInt(2, accountID);

        updateAccountBalancePS.execute();
        Assistant.homePageController.getAccountsCB().setItems(getAccounts());
        Assistant.homePageController.calculateSummary();
        Assistant.homePageController.setMonthlySummary();
    }


    public static Type getType(int typeID) throws SQLException {
        getTypePS.setInt(1, typeID);
        resultSet = getTypePS.executeQuery();
        resultSet.next();

        return new Type(resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3));
    }

    public static ObservableList<Type> getTypes() throws SQLException {
        resultSet = getTypesPS.executeQuery();
        ObservableList<Type> types = FXCollections.observableArrayList();

        while (resultSet.next()) {
            types.add(new Type(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3)));
        }

        return types;
    }

    public static Category getCategory(int categoryID) throws SQLException {
        getCategoryPS.setInt(1, categoryID);
        resultSet = getCategoryPS.executeQuery();
        resultSet.next();

        return new Category(resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3));
    }
    public static List<Category> getCategories() throws SQLException {
        resultSet = getCategoriesPS.executeQuery();
        List<Category> categories = new ArrayList<>();

        while (resultSet.next()) {
            categories.add(new Category(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3)));
        }

        return categories;
    }

    public static ObservableList<PieChart.Data> getCategoriesStat() throws SQLException {
        resultSet =  getCategoriesStatsPS.executeQuery();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        while (resultSet.next()) {
            pieChartData.add(new PieChart.Data(resultSet.getString(2), resultSet.getInt(1)));
        }

        return pieChartData;
    }

    public static ObservableList<Transaction> getTransactions() throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        ResultSet resultSet = getTransactionsPS.executeQuery();


        while (resultSet.next()) {

            int transactionID = resultSet.getInt(1);
            int typeID = resultSet.getInt(2);
            int categoryID = resultSet.getInt(3);
            int size = resultSet.getInt(4);
            LocalDate date = resultSet.getDate(5).toLocalDate();
            LocalTime time = resultSet.getTime(5).toLocalTime();
            int contactID = resultSet.getInt(6);
            String description = resultSet.getString(7);
            int accountID = resultSet.getInt(8);
            int balance = resultSet.getInt(9);


            transactions.add(new Transaction(transactionID,
                                            getType(typeID),
                                            getCategory(categoryID),
                                            size,
                                            date,
                                            time,
                                            getContactByID(contactID),
                                            description,
                                            getAccount(accountID),
                                            balance));

        }

        return transactions;
    }

    public static ObservableList<Transaction> getTransactionsByAccount(Account account) throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        getTransactionsByAccountPS.setInt(1, account.getId());
        ResultSet resultSet = getTransactionsPS.executeQuery();

        while (resultSet.next()) {
            int transactionID = resultSet.getInt(1);
            int typeID = resultSet.getInt(2);
            int categoryID = resultSet.getInt(3);
            int size = resultSet.getInt(4);
            LocalDate date = resultSet.getDate(5).toLocalDate();
            LocalTime time = resultSet.getTime(5).toLocalTime();
            int contactID = resultSet.getInt(6);
            String description = resultSet.getString(7);
            int accountID = resultSet.getInt(8);
            int balance = resultSet.getInt(9);


            transactions.add(new Transaction(transactionID,
                    getType(typeID),
                    getCategory(categoryID),
                    size,
                    date,
                    time,
                    getContactByID(contactID),
                    description,
                    getAccount(accountID),
                    balance));

        }

        return transactions;
    }

    public static Transaction addTransaction(Type type, Category category, String size, LocalDate date, String time, Contact contact, String description, Account account) throws SQLException {
        addTransactionPS.setInt(1, type.getTypeID());
        addTransactionPS.setInt(2, category.getCategoryID());
        addTransactionPS.setInt(3, Integer.parseInt(size));
        addTransactionPS.setString(4, date + " " + time);
        addTransactionPS.setInt(5, contact.getContactID());
        addTransactionPS.setString(6, description);
        addTransactionPS.setInt(7, account.getId());
        addTransactionPS.setInt(8, account.getBalance());

        addTransactionPS.execute();


        getTransactionPS.setInt(1, type.getTypeID());
        getTransactionPS.setInt(2, category.getCategoryID());
        getTransactionPS.setInt(3, Integer.parseInt(size));
        getTransactionPS.setString(4, date + " " + time);
        getTransactionPS.setInt(5, contact.getContactID());
        getTransactionPS.setInt(6, account.getId());

        resultSet = getTransactionPS.executeQuery();
        resultSet.next();

        return new Transaction(resultSet.getInt(1),
                type,
                category,
                Integer.parseInt(size),
                date,
                LocalTime.parse(time),
                contact,
                description,
                account,
                account.getBalance());
    }


    public static int getTransactionModifier(int typeID) throws SQLException {
        getTransactionModifierPS.setInt(1, typeID);

        resultSet = getTransactionModifierPS.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
    }

    public static int getMonthlySummary(int transactionModifier) throws SQLException {
        getMonthlySummaryPS.setInt(1, transactionModifier);
        resultSet =  getMonthlySummaryPS.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
    }


    public static List<String> getBanks() throws SQLException {
        List<String> bankNames = new ArrayList<>();
        resultSet = getBankNamesPS.executeQuery();

        while (resultSet.next()) {
            bankNames.add(resultSet.getString(1));
        }
        return bankNames;
    }
}
