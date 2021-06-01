package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.Constants.DEFAULT_PORT;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class MainPlayer1 extends Application {

   public static Stage stage;



    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Platform.setImplicitExit(false);

        Parent root = FXMLLoader.load(getClass().getResource("/main-menu.fxml"));

        stage = new Stage(StageStyle.UTILITY);

        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();

    }

    public static void launchServer(){

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

        try {

            Stage clientWindow;   clientWindow = new Stage(StageStyle.UTILITY);

            Parent root = FXMLLoader.load(getClass().getResource("/main-menu.fxml"));

            clientWindow.setScene(new Scene(root));
            clientWindow.setTitle("Joindre une partie");
            clientWindow.setOnCloseRequest(Event::consume);
            clientWindow.show();

        } catch (IOException e) {
            throw new Error();
        }



    }

    public static void launchClient(String host, int port){

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), host, port);

        new Thread(remotePlayerClient::run).start();



    }
}
