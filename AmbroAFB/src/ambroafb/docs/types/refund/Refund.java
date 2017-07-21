/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.refund;

import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.SceneWithVBoxRoot;
import ambroafb.general.DataDistributor;
import javafx.scene.Node;

/**
 *
 * @author dkobuladze
 */
public class Refund extends SceneWithVBoxRoot implements DocComponent {

    private int recId;
    
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
    public DataDistributor getResult() {
        return null;
    }

    @Override
    public void cancel() {
        System.out.println("Refund cancel method");
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
        return true;
    }

    @Override
    public Refund cloneWithoutID(DocComponent other) {
        Refund clone = new Refund();
        clone.copyFrom(other);
        return clone;
    }

    @Override
    public Refund cloneWithID(DocComponent other) {
        Refund clone = cloneWithoutID(other);
        clone.setRecId(other.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(DocComponent other) {

    }
    
    
}
