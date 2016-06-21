/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


/**
 *
 * @author dato
 */
public class Annotations {
 
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentNotEmpty {
        boolean value() default true;
        String explain() default "This is required.";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentMail {
        String valueForSyntax() default ".+@.+\\..{2,}";
        String explainForSyntax() default "The incorrect syntax. Like: ex@some.some";
        
        String valueForAlphabet() default 
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String explainForAlphabet() default "Only Latin alphabet in text place.";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentPattern {
        String value();
        String explain();
    }
}
