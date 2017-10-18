/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * @author dato
 */
public class Annotations {
 
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentNotEmpty {
        String explain() default "anot_required_field"; // bundle key
        Class predicate() default DEFAULT.class;
        public static class DEFAULT {
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentMail {
        String valueForSyntax() default ".+@.+\\..{2,}";
        String explainForSyntax() default "anot_email_syntax_exp";
        
        String valueForAlphabet() default 
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String explainForAlphabet() default "anot_email_abc_exp";
        
        Class predicate() default DEFAULT.class;
        public static class DEFAULT {
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentTreeItem {
        String valueForLength() default "1";
        String explainForLength() default "anot_tree_item_length_exp";
        
        String valueForSyntax() default "\\w"; // [a-zA-Z_0-9]
        String explainForSyntax() default "anot_tree_item_syntax_exp";
        
        String explainForHasNotParent() default "anot_tree_item_parent_exp";
        String explainForExists() default "anot_tree_item_exists_exp";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentRate {
        static int integerPartLen = 1;
        String valueForIntegerPart() default "^[0-9]\\..*";
        String explainForIntegerPart() default "rate_field_int_part_incorrect_explain";
        
        static int fractionalPartLen = 4;
        String valueForFractionalPart() default ".*\\.\\d{" + fractionalPartLen + "}$";
        String explainForFractionalPart() default "rate_field_frac_part_incorrect_explain";
        
        String valueForWhole() default "^([0-9]\\.\\d{" + fractionalPartLen + "})$";
        String explainForWhole() default "rate_field_whole_incorrect_explain";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentAmount {
        int integerPartMaxLen() default 0;
        static int integerPartMaxLen = 10;
        String valueForIntegerPart() default "(0|[1-9]\\d{0,"  + (integerPartMaxLen-1) + "})(\\.\\d*)?";
        String explainForIntegerPart() default "anot_amount_integer_part_exp";
        
        static int fractionalPartLen = 2;
        String valueForFractionalPart() default "\\d*(\\.\\d{" + fractionalPartLen + "})?";
        String explainForFractionalPart() default "anot_amount_fractional_part_exp";
        
        String valueForWhole() default "(0|[1-9]\\d{0," + integerPartMaxLen + "})(\\.\\d{" + fractionalPartLen + "})?";
        String explainForWhole() default "anot_amount_explain";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentMapEditor {
        String key() default "\\d"; // [0-9] {1}  for default
        String value() default "\\d";
        String explainKey() default "anot_map_editor_left_exp";
        String explainValue() default "anot_map_editor_right_exp";
        String explainEmpty() default "anot_map_editor_empty_value_exp";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentISO {
        static int isoLen = 3;
        String value() default "[a-zA-Z]{" + isoLen + "}";
        String explain() default "anot_iso_exp";
        Class predicate() default DEFAULT.class;
        public static class DEFAULT {
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ContentPattern {
        String value() default "";
        String explain() default "";
        Class predicate() default DEFAULT.class;
        public static class DEFAULT {
        }
    }
}
