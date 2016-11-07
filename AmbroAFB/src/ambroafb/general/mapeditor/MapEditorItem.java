/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.mapeditor;

import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author dato
 */
public class MapEditorItem extends HBox {
    
    @FXML
    private Label itemDescrip;
    @FXML
    private Button delete;
    @FXML
    private Button edit;
    
    private static final String deleteAlertText = GeneralConfig.getInstance().getTitleFor("map_editor_delete_alert_text");
    private static final String editAlertText = GeneralConfig.getInstance().getTitleFor("map_editor_edit_alert_text");
    
    public MapEditorItem(MapEditorElement elem, String delimiter, Consumer<MapEditorElement> removable, Consumer<MapEditorElement> editable){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ambroafb/general/mapeditor/MapEditorItem.fxml"));
        assignLoader(loader);
        
        itemDescrip.setText(elem.getKey() + delimiter + elem.getValue());
        delete.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(deleteAlertText.replace("#", elem.getKey()));
            Optional<ButtonType> buttonType = alert.showAndWait();
            ButtonType type = buttonType.get();
            if (type.equals(ButtonType.OK)){
                removable.accept(elem);
            }
        });
        edit.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(editAlertText.replace("#", elem.getKey()));
            Optional<ButtonType> buttonType = alert.showAndWait();
            ButtonType type = buttonType.get();
            if (type.equals(ButtonType.OK)){
                editable.accept(elem);
            }
        });
    }

    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MapEditorItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
