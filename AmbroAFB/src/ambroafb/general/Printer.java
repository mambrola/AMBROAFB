/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

/**
 *
 * @author dkobuladze
 */
public class Printer {
    
    public static void printInfo(String className, String methodName, String params){
        System.out.println(String.format("<< %s >> -- %s (%s)", className, methodName, params));
    }
    
    public static void printIsNull(String objectName, Object object){
        System.out.println(String.format("--------------- object: %s, %s", objectName, (object == null) ? "is null" : "is not null"));
    }
}
