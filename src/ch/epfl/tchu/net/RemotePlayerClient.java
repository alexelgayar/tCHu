package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Ticket;

import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, represents a client of a distant player
 */
public final class RemotePlayerClient {

    Player player;
    String name;
    int port;


    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.port = port;

    }

    public void run() {

        try (Socket socket = new Socket(name, port);
             BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII))) {

            String s;

            while ((s = r.readLine()) != null) {

                String[] list = s.split(Pattern.quote(" "), -1);

                switch (MessageId.valueOf(list[0])) {
                    case INIT_PLAYERS:
                        List<String> names = Serdes.stringListSerde.deserialize(list[2]);
                        Map<PlayerId, String> playerNames = new EnumMap<>(PlayerId.class);
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
                            w.write(Serdes.ticketBagSerde.serialize(player.chooseInitialTickets()));
                            w.write('\n');
                            w.flush();
                        break;
                    case NEXT_TURN:
                            w.write(Serdes.turnKindSerde.serialize(player.nextTurn()));
                            w.write('\n');
                            w.flush();
                        break;
                    case CHOOSE_TICKETS:
                        SortedBag<Ticket> bag = player.chooseTickets(Serdes.ticketBagSerde.deserialize(list[1]));
                            w.write(Serdes.ticketBagSerde.serialize(bag));
                            w.write('\n');
                            w.flush();
                        break;
                    case DRAW_SLOT:
                            w.write(Serdes.intSerde.serialize(player.drawSlot()));
                            w.write('\n');
                            w.flush();
                        break;
                    case ROUTE:
                            w.write(Serdes.routeSerde.serialize(player.claimedRoute()));
                            w.write('\n');
                            w.flush();
                        break;
                    case CARDS:
                            w.write(Serdes.cardBagSerde.serialize(player.initialClaimCards()));
                            w.write('\n');
                            w.flush();
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        SortedBag<Card> options = player.chooseAdditionalCards(Serdes.cardBagListSerde.deserialize(list[1]));
                            w.write(Serdes.cardBagSerde.serialize(options));
                            w.write('\n');
                            w.flush();
                        break;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}



