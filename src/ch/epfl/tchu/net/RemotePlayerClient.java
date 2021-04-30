package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Ticket;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Instantiable class, represents a client of a distant player
 */
public final class RemotePlayerClient {

    Player player;
    String name;
    int port;
    BufferedReader r;
    BufferedWriter w;
    Socket socket;

    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.port = port;

        try {
            socket = new Socket(name, port);

            r = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), US_ASCII));

            w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                    US_ASCII));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void run() {

        String s;

        do {
            try {
                s = r.readLine();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            if (s != null) {

                String[] list = s.split(Pattern.quote(" "));

                switch (MessageId.valueOf(list[0])) {
                    case INIT_PLAYERS:
                        List<String> names = Serdes.stringListSerde.deserialize(list[2]);
                        Map<PlayerId, String> playerNames = new HashMap<>();
                        playerNames.put(PlayerId.PLAYER_1, names.get(0));
                        playerNames.put(PlayerId.PLAYER_2, names.get(1));
                        player.initPlayers(Serdes.playerIdSerde.deserialize(list[1]), playerNames);
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(Serdes.stringSerde.deserialize(list[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(Serdes.publicGameStateSerde.deserialize(list[1]),
                                Serdes.playerStateSerde.deserialize(list[2]));
                        break;
                    case SET_INITIAL_TICKETS:
                        player.setInitialTicketChoice(Serdes.ticketBagSerde.deserialize(list[1]));
                        break;
                    case CHOOSE_INITIAL_TICKETS:
                        try {
                            w.write(Serdes.ticketBagSerde.serialize(player.chooseInitialTickets()));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                    case NEXT_TURN:
                        try {
                            w.write(Serdes.turnKindSerde.serialize(player.nextTurn()));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                    case CHOOSE_TICKETS:
                        SortedBag<Ticket> bag = player.chooseTickets(Serdes.ticketBagSerde.deserialize(list[1]));
                        try {
                            w.write(Serdes.ticketBagSerde.serialize(bag));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                    case DRAW_SLOT:
                        try {
                            w.write(Serdes.intSerde.serialize(player.drawSlot()));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                    case ROUTE:
                        try {
                            w.write(Serdes.routeSerde.serialize(player.claimedRoute()));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                    case CARDS:
                        try {
                            w.write(Serdes.cardBagSerde.serialize(player.initialClaimCards()));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        SortedBag<Card> options = player.chooseAdditionalCards(Serdes.cardBagListSerde.deserialize(list[1]));
                        try {
                            w.write(Serdes.cardBagSerde.serialize(options));
                            w.flush();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        break;
                }
            }


        }
        while (s != null);
    }


}
