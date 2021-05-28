package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

import static ch.epfl.tchu.game.Constants.DEFAULT_PORT;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Contains the main program of the tCHU client
 */
public final class ClientMain extends Application {

    public static final int NBR_PARAM_ARGS = 2; //TODO: Make private constant
    public static final String LOCALHOST = "localhost";

    /**
     * The main method of ClientMain
     *
     * @param args the program arguments of ClientMain
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method which calls the run method of the RemotePlayerClient
     *
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        List<String> parameters = getParameters().getRaw();

        String name = (parameters.size() == NBR_PARAM_ARGS)
                ? parameters.get(0)
                : LOCALHOST;

        int port = (parameters.size() == NBR_PARAM_ARGS)
                ? Integer.parseInt(parameters.get(1))
                : DEFAULT_PORT;

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), name, port);

        new Thread(remotePlayerClient::run).start();
    }
}
