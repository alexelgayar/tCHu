package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Instantiable class, represents a proxy of a distant player.
 */
public final class RemotePlayerProxy implements Player {

    private final Socket socket;

    /**
     * Constructor for RemotePlayerProxy
     * @param socket the socket used to communicate with the player
     */
    public RemotePlayerProxy(Socket socket){
        this.socket = socket;
    }

    private void sendMessage(MessageId messageId, String string){
        //Send message through the socket
    }

    private void receiveMessage(String string){

    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

    }

    @Override
    public void receiveInfo(String info) {

    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }
}
