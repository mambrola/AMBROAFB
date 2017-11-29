/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.accounts.detail_pane.helper.AccountEntry;
import java.time.LocalDate;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * The class provides to show LocalDate item into table cell. The date text format will be -  dd MMM yyyy
 * @author dkobuladze
 */
public class DateCellFactory implements Callback<TableColumn<AccountEntry, LocalDate>, TableCell<AccountEntry, LocalDate>> {

    @Override
    public TableCell<AccountEntry, LocalDate> call(TableColumn<AccountEntry, LocalDate> param) {
        return new TableCell<AccountEntry, LocalDate>(){
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : DateConverter.getInstance().getDayMonthnameYearBySpace(item));
            }

        };
    }
}
