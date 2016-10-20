/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.filterablecombobox;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 * @param <T>
 */
public class FilterableComboBox<T> extends ComboBox<T> {

    public FilterableComboBox() {

    }

    private class DefaultConverter<T> extends StringConverter<T> {

        @Override
        public String toString(T objectInput) {
            return (objectInput == null) ? null : objectInput.toString();
        }

        @Override
        public T fromString(String string) {
            return null;
        }

    }
}
