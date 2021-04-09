package ch.epfl.tchu.game;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.*;


public class GameTest {
    private final long randomSeed = 54;
    private final List<Route> allRoutes = new ArrayList<>(ChMap.routes());


    TestPlayer player1 = new TestPlayer(randomSeed, allRoutes);
    TestPlayer player2 = new TestPlayer(randomSeed, allRoutes);



    @Test
    void playTestWorks(){
        Map<PlayerId,Player> players = new EnumMap<>(PlayerId.class);
        players.put(PlayerId.PLAYER_1, player1);
        players.put(PlayerId.PLAYER_2, player2);
        Map<PlayerId, String> playerNames = new EnumMap<>(PlayerId.class);
        playerNames.put(PlayerId.PLAYER_1, "Sharif");
        playerNames.put(PlayerId.PLAYER_2, "Chidmoumou");
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Random rng = new Random(randomSeed);
        Game.play(players, playerNames, tickets, rng);
    }



    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int infoCounter;
        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        private PlayerId ownId;
        private Map<PlayerId, String> playerNames;

        private SortedBag<Ticket> initialTickets;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
            this.infoCounter = 0;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.ownId = ownId;
            this.playerNames = playerNames;
        }

        @Override
        public void receiveInfo(String info) {
            infoCounter += 1;
            if (playerNames != null) {
                System.out.printf("---| %s : %s", playerNames.get(ownId), info);
            } else {
                System.out.println(info);
            }
            if (turnCounter > infoCounter){
                throw new Error("t'as merdé ma gueule");
            }
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

            this.initialTickets = tickets;
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            SortedBag.Builder<Ticket> chosenTicket = new SortedBag.Builder<>();

            for(int i = 0; i < rng.nextInt(3) + 1; ++i){
                int randomT = rng.nextInt(Constants.INITIAL_TICKETS_COUNT - i);
                chosenTicket.add(initialTickets.get(randomT));
                initialTickets =  initialTickets.difference(SortedBag.of(initialTickets.get(randomT)));
            }

            return chosenTicket.build();
        }

        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT){
                throw new Error("Trop de tours joués !");
            }

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = new ArrayList<>();
            for(Route r : allRoutes){
                if(ownState.canClaimRoute(r) && !gameState.claimedRoutes().contains(r)){
                    claimableRoutes.add(r);
                }
            }
            int random = rng.nextInt(5);
            if ((claimableRoutes.isEmpty() || random == 2 || random == 3) &&
                    (gameState.cardState().deckSize() + gameState.cardState().discardsSize() >= 6)) {
                return TurnKind.DRAW_CARDS;
            } else if ((random == 1 || claimableRoutes.isEmpty()) && gameState.canDrawTickets()) {
                return TurnKind.DRAW_TICKETS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);
                int claimCardsIndex = rng.nextInt(cards.size());

                routeToClaim = route;
                initialClaimCards = cards.get(claimCardsIndex);
                return TurnKind.CLAIM_ROUTE;
            }
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            SortedBag.Builder<Ticket> chosenTicket = new SortedBag.Builder<>();
            int random = rng.nextInt(3) + 1;
            for(int i = 0; i < random; ++i){
                int randomT = rng.nextInt(Constants.IN_GAME_TICKETS_COUNT - i);
                Ticket ticketToRemove = options.get(randomT);
                chosenTicket.add(ticketToRemove);
                options = options.difference(SortedBag.of(ticketToRemove));
            }

            return chosenTicket.build();
        }

        @Override
        public int drawSlot() {
            int random1 = rng.nextInt(5);
            int random2 = rng.nextInt(6) - 1;
            return (nextTurn() == TurnKind.CLAIM_ROUTE) ? random1 : random2;
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards( List<SortedBag<Card>> options) {
            if(options.isEmpty()) {
                return SortedBag.of();
            }
            return options.get(rng.nextInt(options.size()));
        }
    }

}