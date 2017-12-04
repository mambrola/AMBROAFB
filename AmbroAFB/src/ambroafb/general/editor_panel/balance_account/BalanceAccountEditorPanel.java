/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.balance_account;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.editor_panel.standard.StandardEditorPanel;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class BalanceAccountEditorPanel extends StandardEditorPanel {

    @Override
    public void notify(EditorPanelable selected) {
        super.notify(selected); // make parent class actions  for buttons disable, seected saving and changes new action for delete button.
        BalanceAccount item = (BalanceAccount) selected;
        delete.setDisable(item == null || !item.isLeaf());
    }
    
    
    
}
