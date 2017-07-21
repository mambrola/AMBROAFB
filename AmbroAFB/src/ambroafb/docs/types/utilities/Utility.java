/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities;

import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.SceneWithVBoxRoot;
import ambroafb.general.DataDistributor;
import javafx.scene.Node;

/**
 *
 * @author dkobuladze
 */
public class Utility extends SceneWithVBoxRoot implements DocComponent {

    private int recId;
    
    public Utility(){
        load("/ambroafb/docs/types/utilities/Utility.fxml");
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
        System.out.println("Utilities cancel method"); 
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
    public Utility cloneWithoutID(DocComponent other) {
        Utility clone = new Utility();
        clone.copyFrom(other);
        return clone;
    }

    @Override
    public Utility cloneWithID(DocComponent other) {
        Utility clone = cloneWithoutID(other);
        clone.setRecId(other.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(DocComponent other) {

    }
    
}
