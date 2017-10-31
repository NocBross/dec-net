package main.java.validator;

/**
 * This interface defines the structure of a validator class.
 * 
 * @author Kay Oliver Szesny
 *
 */

public interface Validator<T> {

    /**
     * Checks if the given string has the correct syntax.
     * 
     * @param valueToValidate
     * @return true in case of the string is correct<br>
     *         false otherwise
     */
    public boolean validate(T valueToValidate);
}
