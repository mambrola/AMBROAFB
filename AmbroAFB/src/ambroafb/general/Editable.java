/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import javafx.beans.value.ObservableValue;

/**
 *
 * @author tabramishvili
 * @param <T>
 */
public interface Editable<T> {
    public void edit(T param);
    public ObservableValue<String> getObservableString();
}
