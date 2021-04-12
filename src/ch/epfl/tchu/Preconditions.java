package ch.epfl.tchu;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Non-instantiable, public, final class with the sole purpose to verify the arguments of the methods are as expected
 */
public final class Preconditions {

    /**
     * Private constructor class for the Preconditions class, we will leave it empty (as we want this class to be non-instantiable)
     */
    private Preconditions() { }

    /**
     * Throws an IllegalArgumentException if the argument is false, otherwise does nothing
     * @param shouldBeTrue the condition to be tested
     * @throws IllegalArgumentException if shouldBeTrue is false
     */
    public static void checkArgument(boolean shouldBeTrue){
        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }

}
