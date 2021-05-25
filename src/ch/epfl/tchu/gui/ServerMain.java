package ch.epfl.tchu.gui;


import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class ServerMain extends Application { //TODO: I don't get anything when I run ServerMain

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        ServerSocket serverSocket = new ServerSocket(5108); //TODO: Do I need to set a try (similar to TestServer of Etape 8?

        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Random rng = new Random();

        //TODO: Is this the correct way to set up the graphical and remote players?
        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        System.out.println("Test before socket");
        RemotePlayerProxy remotePlayerProxy = new RemotePlayerProxy(serverSocket.accept());
        System.out.println("Test after socket");

        players.put(PLAYER_1, graphicalPlayerAdapter);
        players.put(PLAYER_2, remotePlayerProxy);

        playerNames.put(PLAYER_1, (parameters.isEmpty())
                ? "Ada"
                : parameters.get(0));
        playerNames.put(PLAYER_2, (parameters.isEmpty())
                ? "Charles"
                : parameters.get(1));

        System.out.println("Test");
        playerNames.forEach((playerId, playerName) -> System.out.println("playerId: " + playerId + "  playerName: " + playerName));
        new Thread(()-> Game.play(players, playerNames, tickets, rng)).start(); //TODO: Do I run this on a new thread?
    }
}

//        public final class TestServer {
//            public static void main(String[] args) throws IOException {
//                System.out.println("Starting server!");
//                try (ServerSocket serverSocket = new ServerSocket(5108);
//                     Socket socket = serverSocket.accept()) {
//                    Player playerProxy = new RemotePlayerProxy(socket);
//                    var playerNames = Map.of(PLAYER_1, "Ada",
//                            PLAYER_2, "Charles");
//                    playerProxy.initPlayers(PLAYER_1, playerNames);
//                }
//                System.out.println("Server done!");
//            }
//        }
