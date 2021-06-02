package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.Constants.DEFAULT_PORT;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class menuController {

    public void hostButton(ActionEvent actionEvent) throws IOException{

        ((Node)actionEvent.getSource()).getScene().getWindow().hide();

        ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);

        Map<PlayerId, Player> players = new HashMap<>();
        // Map<PlayerId, String> playerNames = new HashMap<>();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Random rng = new Random();

        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        RemotePlayerProxy remotePlayerProxy = new RemotePlayerProxy(serverSocket.accept());

        players.put(PLAYER_1, graphicalPlayerAdapter);
        players.put(PLAYER_2, remotePlayerProxy);

        new Thread(() -> Game.play(players, tickets, rng)).start();

    }

    public void clientButton(ActionEvent actionEvent)  throws IOException {

        ((Node)actionEvent.getSource()).getScene().getWindow().hide();

            Stage clientWindow = new Stage(StageStyle.UTILITY);

            Parent root = FXMLLoader.load(getClass().getResource("/clientMenu.fxml"));

            clientWindow.setScene(new Scene(root));
            clientWindow.setTitle("Joindre une partie");
            clientWindow.setOnCloseRequest(Event::consume);
            clientWindow.show();

    }
}
