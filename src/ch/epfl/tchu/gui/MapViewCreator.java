package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Route;
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
final class MapViewCreator {

    private static final int RECT_WIDTH = 36;
    private static final int RECT_HEIGHT = 12;
    private static final int CIRCLE_RADIUS = 3;
    private static final int CIRCLE_SPACE = 6;

    public static Node createMapView(ObservableGameState observableGameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser){
        //TODO: Ignore the parameters for this first phase, instead focus on creating the scene graph
        Pane mapPane = new Pane();
        mapPane.getStylesheets().addAll("map.css", "colors.css");

        ImageView bgNode = createBGNode();
        mapPane.getChildren().add(bgNode);

        List<Node> routeGroups = new ArrayList<>();
        for (Route route: ChMap.routes()){
            for (int i = 1; i <= route.length(); ++i){
                //Route:
                Group routeGroup = new Group();
                routeGroup.setId(route.id());
                System.out.println(route.id());

                routeGroup.getStyleClass().add("route");

                if (route.level() == OVERGROUND){
                    routeGroup.getStyleClass().add("OVERGROUND");
                } else {
                    routeGroup.getStyleClass().add("UNDERGROUND");
                }

                for (Color color: Color.values()) {
                    if (route.color() == color) {
                        routeGroup.getStyleClass().add(color.toString());
                    }
                    else if (route.color() == null){
                        routeGroup.getStyleClass().add("NEUTRAL");
                    }
                }

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
                Circle c1 = new Circle(12, 6, CIRCLE_RADIUS); //TODO: Are circles centered properly?
                Circle c2 = new Circle(24, 6, CIRCLE_RADIUS);

                wagon.getChildren().addAll(r, c1, c2);

                caseGroup.getChildren().addAll(track, wagon);

                routeGroup.getChildren().addAll(caseGroup);
                routeGroups.add(routeGroup);
            }
        }

        mapPane.getChildren().addAll(routeGroups);

        return mapPane;
    }

    private static ImageView createBGNode(){
        Image map = new Image("map.png");
        ImageView bg = new ImageView();

        bg.setImage(map);
        bg.setSmooth(true);
        bg.setCache(true);

        return bg;
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ChooseCardsHandler handler);
    }
}
