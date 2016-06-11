/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public interface Filterable {
    public JSONObject getResult();
    public void setResult(boolean isOk);
    
    public static final String LOCAL_NAME = "/DialogOrFilter";
}
