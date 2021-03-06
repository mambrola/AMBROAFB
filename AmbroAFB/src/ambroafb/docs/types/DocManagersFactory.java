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
import ambroafb.general.interfaces.EditorPanelableManager;

/**
 *
 * @author dkobuladze
 */
public class DocManagersFactory {
    
    public static EditorPanelableManager getEPManager(Doc doc){
        if (doc.isChildDoc()){
            return new CustomManager();
        }
        else if (doc.isParentDoc()){
            switch (doc.getDocType()) {
                case 12:
                    return new ChargeUtilityManager();
                case 60:
                    return new ConversionManager();
                default:
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
