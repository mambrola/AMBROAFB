/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.viewadd.client_dialog;

import ambroafb.AmbroAFB;
import ambroafb.clients.Client;
import ambroafb.general.AlertMessage;
import ambroafb.general.Utils;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends Stage {

    private ClientDialogController controller;
    private Client result;
    private boolean cancelled = true;

    public ClientDialog() {
        this(new Client());
    }

    public ClientDialog(Client client) {
        super();

        try {
            Scene scene = Utils.createScene("/ambroafb/clients/viewadd/client_dialog/ClientDialog.fxml");
            setScene(scene);
            controller = (ClientDialogController) scene.getProperties().get("controller");

        } catch (IOException ex) {
            Logger.getLogger(ClientDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        initOwner(AmbroAFB.mainStage);
        setResizable(false);

        controller.setClient(client);
        controller.onCreate((Client t) -> {
            System.out.println("on create: " + t);
            result = t;
            cancelled = false;
            close();
        });

        setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            onClose(null);
        });
        controller.onCancell(this::onClose);

    }

    private void onClose(Object o) {
        System.out.println("on close");
        if (new AlertMessage(Alert.AlertType.CONFIRMATION, null, "Do you want to exit without saving?").showAndWait().get().equals(ButtonType.OK)) {
            cancelled = true;
            System.out.println("cancelling");
            close();
        }
    }

    /**
     * თუ isCancelled() არის true, აბრუნებს null-ს, თუ არადა შესაბამის კლიენტს
     *
     * @return
     */
    public Client getResult() {
        return isCancelled() ? null : result;
    }

    /**
     * აბრუნებს გაუქმენულია თუ არა კლიენტის შექმნა/შეცვლა
     *
     * @return
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * ყველა ველი ხდება disable, გამოიყენება მხოლოდ კლიენტის სანახავად
     */
    public void setDisabled() {
        controller.setDisabled();
    }

    /**
     * ანულებს კლიენტს, კლიენტსი შეცვლის შემთხვევაში გადაცემული კლიენტი არ
     * შეიცვლება და მის მაგივრად ახალი კლიენტი დაბრუნდება ახალი კლიენტის შექმნის
     * შემთხვევაში ამ მეთოდის გამოძახება იდეურად არაფერს შეცვლის
     */
    public void resetClient() {
        controller.setClient(null);
    }

}
