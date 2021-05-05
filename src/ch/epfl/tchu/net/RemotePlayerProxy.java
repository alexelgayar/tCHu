package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Instantiable class, represents a proxy of a distant player.
 */
public final class RemotePlayerProxy implements Player {

    private Socket socket;
    BufferedReader r;
    BufferedWriter w;

    /**
     * Constructor for RemotePlayerProxy
     *
     * @param socket the socket used to communicate with the player
     */
    public RemotePlayerProxy(Socket socket) {
        this.socket = socket;
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

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        List<String> names = new ArrayList<>();
        names.add(playerNames.get(PlayerId.PLAYER_1));
        names.add(playerNames.get(PlayerId.PLAYER_2));

        sendMessage(MessageId.INIT_PLAYERS.name() + " " +
                Serdes.playerIdSerde.serialize(ownId) + " " +
                Serdes.stringListSerde.serialize(names) + '\n');

    }

    @Override
    public void receiveInfo(String info) {

        sendMessage(MessageId.RECEIVE_INFO.name() + " " +
                Serdes.stringSerde.serialize(info) + '\n');

    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

        sendMessage(MessageId.UPDATE_STATE.name() + " " +
                Serdes.publicGameStateSerde.serialize(newState) +
                " " + Serdes.playerStateSerde.serialize(ownState) + '\n');

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        sendMessage(MessageId.SET_INITIAL_TICKETS.name() + " " +
                Serdes.ticketBagSerde.serialize(tickets) + '\n');

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS.name() + '\n');
        return Serdes.ticketBagSerde.deserialize(receiveMessage());
    }

    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN.name() + '\n');
        return Serdes.turnKindSerde.deserialize(receiveMessage());
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS.name() + " " +
                Serdes.ticketBagSerde.serialize(options) + '\n');
        return Serdes.ticketBagSerde.deserialize(receiveMessage());
    }

    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT.name() + '\n');
        return Serdes.intSerde.deserialize(receiveMessage());
    }

    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE.name() + '\n');
        return Serdes.routeSerde.deserialize(receiveMessage());
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS.name() + '\n');
        return Serdes.cardBagSerde.deserialize(receiveMessage());
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS.name() + " " +
                Serdes.cardBagListSerde.serialize(options));
        return Serdes.cardBagSerde.deserialize(receiveMessage());
    }

}
