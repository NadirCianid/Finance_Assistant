package entities;

import controllers.DataController;

import java.sql.SQLException;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Account {
    public Account(int id, String bankName, String name, int balance) {
        this.id = id;
        this.bankName = bankName;
        this.name = name;
        this.balance = balance;
    }
    private int id;
    private String name;
    private String bankName;
    private int balance;

    public boolean use(int amount, int typeID) throws SQLException {
        int transactionModifier = DataController.getTransactionModifier(typeID);
        if(balance + transactionModifier * amount < 0) {
            showMessageDialog(null,"Такая транзакция приведет \n " +
                    "к отрицательному балансу. \n " +
                    "Проверьте числа.", "Ошибка ввода", INFORMATION_MESSAGE);
            return false;
        }
        balance = balance + transactionModifier * amount;
        DataController.updateBalance(id, balance);
        return true;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return name + " | " + bankName + " | " + balance + "р";
    }
}
