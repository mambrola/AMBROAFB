/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.DocType;
import ambroafb.general.DataDistributor;
import javafx.scene.Node;

/**
 *
 * @author dkobuladze
 */
public interface DocComponent {
    
    public int getRecId();
    public void setRecId(int id);
    
    public Node getSceneNode();
    public DataDistributor getDocData();
    public void discardData();
    public boolean compare(DocComponent other);
    public DocComponent cloneWithoutID();
    public DocComponent cloneWithID();
    public void copyFrom(DocComponent other);
    
    public DocType getType();
}
