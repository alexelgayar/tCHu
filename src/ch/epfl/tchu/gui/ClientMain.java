package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Contains the main program of the tCHU client
 */
public class ClientMain extends Application { //TODO: When I run ClientMain I don't get anything

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();

        System.out.println("Parameters: " + parameters);

        RemotePlayerClient remotePlayerClient = (parameters.isEmpty())
                ? new RemotePlayerClient(new GraphicalPlayerAdapter(), "localhost", 5108) //TODO: Should this be hardcoded
                : new RemotePlayerClient(new GraphicalPlayerAdapter(), parameters.get(0), Integer.parseInt(parameters.get(1))); //TODO: Should the index I take be hardcoded?

        new Thread(remotePlayerClient::run); //TODO: Do I run the RemotePlayerClient on a new thread?
    }
}
