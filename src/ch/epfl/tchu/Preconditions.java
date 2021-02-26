package ch.epfl.tchu;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Preconditions {


    private Preconditions() {

    }

    /**
     *
     * @param shouldBeTrue the condition to e tested
     * @throws IllegalArgumentException() if shouldBeTrue is false
     */
    public static void checkArgument(boolean shouldBeTrue){
        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }

}
