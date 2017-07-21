/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom;

import ambroafb.docs.DocType;
import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.SceneWithVBoxRoot;
import ambroafb.general.DataDistributor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Custom extends SceneWithVBoxRoot implements DocComponent {
    
    private int recId;
    private final DocType type = new DocType(1, "Custom");
    private final DataDistributor dataDistributor = new DataDistributor();
    private final String Custom_Doc_Table = "Some Table";
    private boolean dataIsValid = true;
    
    public Custom(){
        load("/ambroafb/docs/types/custom/Custom.fxml");
    }
    
    private void load(String fxmlPath){
        super.assignLoader(fxmlPath, this);
    }

    @Override
    public int getRecId() {
        return recId;
    }

    @Override
    public void setRecId(int id) {
        recId = id;
    }
    
    
    
    @Override @JsonIgnore
    public Node getSceneNode() {
        return this;
    }

    @Override @JsonIgnore
    public DataDistributor getDocData() {
        if (dataIsValid){
            dataDistributor.setTableName(Custom_Doc_Table);
            try {
                dataDistributor.setData(new JSONObject("{id: 1}"));
            } catch (JSONException ex) {
                Logger.getLogger(Custom.class.getName()).log(Level.SEVERE, null, ex);
            }
            return dataDistributor;
        }
        return null;
    }

    @Override
    public void discardData() {
        dataIsValid = false;
    }

    @Override @JsonIgnore
    public boolean compare(DocComponent other) {
        Custom otherCustom = (Custom) other;
        return true;
    }

    @Override @JsonIgnore
    public Custom cloneWithoutID() {
        Custom clone = new Custom();
        clone.copyFrom(this);
        return clone;
    }

    @Override @JsonIgnore
    public Custom cloneWithID() {
        Custom clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(DocComponent other) {
        // ar unda id-is copy  aq.......
    }

    @Override @JsonIgnore
    public DocType getType() {
        return type;
    }

}
