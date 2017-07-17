/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.types.custom.Custom;
import ambroafb.docs.types.utilities.Utility;

/**
 *
 * @author dkobuladze
 */
public class DocDialogsFactory {
    
    public static DocDialogAbstraction getDocDialogObject(int id){
        if (id == 1){
            return new Custom();
        }
        return new Utility();
    }
}
