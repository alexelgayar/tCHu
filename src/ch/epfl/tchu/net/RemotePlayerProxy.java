package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents a proxy of a distant player.
 */
public final class RemotePlayerProxy implements Player {

    private final BufferedReader r;
    private final BufferedWriter w;

    /**
     * Constructor for RemotePlayerProxy
     *
     * @param socket the socket used to communicate with the player
     */
    public RemotePlayerProxy(Socket socket) {
        try {
            r = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), US_ASCII));

            w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                    US_ASCII));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void sendMessage(String string) {
        try {
            w.write(string);
            w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private String receiveMessage() {
        String msg;
        try {
            msg = r.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return msg;
    }

    /**
     * Called at the beginning of the game to communicate to players their own Id's and both player's names
     *
     * @param ownId       the player's own Id
     * @param playerNames Map that maps each player's Id to their name
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        List<String> names = new ArrayList<>();
        playerNames.forEach((playerId, name) -> names.add(name));

        sendMessage(String.join(" ",
                MessageId.INIT_PLAYERS.name(),
                Serdes.PLAYER_ID_SERDE.serialize(ownId),
                Serdes.STRING_LIST_SERDE.serialize(names)));

    }

    /**
     * Called to communicate information to players during the game
     *
     * @param info information to be sent to the players
     */
    @Override
    public void receiveInfo(String info) {

        sendMessage(String.join(" ",
                MessageId.RECEIVE_INFO.name(),
                Serdes.STRING_SERDE.serialize(info)));

    }

    /**
     * Called whenever the game's state changes to inform the player of the public game state and their updated own state
     *
     * @param newState the new public game state
     * @param ownState the player's updated own state
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

        sendMessage(String.join(" ",
                MessageId.UPDATE_STATE.name(),
                Serdes.PUBLIC_GAME_STATE_SERDE.serialize(newState),
                Serdes.PLAYER_STATE_SERDE.serialize(ownState)
        ));

    }

    /**
     * Called at the beginning of the game to communicate to the players the 5 tickets that they got proposed
     *
     * @param tickets the 5 tickets proposed to each player
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        sendMessage(String.join(" ",
                MessageId.SET_INITIAL_TICKETS.name(),
                Serdes.TICKET_BAG_SERDE.serialize(tickets)));

    }

    /**
     * Called at the beginning the game a ask the players which tickets they wish to keep
     *
     * @return the tickets that the player wishes to keep
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS.name());
        return Serdes.TICKET_BAG_SERDE.deserialize(receiveMessage());
    }

    /**
     * Called at the beginning of a player's turn to know which action they would like to do during that turn
     *
     * @return the action that the player will do during their next turn
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN.name());
        return Serdes.TURN_KIND_SERDE.deserialize(receiveMessage());
    }

    /**
     * Called when a player chooses to draw tickets to give them choices and to know which ones they will keep
     *
     * @param options ticket options that the player chooses from
     * @return tickets that the player chose to keep
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(String.join(" ",
                MessageId.CHOOSE_TICKETS.name(),
                Serdes.TICKET_BAG_SERDE.serialize(options)));
        return Serdes.TICKET_BAG_SERDE.deserialize(receiveMessage());
    }

    /**
     * Called when a players chooses to draw a card to specify from the 5 visible card or from the top of the deck
     *
     * @return the slot of the card from the 5 visible cards or -1 if the players want the card at the top of the deck
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT.name());
        return Serdes.INT_SERDE.deserialize(receiveMessage());
    }

    /**
     * Called when a player claims a route to specify which route it is
     *
     * @return the route that the player claimed
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE.name());
        return Serdes.ROUTE_SERDE.deserialize(receiveMessage());
    }

    /**
     * Called when player wishes to claim a tunnel to specify the initial cards they wish to claim the tunnel with
     *
     * @return the initial cards the player wishes to claim the tunnel with
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS.name());
        return Serdes.CARD_BAG_SERDE.deserialize(receiveMessage());
    }

    /**
     * Called when a player wishes to claim a tunnel so the player can or not play additional cards to claim the tunnel
     *
     * @param options all the options of additional cards the player could use to claim the tunnel
     * @return the additional cars the player used to claim the tunnel or an empty set if they cannot or do not want to claim the tunnel
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(String.join(" ",
                MessageId.CHOOSE_ADDITIONAL_CARDS.name(),
                Serdes.CARD_BAG_LIST_SERDE.serialize(options)));
        return Serdes.CARD_BAG_SERDE.deserialize(receiveMessage());
    }

}
