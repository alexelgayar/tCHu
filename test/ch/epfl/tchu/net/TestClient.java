package ch.epfl.tchu.net;

import java.util.List;
import java.util.Map;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;
import ch.epfl.tchu.net.RemotePlayerClient;

public final class TestClient {
    public static void main(String[] args) {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient =
                new RemotePlayerClient(new TestPlayer(),
                        "localhost",
                        5108);
        playerClient.run();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {
        @Override
        public void initPlayers(PlayerId ownId,
                                Map<PlayerId, String> names) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", names);
        }



        @Override
        public void receiveInfo(String info) {
            System.out.println("receivedInfo: "+ info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.printf("newState: %s\n", newState);
            System.out.printf("ownState: %s\n", ownState);

        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.printf("tickets : %s\n", tickets);

        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            return SortedBag.of(2,ChMap.tickets().get(12),1,ChMap.tickets().get(9));
        }

        @Override
        public TurnKind nextTurn() {
            return TurnKind.DRAW_CARDS;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.printf("options: %s\n", options);
            return SortedBag.of(options.get(0));
        }

        @Override
        public int drawSlot() {
            return 3;
        }

        @Override
        public Route claimedRoute() {
            return ChMap.routes().get(0);
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return null;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(
                List<SortedBag<Card>> options) {
            return null;
        }

        // … autres méthodes de Player
    }
}