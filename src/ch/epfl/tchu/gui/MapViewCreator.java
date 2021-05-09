package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.game.Color.BLACK;
import static ch.epfl.tchu.game.Color.RED;
import static ch.epfl.tchu.game.Route.Level.OVERGROUND;
import static ch.epfl.tchu.game.Route.Level.UNDERGROUND;
import static ch.epfl.tchu.gui.ActionHandlers.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable (=final) and package private (=no modifier). Contains a unique public method named "createMapView"
 */
final class MapViewCreator implements ActionHandlers {

    private static final int RECT_WIDTH = 36;
    private static final int RECT_HEIGHT = 12;
    private static final int CIRCLE_RADIUS = 3;

    private MapViewCreator() {
    }

    public static Node createMapView(ObservableGameState gameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        Pane mapPane = new Pane();
        mapPane.getStylesheets().addAll("map.css", "colors.css");

        ImageView bgNode = createBGNode();
        mapPane.getChildren().add(bgNode);

        for (Route route : ChMap.routes()) {
            Group routeGroup = new Group();
            routeGroup.setId(route.id());
            routeGroup.getStyleClass().add("route");
            routeGroup.getStyleClass().add(
                    route.level() == OVERGROUND
                            ? "OVERGROUND"
                            : "UNDERGROUND");

            routeGroup.getStyleClass().add(
                    route.color() == null
                            ? "NEUTRAL"
                            : route.color().name());

            gameState.routeOwner(route).addListener((p, o, n) -> routeGroup.getStyleClass().add(n.name()));
            routeGroup.disableProperty().bind(claimRouteHandler.isNull().or(gameState.claimable(route).not()));
            routeGroup.setOnMouseClicked(e -> pickClaimCards(gameState, route, claimRouteHandler, cardChooser));

            for (int i = 1; i <= route.length(); ++i) {
                //Case
                Group caseGroup = new Group();
                caseGroup.setId(route.id() + "_" + i);

                //Creating Track
                Rectangle track = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
                track.getStyleClass().addAll("track", "filled");

                //Creating Wagon
                Group wagon = new Group();
                wagon.getStyleClass().add("car");

                Rectangle r = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
                r.getStyleClass().add("filled");
                Circle c1 = new Circle(12, 6, CIRCLE_RADIUS);
                Circle c2 = new Circle(24, 6, CIRCLE_RADIUS);

                wagon.getChildren().addAll(r, c1, c2);

                caseGroup.getChildren().addAll(track, wagon);

                routeGroup.getChildren().addAll(caseGroup);
            }
            mapPane.getChildren().addAll(routeGroup);
        }

        return mapPane;
    }

    private static ImageView createBGNode() {
        Image map = new Image("map.png");
        ImageView bg = new ImageView();
        bg.setImage(map);
        return bg;
    }

    private static void pickClaimCards(ObservableGameState gameState, Route route, ObjectProperty<ClaimRouteHandler> claimRouteH, CardChooser cardChooser) {
        List<SortedBag<Card>> possibleClaimCards = gameState.possibleClaimCards(route);
        if (possibleClaimCards.size() == 1) {
            claimRouteH.get().onClaimRoute(route, possibleClaimCards.get(0));
        } else {
            ChooseCardsHandler chooseCardsH = chosenCards -> claimRouteH.get().onClaimRoute(route, chosenCards);
            cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
        }
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ChooseCardsHandler handler);
    }
}
