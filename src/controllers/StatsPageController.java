package controllers;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static entities.Category.*;

public class StatsPageController extends Application implements Initializable {

    @FXML
    private PieChart categoriesStatsPieChart;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("fxmls/StatsPage.fxml"))));
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieChartData = null;
        try {
            pieChartData = DataController.getCategoriesStat();
            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(data.getName(), " : ", data.pieValueProperty(), "р"))
            );

            categoriesStatsPieChart.getData().addAll(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
