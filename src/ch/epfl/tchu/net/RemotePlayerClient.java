package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;

/**
 * Instantiable class, represents a client of a distant player
 */
public final class RemotePlayerClient { //TODO: What is meant by an instantiable class

    Player player;
    String name;
    int port;

    public RemotePlayerClient(Player player, String name, int port){
        this.player = player;
        this.name = name;
        this.port = port;
    }

    public void run(){

    }

}
