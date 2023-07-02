package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Transaction {
    private  int transactionID;
    private final Type type;
    private Category category;
    private int size;
    private final LocalDate date;
    private LocalTime time;
    private Contact contact;
    private String description;
    private Account account;
    private int currentBalance;

    public Transaction(int transactionID, Type type, Category category, int size, LocalDate date,LocalTime time, Contact contact, String description, Account account, int currentBalance) {
        this.transactionID = transactionID;
        this.type = type;
        this.category = category;
        this.size = size;
        this.date = date;
        this.time = time;
        this.contact = contact;
        this.description = description;
        this.account = account;
        this.currentBalance = currentBalance;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public Type getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public int getSize() {
        return size;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Contact getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public Account getAccount() {
        return account;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }
}
