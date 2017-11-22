/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.helpers.ProductDiscount;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ProductDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "product_delete";
    private final String INSERT_UPDATE_PROCEDURE = "product_insert_update";
    
    @Override
    public Product deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
        return null;
    }

    @Override
    public Product editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Product saveOneToDB(EditorPanelable object) throws Exception {
        Product product = (Product) object;
        JSONObject discountsSeparateSaving = new JSONObject();
        Integer productId = (product.getRecId() == 0) ? null : product.getRecId();
        discountsSeparateSaving.put("discounts", discountsToArray(productId, product.getDiscounts()));
        return saveObjectByProcedure(product, INSERT_UPDATE_PROCEDURE, discountsSeparateSaving);
    }
    
    // Converts discounts list to appropriate DB discounts entries json array.
    private JSONArray discountsToArray(int productId, ObservableList<ProductDiscount> discountList){
        JSONArray array = new JSONArray();
        discountList.stream().forEach((discount) -> {
            Integer discountId = (discount.getRecId() == 0) ? null : discount.getRecId();
            JSONObject discountEntry = new JSONObject();
            try {
                discountEntry.put(DB_ID, discountId);
                discountEntry.put("product_id", productId);
                discountEntry.put("days", discount.getDays());
                discountEntry.put("discount_rate", discount.getDiscountRate());
            } catch (JSONException ex) {
                Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
            }
            array.put(discountEntry);
        });
        return array;
    }
    
}
