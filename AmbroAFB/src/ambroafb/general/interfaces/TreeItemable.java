/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import javafx.collections.ObservableList;

/**
 *
 * @author dkobuladze
 */
public interface TreeItemable {
    
    public <T> ObservableList<T> getChildren();
    public int getIdentificator();
    public int getParentIdentificator();
    
}
