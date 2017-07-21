/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.types.custom.Custom;
import ambroafb.docs.types.monthly.Monthly;
import ambroafb.docs.types.refund.Refund;
import ambroafb.docs.types.utilities.Utility;

/**
 *
 * @author dkobuladze
 */
public class DocDialogsFactory {
    
    public static DocComponent getDocTypeComponent(int id){
        switch (id) {
            case 1:
                return new Custom();
            case 2:
                return new Monthly();
            case 3:
                return new Refund();
            case 4:
                return new Utility();
            default:
                break;
        }
        return null;
    }
}
