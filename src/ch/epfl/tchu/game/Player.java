package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

public interface Player {

    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    void receiveInfo(String info);

    void updateState(PublicGameState newState, PlayerState ownState);

    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    SortedBag<Ticket> chooseInitialTickets();

    TurnKind nextTurn();

    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    int drawSlot();

    Route claimedRoute();

    SortedBag<Card> initialClaimCards();

    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);


    public enum TurnKind {

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;


        private static final TurnKind[] AllArray = TurnKind.values();

        /**
         * List containing all TurnKinds
         */
        public static final List<TurnKind> ALL = List.of(AllArray);

        /**
         * Size of ALL
         */
        public static final int COUNT = ALL.size();

    }
}
