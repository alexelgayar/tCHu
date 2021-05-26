package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public interface
 * Represents an object that can serialize or deserialize other objects
 */
public interface Serde<E> {

    /**
     * Returns the corresponding serialized string for a given object
     * @param plainObject the object to be serialized
     * @return the corresponding serialized string for a given object
     */
    String serialize(E plainObject);

    /**
     * Returns the corresponding object for a given string serialization
     * @param serializedObject the serialized string that must be converted back into the object
     * @return the corresponding object for a given string serialization
     */
    E deserialize(String serializedObject);

    /**
     * Returns the serde corresponding to the given serialisation and deserialization function
     * @param serFun the serialization function
     * @param deserFun the deserialization function
     * @param <T> the parameter of the type of the method
     * @return the serde corresponding to the given serialisation and deserialization function
     */
    static <T> Serde<T> of(Function<T, String> serFun, Function<String, T> deserFun) {

        return new Serde<>() {
            @Override
            public String serialize(T plainObject) {
                return serFun.apply(plainObject);
            }

            @Override
            public T deserialize(String serializedObject) {
                return deserFun.apply(serializedObject);
            }
        };
    }

    /**
     * Returns the serde corresponding to the list of all the values of a enum values set
     * @param list the list of all the values of a set of enum values
     * @param <T> the parameter of the type of the method
     * @return the serde corresponding to the list of all the values of a enum values set
     */
    static <T> Serde<T> oneOf(List<T> list) {
        Function<T, String> serFun = t -> Integer.toString(list.indexOf(t));
        Function<String, T> deserFun = s -> list.get(Integer.parseInt(s));

        return Serde.of(serFun, deserFun);
    }

    /**
     * Returns a serde capable of (de)serializing the list of values (de)serialized by the given separator and serde
     * @param serde the provided serde that was used to (de)serialized the list of values
     * @param separator the separator of the list of values
     * @param <T> the parameter of the type of the method
     * @return a serde capable of (de)serializing the list of values (de)serialized by the given separator and serde
     */
    static <T> Serde<List<T>> listOf(Serde serde, String separator) {

        return new Serde<List<T>>() {

            @Override
            public String serialize(List<T> list) {

                if(list.isEmpty()){
                    return "";
                }
                else {
                    List<String> strings = new ArrayList<>();

                    for (T t : list) {
                        strings.add(serde.serialize(t));
                    }

                    String s = String.join(separator, strings);
                    return s;
                }
            }

            @Override
            public List<T> deserialize(String serializedObject) {

                if(serializedObject.equals("")){
                    return new ArrayList<T>();
                }
                else {
                    String[] s = serializedObject.split(Pattern.quote(separator), -1);
                    List<T> tList = new ArrayList<>();

                    for (int i = 0; i < s.length; ++i) {
                        tList.add((T) serde.deserialize(s[i]));
                    }
                    return tList;
                }
            }
        };
    }

    /**
     * Returns a serde capable of (de)serializing the SortedBag of values (de)serialized by the given separator and serde
     * @param serde the provided serde that was used to (de)serialized the list of values
     * @param separator the separator of the list of values
     * @param <T> the parameter of the type of the method
     * @return a serde capable of (de)serializing the SortedBag of values (de)serialized by the given separator and serde
     */
    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde serde, String separator) {

        return new Serde<SortedBag<T>>() {

            @Override
            public String serialize(SortedBag<T> bag) {

                List list = bag.toList();
                String y = listOf(serde, separator).serialize(list);
                return y;
            }

            @Override
            public SortedBag<T> deserialize(String serializedObject) {
                List list = Serde.listOf(serde, separator).deserialize(serializedObject);
                return SortedBag.of(list);
            }
        };
    }
}




