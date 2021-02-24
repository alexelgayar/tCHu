package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

public final class Station {

    private int id;
    private String name;

    Station(int id, String name) {
        this.id = id;
        this.name = name;
        Preconditions.checkArgument(id >= 0);
    }

    public int id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
