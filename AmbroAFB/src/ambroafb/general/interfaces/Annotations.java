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
        String explainForSyntax() default "The incorrect syntax. Like: example@some.ex";
        
        String valueForAlphabet() default 
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String explainForAlphabet() default "Only Latin alphabet in text place.";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentTreeItem {
        String valueForLength() default "1";
        String explainForLength() default "The content length must be ";
        
        String valueForSyntax() default "\\w"; // [a-zA-Z_0-9]
        String explainForSyntax() default "The content must contains only digits.";
        
        String explainForHasNotParent() default "The item has not parent.";
        String explainForExists() default "This item has already exist.";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentMapEditor {
        String key() default "\\d"; // [0-9] {1}  for default
        String value() default "\\d";
        String explainKey() default "";
        String explainValue() default "";
        String explainEmpty() default "Left or right value of delimiter must not be empty.";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentPattern {
        String value() default "";
        String explain() default "";
    }
}
