package ch.epfl.tchu.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Contains the main program of the tCHU client
 */
public class ClientMain extends Application {

    private static void main(String[] args){
        System.out.println(args.length + " arguments :");
        for (String arg: args)
            System.out.println("  " + arg);
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Analyse the arguments passed to the program, to determine the host name and server port number
        //Creating a distant client, RemotePlayerClient - Associated to the graphic player - GraphicalPlayerAdapter
        //Starts the thread manageing the access to the network, which only execute s the run method of the previous client

        List<String> parameters = getParameters().getRaw();
    }


}
