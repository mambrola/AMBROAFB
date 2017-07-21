/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DataDistributor {
    
    private String tableName;
    private JSONObject data;
    
    public DataDistributor(){
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataDistributor{" + "tableName=" + tableName + ", data=" + data + '}';
    }
    
    
}
