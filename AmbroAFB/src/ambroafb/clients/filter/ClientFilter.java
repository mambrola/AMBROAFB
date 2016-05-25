/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambroafb.AmbroAFB;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.Filterable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public class ClientFilter  extends Stage implements Filterable{
//    private ClientFilterController filterController;
    
    public ClientFilter(Stage owner) {
        this.initStyle(StageStyle.UTILITY);
        try {
            Scene currentScene = Utils.createScene("/ambroafb/clients/filter/ClientFilter.fxml");
//            filterController = (ClientFilterController) currentScene.getProperties().get("controller");
            this.setScene(currentScene);
        } catch (IOException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
        initOwner(owner);
        setResizable(false);
    }

    @Override
    public JSONObject getResult() {
        showAndWait();
        return null;
    }
}
