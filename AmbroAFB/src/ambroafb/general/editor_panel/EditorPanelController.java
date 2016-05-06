/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambro.ATable;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.clients.dialog.ClientDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class EditorPanelController implements Initializable {

    @FXML
    private Button exit, delete, edit, view;

    @FXML
    private TableView table;
       

    @FXML
    private void edit(ActionEvent e) {
        System.out.println("clas names: " + getClassName("") + ":" + getClassName("dialogClass"));
        
        

//        Client editingClient = table.getSelectionModel().getSelectedItem();
//        Client real = Client.getClient(editingClient.clientId);
//        if (real != null) {
//            editingClient.copyFrom(real);
//        }
//        Client backup = editingClient.cloneWithID();
//
//        ClientDialog dialog = new ClientDialog(editingClient);
//        Client editedClient = dialog.getResult();
//        if (editedClient == null) {
//            editingClient.copyFrom(backup);
//            System.out.println("dialog is cancelled");
//        } else {
//            System.out.println("changed client: " + editedClient);
//            System.out.println("phones = " + editedClient.getPhoneNumbers());
//        }
    }

    
       
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    
    private String getClassName(String type){
        String rtrn = (String)exit.getScene().getProperties().get("controller").toString();
        String path = rtrn.substring(0, rtrn.lastIndexOf(".") + 1);
        String className = rtrn.substring(path.length(), rtrn.lastIndexOf("Controller"));
        System.out.println("<-------> " + rtrn + " : " + path + " : " + className);
        switch (className){
            case "Countries":
                rtrn = path + (type.equals("dialogClass") ? "dialog." : "") + "Country" + (type.equals("dialogClass") ? "Dialog" : "");
                break;
            default:
                rtrn = path + (type.equals("dialogClass") ? "dialog." : "") + className.substring(0, className.length() - 1) + (type.equals("dialogClass") ? "Dialog" : "");
        }
        return rtrn;
    }
}
