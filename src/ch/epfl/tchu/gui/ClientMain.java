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
public class ClientMain extends Application {

    public static final int NBR_PARAM_ARGS = 2;
    public static final int DEFAULT_PORT = 5108;
    public static final String LOCALHOST = "localhost";

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();

        String name = (parameters.size() == NBR_PARAM_ARGS)
                ? parameters.get(0)
                : LOCALHOST;

        int port = (parameters.size() == NBR_PARAM_ARGS)
                ? Integer.parseInt(parameters.get(1))
                : DEFAULT_PORT;//TODO: Should this be hardcoded

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), name, port);//TODO: Should the index I take be hardcoded?

        System.out.println("Running remotePlayerClient with host: " + name + " port: " + port);
        new Thread(remotePlayerClient::run).start(); //TODO: Do I run the RemotePlayerClient on a new thread?
    }
}
