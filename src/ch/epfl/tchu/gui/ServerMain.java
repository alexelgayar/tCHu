package ch.epfl.tchu.gui;


import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class ServerMain extends Application {

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        //Analyse the names of the two players from the parameters
        //Wait for a connection from the client on port 5108
        //Create the two players, 1. GraphicalPlayer 2. RemotePlayerProxy on Client

        RemotePlayerClient remotePlayerClient = (parameters.isEmpty())
                ? new RemotePlayerClient(new GraphicalPlayerAdapter(), "localhost", 5108) //TODO: Should this be hardcoded
                : new RemotePlayerClient(new GraphicalPlayerAdapter(), parameters.get(0), Integer.parseInt(parameters.get(1))); //TODO: Should the index I take be hardcoded?


        //new Thread(()-> Game.play()); //TODO: Do I run the RemotePlayerClient on a new thread?
    }
}
