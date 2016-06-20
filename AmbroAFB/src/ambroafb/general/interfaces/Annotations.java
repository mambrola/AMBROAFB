/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

/**
 *
 * @author dato
 */
public class Annotations {
 
    public @interface IsNotEpmty{
        boolean value() default true;
    }
    
    public @interface Pattern{
        String value();
    }
}
