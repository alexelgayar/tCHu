package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.util.Map;

import static javafx.application.Platform.isFxApplicationThread;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents the graphic interface of a tCHu player
 */
public class GraphicalPlayer {

    PlayerId id;
    Map<PlayerId, String> playerNames;
    ObservableGameState gameState;
    ObservableList<Text> textList = null;

    public GraphicalPlayer(PlayerId id, Map<PlayerId, String> playerNames){

        this.id = id;
        this.playerNames = playerNames;
        gameState = new ObservableGameState(id);
    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState){
        assert isFxApplicationThread();
        gameState.setState(newGameState, newPlayerState);
    }

    public void recieveInfo(String message){
        assert isFxApplicationThread();

        textList.add(new Text(message));
        if(textList.size() == 6) textList.remove(0);
        InfoViewCreator.createInfoView(id, playerNames, gameState, textList);
    }

    public void startTurn(ActionHandlers.DrawTicketsHandler ticketsHandler, ActionHandlers.DrawCardHandler cardHandler, ActionHandlers.ClaimRouteHandler routeHandler){


    }

    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler ticketsHandler){


    }





}
