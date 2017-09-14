/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.types.utilities.charge.ChargeUtilityManager;
import ambroafb.docs.types.utilities.payment.PaymentUtilityManager;

/**
 *
 * @author dkobuladze
 */
public class DocManagersFactory {
    
    /**
     * The method returns specific DocManager by docType.
     * @param docType
     * @return 
     */
    public static DocManager getDocManager(int docType){
        switch(docType){
            case 82:
                return new PaymentUtilityManager();
            case 12:
                return new ChargeUtilityManager();
            default:
                return null;
        }
    }
    
}
