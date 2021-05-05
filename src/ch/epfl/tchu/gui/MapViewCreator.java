package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

import static ch.epfl.tchu.gui.ActionHandlers.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable (=final) and package private (=no modifier). Contains a unique public method named "createMapView"
 */
final class MapViewCreator {

    public static Node createMapView(ObservableGameState observableGameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser){
        //Ignorer the parameters for this first phase, instead focus on creating the scene graph

        ImageView bgNode = createBGNode();
        Pane mapPane = new Pane();

        mapPane.getStylesheets().addAll("map.css", "colors.css");

        mapPane.getChildren().add(bgNode);
        mapPane.getChildren().addAll();
        //Background ImageView


        //Routes
        for (Route route: ChMap.routes()){
            for (int i = 1; i <= route.length(); ++i){

                //Create a route (always visible) + wagon
                //Wagon is only visible when player id is attached

                /*
                Rectangles: w: 36d, h: 12d
                Circles: r:3d, centered vertically in the rectangle, placed horizontally at 6 units on either side of its centre

                Notez que lorsque vous créez les instances de Circle représentant les cercles, la position du centre que vous devez spécifier est relative au groupe.
                Dès lors, pour les placer comme demandé, il faut centrer le premier en (12, 6), le second en (24, 6).
                 */
            }
        }

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

    /*\
    How CardChooser will be able to work:
    CardChooser firstChoice = (options, handler) -> {
        handler.onChooseCards(options.get(0));
    };

     */



}
