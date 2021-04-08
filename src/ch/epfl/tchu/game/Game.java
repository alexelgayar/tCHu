package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Class which represents a game of tCHu. It offers only one single public, static method
 * Public, Final and Non-Instantiable
 */
public final class Game {

    private static Map<PlayerId, Player> playersMap;
    private static Map<PlayerId, Info> infos = new HashMap<>();
    private static GameState gameState;

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

        playersMap = players; //TODO: Should I make players a class attribute (for sendInformation method) or no?
        players.forEach(((playerId, player) -> infos.put(playerId, new Info(playerNames.get(playerId))))); //TODO: Does infos store values or no?

        //1.1:Initial Players
        players.forEach((id, player) -> player.initPlayers(id, playerNames));
        gameState = GameState.initial(tickets, rng); //method picks first player randomly
        infos.forEach(((playerId, info) -> sendInformation(info.willPlayFirst())));


        //1.2:Set Initial Ticket Choice, Pick Initial Tickets
        for (PlayerId playerId: PlayerId.ALL){
            players.get(playerId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }
        players.forEach(((playerId, player) -> gameState.withInitiallyChosenTickets(playerId, player.chooseInitialTickets())));
        infos.forEach(((playerId, info) -> sendInformation(info.keptTickets(gameState.playerState(playerId).ticketCount()))));

        //2.0: First Turn begins (Game starts)
        //Until the end of the game => Each player must play a role, for each current player
        boolean runGame = true;

        //TODO: Is this how to store playerId?
        //1. While + break or while + update runGame when game is over
        while (runGame){
            //Change CardState when player draws cards
            //GameState.cardstate
            //gameState.cardState().;
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
                    //boolean threeDeckCardsRequireAdditionalCards = claimedRoute.additionalClaimCardsCount().size() == 0;
                    //boolean playerHasAdditionalCards = currentPlayer.cards.contains(additionalClaimCards);
                    //if (routeIsUnderground && threeDeckCardsRequireAdditionalCards && playerHasAdditionalCards){
                        //currentPlayer.chooseAdditionalCards(); //Ask player to pick the additional cards to play
                    //}
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
    private static void sendInformation(String info){
        playersMap.forEach((playerId, player) -> player.receiveInfo(info));
    }

    //Method which informs all players of a change of state, calling the method updateState of each of them
    private static void updateState(){
        //Similar structure to sendInformation
        //Takes player, and gamestate => tell player state has changed
    }

}
