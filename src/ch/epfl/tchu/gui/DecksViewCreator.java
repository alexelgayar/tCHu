package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Group;
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

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable (=final) and package private (=no modifier). Constructs scene graph to represent the cards.
 */
final class DecksViewCreator implements ActionHandlers {

    public static final int MIN_CARD_VISIBILITY = 0;
    public static final int MIN_COUNTER_VISIBILITY = 1;

    private static final int OUTSIDE_RECT_W = 60;
    private static final int OUTSIDE_RECT_H = 90;

    private static final int INSIDE_RECT_W = 40;
    private static final int INSIDE_RECT_H = 70;

    private static final int GAUGE_W = 50;
    private static final int GAUGE_H = 5;

    private DecksViewCreator() {
    }

    /**
     * Returns the HBox of the player's hand view
     * @param observableGameState the observable game state
     * @return the HBox of the player's hand view
     */
    public static HBox createHandView(ObservableGameState observableGameState) {
        HBox main = new HBox();
        main.getStylesheets().addAll("decks.css", "colors.css");

        ListView<Ticket> playerTickets = new ListView<>(observableGameState.playerTickets());
        playerTickets.setId("tickets");

        HBox handPane = new HBox();
        handPane.setId("hand-pane");

        //Create a stackPane for each card, then add to the handPane
        for (Card card : Card.values()) {
            ReadOnlyIntegerProperty count = observableGameState.playerCardTypeCount(card);

            StackPane cardStackPane = createCardStackPane(card, count);
            handPane.getChildren().add(cardStackPane);
        }

        main.getChildren().addAll(playerTickets, handPane);

        return main;
    }

    //Creates a card stack pane with text counter
    private static StackPane createCardStackPane(Card card, ReadOnlyIntegerProperty count){
        StackPane cardStackPane = createCard(card);
        cardStackPane.visibleProperty().bind(Bindings.greaterThan(count, MIN_CARD_VISIBILITY));

        Text counterText = createCardCounter(count);

        cardStackPane.getChildren().add(counterText);

        return cardStackPane;
    }

    //Creates a card structure for the given card
    private static StackPane createCard(Card card) {
        StackPane stackPane = new StackPane();

        if (card != null) stackPane.getStyleClass().addAll("card", (card == Card.LOCOMOTIVE)
                ? "NEUTRAL"
                : card.color().name());

        Rectangle outside = new Rectangle(OUTSIDE_RECT_W, OUTSIDE_RECT_H);
        outside.getStyleClass().add("outside");

        Rectangle inside = new Rectangle(INSIDE_RECT_W, INSIDE_RECT_H);
        inside.getStyleClass().addAll("filled", "inside");

        Rectangle trainImage = new Rectangle(INSIDE_RECT_W, INSIDE_RECT_H);
        trainImage.getStyleClass().add("train-image");

        stackPane.getChildren().addAll(outside, inside, trainImage);

        return stackPane;
    }

    //Creates a Text which displays the given count
    private static Text createCardCounter(ReadOnlyIntegerProperty count){
        Text counterText = new Text();

        counterText.getStyleClass().add("count");
        counterText.textProperty().bind(Bindings.convert(count));
        counterText.visibleProperty().bind(Bindings.greaterThan(count, MIN_COUNTER_VISIBILITY));

        return counterText;
    }

    //TODO: See if this can be cleaned up
    //TODO: Modularise
    /**
     * Returns the VBox of the FaceUpCards view, Tickets and Card buttons
     * @param gameState the observable game state
     * @param drawTicketsHandler the action handler for drawing tickets
     * @param drawCardHandler the action handler for drawing cards
     * @return the VBox of the FaceUpCards view, Tickets and Card buttons
     */
    public static VBox createCardsView(ObservableGameState gameState, ObjectProperty<DrawTicketsHandler> drawTicketsHandler, ObjectProperty<DrawCardHandler> drawCardHandler) {
        VBox cardPane = new VBox();
        cardPane.setId("card-pane");
        cardPane.getStylesheets().addAll("decks.css", "colors.css");

        Button ticketsButton = createButtonView(gameState.ticketsPercentage(), StringsFr.TICKETS);
        ticketsButton.disableProperty().bind(drawTicketsHandler.isNull());
        ticketsButton.setOnMouseClicked(e -> drawTicketsHandler.get().onDrawTickets());

        List<StackPane> stackPanes = new ArrayList<>();

        for (int i = 0; i < FACE_UP_CARDS_COUNT; ++i) {
            int finalI = i;
            stackPanes.add(createCard(gameState.faceUpCard(i).get()));
            gameState.faceUpCard(i).addListener((p, o, n) -> {
                stackPanes.get(finalI).getStyleClass().setAll("card", (n == Card.LOCOMOTIVE)
                        ? "NEUTRAL"
                        : n.color().name());;
            });
            stackPanes.get(i).disableProperty().bind(drawCardHandler.isNull());
            stackPanes.get(i).setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(finalI));
        }

        Button cardsButton = createButtonView(gameState.cardsPercentage(), StringsFr.CARDS);
        cardsButton.disableProperty().bind(drawCardHandler.isNull());
        cardsButton.setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(-1));

        cardPane.getChildren().add(ticketsButton);
        cardPane.getChildren().addAll(stackPanes);
        cardPane.getChildren().add(cardsButton);

        return cardPane;

    }

    private static Button createButtonView(ReadOnlyIntegerProperty gaugePercentage, String buttonText) {
        Button button = new Button(buttonText);
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
}