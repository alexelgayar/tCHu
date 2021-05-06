package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARDS_COUNT;
import static ch.epfl.tchu.gui.ActionHandlers.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable (=final) and package private (=no modifier). Constructs scene graph to represent the cards.
 */
final class DecksViewCreator implements ActionHandlers{

    private static final int OUTSIDE_RECT_W = 60;
    private static final int OUTSIDE_RECT_H = 90;

    private static final int INSIDE_RECT_W = 40;
    private static final int INSIDE_RECT_H = 70;

    private static final int GAUGE_W = 50;
    private static final int GAUGE_H = 5;

    private DecksViewCreator(){}


    //Constructs the view of the Player cards
    public static Node createHandView(ObservableGameState gameState){
        HBox main = new HBox();
        main.getStylesheets().addAll("decks.css", "colors.css");

        ListView<Ticket> playerTickets = new ListView<>(gameState.playerTickets()); //TODO: Convert tickets to string, weird bug when trying to loop through playerTickets
        playerTickets.setId("tickets");

        List<HBox> handPanes = new ArrayList<>();

        for (Card card: Card.values()) {
            HBox handPane = new HBox();
            handPane.setId("hand-pane"); //TODO: Updating the tickets + cards in the deck

            ReadOnlyIntegerProperty count = gameState.playerCardTypeCount(card);

            StackPane cardStackPane = createCardStackPane(card);
            cardStackPane.visibleProperty().bind(Bindings.greaterThan(count, 0));

            Text counterText = new Text();
            counterText.getStyleClass().add("count");
            counterText.textProperty().bind(Bindings.convert(count));
            counterText.visibleProperty().bind(Bindings.greaterThan(count, 1));

            cardStackPane.getChildren().add(counterText);

            handPane.getChildren().add(cardStackPane);
            handPanes.add(handPane);
        }

        main.getChildren().addAll(playerTickets);
        main.getChildren().addAll(handPanes);

        return main;
    }

    //Constructs the view of the Cards + Deck
    public static Node createCardsView(ObservableGameState gameState, ObjectProperty<DrawTicketsHandler> drawTicketsHandler, ObjectProperty<DrawCardHandler> drawCardHandler){
        VBox cardPane = new VBox();
        cardPane.setId("card-pane");
        cardPane.getStylesheets().addAll("decks.css", "colors.css");
        Button ticketsButton = createButtonView(gameState.ticketsPercentage());
        //ticketsButton.disableProperty().bind(drawTicketsHandler.isNull().or(gameState.canDrawTickets().not())); //TODO: Is handler correct?
        //gameState.ticketsPercentage().addListener((p, o, n) -> ticketsButton = createButtonView(gameState.ticketsPercentage())); //TODO: Fix listener

        ticketsButton.setOnMouseClicked(e -> drawTicketsHandler.get().onDrawTickets());

        List<StackPane> stackPanes = new ArrayList<>();
        for (int i = 0; i < FACE_UP_CARDS_COUNT; ++i) {
            int finalI = i;
            stackPanes.add(createCardStackPane(gameState.faceUpCard(i).get()));
            gameState.faceUpCard(i).addListener((p, o, n) -> {
                stackPanes.get(finalI).getStyleClass().add(n.name());
                System.out.println("p:" + p + " o:" + o + " n:" + n);
            }); //TODO: Is this correct listener implementation?


            //stackPanes.get(i).disableProperty().bind(drawCardHandler.isNull().or(gameState.canDrawCards().not())); //TODO: Is handler correct?
            stackPanes.get(i).setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(finalI));
        }

        Button cardsButton = createButtonView(gameState.cardsPercentage());
        //cardsButton.disableProperty().bind(drawCardHandler.isNull().or(gameState.canDrawCards().not())); //TODO: Is handler correct?
        //gameState.cardsPercentage().addListener((p, o, n) -> ticketsButton = createButtonView(gameState.ticketsPercentage())); //TODO: Fix listener
        cardsButton.setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(-1));

        cardPane.getChildren().add(ticketsButton);
        cardPane.getChildren().addAll(stackPanes);
        cardPane.getChildren().add(cardsButton);

        return cardPane;

    }

    private static StackPane createCardStackPane(Card card){
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add("card");

        if (card == null){
            System.out.println("Card is null");
        }
        else if (card == Card.LOCOMOTIVE){
            stackPane.getStyleClass().addAll("NEUTRAL");
        } else{
            System.out.println(card);
            stackPane.getStyleClass().addAll(card.color().name());
        }

        Rectangle outside = new Rectangle(OUTSIDE_RECT_W, OUTSIDE_RECT_H);
        outside.getStyleClass().add("outside");

        Rectangle inside = new Rectangle(INSIDE_RECT_W, INSIDE_RECT_H);
        inside.getStyleClass().addAll("filled", "inside");

        Rectangle trainImage = new Rectangle(INSIDE_RECT_W, INSIDE_RECT_H);
        trainImage.getStyleClass().add("train-image");

        stackPane.getChildren().addAll(outside, inside, trainImage);

        return stackPane;
    }

    private static Button createButtonView(ReadOnlyIntegerProperty gaugePercentage){
        Button button = new Button();
        button.getStyleClass().add("gauged");

        Group group = new Group();

        Rectangle bg = new Rectangle(GAUGE_W, GAUGE_H);
        bg.getStyleClass().add("background");

        Rectangle fg = new Rectangle(GAUGE_W, GAUGE_H);
        fg.getStyleClass().add("foreground");

        fg.widthProperty().bind(
                gaugePercentage.multiply(GAUGE_W).divide(100));

        group.getChildren().addAll(bg, fg);

        button.setGraphic(group);

        return button;
    }


    private static void drawTickets(ObjectProperty<DrawTicketsHandler> drawTicketsH){
        drawTicketsH.get().onDrawTickets();
    }

    private static void drawCards(ObjectProperty<DrawCardHandler> drawCardH, int slot){
        drawCardH.get().onDrawCard(slot);
    }
}