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
public interface TableColumnFeatures {

    static class Width {
        
        public static final String ISO = "50";
        public static final String DATE = "80";
        public static final String DATETIME = "150";
        public static final String CLIENT = "180";
        public static final String MAIL = "200";
        public static final String CLIENT_MAIL = "300";
        public static final String INVOICE = "100";
        public static final String LICENSE = "90";
        public static final String PRODUCT = "170";
        public static final String PHONE = "110";
        public static final String MONEY = "80";
        public static final String MAC_ADDRESS = "120";

    }

    static class Style {

        public static final String TEXT_CENTER = "textCenter";
        public static final String TEXT_RIGHT = "textRight";

    }
}
