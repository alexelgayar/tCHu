package ch.epfl.tchu.gui;


import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

import static ch.epfl.tchu.game.Constants.DEFAULT_PORT;

public final class ServerMain extends Application {

    /**
     * The main method of ServerMain
     *
     * @param args the program arguments of ServerMain
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method which calls the play method of the Game
     *
     * @param primaryStage the primary stage
     * @throws Exception an exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT); //TODO: Do I need to set a try (similar to TestServer of Etape 8?

        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Random rng = new Random();

        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        RemotePlayerProxy remotePlayerProxy = new RemotePlayerProxy(serverSocket.accept());

        //TODO: Is there a better way to write this code?
        players.put(PLAYER_1, graphicalPlayerAdapter);
        players.put(PLAYER_2, remotePlayerProxy);

        playerNames.put(PLAYER_1, (parameters.isEmpty())
                ? "Ada"
                : parameters.get(0));
        playerNames.put(PLAYER_2, (parameters.isEmpty())
                ? "Charles"
                : parameters.get(1));

        new Thread(() -> Game.play(players, playerNames, tickets, rng)).start();
    }
}