package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.epfl.tchu.gui.ActionHandlers.*;
import javafx.util.StringConverter;

import static javafx.application.Platform.isFxApplicationThread;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents the graphic interface of a tCHu player
 */
public class GraphicalPlayer {

    private PlayerId id;
    private Map<PlayerId, String> playerNames;
    private ObservableGameState gameState;
    private ObservableList<Text> textList = observableArrayList();
    private Stage mainStage;
    SimpleObjectProperty<ClaimRouteHandler> claimRouteHandler;
    SimpleObjectProperty<DrawTicketsHandler> drawTicketHandler;
    SimpleObjectProperty<DrawCardHandler> drawCardHandler;

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
        mainStage.setTitle("tCHu - " + playerNames.get(id));
        mainStage.show();
    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert isFxApplicationThread();
        gameState.setState(newGameState, newPlayerState);
    }

    public void receiveInfo(String message) {
        assert isFxApplicationThread();

        textList.add(new Text(message));
        if (textList.size() == 6) textList.remove(0);

    }

    public void startTurn(DrawTicketsHandler ticketsHandler, DrawCardHandler cardHandler, ClaimRouteHandler routeHandler) {
        assert isFxApplicationThread();

        if(gameState.canDrawTickets().get()){
            DrawTicketsHandler tHandler = () -> {
                setNull();
                ticketsHandler.onDrawTickets();

            };
            drawTicketHandler.set(tHandler);
        }

        if(gameState.canDrawCards().get()){
            DrawCardHandler cHandler = (int i) -> {
                setNull();
                cardHandler.onDrawCard(i);
                drawCard(cardHandler);
            };
            drawCardHandler.set(cHandler);
        }

        ClaimRouteHandler rHandler = (route, claimCards) -> {
            setNull();
            routeHandler.onClaimRoute(route, claimCards);
        };
        claimRouteHandler.set(rHandler);



    }

    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler ticketsHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);

        String message = String.format(StringsFr.CHOOSE_TICKETS, tickets.size() - 2, StringsFr.plural(tickets.size() - 2));

        ObservableList<Ticket> observableList = observableArrayList(tickets.toList());
        ListView<Ticket> listView = new ListView<>(observableList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button button = new Button();

        button.disableProperty().bind(Bindings.size(listView.getSelectionModel().getSelectedItems())
                .lessThan(tickets.size() - 2));

        button.setOnAction(e -> {
            stage.hide();
            ticketsHandler.onChooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems()));
        });

        createPane(stage, StringsFr.TICKETS_CHOICE, message, button, listView);
    }

    public void drawCard(DrawCardHandler cardHandler) {
        assert isFxApplicationThread();

        DrawCardHandler handler = (int i) -> {
            setNull();
            cardHandler.onDrawCard(i);
        };
        drawCardHandler.set(handler);
    }

    public void chooseClaimCards(List<SortedBag<Card>> bagList, ChooseCardsHandler cardsHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);

        String message = String.format(StringsFr.CHOOSE_CARDS);

        ObservableList<SortedBag<Card>> observableList = observableArrayList(bagList);
        ListView<SortedBag<Card>> listView = new ListView<>(observableList);

        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        Button button = new Button();
        button.disableProperty().bind(Bindings.size(listView.getSelectionModel().getSelectedItems()).lessThan(1));

        button.setOnAction(e -> {
            stage.hide();
            cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItem());
        });

        createPane(stage, StringsFr.CARDS_CHOICE, message, button, listView);
    }

    public void chooseAdditionalCards(List<SortedBag<Card>> bagList, ChooseCardsHandler cardsHandler) {
        assert isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);

        String message = String.format(StringsFr.CHOOSE_ADDITIONAL_CARDS);

        ObservableList<SortedBag<Card>> observableList = observableArrayList(bagList);
        ListView<SortedBag<Card>> listView = new ListView<>(observableList);
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        Button button = new Button();

        button.setOnAction(e -> {
            stage.hide();
            cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItem());
        });

        createPane(stage, StringsFr.CARDS_CHOICE, message, button, listView);
    }


    private <E> void createPane(Stage stage, String title, String message, Button button, ListView<E> listView) {

        stage.initOwner(mainStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();

        button.setText(StringsFr.CHOOSE);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().addAll("chooser.css");

        TextFlow textFlow = new TextFlow();
        Text text = new Text(message);
        textFlow.getChildren().addAll(text);

        vBox.getChildren().addAll(textFlow, listView, button);
        stage.setScene(scene);
        stage.show();
    }

    private void setNull() {
        claimRouteHandler.set(null);
        drawTicketHandler.set(null);
        drawCardHandler.set(null);
    }

    public class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        @Override
        public String toString(SortedBag<Card> object) {
            return Info.cardsName(object);
        }

        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }
}
