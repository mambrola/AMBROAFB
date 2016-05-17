/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambroafb.AmbroAFB;
import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.ListEditor;
import ambroafb.general.PhoneNumber;
import ambroafb.general.Utils;
import ambroafb.invoices.Invoice;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 *
 * @author tabramishvili
 */
public class InvoiceDialog extends Stage implements Initializable {

    GeneralConfig conf = GeneralConfig.getInstance();
    ArrayList<Node> focusTraversableNodes;
    Invoice invoice, invoiceBackup;

    @FXML
    VBox formPane;
    @FXML
    private Label first_name, last_name;
    @FXML
    DatePicker openDate;
    @FXML
    CheckBox juridical, rezident;
    @FXML
    TextField firstName, lastName, idNumber, email, fax, address, zipCode, city;
    @FXML
    ComboBox<Country> country;
    @FXML
    ListEditor<PhoneNumber> phone;
//    @FXML
//    OkayCancel okayCancel;

    private boolean askClose = true;

    public InvoiceDialog() {
        this(new Invoice());
    }

    public InvoiceDialog(Invoice invoice) {
        super();
        this.invoice = invoice;
        //this.invoiceBackup = invoice.cloneWithID();

        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/invoices/dialog/ClientDialog.fxml"));
        loader.setResources(conf.getBundle());
        loader.setController(this);
        try {
            setScene(new Scene(loader.load()));
        } catch (IOException ex) {
            Logger.getLogger(InvoiceDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        initOwner(AmbroAFB.mainStage);
        setResizable(false);

        setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            onClose();
        });

    }

    private void onClose() {
        System.out.println("on close");
        boolean close = askClose ? new AlertMessage(Alert.AlertType.CONFIRMATION, null, "Do you want to exit without saving?").showAndWait().get().equals(ButtonType.OK) : true;
        if (close) {
            invoice = null;
            System.out.println("cancelling");
            close();
        }
    }

    /**
     * თუ isCancelled() არის true, აბრუნებს null-ს, თუ არადა შესაბამის კლიენტს
     *
     * @return
     */
    public Invoice getResult() {
        showAndWait();
        return invoice;
    }

    /**
     * დიალოგის დახურვისას ამოაგდებს გაფრთხილებას ნამდვილად უნდა თუ არა დახურვა
     *
     * @param ask
     */
    public void askClose(boolean ask) {
        askClose = ask;
    }

    @FXML
    private void cancel(ActionEvent e) {

        System.out.println("CCCCCCCCCCCCCCCCCCanceled");
//        if (onCancell != null) {
//            onCancell.accept(null);
//        }
    }

    @FXML
    private void okay(ActionEvent e) {
//        System.out.println("OOOOOOOOOOOOOOOOOOkaied");
//        try {
//            Invoice.saveInvoice(invoice);
//            close();
//        } catch (Exception ex) {
//            invoice.copyFrom(invoiceBackup);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
//        }
    }

    @FXML
    private void switchJuridical(ActionEvent e) {
        System.out.println("e.getSource(): " + firstName.widthProperty().getValue());
        double w = firstName.widthProperty().getValue() + lastName.widthProperty().getValue();
        if (((CheckBox) e.getSource()).isSelected()) {
            first_name.setText(conf.getTitleFor("firm_name"));
            last_name.setText(conf.getTitleFor("firm_form"));
            firstName.setPrefWidth(0.75 * w);
            lastName.setPrefWidth(0.25 * w);
        } else {
            first_name.setText(conf.getTitleFor("first_name"));
            last_name.setText(conf.getTitleFor("last_name"));
            firstName.setPrefWidth(0.50 * w);
            lastName.setPrefWidth(0.50 * w);
        }
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        country.setConverter(new StringConverter<Country>() {

            @Override
            public String toString(Country object) {
                return object.getCode() + "   " + object.getName();
            }

            @Override
            public Country fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        country.getItems().addAll(Country.getAllFromDB());
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        phone.setConverter(new StringConverter<PhoneNumber>() {

            @Override
            public String toString(PhoneNumber object) {
                return object != null ? object.getNumber() : null;
            }

            @Override
            public PhoneNumber fromString(String string) {
                return new PhoneNumber(string);
            }
        });
        juridical.setOnAction(this::switchJuridical);
//        okayCancel.setOnOkay(this::okay);
//        okayCancel.setOnCancel(this::cancel);
        //System.out.println("client: " + client);
        
    }

    public void setDisabled() {
        focusTraversableNodes.forEach((Node t) -> {
            if (t != phone) {
                t.setDisable(true);
            }
        });
        phone.setEditable(false);
    }

   

}
