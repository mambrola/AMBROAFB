/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class CurrencyRateDataChangeProvider extends DataChangeProvider {

    private final String DB_TABLE_NAME = "rates";
    
    @Override
    public CurrencyRate deleteOneFromDB(int recId) throws Exception {
        System.out.println("delete from DB... ??");
        return null;
    }

    @Override
    public CurrencyRate editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public CurrencyRate saveOneToDB(EditorPanelable object) throws Exception {
        return saveSimple((CurrencyRate)object, DB_TABLE_NAME, true);
    }
    
}
