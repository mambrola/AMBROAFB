/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.car_fines;

import javafx.animation.AnimationTimer;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.MultiSceneStage;
import ambroafb.general.Utils;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class CarFinesController implements Initializable {

    @FXML
    private ToggleButton enter;

    @FXML
    private ListView<String> list;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list.setEditable(true);
        list.setCellFactory(TextFieldListCell.forListView());
        list.setOnEditCommit((ListView.EditEvent<String> t) -> {
            list.getItems().set(t.getIndex(), t.getNewValue());

            int nextIndex = t.getIndex() + 1;
            if (nextIndex == list.getItems().size()) {
                list.getItems().add("");
            }
            editListItem(nextIndex);
        });
        list.getItems().add("");
        editListItem(0);
    }

    private void editListItem(int nextIndex) {
        new AnimationTimer() {
            int count = 0;

            @Override
            public void handle(long now) {
                count++;
                if (count > 1) {
                    list.scrollTo(nextIndex);
                    list.getSelectionModel().select(nextIndex);
                    list.getFocusModel().focus(nextIndex);
                    list.edit(nextIndex);
                    stop();
                }
            }
        }.start();
    }

    @FXML
    private void enter(ActionEvent event) {
        enter.setDisable(true);
        new Thread(() -> {
            MultiSceneStage controller = (MultiSceneStage) enter.getScene().getWindow();
            ArrayList<String> numbers = new ArrayList<>();
            String carNumbers = "";
            list.getItems().stream().forEach((item) -> {
                numbers.add(item);
            });
            for (String dt : numbers) {
                carNumbers += (carNumbers.length() > 0 ? "," : "");
                carNumbers += dt;
            }

            System.out.println("carNumbers: " + carNumbers);

            ArrayList<CarAllData> data = new ArrayList<>();
            try (Connection conn = GeneralConfig.getInstance().getConnectionToDB();
                    CallableStatement cstmt = conn.prepareCall("{call [BANK2000].[dbo].JS_MBC_FX_CAR_FINES (?)}")) {

                cstmt.setString(1, carNumbers);
                Boolean bol = cstmt.execute();
                ResultSet resultSet;
                while (!bol) {
                    bol = cstmt.getMoreResults();
                }
                resultSet = cstmt.getResultSet();

                while (resultSet.next()) {
                    data.add(new CarAllData(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getString(4),
                            resultSet.getInt(5),
                            resultSet.getString(6),
                            resultSet.getInt(7),
                            resultSet.getInt(8)
                    ));
                }
                cstmt.close();
            } catch (SQLException | NullPointerException ex) {
                Platform.runLater(() -> {
                    new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR).showAlert();
                });
            }
            GeneralConfig.getInstance().setAttribute("car_data", data);
            Platform.runLater(() -> {
                enter.setDisable(false);
                enter.setSelected(false);
                try {
                    Scene newScene = Utils.createScene(Names.CAR_FINS_SECOND_FXML);
                    controller.addScene(newScene);
                } catch (IOException ex) {
                    AlertMessage alertMessage = new AlertMessage(Alert.AlertType.ERROR, ex, Names.ERROR_BUTTON_CLICK);
                    alertMessage.showAlert();
                }
            });
        }).start();
    }
}
