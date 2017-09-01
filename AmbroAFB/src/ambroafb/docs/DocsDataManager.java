/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.docs.types.DocComponent;

/**
 * The abstraction that provides to save and retrieve data from data storage.
 */
public interface DocsDataManager {
    
    /**
     *  According to docType, the method returns appropriate DocComponent.
     * @param flag The digit consider Doc Component scene type (Utility, Monthly, Refund, Custom ...).
     * @return Appropriate DocComponent on flag.
     */
    public DocComponent getDocComponentBy(int flag);
}
