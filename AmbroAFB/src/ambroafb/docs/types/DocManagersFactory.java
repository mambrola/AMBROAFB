/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.Doc;
import ambroafb.docs.types.conversion.ConversionManager;
import ambroafb.docs.types.custom.CustomManager;
import ambroafb.docs.types.monthly.MonthlyManager;
import ambroafb.docs.types.utilities.charge.ChargeUtilityManager;
import ambroafb.docs.types.utilities.payment.PaymentUtilityManager;

/**
 *
 * @author dkobuladze
 */
public class DocManagersFactory {
    
    /**
     * The method returns specific DocManager by doc object docType and parent/child status.
     * @param doc
     * @return 
     */
    public static DocManager getDocManager(Doc doc){
        if (doc.isChildDoc()){
            return new CustomManager();
        }
        else if (doc.isParentDoc()){
            if (doc.getDocType() == 12){
                return new ChargeUtilityManager();
            }
            else if (doc.getDocType() == 60){
                return new ConversionManager();
            }
            else {
                return new MonthlyManager();
            }
        }
        else { // Doc has not children
            if (doc.getDocType() == 82){
                return new PaymentUtilityManager();
            }
            else {
                return new CustomManager();
            }
        }
    }
    
}
