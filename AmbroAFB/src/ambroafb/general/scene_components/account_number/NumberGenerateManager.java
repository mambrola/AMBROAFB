/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.account_number;

/**
 *
 * @author dkobuladze
 */
public interface NumberGenerateManager {
    
    public String getNumberWithKey(String accNum);
    public String getNewNumber();
    
}
