/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.detail_pane;

import ambro.AView;
import ambroafb.general.interfaces.TableColumnWidths;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class AccountEntry {
    
    @AView.Column(title = "%date", width = TableColumnWidths.DATE)
    private final StringProperty dateProperty = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dateObj = new SimpleObjectProperty<>();
    
    @AView.Column(title = "%correspondent", width = "200")
    private final StringProperty correspondent = new SimpleStringProperty("");
    
    @AView.Column(title = "%debit", width = "60")
    private final StringProperty debit = new SimpleStringProperty("");
    
    @AView.Column(title = "%credit", width = "60")
    private final StringProperty credit = new SimpleStringProperty("");
    
    public AccountEntry() {
        
    }

    
}
