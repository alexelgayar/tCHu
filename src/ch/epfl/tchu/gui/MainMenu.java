package ch.epfl.tchu.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.application.Platform.isFxApplicationThread;
import static javafx.application.Platform.runLater;

public final class MainMenu {

    private MainMenu() {
    }

    public static void idk() {
        runLater(() -> createMainMenu());
    }

    public static void createMainMenu() {
        assert isFxApplicationThread();




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
            ServerMain.main(new String[0]);


        });

        clientButton.setOnAction(e -> {
            stage.hide();
            ClientMain.main(new String[0]);

        });


    }


}
