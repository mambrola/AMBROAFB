/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.general.interfaces.EditorPanelable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * The class provides to show LocalDate item into table cell. The date text format will be -  dd MMM yyyy
 * @author dkobuladze
 */
public class DateCellFactory {

    public static class LocalDateCell implements Callback<TableColumn<EditorPanelable, LocalDate>, TableCell<EditorPanelable, LocalDate>> {
        
        @Override
        public TableCell<EditorPanelable, LocalDate> call(TableColumn<EditorPanelable, LocalDate> param) {
            return new TableCell<EditorPanelable, LocalDate>(){
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || empty) ? null : DateConverter.getInstance().getDayMonthnameYearBySpace(item));
                }

            };
        }
    }
    
    
    public static class LocalDateTimeCell implements Callback<TableColumn<EditorPanelable, LocalDateTime>, TableCell<EditorPanelable, LocalDateTime>> {
        
        @Override
        public TableCell<EditorPanelable, LocalDateTime> call(TableColumn<EditorPanelable, LocalDateTime> param) {
            return new TableCell<EditorPanelable, LocalDateTime>(){
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || empty) ? null : DateConverter.getInstance().getDayMonthnameYearBySpace(item));
                }

            };
        }
        
    }
}
