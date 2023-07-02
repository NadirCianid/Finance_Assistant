package controllers;

import controllers.DataController;
import javafx.fxml.FXMLLoader;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class test {
    public static void main(String[] args) throws SQLException, IOException {
        DataController.initializeDataBase();
        System.out.println() ;
    }
}
