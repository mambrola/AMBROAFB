/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.FilterModel;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public interface Filterable {
    public JSONObject getResult();
    public FilterModel getFilterResult();
    public void setResult(boolean isOk);
}
