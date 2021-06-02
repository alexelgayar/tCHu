package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Ticket;

import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.epfl.tchu.gui.StringsFr.SPACE;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents a client of a distant player
 */
public final class RemotePlayerClient {

    private final Player player;
    private final BufferedReader r;
    private final BufferedWriter w;

    /**
     * Constructor for RemotePlayerClient
     *
     * @param player the distant player that is represented by the client
     * @param name   name of the proxy
     * @param port   port used to connect to server
     */
    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;

        try {
            Socket socket = new Socket(name, port);
            r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
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

    /**
     * Method that creates a loop during which it waits for a message from the proxy, determines the message type
     * then deserializes it and then calls the player's corresponding method
     */
    public void run() {

        try {

            String s;

            while ((s = r.readLine()) != null) {


                String[] list = s.split(Pattern.quote(SPACE), -1);

                switch (MessageId.valueOf(list[0])) {

                    case SET_PLAYER_NAME:
                        player.setPlayerName();
                        break;
                    case CHOOSE_NAME:
                        String name = player.choosePlayerName();
                        sendMessage(Serdes.STRING_SERDE.serialize(name));
                        break;
                    case INIT_PLAYERS:
                        List<String> names = Serdes.STRING_LIST_SERDE.deserialize(list[2]);
                        Map<PlayerId, String> playerNames = new EnumMap<>(PlayerId.class);
                        playerNames.put(PlayerId.PLAYER_1, names.get(0));
                        playerNames.put(PlayerId.PLAYER_2, names.get(1));
                        player.initPlayers(Serdes.PLAYER_ID_SERDE.deserialize(list[1]), playerNames);
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(Serdes.STRING_SERDE.deserialize(list[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(Serdes.PUBLIC_GAME_STATE_SERDE.deserialize(list[1]),
                                Serdes.PLAYER_STATE_SERDE.deserialize(list[2]));
                        break;
                    case SET_INITIAL_TICKETS:
                        player.setInitialTicketChoice(Serdes.TICKET_BAG_SERDE.deserialize(list[1]));
                        break;
                    case CHOOSE_INITIAL_TICKETS:
                        sendMessage(Serdes.TICKET_BAG_SERDE.serialize(player.chooseInitialTickets()));
                        break;
                    case NEXT_TURN:
                        sendMessage(Serdes.TURN_KIND_SERDE.serialize(player.nextTurn()));
                        break;
                    case CHOOSE_TICKETS:
                        SortedBag<Ticket> bag = player.chooseTickets(Serdes.TICKET_BAG_SERDE.deserialize(list[1]));
                        sendMessage(Serdes.TICKET_BAG_SERDE.serialize(bag));
                        break;
                    case DRAW_SLOT:
                        sendMessage(Serdes.INT_SERDE.serialize(player.drawSlot()));
                        break;
                    case ROUTE:
                        sendMessage(Serdes.ROUTE_SERDE.serialize(player.claimedRoute()));
                        break;
                    case CARDS:
                        sendMessage(Serdes.CARD_BAG_SERDE.serialize(player.initialClaimCards()));
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        SortedBag<Card> options = player.chooseAdditionalCards(Serdes.CARD_BAG_LIST_SERDE.deserialize(list[1]));
                        sendMessage(Serdes.CARD_BAG_SERDE.serialize(options));
                        break;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}



