/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.doc_editor_panel;

import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.DocDialogsFactory;
import ambroafb.general.interfaces.DocsDataManager;

/**
 *
 * @author dkobuladze
 */
public class DBDocsDataManager implements DocsDataManager {

    @Override
    public DocComponent getDocComponentBy(int id) {
        return DocDialogsFactory.getDocTypeComponent(id);
    }
    
}
