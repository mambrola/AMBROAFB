/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

/**
 *
 * @author mambroladze
 */
public interface Filterable {
    
    /**
     * @return FilterModel that access user selected scene parameters.
     */
    public FilterModel getResult();
   
    /**
     *  The method makes action according to dialog buttons.
     * @param isOk The boolean value is true if user select OK dialog button. False, if user select  Cancel dialog button.
     */
    public void setResult(boolean isOk);
}
