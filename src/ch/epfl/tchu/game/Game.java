package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.Map;
import java.util.Random;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Class which represents a game of tCHu. It offers only one single public, static method
 * Public, Final and Non-Instantiable
 */
public final class Game {

    /**
     * Method which plays a game of tCHu for the given players, who names appear in the table playerNames.
     * The tickets available for this game are those of tickets, and
     * the random generator tng is used to create the initial state of the game and to shuffle the cards from the discard pile to make a new draw when necessary
     * @param players the players who will play the game of tCHu
     * @param playerNames the name of the players
     * @param tickets the tickets available for the game of tCHu
     * @param rng the random number generator rng is used to create the initial state of the game and to shuffle the cards from the discards pile to make a new draw when necessary
     * @throws IllegalArgumentException if one of the two associative tables has a size other than 2.
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){
        Preconditions.checkArgument(players.size() == 2 && playerNames.size() == 2);
        //First call a method for each player
        //Then receive info to get the player choice for each executed method

        Info player1Info = new Info(playerNames.get(0));
        Info player2Info = new Info(playerNames.get(1));

        //1.1:Initial Players
        //TODO: Should I first initPlayers both players, or only initPlayer for firstPlayer then receiveInfo?
        players.forEach((id, player) -> player.initPlayers(id, playerNames));

        //TODO: should I create a gamestate? (because it's constructor chooses the first player at random)
        GameState gameState = GameState.initial(tickets, rng);

        //TODO: Is this how I set the first Player?

        players.forEach((id, player) -> player.receiveInfo(new Info(playerNames.get(gameState.currentPlayerId())).willPlayFirst()));


        //1.2:Set Initial Ticket Choice, Pick Initial Tickets
        players.forEach((id, player) -> player.setInitialTicketChoice(tickets));
        players.forEach((id, player) -> player.chooseInitialTickets());
        players.forEach((id, player) -> player.receiveInfo(new Info(playerNames.get(id)).keptTickets(player.chooseInitialTickets().size())));


        //=====Game Start=====//
        //Until the end of the game => Each player must play a role, for each current player
        boolean runGame = true;

        //TODO: Is this how to store playerId?
        while (runGame){
            for (PlayerId playerId: PlayerId.ALL){
                Player currentPlayer = players.get(playerId);
                Player.TurnKind turnKind = currentPlayer.nextTurn();

                if (turnKind == Player.TurnKind.DRAW_TICKETS){
                    currentPlayer.chooseTickets(tickets);
                }

                else if (turnKind == Player.TurnKind.DRAW_CARDS){
                    currentPlayer.drawSlot(); //1. Draw first card
                    currentPlayer.drawSlot(); //2. Draw second card
                }

                else if (turnKind == Player.TurnKind.CLAIM_ROUTE){
                    Route claimedRoute = currentPlayer.claimedRoute();
                    currentPlayer.initialClaimCards();

                    boolean routeIsUnderground = claimedRoute.level() == Route.Level.UNDERGROUND;
                    boolean threeDeckCardsRequireAdditionalCards = claimedRoute.additionalClaimCardsCount().size() == 0;
                    boolean playerHasAdditionalCards = currentPlayer.cards.contains(additionalClaimCards);
                    if (routeIsUnderground && threeDeckCardsRequireAdditionalCards && playerHasAdditionalCards){
                        currentPlayer.chooseAdditionalCards(); //Ask player to pick the additional cards to play
                    }
                }

            }
        }


        /*
        for (PlayerId playerId: players.keySet()){
            Player player = players.get(playerId);
            player.initPlayers(playerId, playerNames);

            Info info = new Info(playerNames.get(playerId));
            player.receiveInfo(info.willPlayFirst());

            player.setInitialTicketChoice(tickets);
            player.chooseInitialTickets();
            player.receiveInfo(info.keptTickets(player.chooseInitialTickets().size()));
        }

        Read through everything first
        Don't use one single for loops, instead use multiple forEach loops of the lamdas
        => (less optimal, but more readable)
        Create the auxiliary methods in the prog tips
         */
    }

    //Method which sends information to all the players, by calling the method receiveInfo for each
    private Info sendInformation(Player player){
        return null;
    }

    private Info updateState(){
        return null;
    }

}
