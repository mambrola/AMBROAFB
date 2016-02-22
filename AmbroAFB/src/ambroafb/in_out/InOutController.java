/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.in_out;

import ambro.ATreeTableView;
import ambroafb.AmbroAFB;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class InOutController implements Initializable {

    @FXML
    private DatePicker beginDate, endDate;
    
    private final String currency = "USD";
    @FXML
    private Slider expand;
    @FXML
    private ToggleButton enter;
    @FXML
    private ATreeTableView<InOutLine> treeTable;

    private double oldSliderValue = 2.0;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInitialValuesOfParameters();
        treeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeTable.setVisible(false);
        treeTable.setBundle(rb);
        expand.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != oldSliderValue) {
                if ((double) newValue.intValue() == observable.getValue().doubleValue()) {
                    System.out.println("Slider Value Changed (Values: " + oldSliderValue + " : " + newValue.doubleValue() + ") " + observable.getValue().doubleValue());
                    treeTable.expand(newValue.intValue());
                    oldSliderValue = Math.round(newValue.doubleValue());
                }
            }
        });
    }

    @FXML
    private void copy(ActionEvent e) {
        System.out.println("1");
        ObservableList<TreeItem<InOutLine>> treeItems = treeTable.getSelectionModel().getSelectedItems();
        System.out.println("treeItems.size: " + treeItems.size());
        StringBuilder clipboardString = new StringBuilder();
        for (TreeItem ti : treeItems) {
            System.out.println("r : s  - " + ti + " : " + ((InOutLine) ti.getValue()).descrip);
            clipboardString.append(((InOutLine) ti.getValue()).descrip);
            clipboardString.append('\t');
            clipboardString.append(((InOutLine) ti.getValue()).amount);
            clipboardString.append('\n');
        }
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     *
     * @param e
     */
    @FXML
    private void enter(ActionEvent e) {
        enter.setDisable(true);
        new Thread(() -> {
            Map<Double, Double> subs = new HashMap<>();
            Map<Double, InOutLine> inouts = new HashMap<>();
            ArrayList<InOutLine> roots = new ArrayList<>();
            try {
                Connection conn = GeneralConfig.getInstance().getConnectionToDB();
                try (CallableStatement cstmt = conn.prepareCall("{call [BANK2000].[dbo].JS_MBC_FX_INOUT (?,?,?)}")) {
                    cstmt.setDate(1, Date.valueOf(beginDate.getValue()));
                    cstmt.setDate(2, Date.valueOf(endDate.getValue()));
                    cstmt.setString(3, currency);
                    Boolean bol = cstmt.execute();
                    ResultSet resultSet;
                    while (!bol) {
                        bol = cstmt.getMoreResults();
                    }
                    resultSet = cstmt.getResultSet();
                    while (resultSet.next()) {
                        subs.put(resultSet.getDouble(2), resultSet.getDouble(1));
                    }
                    do {
                        bol = cstmt.getMoreResults();
                    } while (!bol);
                    resultSet = cstmt.getResultSet();
                    while (resultSet.next()) {
                        InOutLine inOutLine = new InOutLine(resultSet.getDouble(1), resultSet.getString(2), resultSet.getDouble(3));
                        inOutLine.styles.add("font" + resultSet.getInt(4) + "Size");
                        if (subs.containsKey(inOutLine.fieldNo)) {
                            inouts.get(subs.get(inOutLine.fieldNo)).children.add(inOutLine);
                        } else {
                            roots.add(inOutLine);
                        }
                        inouts.put(inOutLine.fieldNo, inOutLine);
                    }

                    cstmt.close();
                }
            } catch (SQLException | NullPointerException ex) {
                Platform.runLater(() -> {
                    new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR).showAlert();
                });
            }
            Platform.runLater(() -> {
                treeTable.removeAll();
                roots.stream().forEach((item) -> {
                    treeTable.append(item);
                });
                treeTable.expand((int) expand.getValue());
                treeTable.setVisible(true);
                enter.setDisable(false);
                enter.setSelected(false);
                ((ImageView)enter.getGraphic()).setImage(new Image(Names.IMAGE_REFRESH));
                enter.getTooltip().setText(GeneralConfig.getInstance().getTitleFor(Names.TOOLTIP_REFRESH));
            });
        }).start();

    }

    /**
     * აყენებს ნაგულისხმევ თარიღებსა და ვალუტას. თუ 10 რიცხვზე ნაკლებია, მაშინ
     * წინა თვეს სრულად, თუ არა მაშინ მიმდინარე თვეს დღემდე. ვალუტა USD.
     */
    private void setInitialValuesOfParameters() {
        LocalDate cDate = LocalDate.now();
        LocalDate bDate = LocalDate.of(cDate.getYear(), cDate.getMonth(), 1).plusDays(-1);
        LocalDate eDate = cDate;
        if (cDate.getDayOfMonth() < 10) {
            eDate = bDate;
            bDate = bDate.plusDays(1).plusMonths(-1).plusDays(-1);
        }
        beginDate.setValue(bDate);
        endDate.setValue(eDate);
    }

}
