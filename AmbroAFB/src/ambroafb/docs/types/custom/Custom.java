/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom;

import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.SceneWithVBoxRoot;
import ambroafb.general.DataDistributor;
import javafx.scene.Node;

/**
 *
 * @author dkobuladze
 */
public class Custom extends SceneWithVBoxRoot implements DocComponent {
    
    private int recId;
    
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
        System.out.println("Custom cancel method");
    }

    @Override
    public boolean compare(DocComponent other) {
        return true;
    }

    @Override
    public Custom cloneWithoutID(DocComponent other) {
        Custom clone = new Custom();
        clone.copyFrom(other);
        return clone;
    }

    @Override
    public Custom cloneWithID(DocComponent other) {
        Custom clone = cloneWithoutID(other);
        clone.setRecId(other.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(DocComponent other) {
        
    }

}
