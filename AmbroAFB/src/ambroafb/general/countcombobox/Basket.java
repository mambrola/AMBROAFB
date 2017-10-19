/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author dkobuladze
 */
public class Basket {
    
    private Map<String, Integer> products = new HashMap<>();
    
    public Basket(){
    
    }
    
    /**
     *  The method adds objects quantity to basket by its unique identifier.
     * @param uniqueId The unique identifier of object.
     * @param count The quantity of object.
     */
    public void add(String uniqueId, int count){
        products.put(uniqueId, count);
    }
    
    /**
     *  The method removes objects from basket by unique identifier.
     * @param uniqueId The unique identifier of object.
     */
    public void remove(String uniqueId){
        products.remove(uniqueId);
    }
    
    /**
     *  The method allows to iterate basket objects unique identifiers.
     * @return The iterator of unique identifiers.
     */
    public Iterator<String> getIterator(){
        return products.keySet().iterator();
    }
    
    /**
     *  The method checks basket is empty or not.
     * @return True - if basket is empty. False - otherwise.
     */
    public boolean isEmpty(){
        return products.isEmpty();
    }
    
    /**
     *  The method returns quantity of basket object by uniqueId.
     * @param uniqueId The unique identifier of object.
     * @return The quantity of uniqueId object. If this uniqueId is not in basket, returns 0.
     */
    public int getCountFor(String uniqueId){
        return getCountFor(uniqueId, 0);
    }
    
    /**
     *  The method returns quantity of basket object by uniqueId.
     * @param uniqueId The unique identifier of object.
     * @param defaultValue The default value if uniqueId is not in basket.
     * @return The quantity of uniqueId object. If this uniqueId is not in basket, returns defaultValue.
     */
    public int getCountFor(String uniqueId, int defaultValue){
        return (products.containsKey(uniqueId)) ? products.get(uniqueId) : defaultValue;
    }
    
    @Override
    public String toString(){
        String result = "";
        Iterator<String> itr = getIterator();
        while(itr.hasNext()){
            String uniqueId = itr.next();
            result += "(" + uniqueId + " : " + products.get(uniqueId) + ")" + "\n";
        }
        return result;
    }
}
