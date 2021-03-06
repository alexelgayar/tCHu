package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable (=final) and package private (=no modifier). Contains a unique public method named "createMapView"
 */
final class MapViewCreator implements ActionHandlers {

    private static final int RECT_WIDTH = 36;
    private static final int RECT_HEIGHT = 12;

    private static final int CIRCLE_RADIUS = 3;

    private static final int C1_CENTER_X = 12;
    private static final int C1_CENTER_Y = 6;

    private static final int C2_CENTER_X = 24;
    private static final int C2_CENTER_Y = 6;

    /**
     * If possible card choices = this constant (1) , then the route will be automatically claimed
     */
    private static final int ROUTE_AUTO_CLAIM_VALUE = 1;

    private MapViewCreator() {
    }

    /**
     * Constructs and returns the pane view of the map
     *
     * @param observableGameState the observable game state
     * @param claimRouteHandler   the action handler for when the player attempts to claim a route
     * @param cardChooser         a card chooser to claim a route
     * @return the pane view of the map
     */
    public static Pane createMapView(ObservableGameState observableGameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        Pane mapPane = new Pane();
        mapPane.getStylesheets().addAll("map.css", "colors.css");
        mapPane.getStyleClass().add("parent");

        ImageView bgNode = createBGNode();
        mapPane.getChildren().add(bgNode);

        List<Group> routeGroups = createRoutes(ChMap.routes(), observableGameState, claimRouteHandler, cardChooser);
        mapPane.getChildren().addAll(routeGroups);

        return mapPane;
    }

    //Creates the background view
    private static ImageView createBGNode() {
        Image map = new Image("dark.png");
        ImageView bg = new ImageView();
        bg.setImage(map);
        return bg;
    }

    //Creates the list of route groups
    private static List<Group> createRoutes(List<Route> routes, ObservableGameState observableGameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        List<Group> routeGroups = new ArrayList<>();

        for (Route route : routes) {
            Group routeGroup = createRoute(route, observableGameState, claimRouteHandler, cardChooser);
            routeGroups.add(routeGroup);
        }

        return routeGroups;
    }

    //Creates a single route group
    private static Group createRoute(Route route, ObservableGameState observableGameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        Group routeGroup = new Group();

        routeGroup.setId(route.id());
        routeGroup.getStyleClass().add("route");
        routeGroup.getStyleClass().add(route.level().name());
        routeGroup.getStyleClass().add(route.color() == null ? "NEUTRAL" : route.color().name());

        observableGameState.routeOwner(route).addListener((p, o, n) -> routeGroup.getStyleClass().add(n.name()));

        observableGameState.dijkstraHighlighted(route).addListener((p,o,n) -> {
            if (n) {
                routeGroup.getStyleClass().add("DIJKSTRA");
            } else {
                routeGroup.getStyleClass().remove("DIJKSTRA");
            }
        });

        routeGroup.disableProperty().bind(claimRouteHandler.isNull().or(observableGameState.claimable(route).not()));
        routeGroup.setOnMouseClicked(e -> pickClaimCards(observableGameState, route, claimRouteHandler, cardChooser));

        List<Group> caseGroups = createCases(route);
        routeGroup.getChildren().addAll(caseGroups);

        return routeGroup;
    }

    //Creates a list of route cases
    private static List<Group> createCases(Route route) {
        List<Group> caseGroups = new ArrayList<>();

        for (int i = 1; i <= route.length(); ++i) {
            //Case
            Group caseGroup = new Group();
            caseGroup.setId(route.id() + "_" + i);

            //Creating Track
            Rectangle track = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
            track.getStyleClass().addAll("track", "filled");

            //Creating Wagon
            Group wagon = createWagon();

            caseGroup.getChildren().addAll(track, wagon);
            caseGroups.add(caseGroup);
        }

        return caseGroups;
    }

    //Creates a wagon
    private static Group createWagon() {
        Group wagon = new Group();
        wagon.getStyleClass().add("car");

        Rectangle r = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
        r.getStyleClass().add("filled");
        Circle c1 = new Circle(C1_CENTER_X, C1_CENTER_Y, CIRCLE_RADIUS);
        Circle c2 = new Circle(C2_CENTER_X, C2_CENTER_Y, CIRCLE_RADIUS);

        wagon.getChildren().addAll(r, c1, c2);
        return wagon;
    }

    private static void pickClaimCards(ObservableGameState observableGameState, Route route, ObjectProperty<ClaimRouteHandler> claimRouteH, CardChooser cardChooser) {
        List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(route);

        if (possibleClaimCards.size() == ROUTE_AUTO_CLAIM_VALUE) {
            claimRouteH.get().onClaimRoute(route, possibleClaimCards.get(0));
        } else {
            ChooseCardsHandler chooseCardsH = chosenCards -> claimRouteH.get().onClaimRoute(route, chosenCards);
            cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
        }
    }

    /**
     * The card chooser for when a player attempts to claim a route
     */
    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ChooseCardsHandler handler);
    }

}
