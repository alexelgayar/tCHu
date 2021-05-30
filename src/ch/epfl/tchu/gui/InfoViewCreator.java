package ch.epfl.tchu.gui;


import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable class, represents the creator for the information view
 */
final class InfoViewCreator {

    private InfoViewCreator() {
    }

    /**
     * Method that creates the info view
     *
     * @param playerId    the id of the player for which the info view is associated
     * @param playerNames map that maps the players' Ids to their names
     * @param gameState   the observable game state of the current game
     * @param textList    list containing the text that will show up in the InfoView
     * @return Vbox of the InfoView
     */
    public static VBox createInfoView(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState gameState, ObservableList<Text> textList) {

        VBox main = new VBox();
        main.getStylesheets().addAll("info.css", "colors.css");

        VBox playerStats = new VBox();
        playerStats.setId("player-stats");

        createPlayerStats(playerId, playerNames, gameState, playerStats);
        createPlayerStats(playerId.next(), playerNames, gameState, playerStats);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);


        TextFlow textFlow = new TextFlow();
        textFlow.setId("game-info");




        Bindings.bindContent(textFlow.getChildren(), textList);

        main.getChildren().addAll(playerStats, separator, textFlow);

        return main;
    }

    private static void createPlayerStats(PlayerId id, Map<PlayerId, String> playerNames, ObservableGameState gameState, VBox playerStats) {

        TextFlow playerTextFlow = new TextFlow();
        playerTextFlow.getStyleClass().addAll(id.name());

        Circle circle = new Circle();
        circle.getStyleClass().addAll("filled");
        circle.setRadius(5);

        Text text = new Text();
       // text.setFont(Font.font("Courier New", 12));

        text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                playerNames.get(id),
                gameState.playerTicketsCount(id),
                gameState.playerCardsCount(id),
                gameState.playerCarsCount(id),
                gameState.playerClaimPoints(id)));


        gameState.getCurrentPlayer().addListener((p, o, n) -> {
            if(n == id) circle.setRadius(7);
            else circle.setRadius(5);
        });


        playerTextFlow.getChildren().addAll(circle, text);
        playerStats.getChildren().addAll(playerTextFlow);

    }

    private static Color playerColor(PlayerId id){
        if(id == PlayerId.PLAYER_1) return Color.CORAL;
        else return Color.DARKCYAN;
    }


}
