/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.account_number;

import java.util.function.Consumer;

/**
 *
 * @author dkobuladze
 */
public interface NumberGenerateManager {
    
    /**
     * The method generates key for account number.
     * @param accNum The account number without key.
     * @param success The action if generated was success.
     * @param error The actin if generated was problematic.
     */
    public void generateKeyFor(Consumer<String> success, Consumer<Exception> error);
    
    
    /**
     * The method generates new account number.
     * @param success The action if generated was success.
     * @param error The action if generated was problematic.
     */
    public void generateNewNumber(Consumer<String> success, Consumer<Exception> error);
    
}
