package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class MainPlayer2 extends Application {


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
        grid.add(serverButton, 0, 1, 1, 2);
        grid.add(clientButton, 1, 1, 1, 2);

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
            joinGame();
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

    public void joinGame(){

        Stage stage = new Stage(StageStyle.UTILITY);

        GridPane grid = new GridPane();

        Label hostName = new Label("Entrez le nom du hote: ");
        TextField hostText = new TextField();

        Label port = new Label("Enter le numero du port: ");
        TextField portNumber = new TextField();

        grid.addRow(0, hostName, hostText);
        grid.addRow(1, port, portNumber);

        Button submitButton = new Button("Soumettre");

        grid.add(submitButton, 1, 2, 1, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.setTitle("Joindre une partie");
        stage.setOnCloseRequest(Event::consume);
        stage.show();

        submitButton.setOnAction(e -> {
            stage.hide();
            launchClient(hostText.getText(), Integer.parseInt(portNumber.getText()));
        });
    }

    public void launchClient(String host, int port){

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), host, port);

        new Thread(remotePlayerClient::run).start();



    }
}
