package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.awt.*;

public class clientController {

    @FXML
    TextField hostname;

    @FXML
    TextField portnumber;


    public void connectClient(ActionEvent actionEvent) {

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), hostname.getText(), Integer.parseInt(portnumber.getText()));

        new Thread(remotePlayerClient::run).start();

        ((Node)actionEvent.getSource()).getScene().getWindow().hide();

    }
}
