/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.types.custom.Custom;
import ambroafb.docs.types.monthly.Monthly;
import ambroafb.docs.types.refund.Refund;

/**
 *
 * @author dkobuladze
 */
public class DocDialogsFactory {
    
    public static DocComponent getDocTypeComponent(int typeLastDigit){
        switch (typeLastDigit) {
            case 4:
                return new Monthly();
            case 8:
                return new Refund();
            default:
                return new Custom();
        }
    }
}
