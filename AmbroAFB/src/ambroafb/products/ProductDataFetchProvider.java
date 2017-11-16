/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.products.helpers.ProductSpecific;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ProductDataFetchProvider extends DataFetchProvider {

    private final String DB_SPECIFIC_TABLE_NAME = "product_specific_descrips";
    
    public ProductDataFetchProvider(){
        DB_VIEW_NAME = "products_whole";
    }
    
    @Override
    public List<Product> getFilteredBy(JSONObject params) throws Exception {
        List<Product> products = getObjectsListFromDBTable(Product.class, DB_VIEW_NAME, params);
        products.sort((Product p1, Product p2) -> p1.compareById(p2));
        return products;
    }

    @Override
    public List<Product> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Product.class, DB_VIEW_NAME, params);
    }
    
    public List<ProductSpecific> getAllSpecificsFromDB() throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        ConditionBuilder condition = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition();
        JSONObject params = condition.build();
        return getObjectsListFromDBTable(ProductSpecific.class, DB_SPECIFIC_TABLE_NAME, params);
    }
    
}
