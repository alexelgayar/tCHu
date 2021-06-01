package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.Constants.DEFAULT_PORT;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class MainPlayer1 extends Application {

    private static final int NBR_PARAM_ARGS = 2;
    private static final String LOCALHOST = "localhost";

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Platform.setImplicitExit(false);

        Stage stage = new Stage(StageStyle.UTILITY);

        Button serverButton = new Button("Host");
        Button clientButton = new Button("Join");

        GridPane grid = new GridPane();
        grid.add(serverButton, 0, 1, 1, 1);
        grid.add(clientButton, 0, 2, 1, 1);

        Scene scene = new Scene(grid);

        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();

        serverButton.setOnAction(e -> {
            stage.hide();
            launchServer();

        });

        clientButton.setOnAction(e -> {
            stage.hide();
            launchClient();
        });


    }

    public void launchServer(){

        try{
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


        } catch (IOException e) {
            throw new Error();
        }

    }

    public void launchClient(){

        List<String> parameters = getParameters().getRaw();

        String name = (parameters.size() == NBR_PARAM_ARGS)
                ? parameters.get(0)
                : LOCALHOST;

        int port = (parameters.size() == NBR_PARAM_ARGS)
                ? Integer.parseInt(parameters.get(1))
                : DEFAULT_PORT;

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), LOCALHOST, DEFAULT_PORT);

        new Thread(remotePlayerClient::run).start();



    }
}
