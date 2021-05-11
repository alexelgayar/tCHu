package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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

import javax.script.Bindings;

import static javafx.application.Platform.isFxApplicationThread;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents the graphic interface of a tCHu player
 */
public class GraphicalPlayer {

    private PlayerId id;
    private Map<PlayerId, String> playerNames;
    private ObservableGameState gameState;
    private ObservableList<Text> textList = null;
    private Stage mainStage;

    public GraphicalPlayer(PlayerId id, Map<PlayerId, String> playerNames) {
        this.id = id;
        this.playerNames = playerNames;
        gameState = new ObservableGameState(id);


        SimpleObjectProperty<ClaimRouteHandler> claimRouteHandler = new SimpleObjectProperty<>();
        SimpleObjectProperty<DrawTicketsHandler> drawTicketHandler = new SimpleObjectProperty<>();
        SimpleObjectProperty<DrawCardHandler> drawCardHandler = new SimpleObjectProperty<>();

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

    public void recieveInfo(String message) {
        assert isFxApplicationThread();

        textList.add(new Text(message));
        if (textList.size() == 6) textList.remove(0);
        InfoViewCreator.createInfoView(id, playerNames, gameState, textList);
    }

    public void startTurn(DrawTicketsHandler ticketsHandler, DrawCardHandler cardHandler, ClaimRouteHandler routeHandler) {


    }

    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler ticketsHandler) {

        String message = String.format(StringsFr.CHOOSE_TICKETS, tickets.size() - 2, tickets.size() == 3 ? "" : "s");

        ObservableList<Ticket> observableList = null;
        observableList.addAll(tickets.toList());
        ListView<Ticket> listView = new ListView<>(observableList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        IntegerProperty minChoice = null;
        minChoice.set(listView.getSelectionModel().getSelectedItems().size());

        Button button = new Button();
        button.disableProperty().bind(minChoice.lessThan(tickets.size() - 2));
        button.setOnAction(e -> {
            mainStage.hide();
            chooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems()), ticketsHandler);
        });

        createPane(StringsFr.TICKETS_CHOICE, message, button, listView);
    }


    private <E> void createPane(String title, String message, Button button, ListView<E> listView) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(mainStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();

        Scene scene = new Scene(vBox);
        scene.getStylesheets().addAll("chooser.css");

        TextFlow textFlow = new TextFlow();
        Text text = new Text(message);
        textFlow.getChildren().addAll(text);

        vBox.getChildren().addAll(textFlow, listView, button);
        stage.setScene(scene);
        stage.show();
    }

    public void chooseClaimCards(List<SortedBag<Card>> cardList, ChooseCardsHandler chooseCardsHandler) {

    }


}
