/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.DocType;
import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.SceneWithVBoxRoot;
import ambroafb.general.DataDistributor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Monthly extends SceneWithVBoxRoot implements DocComponent {

    private int recId;
    private final DocType type = new DocType(2, "Monthly");
    private final DataDistributor dataDistributor = new DataDistributor();
    private final String Monthly_Doc_Table = "Some Table";
    private boolean dataIsValid = true;
    
    public Monthly() {
        load("/ambroafb/docs/types/monthly/Monthly.fxml");
    }
    
    private void load(String fxmlPath){
        super.assignLoader(fxmlPath, this);
    }

    @Override
    public Node getSceneNode() {
        return this;
    }

    @Override
    public DataDistributor getDocData() {
        if (dataIsValid){
            dataDistributor.setTableName(Monthly_Doc_Table);
            try {
                dataDistributor.setData(new JSONObject("{id: 2}"));
            } catch (JSONException ex) {
                Logger.getLogger(Monthly.class.getName()).log(Level.SEVERE, null, ex);
            }
            return dataDistributor;
        }
        return null;
    }

    @Override
    public void discardData() {
        dataIsValid = false;
    }
    
    @Override
    public int getRecId() {
        return recId;
    }

    @Override
    public void setRecId(int id) {
        recId = id;
    }

    @Override
    public boolean compare(DocComponent other) {
        Monthly otherMonthly = (Monthly)other;
        return true;
    }

    @Override
    public Monthly cloneWithoutID() {
        Monthly clone = new Monthly();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Monthly cloneWithID() {
        Monthly clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(DocComponent other) {
        
    }

    @Override
    public DocType getType() {
        return type;
    }
    
}
