/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;


/**
 *
 * @author tabramishvili
 */
public class EditorPanel extends HBox  {

    @FXML private Button view, edit, delete;
    @FXML private ToggleButton refresh;
    @FXML private SplitMenuButton add;
    @FXML private MenuItem addBySample;

    public EditorPanel() {
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/editor_panel/EditorPanel.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
/*Now*/ loader.setRoot(this);
/*Now*/ loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * *************************************************************************
     * Event Handlers *
     * *************************************************************************
     */
//    public void setOnDelete     (EventHandler<ActionEvent> handler) { delete.setOnAction(handler);}
//    public void setOnEdit       (EventHandler<ActionEvent> handler) { edit.setOnAction(handler);}
//    public void setOnView       (EventHandler<ActionEvent> handler) { view.setOnAction(handler);}
//    public void setOnNew        (EventHandler<ActionEvent> handler) { add.setOnAction(handler);}
//    public void setOnNewBySample(EventHandler<ActionEvent> handler) { addBySample.setOnAction(handler);}
//    public void setOnRefresh    (EventHandler<ActionEvent> handler) { refresh.setOnAction(handler);}
//    
//    public EventHandler<ActionEvent> getOnDelete()      { return delete.getOnAction();}
//    public EventHandler<ActionEvent> getOnEdit()        { return edit.getOnAction();}
//    public EventHandler<ActionEvent> getOnView()        { return view.getOnAction();}
//    public EventHandler<ActionEvent> getOnNew()         { return add.getOnAction();}
//    public EventHandler<ActionEvent> getOnNewBySample() { return addBySample.getOnAction();}
//    public EventHandler<ActionEvent> getOnRefresh()     { return refresh.getOnAction();}
   
    /**
     * 
     * @param table
     */
    public void disablePropertyBinder(TableView table){
        this.edit.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        this.view.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        this.addBySample.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    }

    /*
     * *************************************************************************
     * For Reach, if is needed, to Parameters *
     * *************************************************************************
     */
    
//    public Button           getDeleteButton()   { return delete;}
//    public Button           getEditButton()     { return edit;}
//    public Button           getViewButton()     { return view;}
//    public SplitMenuButton  getAddButton()      { return add;}
//    public MenuItem         getAddBySample()    { return addBySample;}
//    public ToggleButton     getRefreshButton()  { return refresh;}
//    Murman: თოკა, ესენიც თითქოს არაა საჭირო, სადაც ამათ იყენებდი შევცვალე disablePropertyBinder მეთოდით. რას ფიქრობ? 
//    არ იყო სწორი, რომ ფაქტიურად წვდომა გვქონდა private ცვლადებზე (მაშინ ბარემ გაგვეpublic-ებია), თან მეთოდი disablePropertyBinder
//    ამ კლასის მახასიათებელია, კლასმა იცის რომელი ღილაკები დაბაინდდება, მხოლოდ უნდა გადმოვაწოდოთ რომელ ცხრილთან ბაინდდება.
    
        
//    public ObjectProperty<EventHandler<ActionEvent>> onDeleteProperty()         { return delete.onActionProperty();}
//    public ObjectProperty<EventHandler<ActionEvent>> onEditProperty()           { return edit.onActionProperty();}
//    public ObjectProperty<EventHandler<ActionEvent>> onViewProperty()           { return view.onActionProperty();}
//    public ObjectProperty<EventHandler<ActionEvent>> onNewProperty()            { return add.onActionProperty();}
//    public ObjectProperty<EventHandler<ActionEvent>> onNewBySampleProperty()    { return addBySample.onActionProperty();}
//    public ObjectProperty<EventHandler<ActionEvent>> onRefreshProperty()        { return refresh.onActionProperty();}
//    Murman: თოკა, ესენი თითქოს არაა საჭირო, რას ფიქრობ? 

}