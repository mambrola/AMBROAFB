/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.doc_editor_panel;

import ambroafb.docs.types.DocComponent;
import ambroafb.general.interfaces.DocsDataManager;

/**
 *
 * @author dkobuladze
 */
public class DocEditorPanelModel {
    
    private DocsDataManager dataManager;
    
    public DocEditorPanelModel(DocsDataManager dm){
        dataManager = dm;
    }
    
    public DocEditorPanelModel(){
        this(new DBDocsDataManager());
    }
    
    /**
     *  According to docType, the method returns appropriate DocComponent.
     * @param docType The double-digit number that last digit consider Doc Component scene type (Utility, Monthly, Refund, Custom ...).
     * @return Appropriate DocComponent object full of data..
     */
    public DocComponent getDocComponent(int docType){
        return dataManager.getDocComponentBy(docType % 10);
    }
}
