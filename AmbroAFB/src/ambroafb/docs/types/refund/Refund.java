/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.refund;

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
public class Refund extends SceneWithVBoxRoot implements DocComponent {

    private int recId;
    private final DocType type = new DocType(3, "Refund");
    private final DataDistributor dataDistributor = new DataDistributor();
    private final String Refund_Doc_Table = "Some Table";
    private boolean dataIsValid = true;
    
    public Refund() {
        load("/ambroafb/docs/types/refund/Refund.fxml");
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
            dataDistributor.setTableName(Refund_Doc_Table);
            try {
                dataDistributor.setData(new JSONObject("{id: 3}"));
            } catch (JSONException ex) {
                Logger.getLogger(Refund.class.getName()).log(Level.SEVERE, null, ex);
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
        Refund otherRefund = (Refund)other;
        return true;
    }

    @Override
    public Refund cloneWithoutID() {
        Refund clone = new Refund();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Refund cloneWithID() {
        Refund clone = cloneWithoutID();
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
