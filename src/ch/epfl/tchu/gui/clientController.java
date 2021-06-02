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

    String defaultName = "localhost";
    int defaultPort = 5108;


    public void connectClient(ActionEvent actionEvent) {

        ((Node)actionEvent.getSource()).getScene().getWindow().hide();


        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(),
                hostname.getText().isEmpty()? defaultName : hostname.getText(),
                portnumber.getText().isEmpty()? defaultPort :Integer.parseInt(portnumber.getText()));

        new Thread(remotePlayerClient::run).start();



    }
}
