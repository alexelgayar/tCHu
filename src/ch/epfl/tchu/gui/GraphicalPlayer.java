package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.tchu.gui.ActionHandlers.*;
import javafx.util.StringConverter;

import static ch.epfl.tchu.gui.StringsFr.T_CHU;
import static javafx.application.Platform.isFxApplicationThread;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents the graphic interface of a tCHu player
 */
public final class GraphicalPlayer {

    private final PlayerId id;
    Map<PlayerId, String> playerNames;
    private final ObservableGameState gameState;
    private final ObservableList<Text> textList = observableArrayList();
    private final Stage mainStage;
    private final SimpleObjectProperty<ClaimRouteHandler> claimRouteHandler;
    private final SimpleObjectProperty<DrawTicketsHandler> drawTicketHandler;
    private final SimpleObjectProperty<DrawCardHandler> drawCardHandler;
    private static final int MAX_TEXT_SIZE = 5;
    private static final int REQUIRED_CARD_CHOICE_VALUE = 1;
    private Color currentColor = null;



    /**
     * Constructor for a GraphicalPlayer that creates the graphical interface
     *
     * @param id          the id of the player to which the interface corresponds
     * @param playerNames map that maps the players' Ids to their names
     */
    public GraphicalPlayer(PlayerId id, Map<PlayerId, String> playerNames) {

        this.id = id;
        this.playerNames = playerNames;
        gameState = new ObservableGameState(id);
        claimRouteHandler = new SimpleObjectProperty<>();
        drawTicketHandler = new SimpleObjectProperty<>();
        drawCardHandler = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator.createMapView(gameState, claimRouteHandler, this::chooseClaimCards);
        Node infoView = InfoViewCreator.createInfoView(id, playerNames, gameState, textList);
        Node cardsView = DecksViewCreator.createCardsView(gameState, drawTicketHandler, drawCardHandler);
        Node handsView = DecksViewCreator.createHandView(gameState);

        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handsView, infoView);


        mainStage = new Stage();
        Scene scene = new Scene(borderPane);
        mainStage.setScene(scene);
        mainStage.setTitle(T_CHU + StringsFr.EN_DASH_SEPARATOR + playerNames.get(id));

        gameState.getCurrentPlayer().addListener((p, o, n) -> {
            if(id == n){
                mainStage.setTitle(T_CHU + StringsFr.EN_DASH_SEPARATOR + playerNames.get(id) + StringsFr.EN_DASH_SEPARATOR + "C'est vote tour!");
            }
            else mainStage.setTitle(T_CHU + StringsFr.EN_DASH_SEPARATOR + playerNames.get(id));
        });


        mainStage.show();
    }

    /**
     * Updates all the values of the observableGameState attributes
     *
     * @param newGameState   the updated version of the gameState
     * @param newPlayerState the updated version of the player state
     */
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert isFxApplicationThread();
        gameState.setState(newGameState, newPlayerState);
    }


    public static void choosePlayerName(ChooseNameHandler nameHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);
        Label chooseName = new Label(" Écrivez votre nom : ");
        chooseName.setTextFill(Color.WHITE);
        chooseName.setFont(new Font("Kefa", 13));

        TextField textField = new TextField();
        textField.setPrefWidth(220);
        textField.getStyleClass().add("txtfield");
        textField.setPromptText("Laissez vide pour un nom au hasard");

        Button btn = new Button("Soumettre");
        btn.getStyleClass().add("btn");

        btn.setOnAction(e -> {
                    try {
                       nameHandler.onChooseName(textField.getText());
                        stage.hide();

                    } catch (IllegalFormatException e1) {
                        chooseName.setText("Entrez seulement une chaîne de caractères");
                    }
                }
        );


        GridPane grid = new GridPane();
        grid.getStyleClass().add("parent");

        grid.addRow(1, chooseName, textField, btn);


        Scene scene = new Scene(grid, 405, 30);
        scene.getStylesheets().add("menu.css");

        stage.setScene(scene);
        //stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Saisie du nom");
        stage.setOnCloseRequest(Event::consume);
        stage.show();
    }

    /**
     * Updates the InfoView when a new text in received by adding the message to the bottom and making
     * sure that the list doesn't exceed 5 messages
     *
     * @param message the new message that is received
     */
    public void receiveInfo(String message) {
        assert isFxApplicationThread();
        Text text = new Text(message);
        text.setFont(Font.font("Avenir", 14));

        if (message.contains(playerNames.get(id))) currentColor = playerColor(id);
        else if (message.contains(playerNames.get(id.next()))) currentColor = playerColor(id.next());

        text.setFill(currentColor);


        if (textList.size() == MAX_TEXT_SIZE) textList.remove(0);
        textList.add(text);
    }

    /**
     * Lets a player perform one of the three permitted action during their turn
     *
     * @param ticketsHandler handler that is used to draw tickets
     * @param cardHandler    handler that is used to draw cards
     * @param routeHandler   handler that is used to claim routes
     */
    public void startTurn(DrawTicketsHandler ticketsHandler, DrawCardHandler cardHandler, ClaimRouteHandler routeHandler) {
        assert isFxApplicationThread();

        if (gameState.canDrawTickets().get()) {
            drawTicketHandler.set(() -> {
                setNull();
                ticketsHandler.onDrawTickets();
            });
        }

        if (gameState.canDrawCards().get()) {
            drawCardHandler.set((int i) -> {
                setNull();
                cardHandler.onDrawCard(i);
                drawCard(cardHandler);
            });
        }

        claimRouteHandler.set((route, claimCards) -> {
            setNull();
            routeHandler.onClaimRoute(route, claimCards);
        });

    }

    /**
     * Open a window to let the player choose the tickets that they want to keep
     *
     * @param tickets        all the tickets that the player can choose from
     * @param ticketsHandler handler that is called with the chosen tickets as an argument
     */
    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler ticketsHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);

        String message = String.format(StringsFr.CHOOSE_TICKETS, tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT,
                StringsFr.plural(tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT));

        ObservableList<Ticket> observableList = observableArrayList(tickets.toList());
        ListView<Ticket> listView = new ListView<>(observableList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button button = new Button();

        button.disableProperty().bind(Bindings.size(listView.getSelectionModel().getSelectedItems())
                .lessThan(tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT));

        button.setOnAction(e -> {
            stage.hide();
            ticketsHandler.onChooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems()));
        });

        createPane(stage, StringsFr.TICKETS_CHOICE, message, button, listView);
    }

    /**
     * Called that is called when a player chooses to draw cards, letting them draw a second one
     *
     * @param cardHandler handler that is called with the drawn card slot as an argument
     */
    public void drawCard(DrawCardHandler cardHandler) {
        assert isFxApplicationThread();

        drawCardHandler.set((int i) -> {
            setNull();
            cardHandler.onDrawCard(i);
        });
    }

    /**
     * Opens a window to let player choose with which cards they want to claim a route
     *
     * @param bagList      list containing all sorted bags of cards the player could claim the route with
     * @param cardsHandler handler that is called with the player's choice as an argument
     */
    public void chooseClaimCards(List<SortedBag<Card>> bagList, ChooseCardsHandler cardsHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);

        String message = StringsFr.CHOOSE_CARDS;

        ListView<SortedBag<Card>> listView = createCardsListView(bagList);

        Button button = new Button();
        button.disableProperty().bind(Bindings.size(listView.getSelectionModel().getSelectedItems()).lessThan(REQUIRED_CARD_CHOICE_VALUE));

        button.setOnAction(e -> {
            stage.hide();
            cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItem());
        });

        createPane(stage, StringsFr.CARDS_CHOICE, message, button, listView);
    }

    /**
     * Opens a window to let player choose with which additional cards they want to claim an underground route
     *
     * @param bagList      list containing all sorted bags of additional cards the player could claim the route with
     * @param cardsHandler handler that is called with the player's choice as an argument
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> bagList, ChooseCardsHandler cardsHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);

        String message = StringsFr.CHOOSE_ADDITIONAL_CARDS;

        ListView<SortedBag<Card>> listView = createCardsListView(bagList);

        Button button = new Button();

        button.setOnAction(e -> {
            stage.hide();
            cardsHandler.onChooseCards((listView.getSelectionModel().getSelectedItem() == null) ? SortedBag.of() :
                    listView.getSelectionModel().getSelectedItem());
        });

        createPane(stage, StringsFr.CARDS_CHOICE, message, button, listView);
    }

    private static ListView<SortedBag<Card>> createCardsListView(List<SortedBag<Card>> bagList) {
        ObservableList<SortedBag<Card>> observableList = observableArrayList(bagList);
        ListView<SortedBag<Card>> listView = new ListView<>(observableList);
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));
        return listView;
    }


    private <E> void createPane(Stage stage, String title, String message, Button button, ListView<E> listView) {

        stage.initOwner(mainStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();
        vBox.getStyleClass().add("backgr");


        button.setText(StringsFr.CHOOSE);
        button.getStyleClass().add("btn");

        Scene scene = new Scene(vBox);
        scene.getStylesheets().addAll("chooser.css", "colors.css");

        TextFlow textFlow = new TextFlow();
        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Avenir"));
        textFlow.getChildren().addAll(text);
        listView.getStyleClass().add("backgr");
        listView.setStyle("-fx-control-inner-background: #171616;");

        vBox.getChildren().addAll(textFlow, listView, button);
        stage.setScene(scene);
        stage.show();
    }

    private void setNull() {
        claimRouteHandler.set(null);
        drawTicketHandler.set(null);
        drawCardHandler.set(null);
    }

    /**
     * Class that manages the conversion between SortedBag<Card> and String
     */
    public static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        /**
         * Converts a sorted bag of cards to a string
         *
         * @param object sorted bag to be converted
         * @return string corresponding to the sorted bag passed as an argument
         */
        @Override
        public String toString(SortedBag<Card> object) {
            return Info.cardsName(object);
        }

        /**
         * throws UnsupportedOperationException
         */
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }

    private Color playerColor(PlayerId id) {
        if (id == PlayerId.PLAYER_1) return Color.CORNFLOWERBLUE;
        else return Color.LIGHTCORAL;
    }
}
