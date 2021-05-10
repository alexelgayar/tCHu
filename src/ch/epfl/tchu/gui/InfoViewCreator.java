package ch.epfl.tchu.gui;


import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable class, represents the creator for the information view
 */
final class InfoViewCreator {

    private InfoViewCreator(){}

    public static Node createInfoView(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState gameState, ObservableList<Text> textList){

        VBox main = new VBox();
        main.getStylesheets().addAll("info.css", "colors.css");

        VBox playerStats = new VBox();
        playerStats.setId("player-stats");

        for(PlayerId id : PlayerId.ALL){
            TextFlow playerTextFlow = new TextFlow();
            playerTextFlow.getStyleClass().addAll(id.name());

            Circle circle = new Circle();
            circle.getStyleClass().addAll("filled");
            circle.setRadius(5);

            Text text = new Text();

            text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                    playerNames.get(id),
                    gameState.playerTicketsCount(id),
                    gameState.playerCardsCount(id),
                    gameState.playeCarsCount(id),
                    gameState.playerClaimPoints(id)));

            playerTextFlow.getChildren().addAll(circle, text);
            playerStats.getChildren().addAll(playerTextFlow);
        }



        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);


        TextFlow textFlow = new TextFlow();
        textFlow.setId("game-info");

        Bindings.bindContent(textFlow.getChildren(), textList);

        main.getChildren().addAll(playerStats, separator, textFlow);

        return main;
    }


}
